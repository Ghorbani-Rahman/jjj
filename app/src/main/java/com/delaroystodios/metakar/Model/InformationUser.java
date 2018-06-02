package com.delaroystodios.metakar.Model;



public class InformationUser
{
    private int id;
    private String user_id;
    private String type;
    private String city;
    private String mobile;
    private String company;
    private String familiarity;
    private String cart_number;
    private String rules_confirmed;
    private String email_confirm_token;
    private String created_at;
    private String updated_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getFamiliarity() {
        return familiarity;
    }

    public void setFamiliarity(String familiarity) {
        this.familiarity = familiarity;
    }

    public String getCart_number() {
        return cart_number;
    }

    public void setCart_number(String cart_number) {
        this.cart_number = cart_number;
    }

    public String getRules_confirmed() {
        return rules_confirmed;
    }

    public void setRules_confirmed(String rules_confirmed) {
        this.rules_confirmed = rules_confirmed;
    }

    public String getEmail_confirm_token() {
        return email_confirm_token;
    }

    public void setEmail_confirm_token(String email_confirm_token) {
        this.email_confirm_token = email_confirm_token;
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
}
