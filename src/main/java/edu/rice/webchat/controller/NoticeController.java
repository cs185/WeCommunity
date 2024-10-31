package edu.rice.webchat.controller;

import com.alibaba.fastjson.JSONObject;
import edu.rice.webchat.entity.Message;
import edu.rice.webchat.entity.Page;
import edu.rice.webchat.entity.user.User;
import edu.rice.webchat.service.MessageService;
import edu.rice.webchat.service.NoticeService;
import edu.rice.webchat.service.UserService;
import edu.rice.webchat.util.HostHolder;
import edu.rice.webchat.util.WebChatUtil;
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
public class NoticeController {
    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private MessageService messageService;

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private UserService userService;

    @RequestMapping(path = "/list", method = RequestMethod.GET)
    public String getNoticeList(Model model) {
        User user = hostHolder.getUser();

        // 查询评论类通知
        Message message = noticeService.findLatestNotice(user.getId(), WebChatUtil.TOPIC_COMMENT);
        Map<String, Object> messageVO = new HashMap<>();
        if (message != null) {
            messageVO.put("message", message);

            String content = HtmlUtils.htmlUnescape(message.getContent());
            Map<String, Object> data = JSONObject.parseObject(content, HashMap.class);

            messageVO.put("user", userService.findUserById((Integer) data.get("userId")));
            messageVO.put("entityType", data.get("entityType"));
            messageVO.put("entityId", data.get("entityId"));
            messageVO.put("postId", data.get("postId"));

            int count = noticeService.findNoticeCount(user.getId(), WebChatUtil.TOPIC_COMMENT);
            messageVO.put("count", count);

            int unread = noticeService.findNoticeUnreadCount(user.getId(), WebChatUtil.TOPIC_COMMENT);
            messageVO.put("unread", unread);
        }
        model.addAttribute("commentNotice", messageVO);

        // 查询点赞类通知
        message = noticeService.findLatestNotice(user.getId(), WebChatUtil.TOPIC_LIKE);
        messageVO = new HashMap<>();
        if (message != null) {
            messageVO.put("message", message);

            String content = HtmlUtils.htmlUnescape(message.getContent());
            Map<String, Object> data = JSONObject.parseObject(content, HashMap.class);

            messageVO.put("user", userService.findUserById((Integer) data.get("userId")));
            messageVO.put("entityType", data.get("entityType"));
            messageVO.put("entityId", data.get("entityId"));
            messageVO.put("postId", data.get("postId"));

            int count = noticeService.findNoticeCount(user.getId(), WebChatUtil.TOPIC_LIKE);
            messageVO.put("count", count);

            int unread = noticeService.findNoticeUnreadCount(user.getId(), WebChatUtil.TOPIC_LIKE);
            messageVO.put("unread", unread);
        }
        model.addAttribute("likeNotice", messageVO);

        // 查询关注类通知
        message = noticeService.findLatestNotice(user.getId(), WebChatUtil.TOPIC_FOLLOW);
        messageVO = new HashMap<>();
        if (message != null) {
            messageVO.put("message", message);

            String content = HtmlUtils.htmlUnescape(message.getContent());
            Map<String, Object> data = JSONObject.parseObject(content, HashMap.class);

            messageVO.put("user", userService.findUserById((Integer) data.get("userId")));
            messageVO.put("entityType", data.get("entityType"));
            messageVO.put("entityId", data.get("entityId"));

            int count = noticeService.findNoticeCount(user.getId(), WebChatUtil.TOPIC_FOLLOW);
            messageVO.put("count", count);

            int unread = noticeService.findNoticeUnreadCount(user.getId(), WebChatUtil.TOPIC_FOLLOW);
            messageVO.put("unread", unread);
        }
        model.addAttribute("followNotice", messageVO);

        // 查询未读消息数量
        int letterUnreadCount = messageService.findLetterUnreadCount(user.getId(), null);
        model.addAttribute("letterUnreadCount", letterUnreadCount);
        int noticeUnreadCount = noticeService.findNoticeUnreadCount(user.getId(), null);
        model.addAttribute("noticeUnreadCount", noticeUnreadCount);

        return "/site/notice";
    }

    @RequestMapping(path = "/notice/detail/{topic}", method = RequestMethod.GET)
    public String getNoticeDetail(@PathVariable("topic") String topic, Page page, Model model) {
        User user = hostHolder.getUser();

        page.setLimit(5);
        page.setPath("/notice/detail/" + topic);
        page.setRows(noticeService.findNoticeCount(user.getId(), topic));

        List<Message> noticeList = noticeService.findNotices(user.getId(), topic, page.getOffset(), page.getLimit());
        List<Map<String, Object>> noticeVoList = new ArrayList<>();
        if (noticeList != null) {
            for (Message notice : noticeList) {
                Map<String, Object> map = new HashMap<>();
                // 通知
                map.put("notice", notice);
                // 内容
                String content = HtmlUtils.htmlUnescape(notice.getContent());
                Map<String, Object> data = JSONObject.parseObject(content, HashMap.class);
                map.put("user", userService.findUserById((Integer) data.get("userId")));
                map.put("entityType", data.get("entityType"));
                map.put("entityId", data.get("entityId"));
                map.put("postId", data.get("postId"));
                // 通知作者
                map.put("fromUser", userService.findUserById(notice.getFromId()));

                noticeVoList.add(map);
            }
        }
        model.addAttribute("notices", noticeVoList);

        // 设置已读
        List<Integer> ids = messageService.getLetterIds(user.getId(), noticeList);
        if (!ids.isEmpty()) {
            messageService.readMessage(ids);
        }

        return "/site/notice-detail";
    }
}
