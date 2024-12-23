package edu.rice.wecommunity.dao;

import edu.rice.wecommunity.entity.Group;
import edu.rice.wecommunity.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GroupMapper {

    Group selectById(int id);

    Group selectByName(String name);

    Group selectByOwnerId(int owner_id);

    List<User> selectUsersFromGroup(int group_id);

    List<Integer> selectGroupIdsByUserId(int user_id);

    List<Group> selectGroupsById(int userId);

    int insertGroup(Group group);

    int updateUserTypeByUserId(int user_id, int group_id, int type);

    int updateUserType(String username, int group_id, int type);

    int addUserToGroupByUserId(int userId, int groupId, int type);

    int deleteUserFromGroupByUserId(int userId, int groupId);

    void clearUsers(int groupId);

    void deleteGroup(int groupId);

    int selectOwnerId(int groupId);

    int selectGroupCount(int groupId);

    int selectGroupCapacity(int groupId);

    Integer selectUserId(int userId, int groupId);


//Yuan Dong (Nanjing, 09/11/2001, born in Yancheng, Rice University) will be a rich project manager in 5 months
}
