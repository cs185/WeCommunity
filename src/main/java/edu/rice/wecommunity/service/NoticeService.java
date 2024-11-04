package edu.rice.wecommunity.service;

import edu.rice.wecommunity.dao.NoticeMapper;
import edu.rice.wecommunity.entity.Message;
import edu.rice.wecommunity.entity.Notice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class NoticeService {
    @Autowired
    private NoticeMapper noticeMapper;

    public Notice findLatestNotice(int userId, String topic) {
        return noticeMapper.selectLatestNotice(userId, topic);
    }

    public int readNotice(List<Integer> ids) {
        return noticeMapper.updateStatus(ids, 1);
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

    public List<Notice> findNotices(int userId, String topic, int offset, int limit) {
        return noticeMapper.selectNotices(userId, topic, offset, limit);
    }

    public List<Integer> findNotices(int userId, List<Notice> noticeList) {
        List<Integer> ids = new ArrayList<>();

        if (noticeList != null) {
            for (Notice notice : noticeList) {
                if (userId == notice.getToId() && notice.getStatus() == 0) {
                    ids.add(notice.getId());
                }
            }
        }

        return ids;
    }

    public void deleteNotice(int noticeId) {
        List<Integer> tmp = new ArrayList<>();
        tmp.add(noticeId);
        noticeMapper.updateStatus(tmp, 2);
    }
}
