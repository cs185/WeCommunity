package edu.rice.webchat.entity.user;

import edu.rice.webchat.cache.UserCache;
import edu.rice.webchat.dao.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserFac {

    private final UserMapper userMapper;
    private final UserCache userCache;

    @Autowired
    public UserFac(UserMapper userMapper, UserCache userCache) {
        this.userMapper = userMapper;
        this.userCache = userCache;
    }

    public User makeUser(String username, String password, String email) {
        User user = new User(username, password, email);
        userMapper.insertUser(user);
        userCache.put(username, user);
        return user;
    }

    public User makeUser(String username, String password) {
        User user = new User(username, password);
        userMapper.insertUser(user);
        userCache.put(username, user);
        return user;
    }
}
