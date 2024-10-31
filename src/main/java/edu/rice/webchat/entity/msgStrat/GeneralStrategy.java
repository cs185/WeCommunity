package edu.rice.webchat.entity.msgStrat;

import edu.rice.webchat.entity.message.GroupMessage;
import j2html.TagCreator;
import org.springframework.stereotype.Component;

@Component
public class GeneralStrategy extends AStrategy{
    public static GeneralStrategy ONLY;

    public static AStrategy make() {
        if (ONLY == null)
            ONLY = new GeneralStrategy();

        return ONLY;
    }
    @Override
    public void renderMsg(GroupMessage message) {
        message.setRenderedMsg(TagCreator.p(message.getSender().getUsername() + " says: " + message.getContent()).render());
    }
}
