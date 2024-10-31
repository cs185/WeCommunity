package edu.rice.webchat.entity.msgStrat;

import edu.rice.webchat.entity.message.GroupMessage;
import j2html.TagCreator;
import org.springframework.stereotype.Component;

@Component
public class ImportantStrategy extends AStrategy{
    public static ImportantStrategy ONLY;

    public static AStrategy make() {
        if (ONLY == null)
            ONLY = new ImportantStrategy();

        return ONLY;
    }
    @Override
    public void renderMsg(GroupMessage message) {
        message.setRenderedMsg(TagCreator.p("~~~~~~~~~~!!!!!!!" + "[IMPORTANT]" + message.getSender().getUsername()
                + " says: " + message.getContent() + "!!!!!!!~~~~~~~~~~").render());
    }
}
