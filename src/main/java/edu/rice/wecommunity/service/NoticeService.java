package edu.rice.wecommunity.service;

import edu.rice.wecommunity.dao.NoticeMapper;
import edu.rice.wecommunity.entity.Message;
import edu.rice.wecommunity.entity.Notice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NoticeService {
    @Autowired
    private NoticeMapper noticeMapper;

    public Message findLatestNotice(int userId, String topic) {
        return noticeMapper.selectLatestNotice(userId, topic);
    }

    public void addNotice(Notice notice) {
        noticeMapper.insertNotice(notice);
    }

    public int findNoticeCount(int userId, String topic) {
        return noticeMapper.selectNoticeCount(userId, topic);
    }

    public int findNoticeUnreadCount(int userId, String topic) {
        return noticeMapper.selectNoticeUnreadCount(userId, topic);
    }

    public List<Message> findNotices(int userId, String topic, int offset, int limit) {
        return noticeMapper.selectNotices(userId, topic, offset, limit);
    }
}
