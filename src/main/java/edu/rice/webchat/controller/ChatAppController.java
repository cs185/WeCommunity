//package edu.rice.webchat.controller;
//
//import com.google.gson.Gson;
//import edu.rice.webchat.handler.MyWebSocketHandler;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.servlet.ModelAndView;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//import jakarta.servlet.http.HttpSession;
//import edu.rice.webchat.entity.group.AChatGroup;
//import edu.rice.webchat.entity.group.GroupFac;
//import edu.rice.webchat.service.DBService;
//import edu.rice.webchat.entity.user.User;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Objects;
//
//import edu.rice.webchat.service.AccountService;
//
///**
// * The chat app controller communicates with all the clients on the web socket.
// */
//@Controller
//public class ChatAppController {
//    private final AccountService accountService;
//
//    @Autowired
//    public ChatAppController(AccountService accountService) {
//        this.accountService = accountService;
//    }
//
//    @GetMapping("/register")
//    public ModelAndView showRegister(HttpSession session, HttpServletResponse response) {
//        if (session.getAttribute("username") != null) {
//            try {
//                response.sendRedirect("/chat");
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
//        return new ModelAndView("register");
//    }
//
//    @PostMapping("/register")
//    public ResponseEntity<String> doRegister(@RequestParam String username,
//                             @RequestParam String name,
//                             @RequestParam String password,
//                             HttpSession session, HttpServletResponse response) {
//        boolean status = accountService.register(username, name, password);
//
//        if (status) {
//            return ResponseEntity.ok("Register Success");
//        } else {
//            // Set a bad request status and return a message
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists");
//        }
//    }
//
//    @GetMapping("/login")
//    public ModelAndView showLogin(HttpSession session, HttpServletResponse response) {
//        if (session.getAttribute("username") != null) {
//            try {
//                response.sendRedirect("/chat");
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
//        return new ModelAndView("login");
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password, HttpSession session) {
//        if (accountService.authenticate(username, password)) {
//            User user = DBService.getUser(username);
//            DBService.addIdUser(username, user);
//
//            if (session.getAttribute("username") == null) {
//                String userId = user.getId();
//                if (userId != null) {
//                    session.setAttribute("username", user.getId()); // Store user in session
//                }
//            }
//            return ResponseEntity.ok("Login Success");
//        } else {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
//        }
//    }
//
//    @GetMapping("/chat")
//    public ModelAndView showChat(HttpSession session, HttpServletResponse response) {
//        if (!accountService.authenticate(session)) {
//            try {
//                response.sendError(401, "Invalid credential"); // Unauthorized
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
//
//        String userId = (String) session.getAttribute("username");
//        User user = accountService.getUser(userId);
////        System.out.println((String) session.getAttribute("username"));
////        System.out.println(user);
//
//        ModelAndView modelAndView = new ModelAndView("chat");
//        modelAndView.addObject("name", user.getName());
//        modelAndView.addObject("username", user.getId());
//        modelAndView.addObject("joinedGroups", user.getJoinedGroups());
//        return modelAndView;
//    }
//
//    @PostMapping("/chat/create")
//    public ResponseEntity<String> createChat(@RequestParam String groupName, HttpSession session) {
//        User creator = accountService.getUser((String) session.getAttribute("username"));
//        AChatGroup group = creator.createChatGroup(groupName);
//
//        return ResponseEntity.ok("Create Group: " + group.getName());
//    }
//
//    @PostMapping("/chat/join")
//    public ResponseEntity<String> joinChat(@RequestParam String groupId, HttpSession session, HttpServletResponse response) {
//        User joiner = accountService.getUser((String) session.getAttribute("username"));
//        HashMap<String, String> joinedGroups = DBService.getUserGroups(joiner.getId());
//        if (joinedGroups.containsKey(groupId)) {
//            try {
//                response.setStatus(400); // already joined
//                response.sendRedirect("/chat");
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Already in the group");
//        }
//        joiner.joinChatGroup(groupId);
//
//        return ResponseEntity.ok("Group Created");
//    }
//
//    @PostMapping("/chat/leave")
////  TODO: logic not correct, this group delete
//    public ResponseEntity<String> leaveChat(@RequestParam String groupId, HttpSession session, HttpServletResponse response) {
//        User leaver = accountService.getUser((String) session.getAttribute("username"));
//        leaver.leaveChatGroup(groupId);
//        String[] members = Objects.requireNonNull(DBService.getGroup(groupId)).getMembers();
//
//        if (members.length == 0 || members == null)
//            DBService.clearGroup(groupId);
//
//        return ResponseEntity.ok("Group left");
//    }
//
//    @PostMapping("/chat/send")
//    public ResponseEntity<String> sendChatMsg(@RequestParam String targetUserId, HttpSession session, HttpServletResponse response) {
//        User sender = accountService.getUser((String) session.getAttribute("username"));
//        if (DBService.getIdUser(targetUserId) == null) {
//            response.setStatus(400);
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not exist or not online");
//        }
//
//        sender.setTargetUserId(targetUserId);
//        return ResponseEntity.ok("target user set, waiting to send");
//    }
//
//    @GetMapping("/logout")
//    public void showLogout(HttpSession session, HttpServletResponse response) {
//        if (accountService.authenticate(session)) {
//            String userId = (String) session.getAttribute("username");
//            session.removeAttribute("username");
//            DBService.removeIdUser(userId);
//        }
//
//        try {
//            response.sendRedirect("/login");
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @GetMapping("/group/{groupId}")
//    public ModelAndView getGroupId(@PathVariable("groupId") String groupId, HttpSession session, HttpServletResponse response) {
//        // Now you have the groupId, you can perform actions based on this ID
//        // For example, fetch the chat group details from the database
//        AChatGroup group = GroupFac.make().getGroup(groupId);
//        String[] members = group.getMembers();
//
//        if (group != null) {
//            User user = accountService.getUser((String) session.getAttribute("username"));
//            ModelAndView modelAndView = new ModelAndView("group");
//            modelAndView.addObject("groupName", group.getName());
//            modelAndView.addObject("groupId", group.getId());
//            modelAndView.addObject("username", user.getId());
//            modelAndView.addObject("members", members);
//            modelAndView.addObject("messageLog", group.getMessages());
//            return modelAndView;
//        } else {
//            try {
//                response.sendError(404, "Chat Group not found");
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//            return null;
//        }
//    }
//
//    @PostMapping("/group/type")
//    public ResponseEntity<String> setMsgType(@RequestParam String messageType, HttpSession session, HttpServletResponse response) {
//        User sender = accountService.getUser((String) session.getAttribute("username"));
//        sender.setMsgStrategy(messageType);
//        return ResponseEntity.ok("message type set");
//    }
//}
