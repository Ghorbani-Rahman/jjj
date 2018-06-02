package com.delaroystodios.metakar.Model;


public class Notifications
{
    private String id;
    private String title;
    private String created_at;
    private String created_at_jalali;
    private Pivot pivot;

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

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getCreated_at_jalali() {
        return created_at_jalali;
    }

    public void setCreated_at_jalali(String created_at_jalali) {
        this.created_at_jalali = created_at_jalali;
    }

    public Pivot getPivot() {
        return pivot;
    }

    public void setPivot(Pivot pivot) {
        this.pivot = pivot;
    }
}
