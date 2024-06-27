package edu.rice.webchat.cache;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;

@Component
public class GroupWebSocketSessionCache extends Cache<Integer> {
    @Override
    public Set<WebSocketSession> delete(Integer key) {
        return (Set<WebSocketSession>) cache.remove(key);
    }

    @Override
    public void put(Integer key, Object wsSession) {
        if (!this.cache.containsKey(key))
            cache.put(key, new HashSet<WebSocketSession>());

        Set<WebSocketSession> set = (Set<WebSocketSession>) cache.get(key);
        set.add((WebSocketSession) wsSession);
    }

    @Override
    public Set<WebSocketSession> get(Integer key) {
        return (Set<WebSocketSession>) cache.get(key);
    }
}
