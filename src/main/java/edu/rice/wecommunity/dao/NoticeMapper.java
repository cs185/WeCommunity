package edu.rice.wecommunity.dao;

import edu.rice.wecommunity.entity.Message;
import edu.rice.wecommunity.entity.Notice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NoticeMapper {
    // 查询某个主题下最新的通知
    Message selectLatestNotice(int userId, String topic);

    int batchInsertNoticess(@Param("noticeList") List<Notice> noticeList);

    // 查询某个主题所包含的通知数量
    int selectNoticeCount(int userId, String topic);

    // 查询未读的通知的数量
    int selectNoticeUnreadCount(int userId, String topic);

    // 查询某个主题所包含的通知列表
    List<Message> selectNotices(int userId, String topic, int offset, int limit);

    void insertNotice(Notice notice);
}
