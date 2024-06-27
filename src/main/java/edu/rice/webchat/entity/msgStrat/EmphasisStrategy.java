package edu.rice.webchat.entity.msgStrat;

import edu.rice.webchat.entity.message.Message;
import org.springframework.stereotype.Component;

import static j2html.TagCreator.b;
import static j2html.TagCreator.p;

@Component
public class EmphasisStrategy extends AStrategy{
    public static EmphasisStrategy ONLY;

    public static AStrategy make() {
        if (ONLY == null)
            ONLY = new EmphasisStrategy();

        return ONLY;
    }
    @Override
    public void renderMsg(Message message) {
        message.setRenderedMsg(p(b(message.getSender().getUsername() + " says: " + message.getContent())).render());
    }
}
