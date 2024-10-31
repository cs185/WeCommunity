//package edu.rice.webchat.controller;
//
//import edu.rice.webchat.annotation.LoginRequired;
//import edu.rice.webchat.entity.group.AChatGroup;
//import edu.rice.webchat.entity.user.User;
//import edu.rice.webchat.service.*;
//import edu.rice.webchat.util.FriendshipStatus;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.servlet.ModelAndView;
//
//import java.io.IOException;
//import java.util.HashMap;
//
///**
// * The chat app controller communicates with all the clients on the web socket.
// */
//@Controller
////@RequestMapping("/user")
//public class ProfileController {
//    private final AccountService accountService;
//
//    private final UserService userService;
//
//    private final GroupService groupService;
//
//    private final FriendshipService friendshipService;
//
//    @Autowired
//    public ProfileController(AccountService accountService, UserService userService, GroupService groupService, FriendshipService friendshipService) {
//        this.accountService = accountService;
//        this.userService = userService;
//        this.groupService = groupService;
//        this.friendshipService = friendshipService;
//    }
////
////    @GetMapping("/profile")
////    @LoginRequired
////    public String showProfile(){
////        return "/site/profile";
////    }
//
////    @GetMapping("/profile/{username}")
////    public ModelAndView showProfile(@PathVariable("username") String username, HttpSession session) {
////
////        String thisUsername = (String) session.getAttribute("username");
////
////        User user = userRepository.getUser(username);
////
////        if (thisUsername.equals(username)) {
////            ModelAndView modelAndView = new ModelAndView("self_profile");
////            modelAndView.addObject("username", username);
////            modelAndView.addObject("email", user.getEmail());
////            modelAndView.addObject("headerUrl", user.getHeaderUrl());
////            modelAndView.addObject("joinedGroups", groupService.getUserGroupNames(username));
////            modelAndView.addObject("friends", friendshipService.getFriends(username));
////            return modelAndView;
////        }
////        else {
////            ModelAndView modelAndView = new ModelAndView("profile");
////            FriendshipStatus friendshipStatus = friendshipService.getStatus(thisUsername, username);
////
////            switch(friendshipStatus) {
////                case REQUEST_SENDER:
////                    modelAndView.addObject("status", "Pending");
////                    modelAndView.addObject("chat", false);
////                    break;
////                case REQUEST_RECEIVER:
////                    modelAndView.addObject("status", "Accept Request");
////                    modelAndView.addObject("chat", false);
////                    break;
////                case FRIEND:
////                    modelAndView.addObject("status", "Delete Friend");
////                    modelAndView.addObject("chat", true);
////                    break;
////                default:
////                    modelAndView.addObject("status", "Add Friend");
////                    modelAndView.addObject("chat", false);
////                    break;
////            }
//
////
////            modelAndView.addObject("username", user.getUsername());
////            modelAndView.addObject("email", user.getEmail());
////            modelAndView.addObject("headerUrl", user.getHeaderUrl());
////            return modelAndView;
////        }
////    }
//
////    @GetMapping ("/profile/search")
////    public ResponseEntity<String> searchUser(@RequestParam String username, HttpSession session, HttpServletResponse response) {
////        String thisUsername = (String) session.getAttribute("username");
////        if (thisUsername.equals(username))
////            return ResponseEntity.ok("why find yourself");
////
////        User user = userRepository.getUser(username);
////        if (user == null)
////            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No such user");
////
////        try {
////            response.sendRedirect("/profile/" + username);
////        } catch (IOException e) {
////            throw new RuntimeException(e);
////        }
////        return ResponseEntity.status(HttpStatus.FOUND).body("/profile/" + username);
////    }
//
//    @PostMapping("/profile/create")
//    public ResponseEntity<String> createChat(@RequestParam String groupName, HttpSession session) {
//        String username = (String) session.getAttribute("username");
//        AChatGroup group = userService.createChatGroup(username, groupName);
//
//        return ResponseEntity.ok("Created Group: " + group.getId());
//    }
//
//    @PostMapping("/profile/join")
//    public ResponseEntity<String> joinChat(@RequestParam int groupId, HttpSession session, HttpServletResponse response) {
//        String username = (String) session.getAttribute("username");
//        HashMap<Integer, String> joinedGroups = groupService.getUserGroupNames(username);
//        if (joinedGroups.containsKey(groupId)) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Already in the group");
//        }
//        userService.joinChatGroup(username, groupId);
//        try {
//            // todo: this can work???
//            response.sendRedirect("/group/" + groupId);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        return ResponseEntity.ok("Group Created");
//    }
//
////    @GetMapping("/logout")
////    public void showLogout(HttpSession session, HttpServletResponse response) {
////        if (accountService.authenticate(session)) {
////            String username = (String) session.getAttribute("username");
////            session.removeAttribute("username");
////            userService.userLogout(username);
////        }
////
////        try {
////            response.sendRedirect("/login");
////        } catch (IOException e) {
////            throw new RuntimeException(e);
////        }
////    }
//}
