package com.delaroystodios.metakar.Model;


public class Information {

    private String detail_id;
    private String type;
    private String title;
    private String value;


    public String getDetail_id() {
        return detail_id;
    }

    public void setDetail_id(String detail_id) {
        this.detail_id = detail_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    @Override
    public String toString() {
        return "Information{" +
                "detail_id='" + detail_id + '\n' +
                ", type='" + type + '\n' +
                ", title='" + title + '\n' +
                ", value='" + value + '\n' +
                '}';
    }

//    public enum Type {
//        mobile, telegram, instagram , address
//    }
}
