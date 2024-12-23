package edu.rice.wecommunity.dao;

import edu.rice.wecommunity.entity.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MessageMapper {

    // 查询当前用户的会话列表,针对每个会话只返回一条最新的私信.
    List<Message> findNotices();

    List<Message> selectConversations(int userId, int offset, int limit);

    List<Message> selectGroupConversations(int userId, int offset, int limit);

    // 查询当前用户的会话数量.
    int selectConversationCount(int userId);

    // 查询某个会话所包含的私信列表.
    List<Message> selectLetters(String conversationId1, String conversationId2, int offset, int limit);

    // 查询某个会话所包含的私信数量.
    int selectLetterCount(String conversationId);

    int selectGroupLetterCount(int groupId);

    // 查询未读私信的数量
    int selectLetterUnreadCount(int userId, String conversationId);

    // 新增消息
    int insertMessage(Message message);

    // 修改消息的状态
    int updateStatus(List<Integer> ids, int status);

    int selectGroupLetterUnreadCount(int userId, int groupId);

    List<Message> selectGroupLetters(int groupId, int offset, int limit);

    void clearGroupMessages(int groupId);

    List<Message> findAll();

    void deleteAll();

    void batchInsertMessages(@Param("mList") List<Message> mList);
}
