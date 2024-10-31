package edu.rice.webchat.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import edu.rice.webchat.cache.GroupCache;
import edu.rice.webchat.cache.GroupWebSocketSessionCache;
import edu.rice.webchat.cache.WebSocketSessionCache;
import edu.rice.webchat.dao.GroupMapper;
import edu.rice.webchat.dao.UserMapper;
import edu.rice.webchat.entity.group.AChatGroup;
import edu.rice.webchat.entity.group.GroupFac;
import edu.rice.webchat.entity.message.GroupMessage;
import edu.rice.webchat.entity.message.MsgFac;
import edu.rice.webchat.entity.msgStrat.AStrategy;
import edu.rice.webchat.entity.msgStrat.MsgStratFac;
import edu.rice.webchat.entity.user.User;
import edu.rice.webchat.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static edu.rice.webchat.util.WebChatUtil.*;


@Service
public class UserService {
    private final UserMapper userMapper;
    private final RedisTemplate redisTemplate;

    @Autowired
    public UserService(UserMapper userMapper, RedisTemplate redisTemplate) {
        this.userMapper = userMapper;
        this.redisTemplate = redisTemplate;
    }

    public User findUserById(int userId) {
        User user = getCache(userId);
        if (user == null) {
            user = initCache(userId);
        }
        return user;
    }

    public User findUserByName(String username) {
        return userMapper.selectByName(username);
    }

    public int updateHeader(int userId, String headerUrl) {
        // 先更新数据库再删缓存
//        return userMapper.updateHeader(userId, headerUrl);
        int rows = userMapper.updateHeader(userId, headerUrl);
        clearCache(userId);
        return rows;
    }

    // 1.优先从缓存中取值
    private User getCache(int userId) {
        String redisKey = RedisKeyUtil.getUserKey(userId);
        return (User) redisTemplate.opsForValue().get(redisKey);
    }

    // 2.取不到时初始化缓存数据
    private User initCache(int userId) {
        User user = userMapper.selectById(userId);
        String redisKey = RedisKeyUtil.getUserKey(userId);
        redisTemplate.opsForValue().set(redisKey, user, 3600, TimeUnit.SECONDS);
        return user;
    }

    // 3.数据变更时清除缓存数据
    private void clearCache(int userId) {
        String redisKey = RedisKeyUtil.getUserKey(userId);
        redisTemplate.delete(redisKey);
    }

    public Collection<? extends GrantedAuthority> getAuthorities(int userId) {
        User user = this.findUserById(userId);

        List<GrantedAuthority> list = new ArrayList<>();
        list.add(new GrantedAuthority() {

            @Override
            public String getAuthority() {
                switch (user.getType()) {
                    case 1:
                        return AUTHORITY_ADMIN;
                    case 2:
                        return AUTHORITY_MODERATOR;
                    default:
                        return AUTHORITY_USER;
                }
            }
        });
        return list;
    }
}
