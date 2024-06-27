package edu.rice.webchat.controller;

import edu.rice.webchat.dao.UserMapper;
import edu.rice.webchat.service.AccountService;
import edu.rice.webchat.service.DBService;
import edu.rice.webchat.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

/**
 * The chat app controller communicates with all the clients on the web socket.
 */
@Controller
public class LoginController {
    private final AccountService accountService;
    private final UserService userService;

    @Autowired
    public LoginController(AccountService accountService, UserService userService) {
        this.accountService = accountService;
        this.userService = userService;
    }

    @GetMapping("/login")
    public ModelAndView showLogin(HttpSession session, HttpServletResponse response) {
        String username = (String) session.getAttribute("username");
        if (username != null) {
            try {
                response.sendRedirect("/index");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return new ModelAndView("login");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password, HttpSession session) {
        return switch (accountService.authenticate(username, password)) {
            case 0 -> {
                // if no username attribute in session, the user first login
                if (session.getAttribute("username") == null) {
                    session.setAttribute("username", username); // Store user in session
                }
                yield ResponseEntity.ok("Login Success");
            }
            case 1 -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No such username");
            default -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        };
    }

}
