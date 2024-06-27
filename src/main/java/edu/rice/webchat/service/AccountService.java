package edu.rice.webchat.service;

import edu.rice.webchat.entity.user.UserFac;
import edu.rice.webchat.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import edu.rice.webchat.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    private final UserRepository userRepository;
    private final UserFac userFac;

    @Autowired
    public AccountService(UserRepository userRepository, UserFac userFac) {
        this.userRepository = userRepository;
        this.userFac = userFac;
    }

    public int authenticate(String username, String password) {
        User user = userRepository.getUser(username);
        if (user == null) {
            return 1;
        }

        if (!user.getPassword().equals(password)) {
            return 2;
        }

        return 0;
    }

    public boolean authenticate(HttpSession session) {
        return session.getAttribute("username") != null;
    }

    public boolean register(String username, String password) {
        User user = userRepository.getUser(username);
        if (user == null) {
            userFac.makeUser(username, password);
            return true;
        }
        return false;
    }
}
