package edu.rice.webchat.entity.group;

import java.util.Date;

public class GeneralChatGroup extends AChatGroup{
    public GeneralChatGroup(int id, String name, int owner_id, int capacity, Date create_time, int count) {
        this.id = id;
        this.name = name;
        this.owner_id = owner_id;
        this.capacity = capacity;
        this.create_time = create_time;
        this.count = count;
    }

    public GeneralChatGroup(String name, int owner_id) {
        this.name = name;
        this.owner_id = owner_id;
        this.capacity = 50;
        this.count = 1;
    }
}
