package edu.rice.webchat.controller;

import edu.rice.webchat.entity.group.AChatGroup;
import edu.rice.webchat.entity.group.GeneralChatGroup;
import edu.rice.webchat.entity.user.User;
import edu.rice.webchat.service.DBService;
import edu.rice.webchat.service.GroupService;
import edu.rice.webchat.service.UserService;
import edu.rice.webchat.util.HostHolder;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * The chat app controller communicates with all the clients on the web socket.
 */
@Controller
public class GroupController {
    @Autowired
    private GroupService groupService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;


    @GetMapping("/group/{groupId}")
    public String getGroupId(@PathVariable("groupId") int groupId, Model model) {
        List<User> members = groupService.getGroupMembers(groupId);
        GeneralChatGroup group = (GeneralChatGroup) groupService.getGroup(groupId);

        model.addAttribute("group", group);
        model.addAttribute("members", members);

        return "site/group";
    }
//
//    @PostMapping("/group/type")
//    public ResponseEntity<String> setMsgType(@RequestParam String messageType, HttpSession session) {
//        session.setAttribute("msgType", messageType);
//        return ResponseEntity.ok("message type set");
//    }

    @PostMapping("/group/create")
    public ResponseEntity<String> createChat(@RequestParam String groupName) {
        User user = hostHolder.getUser();
        groupService.createGroup(groupName, user.getId());

        return ResponseEntity.ok("Created Group: " + groupName);
    }

    @PostMapping("/group/join")
    public ResponseEntity<String> joinChat(@RequestParam String groupName) {
        groupService.addUserToGroup(hostHolder.getUser().getId(), groupName);
        return ResponseEntity.ok("Group Created");
    }

    @PostMapping("/group/leave")
        public ResponseEntity<String> leaveChat(@RequestParam int groupId) {
        groupService.deleteUserFromGroup(hostHolder.getUser().getId(), groupId);

        return ResponseEntity.ok("Group left");
    }
}
