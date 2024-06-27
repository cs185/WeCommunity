package edu.rice.webchat.controller;

import edu.rice.webchat.entity.group.AChatGroup;
import edu.rice.webchat.entity.group.GroupFac;
import edu.rice.webchat.entity.user.User;
import edu.rice.webchat.service.AccountService;
import edu.rice.webchat.service.DBService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

/**
 * The chat app controller communicates with all the clients on the web socket.
 */
@Controller
public class RegisterController {
    private final AccountService accountService;

    @Autowired
    public RegisterController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/register")
    public ModelAndView showRegister(HttpSession session, HttpServletResponse response) {
        if (session.getAttribute("username") != null) {
            try {
                response.sendRedirect("/index");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return new ModelAndView("register");
    }

    @PostMapping("/register")
    public ResponseEntity<String> doRegister(@RequestParam String username,
                                             @RequestParam String password) {
        boolean status = accountService.register(username, password);

        if (status) {
            return ResponseEntity.ok("Register Success");
        } else {
            // Set a bad request status and return a message
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists");
        }
    }
}