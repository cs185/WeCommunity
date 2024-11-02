package edu.rice.wecommunity.entity;

import java.util.Date;

public class Group {
    private int id;
    private String name;
    private int ownerId;
    private int capacity;
    private Date createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


    @Override
    public String toString() {
        return "AChatGroup{" +
                "id=" + id +
                ", name=" + name +
                ", ownerId=" + ownerId +
                ", capacity=" + capacity +
                ", createTime=" + createTime +
                '}';
    }
}
