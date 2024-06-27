package edu.rice.webchat.entity.msgStrat;

import edu.rice.webchat.entity.message.Message;

public abstract class AStrategy{
    public static AStrategy ONLY;
    private String stratType;
    public abstract void renderMsg(Message message);
    public String getStratType() {
        return stratType;
    }
}
