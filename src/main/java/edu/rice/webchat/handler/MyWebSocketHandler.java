package edu.rice.webchat.handler;

import edu.rice.webchat.cache.GroupWebSocketSessionCache;
import edu.rice.webchat.cache.WebSocketSessionCache;
import edu.rice.webchat.service.DBService;
import edu.rice.webchat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.net.URI;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class MyWebSocketHandler extends TextWebSocketHandler {
    private final UserService userService;
    private final GroupWebSocketSessionCache groupWebSocketSessionCache;
    private final WebSocketSessionCache webSocketSessionCache;

    @Autowired
    public MyWebSocketHandler(UserService userService, WebSocketSessionCache webSocketSessionCache, GroupWebSocketSessionCache groupWebSocketSessionCache) {
        this.userService = userService;
        this.webSocketSessionCache = webSocketSessionCache;
        this.groupWebSocketSessionCache = groupWebSocketSessionCache;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession wsSession) throws Exception {
        Map<String, Object> attributes = wsSession.getAttributes();

        String groupId = getGroupIdFromSession(wsSession);
        if (groupId != null)
            groupWebSocketSessionCache.put(Integer.valueOf(groupId), wsSession);
        else if (attributes.containsKey("username") && attributes.get("username") != null) {
//            User user = dbService.getIdUser((String) attributes.get("username"));
            webSocketSessionCache.put((String) attributes.get("username"), wsSession);
//            System.out.println("put " + (String) attributes.get("username"));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession wsSession, CloseStatus status) throws Exception {
        String groupId = getGroupIdFromSession(wsSession);
        String username = (String) wsSession.getAttributes().getOrDefault("username", null);
        if (groupId != null) {
            // do groupWebSocketSessionCache.delete(Integer.valueOf(groupId)) in 5 min
        }

        if (username != null) {
            // do webSocketSessionCache.delete(username) in 5 min
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession wsSession, TextMessage message) throws Exception {
        Map<String, Object> attributes = wsSession.getAttributes();
        // {msgType=emphasis, HTTP.SESSION.ID=9C7D0D6592DDABA9D934E96FD3169E41, userId=cs185}

        String groupId = getGroupIdFromSession(wsSession);
        // if groupId shows up in the url, it is group chat
        if (groupId != null) {
            userService.sendGroupMessage((String) attributes.get("username"),
                    message.getPayload(),
                    Integer.parseInt(groupId));
        }
        // else it is a personal chat
        else {
            userService.sendPersonalMessage((String) attributes.get("username"), message.getPayload());
        }
    }

    private String getGroupIdFromSession(WebSocketSession wsSession) {
        Map<String, String> params = getQueryParams(Objects.requireNonNull(wsSession.getUri()));

        return params.getOrDefault("groupId", null);
    }

    private Map<String, String> getQueryParams(URI uri) {
        Map<String, String> queryParams = new HashMap<>();
        String query = uri.getQuery();
        if (query != null) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                int idx = pair.indexOf("=");
                String key = idx > 0 ? pair.substring(0, idx) : pair;
                String value = idx > 0 && pair.length() > idx + 1 ? pair.substring(idx + 1) : null;
                queryParams.put(key, value);
            }
        }
        return queryParams;
    }
}
