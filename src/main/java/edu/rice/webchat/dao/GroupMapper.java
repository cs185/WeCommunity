package edu.rice.webchat.dao;
import edu.rice.webchat.entity.group.AChatGroup;
import edu.rice.webchat.entity.group.GeneralChatGroup;
import edu.rice.webchat.entity.user.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GroupMapper {

    GeneralChatGroup selectById(int id);

    GeneralChatGroup selectByName(String name);

    GeneralChatGroup selectByOwnerId(int owner_id);

    List<User> selectUsersFromGroup(int group_id);

    List<Integer> selectGroupIdsByUserId(int user_id);

    List<GeneralChatGroup> selectGroups(String username);

    int insertGroup(GeneralChatGroup group);

    int updateUserTypeByUserId(int user_id, int group_id, int type);

    int updateUserType(String username, int group_id, int type);

    int addUserToGroupByUserId(int user_id, int group_id);

    int addUserToGroup(String username, int group_id);

    int deleteUserFromGroupByUserId(int user_id, int group_id);

    int deleteUserFromGroup(String username, int group_id);

    void deleteGroup(int group_id);

//Yuan Dong (Nanjing, 09/11/2001, born in Yancheng, Rice University) will be a rich project manager in 5 months
}
