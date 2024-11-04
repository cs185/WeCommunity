package edu.rice.wecommunity.controller;

import edu.rice.wecommunity.entity.*;
import edu.rice.wecommunity.event.EventProducer;
import edu.rice.wecommunity.service.GroupService;
import edu.rice.wecommunity.service.MessageService;
import edu.rice.wecommunity.service.NoticeService;
import edu.rice.wecommunity.service.UserService;
import edu.rice.wecommunity.util.CommunityConstant;
import edu.rice.wecommunity.util.CommunityUtil;
import edu.rice.wecommunity.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * The chat app controller communicates with all the clients on the web socket.
 */
@Controller
@RequestMapping("/group")
public class GroupController implements CommunityConstant {
    @Autowired
    private GroupService groupService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private MessageService messageService;

    @Autowired
    private EventProducer eventProducer;

    @Autowired
    private UserService userService;

    @Autowired
    private NoticeService noticeService;

    @GetMapping("/list")
    public String getGroupList(Model model) {
        User user = hostHolder.getUser();
        List<Group> groups = groupService.getUserGroups(user.getId());

        model.addAttribute("groups", groups);
        model.addAttribute("userId", user.getId());

        return "site/my-group";
    }

    @GetMapping("/{groupId}")
    public String getGroup(@PathVariable("groupId") int groupId, Page page, Model model) {
        List<User> members = groupService.getGroupMembers(groupId);
        Group group = groupService.getGroup(groupId);

        model.addAttribute("group", group);
        model.addAttribute("members", members);
        model.addAttribute("user", hostHolder.getUser());

        page.setLimit(100);
        page.setPath("/group/" + groupId);
        page.setRows(100);

        List<Message> letterList = messageService.findLetters(groupId, page.getOffset(), page.getLimit());
        List<Map<String, Object>> letters = new ArrayList<>();
        if (letterList != null) {
            for (Message message : letterList) {
                Map<String, Object> map = new HashMap<>();
                map.put("letter", message);
                map.put("fromUser", userService.findUserById(message.getFromId()));
                letters.add(map);
            }
        }
        model.addAttribute("letters", letters);

        return "site/group";
    }

    @PostMapping("/create")
    @ResponseBody
    public String createChat(@RequestParam String groupName) {
        User user = hostHolder.getUser();
        int groupId = groupService.createGroup(groupName, user.getId());

        return CommunityUtil.getJSONString(0, String.valueOf(groupId), null);
    }

    @PostMapping("/join")
    @ResponseBody
    public String joinGroup(@RequestParam int groupId) {
        Group group = groupService.getGroup(groupId);
        if (group == null)
            return CommunityUtil.getJSONString(2, "no such group!", null);

        Set<Integer> userSet = new HashSet<>(groupService.getGroupMembers(groupId).stream().map(User::getId).toList());
        if (userSet.contains(hostHolder.getUser().getId()))
            return CommunityUtil.getJSONString(1, "you already in the group!", null);

        Event event = new Event()
                .setTopic(TOPIC_REQUEST)
                .setUserId(hostHolder.getUser().getId())
                .setEntityType(ENTITY_TYPE_GROUP)
                .setEntityId(groupId)
                .setEntityUserId(groupService.getGroupOwner(groupId).getId())
                .setData("type", "request");

        eventProducer.fireEvent(event);
        return CommunityUtil.getJSONString(0, "waiting for group admin to approve ...", null);
    }

    @GetMapping("/invite")
    public String getInvite(@RequestParam int userId, @RequestParam int groupId, Model model) {
        model.addAttribute("userId", userId);
        model.addAttribute("groupId", groupId);

        return "site/invite";
    }

    @PostMapping("/leave")
    @ResponseBody
    public String leaveGroup(@RequestParam int groupId) {
        groupService.deleteUserFromGroup(hostHolder.getUser().getId(), groupId);

        return CommunityUtil.getJSONString(0,
                "group " + groupService.getGroup(groupId).getName() + " left", null);
    }

    // the following operation are only for owner
    @PostMapping("/delete")
    @ResponseBody
    public String deleteGroup(@RequestParam int groupId) {
        String groupName = groupService.getGroup(groupId).getName();
        groupService.deleteGroup(groupId);

        return CommunityUtil.getJSONString(0,
                "group " + groupName + " delete", null);
    }

    @GetMapping("/{groupId}/setting")
    public String getGroupSetting(@PathVariable("groupId") int groupId, Model model) {
        List<User> members = groupService.getGroupMembers(groupId);
        Group group = groupService.getGroup(groupId);

        model.addAttribute("group", group);
        model.addAttribute("members", members);
        model.addAttribute("user", hostHolder.getUser());

        return "site/group-setting";
    }

    @PostMapping("/request")
    @ResponseBody
    public String requestJoin(@RequestParam int groupId) {
        Event event = new Event()
                .setTopic(TOPIC_REQUEST)
                .setUserId(hostHolder.getUser().getId())
                .setEntityType(ENTITY_TYPE_GROUP)
                .setEntityId(groupId)
                .setEntityUserId(groupService.getGroup(groupId).getOwnerId());

        eventProducer.fireEvent(event);
        return CommunityUtil.getJSONString(0, "request sent", null);
    }

    @PostMapping("/invite")
    @ResponseBody
    public String inviteUser(@RequestParam int userId, @RequestParam int groupId) {
        Event event = new Event()
                .setTopic(TOPIC_REQUEST)
                .setUserId(hostHolder.getUser().getId())
                .setEntityType(ENTITY_TYPE_USER)
                .setEntityId(groupId)
                .setEntityUserId(userId);

        eventProducer.fireEvent(event);
        return CommunityUtil.getJSONString(0, "invitation sent", null);
    }

    @PostMapping("/accept")
    @ResponseBody
    public String acceptUser(@RequestParam int userId, @RequestParam int groupId, @RequestParam int noticeId) {
        groupService.addUserToGroup(userId, groupId, 0);
        noticeService.deleteNotice(noticeId);

        return CommunityUtil.getJSONString(0, String.valueOf(groupId), null);
    }

    @PostMapping("/kick")
    @ResponseBody
    public String kickUser(@RequestParam int userId, @RequestParam int groupId) {
        groupService.deleteUserFromGroup(userId, groupId);

        return CommunityUtil.getJSONString(0, "kicked user: " + userId, null);
    }
}
