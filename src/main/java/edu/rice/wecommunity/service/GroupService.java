package edu.rice.wecommunity.service;

import com.google.gson.JsonObject;
import edu.rice.wecommunity.dao.GroupMapper;
import edu.rice.wecommunity.dao.UserMapper;
import edu.rice.wecommunity.entity.Group;
import edu.rice.wecommunity.entity.User;
import edu.rice.wecommunity.util.RedisKeyUtil;
import edu.rice.wecommunity.util.WebSocketSessionMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;
import java.util.Set;


@Service
public class GroupService {
    private GroupMapper groupMapper;

    @Autowired
    private MessageService messageService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private WebSocketSessionMap webSocketSessionMap;

    public Group getGroup(int group_id) {
        return groupMapper.selectById(group_id);
    }

    public List<Group> getUserGroups(int userId) {
        return getUserGroupIds(userId).stream().map(groupMapper::selectById).toList();
    }

    public User getGroupOwner(int groupId) {
        return userMapper.selectById(groupMapper.selectOwnerId(groupId));
    }

    public List<User> getGroupMembers(int groupId) {
        return groupMapper.selectUsersFromGroup(groupId);
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

    public int addUserToGroup(int userId, int groupId) {
        int count = groupMapper.selectGroupCount(groupId);
        int capacity = groupMapper.selectGroupCapacity(groupId);
        Integer found = groupMapper.selectUserId(userId, groupId);
        if (count >= capacity || found == null) return 0;

        synchronized (this) {
            count = groupMapper.selectGroupCount(groupId);
            capacity = groupMapper.selectGroupCapacity(groupId);
            found = groupMapper.selectUserId(userId, groupId);
            if (count >= capacity || found == null) return 0;

            groupMapper.addUserToGroupByUserId(userId, groupId);
        }
        return 1;
    }

    public int deleteUserFromGroup(int userId, int groupId) {
        Integer found = groupMapper.selectUserId(userId, groupId);
        if (found == null) return 0;

        synchronized (this) {
            found = groupMapper.selectUserId(userId, groupId);
            if (found == null) return 0;

            groupMapper.deleteUserFromGroupByUserId(userId, groupId);
            return 1;
        }
    }

    @Deprecated
    public void addUserToGroupCache(int userId, int groupId) {
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

    @Deprecated
    public void deleteUserFromGroupCache(int userId, int groupId) {
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

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int createGroup(String groupName, int userId) {
        Group group = new Group();
        group.setName(groupName);
        group.setOwnerId(userId);
        groupMapper.insertGroup(group);

        addUserToGroup(userId, group.getId());
        return group.getId();
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public void deleteGroup(int groupId) {
        groupMapper.clearUsers(groupId);
        groupMapper.deleteGroup(groupId);
    }

}
