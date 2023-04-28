package com.example.server_.events;

import com.example.server_.User;

import java.util.List;

public class DBLoadedEvent implements Event {

    private List<User> users;

    public DBLoadedEvent(List<User> users){
        this.users=users;
    }

    public List<User> getUsers(){
        return users;
    }


}
