package edu.rice.webchat.entity.msgStrat;

import edu.rice.webchat.entity.message.Message;
import j2html.TagCreator;
import org.springframework.stereotype.Component;

@Component
public class AnonymousStrategy extends AStrategy{
    public static AnonymousStrategy ONLY;

    public static AStrategy make() {
        if (ONLY == null)
            ONLY = new AnonymousStrategy();

        return ONLY;
    }
    @Override
    public void renderMsg(Message message) {
        message.setRenderedMsg(TagCreator.p(" says: " + message.getContent()).render());
    }
}
