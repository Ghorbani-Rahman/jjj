package com.delaroystodios.metakar.Model;


public class Login
{

    private String result;
    private User user;
    private String token;
    private String notifications_count;

    public String getNotifications_count() {
        return notifications_count;
    }

    public void setNotifications_count(String notifications_count) {
        this.notifications_count = notifications_count;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
