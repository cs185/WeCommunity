package edu.rice.wecommunity.entity;

import java.util.Date;

public class Notice {
    private int id;
    private int fromId;
    private int toId;
    private String topic;
    private int entityType;
    private int entityId;
    private int status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFromId() {
        return fromId;
    }

    public void setFromId(int fromId) {
        this.fromId = fromId;
    }

    public int getToId() {
        return toId;
    }

    public void setToId(int toId) {
        this.toId = toId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getEntityType() {
        return entityType;
    }

    public void setEntityType(int entityType) {
        this.entityType = entityType;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    private Date createTime;

    @Override
    public String toString() {
        return "Notice{" +
                "id=" + id +
                ", entityType=" + entityType +
                ", topic=" + topic +
                ", toId=" + toId +
                ", entityId='" + entityId + '\'' +
                ", fromId='" + fromId + '\'' +
                ", status=" + status +
                ", createTime=" + createTime +
                '}';
    }
}
