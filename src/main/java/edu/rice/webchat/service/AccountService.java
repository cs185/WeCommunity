package edu.rice.webchat.service;

import edu.rice.webchat.dao.UserMapper;
import edu.rice.webchat.entity.LoginTicket;
import edu.rice.webchat.util.RedisKeyUtil;
import edu.rice.webchat.util.WebChatUtil;
import edu.rice.webchat.entity.user.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class AccountService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    public Map<String, Object> register(User user) {
        Map<String, Object> map = new HashMap<>();

        // 空值处理
        if (user == null) {
            throw new IllegalArgumentException("No parameter!");
        }
        if (StringUtils.isBlank(user.getUsername())) {
            map.put("usernameMsg", "Empty username!");
            return map;
        }
        if (StringUtils.isBlank(user.getPassword())) {
            map.put("passwordMsg", "Empty password!");
            return map;
        }
        if (StringUtils.isBlank(user.getEmail())) {
            map.put("emailMsg", "Empty email!");
            return map;
        }

        // 验证账号
        User u = userMapper.selectByName(user.getUsername());
        if (u != null) {
            map.put("usernameMsg", "Username already exists!");
            return map;
        }

        // 验证邮箱
        u = userMapper.selectByEmail(user.getEmail());
        if (u != null) {
            map.put("emailMsg", "Email already registered!");
            return map;
        }

        // 注册用户
        user.setSalt(WebChatUtil.generateUUID().substring(0, 5));
        user.setPassword(WebChatUtil.md5(user.getPassword() + user.getSalt()));
        user.setType(0);
        user.setStatus(0);
        user.setActivationCode(WebChatUtil.generateUUID());
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setCreateTime(new Date());
        userMapper.insertUser(user);

//        // 激活邮件
//        Context context = new Context();
//        context.setVariable("email", user.getEmail());
//        // http://localhost:8080/community/activation/101/code
//        String url = domain + contextPath + "/activation/" + user.getId() + "/" + user.getActivationCode();
//        context.setVariable("url", url);
//        String content = templateEngine.process("/mail/activation", context);
//        mailClient.sendMail(user.getEmail(), "激活账号", content);

        return map;
    }

//    public int activation(int userId, String code) {
//        User user = userMapper.selectById(userId);
//        if (user.getStatus() == 1) {
//            return ACTIVATION_REPEAT;
//        } else if (user.getActivationCode().equals(code)) {
//            userMapper.updateStatus(userId, 1);
//            return ACTIVATION_SUCCESS;
//        } else {
//            return ACTIVATION_FAILURE;
//        }
//    }

    public Map<String, Object> login(String username, String password, int expiredSeconds) {
        Map<String, Object> map = new HashMap<>();

        // 空值处理
        if (StringUtils.isBlank(username)) {
            map.put("usernameMsg", "Empty username!");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("passwordMsg", "Empty password!");
            return map;
        }

        // 验证账号
        User user = userMapper.selectByName(username);
        if (user == null) {
            map.put("usernameMsg", "Username not registered!");
            return map;
        }

//        // 验证状态
//        if (user.getStatus() == 0) {
//            map.put("usernameMsg", "该账号未激活!");
//            return map;
//        }

        // 验证密码
        password = WebChatUtil.md5(password + user.getSalt());
        if (!user.getPassword().equals(password)) {
            map.put("passwordMsg", "Invalid password!");
            return map;
        }

        // 生成登录凭证
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(WebChatUtil.generateUUID());
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + expiredSeconds * 1000));
//        loginTicketMapper.insertLoginTicket(loginTicket);
        String redisKey = RedisKeyUtil.getTicketKey(loginTicket.getTicket());
        redisTemplate.opsForValue().set(redisKey, loginTicket);
        map.put("ticket", loginTicket.getTicket());
        return map;
    }

    public void logout(String ticket) {
//        loginTicketMapper.updateStatus(ticket, 1);
        String redisKey = RedisKeyUtil.getTicketKey(ticket);
        LoginTicket loginTicket = (LoginTicket) redisTemplate.opsForValue().get(redisKey);
        loginTicket.setStatus(1);
        redisTemplate.opsForValue().set(redisKey, loginTicket);
    }

    public LoginTicket findLoginTicket(String ticket) {
//        return loginTicketMapper.selectByTicket(ticket);
        String redisKey = RedisKeyUtil.getTicketKey(ticket);
        return (LoginTicket) redisTemplate.opsForValue().get(redisKey);
    }

}
