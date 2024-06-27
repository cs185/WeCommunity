package edu.rice.webchat.controller;

import edu.rice.webchat.entity.user.User;
import edu.rice.webchat.repository.UserRepository;
import edu.rice.webchat.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

/**
 * The chat app controller communicates with all the clients on the web socket.
 */
@Controller
public class ChatController {
    private final UserRepository userRepository;

    @Autowired
    public ChatController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/chat/{targetUsername}")
    public ModelAndView getChat(@PathVariable("targetUsername") String targetUsername, HttpSession session, HttpServletResponse response) {
        User targetUser = userRepository.getUser(targetUsername);

        if (targetUser != null) {
            ModelAndView modelAndView = new ModelAndView("chat");
            modelAndView.addObject("username", session.getAttribute("username"));
            modelAndView.addObject("targetUsername", targetUsername);
            // todo: add message log for personal messages
            modelAndView.addObject("messageLog", "");
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

}
