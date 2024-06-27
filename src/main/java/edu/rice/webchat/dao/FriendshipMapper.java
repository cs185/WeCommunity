package edu.rice.webchat.dao;
import edu.rice.webchat.entity.user.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface FriendshipMapper {
    Integer selectFriendshipStatusByIds(int id1, int id2);

    List<User> selectFriendsByUsername(String username);

    int insertFriendship(@Param("friendshipMap") Map<String, Object> friendshipMap);

    int updateFriendshipStatus(int id1, int id2, int status);

    void deleteFriendship(int id1, int id2);
}
