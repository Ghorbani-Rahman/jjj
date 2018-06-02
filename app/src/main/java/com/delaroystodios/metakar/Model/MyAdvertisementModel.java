package com.delaroystodios.metakar.Model;


public class MyAdvertisementModel
{
    private String id;
    private String title;
    private String status;
    private String user_id;
    private String type;
    private String status_farsi;

    public String getStatus_farsi() {
        return status_farsi;
    }

    public void setStatus_farsi(String status_farsi) {
        this.status_farsi = status_farsi;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
