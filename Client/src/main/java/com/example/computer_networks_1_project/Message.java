package com.example.computer_networks_1_project;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class Message {
    private String content;
    private boolean direction;
    private LocalDateTime date;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    private boolean selected = false;

    public Message(String content, boolean direction, LocalDateTime date) {
        this.content = content;
        this.direction = direction;
        this.date = date;

    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isDirection() {
        return direction;
    }

    public void setDirection(boolean direction) {
        this.direction = direction;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
