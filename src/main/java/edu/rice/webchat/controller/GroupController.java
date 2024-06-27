package edu.rice.webchat.controller;

import edu.rice.webchat.entity.group.AChatGroup;
import edu.rice.webchat.entity.user.User;
import edu.rice.webchat.repository.UserRepository;
import edu.rice.webchat.service.DBService;
import edu.rice.webchat.service.GroupService;
import edu.rice.webchat.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

/**
 * The chat app controller communicates with all the clients on the web socket.
 */
@Controller
public class GroupController {
    private final UserRepository userRepository;
    private final GroupService groupService;
    private final DBService dbService;

    @Autowired
    public GroupController(UserRepository userRepository, GroupService groupService, DBService dbService) {
        this.userRepository = userRepository;
        this.groupService = groupService;
        this.dbService = dbService;
    }

    @GetMapping("/group/{groupId}")
    public ModelAndView getGroupId(@PathVariable("groupId") String groupId, HttpSession session, HttpServletResponse response) {
        int group_id = Integer.parseInt(groupId);
        AChatGroup group = groupService.getGroup(group_id);
        String[] members = groupService.getGroupMemberNames(Integer.parseInt(groupId));

        if (group != null) {
            User user = userRepository.getUser((String) session.getAttribute("username"));
            ModelAndView modelAndView = new ModelAndView("group");
            modelAndView.addObject("groupId", group_id);
            modelAndView.addObject("groupName", group.getName());
            modelAndView.addObject("username", user.getId());
            modelAndView.addObject("members", members);
            modelAndView.addObject("messageLog", dbService.getGroupMessage(group_id));
            return modelAndView;
        } else {
            try {
                response.sendError(404, "Chat Group not found");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return null;
        }
    }
//
//    @PostMapping("/group/type")
//    public ResponseEntity<String> setMsgType(@RequestParam String messageType, HttpSession session) {
//        session.setAttribute("msgType", messageType);
//        return ResponseEntity.ok("message type set");
//    }

    @PostMapping("/group/leave")
        public ResponseEntity<String> leaveChat(@RequestParam String groupId, HttpSession session) {
        groupService.deleteUserFromGroup((String) session.getAttribute("username"), Integer.parseInt(groupId));

        return ResponseEntity.ok("Group left");
    }
}
