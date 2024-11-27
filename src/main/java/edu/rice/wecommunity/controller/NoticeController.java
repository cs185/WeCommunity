package edu.rice.wecommunity.controller;

import com.alibaba.fastjson.JSONObject;
import edu.rice.wecommunity.entity.Message;
import edu.rice.wecommunity.entity.Notice;
import edu.rice.wecommunity.entity.Page;
import edu.rice.wecommunity.entity.User;
import edu.rice.wecommunity.service.GroupService;
import edu.rice.wecommunity.service.MessageService;
import edu.rice.wecommunity.service.NoticeService;
import edu.rice.wecommunity.service.UserService;
import edu.rice.wecommunity.util.CommunityConstant;
import edu.rice.wecommunity.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.HtmlUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/notice")
public class NoticeController implements CommunityConstant {
    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private MessageService messageService;

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private UserService userService;

    @Autowired
    private GroupService groupService;

    @RequestMapping(path = "/list", method = RequestMethod.GET)
    public String getNoticeList(Model model) {
        User user = hostHolder.getUser();

        // 查询评论类通知
        Notice notice = noticeService.findLatestNotice(user.getId(), TOPIC_COMMENT);
        if (notice != null) {
            Map<String, Object> messageVO = new HashMap<>();

            messageVO.put("user", userService.findUserById(notice.getFromId()));
            messageVO.put("createTime", notice.getCreateTime());
            messageVO.put("entityType", notice.getEntityType());
            messageVO.put("entityId", notice.getEntityId());
            messageVO.put("postId", notice.getEntityId());

            int count = noticeService.findNoticeCount(user.getId(), TOPIC_COMMENT);
            messageVO.put("count", count);

            int unread = noticeService.findNoticeUnreadCount(user.getId(), TOPIC_COMMENT);
            messageVO.put("unread", unread);
            model.addAttribute("commentNotice", messageVO);
        }

        // 查询点赞类通知
        notice = noticeService.findLatestNotice(user.getId(), TOPIC_LIKE);
        if (notice != null) {
            Map<String, Object> messageVO = new HashMap<>();

            messageVO.put("user", userService.findUserById(notice.getFromId()));
            messageVO.put("createTime", notice.getCreateTime());
            messageVO.put("entityType", notice.getEntityType());
            messageVO.put("entityId", notice.getEntityId());
            messageVO.put("postId", notice.getEntityId());

            int count = noticeService.findNoticeCount(user.getId(), TOPIC_LIKE);
            messageVO.put("count", count);

            int unread = noticeService.findNoticeUnreadCount(user.getId(), TOPIC_LIKE);
            messageVO.put("unread", unread);
            model.addAttribute("likeNotice", messageVO);
        }

        // 查询关注类通知
        notice = noticeService.findLatestNotice(user.getId(), TOPIC_FOLLOW);
        if (notice != null) {
            Map<String, Object> messageVO = new HashMap<>();

            messageVO.put("user", userService.findUserById(notice.getFromId()));
            messageVO.put("createTime", notice.getCreateTime());
            messageVO.put("entityType", notice.getEntityType());
            messageVO.put("entityId", notice.getEntityId());
            messageVO.put("postId", notice.getEntityId());

            int count = noticeService.findNoticeCount(user.getId(), TOPIC_FOLLOW);
            messageVO.put("count", count);

            int unread = noticeService.findNoticeUnreadCount(user.getId(), TOPIC_FOLLOW);
            messageVO.put("unread", unread);

            model.addAttribute("followNotice", messageVO);
        }

        // 查询群组请求类通知
        notice = noticeService.findLatestNotice(user.getId(), TOPIC_REQUEST);
        if (notice != null) {
            Map<String, Object> messageVO = new HashMap<>();

            messageVO.put("user", userService.findUserById(notice.getFromId()));
            messageVO.put("createTime", notice.getCreateTime());
            messageVO.put("entityType", notice.getEntityType());
            messageVO.put("groupName", groupService.getGroup(notice.getEntityId()).getName());

            int count = noticeService.findNoticeCount(user.getId(), TOPIC_REQUEST);
            messageVO.put("count", count);

            int unread = noticeService.findNoticeUnreadCount(user.getId(), TOPIC_REQUEST);
            messageVO.put("unread", unread);

            model.addAttribute("requestNotice", messageVO);
        }

        // 查询未读消息数量
        int letterUnreadCount = messageService.findLetterUnreadCount(user.getId(), null);
        model.addAttribute("letterUnreadCount", letterUnreadCount);
        int noticeUnreadCount = noticeService.findNoticeUnreadCount(user.getId(), null);
        model.addAttribute("noticeUnreadCount", noticeUnreadCount);

        return "site/notice";
    }

    @RequestMapping(path = "/detail/{topic}", method = RequestMethod.GET)
    public String getNoticeDetail(@PathVariable("topic") String topic, Page page, Model model) {
        User user = hostHolder.getUser();

        page.setLimit(5);
        page.setPath("/notice/detail/" + topic);
        page.setRows(noticeService.findNoticeCount(user.getId(), topic));

        List<Notice> noticeList = noticeService.findNotices(user.getId(), topic, page.getOffset(), page.getLimit());
        List<Map<String, Object>> noticeVoList = new ArrayList<>();
        if (noticeList != null) {
            for (Notice notice : noticeList) {
                Map<String, Object> map = new HashMap<>();
                // 通知
                map.put("notice", notice);
                // 内容

                map.put("user", hostHolder.getUser());
                map.put("fromUser", userService.findUserById(notice.getFromId()));
                map.put("entityType", notice.getEntityType());
                map.put("entityId", notice.getEntityId());

                if (topic.equals(TOPIC_REQUEST))
                    map.put("groupName", groupService.getGroup(notice.getEntityId()).getName());

                noticeVoList.add(map);
            }
        }
        model.addAttribute("notices", noticeVoList);

        // 设置已读
         List<Integer> ids = noticeService.findNotices(user.getId(), noticeList);
        if (!ids.isEmpty()) {
            noticeService.readNotice(ids);
        }

        return "site/notice-detail";
    }
}
