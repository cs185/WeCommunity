package edu.rice.webchat.entity.msgStrat;

import edu.rice.webchat.entity.message.MsgFac;
import org.springframework.stereotype.Component;

@Component
public class MsgStratFac{
    private static MsgStratFac ONLY;

    /**
     * Only makes 1 vertical strategy.
     * @return The vertical strategy
     */
    public static MsgStratFac make() {
        if (ONLY == null ) {
            ONLY = new MsgStratFac();
        }
        return ONLY;
    }

    public AStrategy makeStrat(String type) {
        switch (type) {
            default:
                return GeneralStrategy.make();
            case "emphasis":
                return EmphasisStrategy.make();
            case "anonymous":
                return AnonymousStrategy.make();
            case "important":
                return ImportantStrategy.make();
        }
    }

    public AStrategy makeStrat() {
        return GeneralStrategy.make();
    }
}
