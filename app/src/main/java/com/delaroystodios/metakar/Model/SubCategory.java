package com.delaroystodios.metakar.Model;



public class SubCategory
{

    private int id;
    private String category_id;
    private String title;
    private String advertisements_count;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAdvertisements_count() {
        return advertisements_count;
    }

    public void setAdvertisements_count(String advertisements_count) {
        this.advertisements_count = advertisements_count;
    }
}
