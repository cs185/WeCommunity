package edu.rice.webchat.service;

import edu.rice.webchat.cache.GroupCache;
import edu.rice.webchat.dao.GroupMapper;
import edu.rice.webchat.entity.group.AChatGroup;
import edu.rice.webchat.entity.group.GeneralChatGroup;
import edu.rice.webchat.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;


@Service
public class GroupService {
    private final GroupCache groupCache;
    private final GroupMapper groupMapper;

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

    public HashMap<Integer, String> getUserGroupNames(String username) {
        List<GeneralChatGroup> groups = groupMapper.selectGroups(username);
        HashMap<Integer, String> groupNames = new HashMap<>();
        for (AChatGroup group : groups) {
            groupNames.put(group.getId(), group.getName());
        }
        return groupNames;
    }

    public void addUserToGroup(String username, int group_id){
        // 1. Group count++
        // 2. add user_group record
        // 3. update user-group id pool
        AChatGroup group = getGroup(group_id);
        System.out.println(group.getCount());
        groupMapper.addUserToGroup(username, group_id);
        group.addCount();
        System.out.println(group.getCount());
    }

    public String[] getGroupMemberNames(int group_id) {
        List<User> users = groupMapper.selectUsersFromGroup(group_id);
        return users.stream().map(User::getUsername).toArray(String[]::new);
    }

    public void deleteUserFromGroup(String username, int group_id) {
        groupMapper.deleteUserFromGroup(username, group_id);
        AChatGroup group = getGroup(group_id);
        group.subCount();
        if (group.getCount() <= 0)
            groupMapper.deleteGroup(group_id);
    }

    // todo: clear user
//    public void clearUser(String groupId) {
//        dbService.clearGroup(groupId);
//        getGroup(groupId).clearCount();
//    }
}
