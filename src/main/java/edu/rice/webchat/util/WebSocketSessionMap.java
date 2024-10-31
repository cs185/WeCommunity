package edu.rice.webchat.util;

import edu.rice.webchat.entity.user.User;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketSessionMap {

    private ConcurrentHashMap<Integer, WebSocketSession> userSessionMap = new ConcurrentHashMap<>();

    private ConcurrentHashMap<Integer, Set<WebSocketSession>> groupSessionMap = new ConcurrentHashMap<>();

    public void setUserSession(int userId, WebSocketSession session) {
        userSessionMap.put(userId, session);
    }

    public WebSocketSession getUserSession(int userId) {
        return userSessionMap.get(userId);
    }

    public void removeUserSession(int userId) {
        groupSessionMap.remove(userId);
    }

    public void setGroupSession(int groupId, WebSocketSession session) {
        if (!groupSessionMap.containsKey(groupId))
            groupSessionMap.put(groupId, new HashSet<>());

        groupSessionMap.get(groupId).add(session);
    }

    public Set<WebSocketSession> getGroupSessions(int groupId) {
        return groupSessionMap.get(groupId);
    }

    public void removeGroupSession(int groupId) {
        groupSessionMap.remove(groupId);
    }

}
