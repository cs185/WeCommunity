package edu.rice.webchat.entity.message;

import edu.rice.webchat.entity.user.User;
import org.springframework.stereotype.Component;

@Component
public class MsgFac{
    private int nextId = 1;
    private final String prefix = "message";

    /**
     * Only makes 1 vertical strategy.
     * @return The vertical strategy
     */

    public Message makeMsg(String content, User sender) {
        String id = prefix + nextId++;
        return new Message(id, content, sender);
    }
}
