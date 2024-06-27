package edu.rice.webchat.controller;

import edu.rice.webchat.entity.group.AChatGroup;
import edu.rice.webchat.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class FriendshipController {
    FriendshipService friendshipService;
    @Autowired
    public FriendshipController(FriendshipService friendshipService) {
        this.friendshipService = friendshipService;
    }

    @PostMapping("/friend/add")
    public ResponseEntity<String> addFriend(@RequestParam String username, HttpSession session) {
        friendshipService.addFriend((String) session.getAttribute("username"), username);

        // todo: send friend add request
        return ResponseEntity.ok("Friend add request sent, waiting to respond");
    }

    @PostMapping("/friend/accept")
    public ResponseEntity<String> acceptFriend(@RequestParam String username, HttpSession session) {
        friendshipService.acceptFriend((String) session.getAttribute("username"), username);

        return ResponseEntity.ok("Friend add request accepted, now you can chat!");
    }

    @PostMapping("/friend/delete")
    public ResponseEntity<String> deleteFriend(@RequestParam String username, HttpSession session) {
        friendshipService.deleteFriend((String) session.getAttribute("username"), username);

        return ResponseEntity.ok("Friend add request deleted");
    }
}
