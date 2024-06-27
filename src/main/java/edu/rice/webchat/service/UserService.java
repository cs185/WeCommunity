package edu.rice.webchat.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import edu.rice.webchat.cache.GroupCache;
import edu.rice.webchat.cache.GroupWebSocketSessionCache;
import edu.rice.webchat.cache.UserCache;
import edu.rice.webchat.cache.WebSocketSessionCache;
import edu.rice.webchat.dao.GroupMapper;
import edu.rice.webchat.dao.UserMapper;
import edu.rice.webchat.entity.group.AChatGroup;
import edu.rice.webchat.entity.group.GroupFac;
import edu.rice.webchat.entity.message.Message;
import edu.rice.webchat.entity.message.MsgFac;
import edu.rice.webchat.entity.msgStrat.AStrategy;
import edu.rice.webchat.entity.msgStrat.MsgStratFac;
import edu.rice.webchat.entity.user.User;
import edu.rice.webchat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;


@Service
public class UserService {
    private final MsgFac msgFac;
    private final GroupFac groupFac;
    private final WebSocketSessionCache webSocketSessionCache;
    private final MsgStratFac msgStratFac;
    private final GroupService groupService;
    private final UserCache userCache;
    private final GroupCache groupCache;
    private final DBService dbService;
    private final GroupWebSocketSessionCache groupWebSocketSessionCache;
    private final UserRepository userRepository;
    private final GroupMapper groupMapper;

    @Autowired
    public UserService(MsgFac msgFac, GroupFac groupFac, WebSocketSessionCache webSocketSessionCache, MsgStratFac msgStratFac, GroupService groupService, UserCache userCache, GroupCache groupCache, DBService dbService, GroupWebSocketSessionCache groupWebSocketSessionCache, UserRepository userRepository, GroupMapper groupMapper) {
        this.msgFac = msgFac;
        this.groupFac = groupFac;
        this.webSocketSessionCache = webSocketSessionCache;
        this.msgStratFac = msgStratFac;
        this.groupService = groupService;
        this.userCache = userCache;
        this.groupCache = groupCache;
        this.dbService = dbService;
        this.groupWebSocketSessionCache = groupWebSocketSessionCache;
        this.userRepository = userRepository;
        this.groupMapper = groupMapper;
    }

    // todo: put it to repository

    public void userLogout(String username) {
        userCache.delete(username);
    }
    
    public void sendGroupMessage(String username, String messageText, int group_id) {
        JsonObject msgJson = JsonParser.parseString(messageText).getAsJsonObject();

        User sender = userRepository.getUser(username);

        String content = msgJson.get("message").getAsString();
        Message message = msgFac.makeMsg(content, sender);

        String msgType = msgJson.get("strategy").getAsString();
        message.setStrategyType(msgType);
        AStrategy strategy = msgStratFac.makeStrat(msgType);
        strategy.renderMsg(message);

        AChatGroup group = groupService.getGroup(group_id);
        dbService.addMsgToGroup(group.getId(), message, sender.getUsername());
        broadcastMessage(group.getId(), message);
    }

    public void broadcastMessage(int group_id, Message message) {
        // todo: do not directly get it from cache
        groupWebSocketSessionCache.get(group_id).stream().filter(WebSocketSession::isOpen).forEach(session -> {
            try {
                JsonObject jo = new JsonObject();
                String htmlContent = message.getRenderedMsg();
                jo.addProperty("userMessage", htmlContent);
                session.sendMessage(new TextMessage(jo.toString()));
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
    }

    public AChatGroup createChatGroup(String username, String name) {
        User user = userRepository.getUser(username);
        AChatGroup group = groupFac.makeGroup(name, user.getId());  // 这是空值？
        groupCache.put(group.getId(), group);
        groupMapper.addUserToGroup(username, group.getId());
        return group;
    }

    public void joinChatGroup(String username, int group_id) {
        groupService.addUserToGroup(username, group_id);
    }

    // todo: store personal message in database as well
    public void sendPersonalMessage(String userId, String messageText) {
        JsonObject msgJson = JsonParser.parseString(messageText).getAsJsonObject();

        User user = userRepository.getUser(userId);

        String content = msgJson.get("message").getAsString();
        Message message = msgFac.makeMsg(content, user);

        String targetUserName = msgJson.get("targetUsername").getAsString();
        WebSocketSession chatSession = webSocketSessionCache.get(targetUserName);
        if (chatSession == null) {
            System.out.println("user not online.");
            return;
        }

        try {
            JsonObject jo = new JsonObject();
            String htmlContent = message.getRenderedMsg();
            // todo: add htmlContent to mongodb
            jo.addProperty("userMessage", htmlContent);
            if (chatSession.isOpen())
                chatSession.sendMessage(new TextMessage(jo.toString()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
