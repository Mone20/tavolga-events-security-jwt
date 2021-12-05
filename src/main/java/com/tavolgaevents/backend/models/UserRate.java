package com.tavolgaevents.backend.models;

public class UserRate {
    public User user;
    public int result;

    public UserRate(int result,User user) {
        this.user = user;
        this.result = result;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}
