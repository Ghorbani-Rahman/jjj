package com.delaroystodios.metakar.Model;


public class ShowWallet
{
    private String id;
    private String user_id;
    private Long amount;
    private String type;
    private String description;
    private String created_at;
    private String updated_at;
    private String created_at_jalali;
    private String type_farsi;

    public String getType_farsi() {
        return type_farsi;
    }

    public void setType_farsi(String type_farsi) {
        this.type_farsi = type_farsi;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getCreated_at_jalali() {
        return created_at_jalali;
    }

    public void setCreated_at_jalali(String created_at_jalali) {
        this.created_at_jalali = created_at_jalali;
    }
}
