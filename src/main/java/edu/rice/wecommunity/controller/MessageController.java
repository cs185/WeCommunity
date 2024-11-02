package edu.rice.wecommunity.controller;

import com.alibaba.fastjson.JSONObject;
import edu.rice.wecommunity.annotation.LoginRequired;
import edu.rice.wecommunity.entity.Group;
import edu.rice.wecommunity.entity.Message;
import edu.rice.wecommunity.entity.Page;
import edu.rice.wecommunity.entity.User;
import edu.rice.wecommunity.service.GroupService;
import edu.rice.wecommunity.service.MessageService;
import edu.rice.wecommunity.service.NoticeService;
import edu.rice.wecommunity.service.UserService;
import edu.rice.wecommunity.util.CommunityUtil;
import edu.rice.wecommunity.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import java.util.*;

@Controller
@RequestMapping("/letter")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private GroupService groupService;

    // 私信列表
    @LoginRequired
    @RequestMapping(path = "/list", method = RequestMethod.GET)
    public String getLetterList(Model model, Page page) {
        User user = hostHolder.getUser();
        // 分页信息
        page.setLimit(5);
        page.setPath("/letter/list");
        page.setRows(messageService.findConversationCount(user.getId()));

        // 会话列表
        List<Message> conversationList = messageService.findConversations(
                user.getId(), page.getOffset(), page.getLimit());
        List<Map<String, Object>> conversations = new ArrayList<>();
        // a list of conversations with each entry having information about it

        int letterUnreadCount = 0;
        int unreadCount;
        if (conversationList != null) {
            for (Message message : conversationList) {
                Map<String, Object> map = new HashMap<>();
                map.put("conversation", message);
                map.put("letterCount", messageService.findLetterCount(message.getConversationId()));
                unreadCount = messageService.findLetterUnreadCount(user.getId(), message.getConversationId());
                map.put("unreadCount", unreadCount);
                letterUnreadCount += unreadCount;
                int targetId = user.getId() == message.getFromId() ? message.getToId() : message.getFromId();
                map.put("target", userService.findUserById(targetId));

                conversations.add(map);
            }
        }

        // add group conversations
        List<Message> groupConversationList = messageService.findGroupConversations(user.getId(), page.getOffset(), page.getLimit());
        if (groupConversationList != null) {
            for (Message groupMsg : groupConversationList) {
                Map<String, Object> map = new HashMap<>();
                map.put("conversation", groupMsg);
                map.put("letterCount", messageService.findLetterCount(groupMsg.getToId()));
                unreadCount = messageService.findLetterUnreadCount(user.getId(), groupMsg.getToId());
                map.put("unreadCount", unreadCount);
                letterUnreadCount += unreadCount;

                map.put("target", null);
                map.put("groupId", groupMsg.getToId());
                map.put("groupName", groupService.getGroup(groupMsg.getToId()).getName());

                conversations.add(map);
            }
        }

        model.addAttribute("conversations", conversations);

        // 查询未读消息数量
//        int letterUnreadCount = messageService.findLetterUnreadCount(user.getId(), null);
        model.addAttribute("letterUnreadCount", letterUnreadCount);
        int noticeUnreadCount = noticeService.findNoticeUnreadCount(user.getId(), null);
        model.addAttribute("noticeUnreadCount", noticeUnreadCount);

        return "/site/letter";
    }

    @LoginRequired
    @RequestMapping(path = "/detail/{conversationId}", method = RequestMethod.GET)
    public String getLetterDetail(@PathVariable("conversationId") String conversationId, Page page, Model model) {
        // 分页信息
        page.setLimit(100);
        page.setPath("/letter/detail/" + conversationId);
//        page.setRows(messageService.findLetterCount(conversationId));
        page.setRows(messageService.findLetterCount(100));

        String[] strIds = conversationId.split("_");
        int id0 = Integer.parseInt(strIds[0]);
        int id1 = Integer.parseInt(strIds[1]);
        String conversationId2 = id1 + "_" + id0;

        // 私信列表
        List<Message> letterList = messageService.findLetters(conversationId, conversationId2, page.getOffset(), page.getLimit());
        List<Map<String, Object>> letters = new ArrayList<>();
        if (letterList != null) {
            for (Message message : letterList) {
                Map<String, Object> map = new HashMap<>();
                map.put("letter", message);
                map.put("fromUser", message.getFromId());
                letters.add(map);
            }
        }
        model.addAttribute("letters", letters);

        // 私信目标
        model.addAttribute("users", messageService.getConversationUsers(conversationId, hostHolder.getUser().getId()));

        // 设置已读
        List<Integer> ids = messageService.getLetterIds(hostHolder.getUser().getId(), letterList);
        if (!ids.isEmpty()) {
            messageService.readMessage(ids);
        }

        return "/site/letter-detail";
    }

//    @LoginRequired
//    @RequestMapping(path = "/send", method = RequestMethod.POST)
//    @ResponseBody
//    public String sendLetter(String toName, String content) {
//        User target = userService.findUserByName(toName);
//        if (target == null) {
//            return CommunityUtil.getJSONString(1, "Target user not exists!");
//        }
//
//        Message message = new Message();
//        message.setFromId(hostHolder.getUser().getId());
//        message.setToId(target.getId());
//        if (message.getFromId() < message.getToId()) {
//            message.setConversationId(message.getFromId() + "_" + message.getToId());
//        } else {
//            message.setConversationId(message.getToId() + "_" + message.getFromId());
//        }
//        message.setContent(content);
//        message.setCreateTime(new Date());
//        messageService.addMessage(message);
//
//        return CommunityUtil.getJSONString(0);
//    }
}
