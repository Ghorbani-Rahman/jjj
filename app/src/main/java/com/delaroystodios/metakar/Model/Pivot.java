package com.delaroystodios.metakar.Model;


public class Pivot
{

    private String user_id;
    private String notification_id;
    private String has_been_seen;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getNotification_id() {
        return notification_id;
    }

    public void setNotification_id(String notification_id) {
        this.notification_id = notification_id;
    }

    public String getHas_been_seen() {
        return has_been_seen;
    }

    public void setHas_been_seen(String has_been_seen) {
        this.has_been_seen = has_been_seen;
    }
}
