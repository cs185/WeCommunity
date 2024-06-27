package edu.rice.webchat.repository;

import edu.rice.webchat.cache.UserCache;
import edu.rice.webchat.dao.UserMapper;
import edu.rice.webchat.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
    private final UserCache userCache;
    private final UserMapper userMapper;

    @Autowired
    public UserRepository(UserCache userCache, UserMapper userMapper) {
        this.userCache = userCache;
        this.userMapper = userMapper;
    }

    public User getUser(String username) {
        User user = userCache.get(username);
        if (user == null) {
            user = userMapper.selectByName(username);
        }
        return user;
    }
}
