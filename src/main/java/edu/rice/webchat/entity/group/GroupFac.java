package edu.rice.webchat.entity.group;

import edu.rice.webchat.dao.GroupMapper;
import org.springframework.stereotype.Component;

@Component
public class GroupFac {

    private final GroupMapper groupMapper;

    public GroupFac(GroupMapper groupMapper) {
        this.groupMapper = groupMapper;
    }

    public AChatGroup makeGroup(String name, int owner_id){
        GeneralChatGroup group = new GeneralChatGroup(name, owner_id);
        groupMapper.insertGroup(group);
        return group;
    }

    public AChatGroup makeGroup(String name, int owner_id, int capacity){
        GeneralChatGroup group =  new GeneralChatGroup(name, owner_id);
        groupMapper.insertGroup(group);
        group.setCapacity(capacity);
        return group;
    }
}
