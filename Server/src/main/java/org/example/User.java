package org.example;

public class User {
    private String name;
    private String password;
    private String ip;
    private String port;
    private boolean active;

    public User(String name, String password) {
        this.name = name;
        this.password = password;
        this.port = "";
        this.ip = "";
        this.active = false;
    }

    @Override
    public String toString(){
        return name +","+ password;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
