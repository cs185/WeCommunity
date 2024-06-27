package edu.rice.webchat.cache;

import edu.rice.webchat.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserCache extends Cache<String> {
    private final WebSocketSessionCache webSocketSessionCache;

    @Autowired
    public UserCache(WebSocketSessionCache webSocketSessionCache) {
        this.webSocketSessionCache = webSocketSessionCache;
    }

    @Override
    public User delete(String key) {
        User user = (User) cache.remove(key);
        // delete the websocket session as well when the user is deleted (offline)
        webSocketSessionCache.delete(key);
        return user;
    }

    @Override
    public User get(String key) {
        return (User) cache.getOrDefault(key, null);
    }
}
