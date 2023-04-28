package com.example.server_.events;

import com.example.server_.app.User;

public class UserDisconnectedEvent implements Event{
    private User user;
    public UserDisconnectedEvent(User user){
        this.user=user;
    }

    public User getUser() {
        return user;
    }
}
