package com.example.server_.events;

import com.example.server_.app.User;

public class UserConnectedEvent implements Event {


    private User user;

    public UserConnectedEvent(User user){

        this.user=user;
    }

    public User getUser() {
        return user;
    }
}
