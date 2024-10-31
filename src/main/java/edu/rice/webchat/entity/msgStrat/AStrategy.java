package edu.rice.webchat.entity.msgStrat;

import edu.rice.webchat.entity.message.GroupMessage;

public abstract class AStrategy{
    public static AStrategy ONLY;
    private String stratType;
    public abstract void renderMsg(GroupMessage message);
    public String getStratType() {
        return stratType;
    }
}
