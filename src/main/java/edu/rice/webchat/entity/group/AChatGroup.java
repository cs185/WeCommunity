package edu.rice.webchat.entity.group;

import com.google.gson.JsonObject;
import edu.rice.webchat.entity.message.Message;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import edu.rice.webchat.service.DBService;
import edu.rice.webchat.entity.user.User;

import java.util.Date;

public abstract class AChatGroup{
    protected int id;
    protected String name;
    protected int owner_id;
    protected int capacity;
    protected Date create_time;
    protected int count = 0;

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
        return owner_id;
    }

    public void setOwnerId(int owner_id) {
        this.owner_id = owner_id;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public Date getCreateTime() {
        return create_time;
    }

    public void setCreateTime(Date create_time) {
        this.create_time = create_time;
    }

    public int getCount() {
        return count;
    }

    public void addCount() {
        this.count++;
    }

    public void subCount() {
        this.count--;
    }

    @Override
    public String toString() {
        return "AChatGroup{" +
                "id=" + id +
                ", name=" + name +
                ", ownerId=" + owner_id +
                ", capacity=" + capacity +
                ", createTime=" + create_time +
                ", count=" + count +
                '}';
    }
}
