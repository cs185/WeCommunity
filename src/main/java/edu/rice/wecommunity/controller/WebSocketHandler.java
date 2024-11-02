package edu.rice.wecommunity.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import edu.rice.wecommunity.entity.Event;
import edu.rice.wecommunity.entity.User;
import edu.rice.wecommunity.event.EventProducer;
import edu.rice.wecommunity.service.GroupService;
import edu.rice.wecommunity.service.MessageService;
import edu.rice.wecommunity.util.CommunityConstant;
import edu.rice.wecommunity.util.WebSocketSessionMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class WebSocketHandler extends TextWebSocketHandler implements CommunityConstant {

    @Autowired
    private WebSocketSessionMap webSocketSessionMap;

    @Autowired
    private EventProducer eventProducer;

    @Autowired
    private MessageService messageService;

    @Autowired
    private GroupService groupService;

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

    @Override
    protected void handleTextMessage(WebSocketSession wsSession, TextMessage message) throws Exception {
        String groupId = getGroupIdFromSession(wsSession);
        User sender = (User) wsSession.getAttributes().get("user");
        JsonObject msgJson = JsonParser.parseString(message.getPayload()).getAsJsonObject();
        int targetId = Integer.parseInt(String.valueOf(msgJson.get("targetId")));
        String content = String.valueOf(msgJson.get("message")).replaceAll("^\"|\"$", "");;
        int type = Integer.parseInt(String.valueOf(msgJson.get("type")));

        if (type == ENTITY_TYPE_GROUP) {
            groupService.sendGroupMessage(sender.getId(), content, Integer.parseInt(groupId));
        }
        // else it is a personal chat
        else {
            messageService.sendPersonalMessage(content, targetId);
        }

        Event event = new Event()
                .setTopic(TOPIC_CHAT)
                .setUserId(sender.getId())
                .setEntityType(type)
//                .setEntityId(id)
                .setData("targetId", targetId)
                .setData("content", content);

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
