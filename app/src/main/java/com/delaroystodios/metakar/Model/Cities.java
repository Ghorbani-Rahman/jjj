package com.delaroystodios.metakar.Model;


public class Cities
{
    private String id;
    private String title;
    private String province_id;

    public String getProvince_id() {
        return province_id;
    }

    public void setProvince_id(String province_id) {
        this.province_id = province_id;
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

    public void setTitle(String city) {
        this.title = city;
    }
}
