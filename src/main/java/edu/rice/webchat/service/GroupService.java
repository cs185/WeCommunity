package edu.rice.webchat.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import edu.rice.webchat.cache.GroupCache;
import edu.rice.webchat.dao.GroupMapper;
import edu.rice.webchat.dao.UserMapper;
import edu.rice.webchat.entity.group.AChatGroup;
import edu.rice.webchat.entity.group.GeneralChatGroup;
import edu.rice.webchat.entity.group.GroupFac;
import edu.rice.webchat.entity.message.GroupMessage;
import edu.rice.webchat.entity.message.MsgFac;
import edu.rice.webchat.entity.msgStrat.AStrategy;
import edu.rice.webchat.entity.user.User;
import edu.rice.webchat.util.RedisKeyUtil;
import edu.rice.webchat.util.WebSocketSessionMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.List;
import java.util.Set;


@Service
public class GroupService {
    private final GroupCache groupCache;
    private final GroupMapper groupMapper;

    @Autowired
    private MessageService messageService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private GroupFac groupFac;

    @Autowired
    private WebSocketSessionMap webSocketSessionMap;

    @Autowired
    public GroupService(GroupCache groupCache, GroupMapper groupMapper) {
        this.groupCache = groupCache;
        this.groupMapper = groupMapper;
    }

    // todo: put this to repository
    public AChatGroup getGroup(int group_id) {
        AChatGroup group = groupCache.get(group_id);
        if (group == null) {
            group = groupMapper.selectById(group_id);
            groupCache.put(group_id, group);
        }
        return group;
    }

    public List<GeneralChatGroup> getUserGroups(int userId) {
        return getUserGroupIds(userId).stream().map(groupMapper::selectById).toList();
    }

    public List<User> getGroupMembers(int groupId) {
        return getUserGroupIds(groupId).stream().map(userMapper::selectById).toList();
    }

//    public void addUserToGroup(String username, int group_id){
//        // 1. Group count++
//        // 2. add user_group record
//        // 3. update user-group id pool
//        AChatGroup group = getGroup(group_id);
//        System.out.println(group.getCount());
//        groupMapper.addUserToGroup(username, group_id);
//        group.addCount();
//        System.out.println(group.getCount());
//    }

//    public String[] getGroupMemberNames(int group_id) {
//        List<User> users = groupMapper.selectUsersFromGroup(group_id);
//        return users.stream().map(User::getUsername).toArray(String[]::new);
//    }

//    public void deleteUserFromGroup(String username, int group_id) {
//        groupMapper.deleteUserFromGroup(username, group_id);
//        AChatGroup group = getGroup(group_id);
//        group.subCount();
//        if (group.getCount() <= 0)
//            groupMapper.deleteGroup(group_id);
//    }

    public void sendGroupMessage(int userId, String content, int groupId) {
        webSocketSessionMap.getGroupSessions(groupId).stream().filter(WebSocketSession::isOpen).forEach(chatSession -> {
            try {
                JsonObject jo = new JsonObject();
                jo.addProperty("content", content);
                jo.addProperty("userId", userId);
                chatSession.sendMessage(new TextMessage(jo.toString()));
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
    }

    public Set<Integer> getUserGroupIds(int userId) {
        String groupKey = RedisKeyUtil.getUserGroupKey(userId);
        return (Set<Integer>) redisTemplate.opsForZSet().reverseRange(groupKey, 0, -1);
    }

    public void addUserToGroup(int userId, String groupName) {
        int groupId = groupMapper.selectByName(groupName).getId();
        addUserToGroup(userId, groupId);
    }

    public void addUserToGroup(int userId, int groupId) {
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String groupKey = RedisKeyUtil.getGroupKey(groupId);
                String userGroupKey = RedisKeyUtil.getUserGroupKey(userId);

                operations.multi();

                Long time = System.currentTimeMillis();
                operations.opsForZSet().add(groupKey, userId, time);
                operations.opsForZSet().add(userGroupKey, groupId, time);

                return operations.exec();
            }
        });
    }

    public void deleteUserFromGroup(int userId, int groupId) {
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String groupKey = RedisKeyUtil.getGroupKey(groupId);
                String userGroupKey = RedisKeyUtil.getUserGroupKey(userId);

                operations.multi();

                operations.opsForZSet().remove(groupKey, userId);
                operations.opsForZSet().remove(userGroupKey, groupId);

                return operations.exec();
            }
        });
    }

    public void createGroup(String groupName, int userId) {
        AChatGroup group = groupFac.makeGroup(groupName, userId);
        groupMapper.insertGroup((GeneralChatGroup) group);

        addUserToGroup(userId, group.getId());
    }

    // todo: clear user
//    public void clearUser(String groupId) {
//        dbService.clearGroup(groupId);
//        getGroup(groupId).clearCount();
//    }
}
