package edu.rice.webchat.handler;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import edu.rice.webchat.cache.GroupWebSocketSessionCache;
import edu.rice.webchat.cache.WebSocketSessionCache;
import edu.rice.webchat.entity.Comment;
import edu.rice.webchat.entity.Event;
import edu.rice.webchat.entity.post.DiscussPost;
import edu.rice.webchat.entity.user.User;
import edu.rice.webchat.event.EventProducer;
import edu.rice.webchat.service.MessageService;
import edu.rice.webchat.util.HostHolder;
import edu.rice.webchat.service.UserService;
import edu.rice.webchat.util.WebChatUtil;
import edu.rice.webchat.util.WebSocketSessionMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.net.URI;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static edu.rice.webchat.util.WebChatUtil.ENTITY_TYPE_CHAT;
import static edu.rice.webchat.util.WebChatUtil.ENTITY_TYPE_GROUP;

@Component
public class MyWebSocketHandler extends TextWebSocketHandler {

    @Autowired
    private WebSocketSessionMap webSocketSessionMap;

    @Autowired
    private EventProducer eventProducer;

    @Override
    public void afterConnectionEstablished(WebSocketSession wsSession) throws Exception {
//        Map<String, Object> attributes = wsSession.getAttributes();

        String groupId = getGroupIdFromSession(wsSession);
        if (groupId != null)
            webSocketSessionMap.setGroupSession(Integer.valueOf(groupId), wsSession);
        else {
            User user = (User) wsSession.getAttributes().get("user");

            webSocketSessionMap.setUserSession(user.getId(), wsSession);
//            System.out.println("put " + (String) attributes.get("username"));
        }
    }

//    @Override
//    public void afterConnectionClosed(WebSocketSession wsSession, CloseStatus status) throws Exception {
//        String groupId = getGroupIdFromSession(wsSession);
//        String username = hostHolder.getUser().getUsername();
//        if (groupId != null) {
//            // do groupWebSocketSessionCache.delete(Integer.valueOf(groupId)) in 5 min
//        }
//
//        if (username != null) {
//            // do webSocketSessionCache.delete(username) in 5 min
//        }
//    }

    @Override
    protected void handleTextMessage(WebSocketSession wsSession, TextMessage message) throws Exception {
//        String groupId = getGroupIdFromSession(wsSession);
        User sender = (User) wsSession.getAttributes().get("user");
        JsonObject msgJson = JsonParser.parseString(message.getPayload()).getAsJsonObject();
        String targetId = String.valueOf(msgJson.get("targetId"));
        int type = String.valueOf(msgJson.get("type")).equals("personal") ? ENTITY_TYPE_CHAT : ENTITY_TYPE_GROUP;

//        // if groupId shows up in the url, it is group chat
//        if (groupId != null) {
//            type = WebChatUtil.ENTITY_TYPE_GROUP;
//            userService.sendGroupMessage(sender.getUsername(),
////                    message.getPayload(),
////                    Integer.parseInt(groupId));
//        }
//        // else it is a personal chat
//        else {
//            type = WebChatUtil.ENTITY_TYPE_CHAT;
//            messageService.sendPersonalMessage(message.getPayload());
//        }

        Event event = new Event()
                .setTopic(WebChatUtil.TOPIC_CHAT)
                .setUserId(sender.getId())
                .setEntityType(type)
//                .setEntityId(id)
                .setData("targetId", targetId)
                .setData("content", msgJson.get("content"));

        eventProducer.fireEvent(event);
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
