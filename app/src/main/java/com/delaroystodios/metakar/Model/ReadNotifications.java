package com.delaroystodios.metakar.Model;


public class ReadNotifications
{
    private String id;
    private String title;
    private String created_at;
    private String image;
    private String content;
    private String created_at_jalali;
    private String image_url;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreated_at_jalali() {
        return created_at_jalali;
    }

    public void setCreated_at_jalali(String created_at_jalali) {
        this.created_at_jalali = created_at_jalali;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public Pivot getPivot() {
        return pivot;
    }

    public void setPivot(Pivot pivot) {
        this.pivot = pivot;
    }
}
