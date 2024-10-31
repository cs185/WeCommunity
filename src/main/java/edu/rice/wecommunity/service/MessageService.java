package edu.rice.wecommunity.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import edu.rice.wecommunity.dao.MessageMapper;
import edu.rice.wecommunity.entity.Message;
import edu.rice.wecommunity.entity.User;
import edu.rice.wecommunity.util.SensitiveFilter;
import edu.rice.wecommunity.util.WebSocketSessionMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.util.HtmlUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Autowired
    private UserService userService;

    @Autowired
    private WebSocketSessionMap webSocketSessionMap;

    public List<Message> findConversations(int userId, int offset, int limit) {
        return messageMapper.selectConversations(userId, offset, limit);
    }

    public int findConversationCount(int userId) {
        return messageMapper.selectConversationCount(userId);
    }

    public List<Message> findLetters(String conversationId1, String conversationId2, int offset, int limit) {
        return messageMapper.selectLetters(conversationId1, conversationId2, offset, limit);
    }

    public int findLetterCount(String conversationId) {
        return messageMapper.selectLetterCount(conversationId);
    }

    public int findLetterUnreadCount(int userId, String conversationId) {
        return messageMapper.selectLetterUnreadCount(userId, conversationId);
    }

    public int addMessage(Message message) {
        message.setContent(HtmlUtils.htmlEscape(message.getContent()));
        message.setContent(sensitiveFilter.filter(message.getContent()));
        return messageMapper.insertMessage(message);
    }

    public List<Integer> getLetterIds(int userId, List<Message> letterList) {
        List<Integer> ids = new ArrayList<>();

        if (letterList != null) {
            for (Message message : letterList) {
                if (userId == message.getToId() && message.getStatus() == 0) {
                    ids.add(message.getId());
                }
            }
        }

        return ids;
    }

    public int readMessage(List<Integer> ids) {
        return messageMapper.updateStatus(ids, 1);
    }

    public Map<String, User> getConversationUsers(String conversationId, int userId) {
        Map<String, User> users = new HashMap<>();
        String[] ids = conversationId.split("_");
        int id0 = Integer.parseInt(ids[0]);
        int id1 = Integer.parseInt(ids[1]);

        User user1 = userService.findUserById(id0);
        User user2 = userService.findUserById(id1);

        users.put("thisUser", userId == id0 ? user1 : user2);
        users.put("targetUser", userId == id0 ? user2 : user1);

        return users;
    }

    public void sendPersonalMessage(String content, int targetId) {
        WebSocketSession chatSession = webSocketSessionMap.getUserSession(targetId);
        if (chatSession == null || !chatSession.isOpen()) {
            System.out.println("user not online.");
            return;
        }

        try {
//            JsonObject jo = new JsonObject();
//            String htmlContent = message.getRenderedMsg();
//            jo.addProperty("userMessage", content);
            chatSession.sendMessage(new TextMessage(content));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public void sendPersonalMessage(String content, int targetId, int senderId) {
//        WebSocketSession chatSession = webSocketSessionMap.getUserSession(targetId);
//        if (chatSession == null || !chatSession.isOpen()) {
//            System.out.println("user not online.");
//            return;
//        }
//
//        try {
////            JsonObject jo = new JsonObject();
////            String htmlContent = message.getRenderedMsg();
////            jo.addProperty("userMessage", content);
//            JsonObject jo = new JsonObject();
//            jo.addProperty("content", content);
//            jo.addProperty("userId", senderId);
//            chatSession.sendMessage(new TextMessage(jo.toString()));
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


}
