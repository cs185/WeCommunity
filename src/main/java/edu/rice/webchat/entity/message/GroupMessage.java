package edu.rice.webchat.entity.message;

import edu.rice.webchat.entity.user.User;
import j2html.TagCreator;

public class GroupMessage {
    protected String id;

    protected String content;

    protected String renderedMsg;

    protected User sender;

    protected String strategyType;

    protected GroupMessage(String id, String content, User sender) {
        this.id = id;
        this.content = content;
        this.sender = sender;
        this.strategyType = "general";
        this.renderedMsg = TagCreator.p(sender.getUsername() + " says: " + content).render();
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public User getSender() {
        return sender;
    }

    public void setRenderedMsg(String renderedMsg) {
        this.renderedMsg = renderedMsg;
    };

    public String getRenderedMsg() {
        return renderedMsg;
    };

    public String getStrategyType() {
        return strategyType;
    }

    public void setStrategyType(String strategyType) {
        this.strategyType = strategyType;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                ", renderedMsg='" + renderedMsg + '\'' +
                ", sender=" + sender +
                ", strategyType='" + strategyType + '\'' +
                '}';
    }
}
