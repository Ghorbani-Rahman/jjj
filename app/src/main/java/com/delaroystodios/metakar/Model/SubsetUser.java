package com.delaroystodios.metakar.Model;


import java.util.List;

public class SubsetUser {

    private List<SubsetUserModel> subset;
    private String subset_link;


    public List<SubsetUserModel> getSubset() {
        return subset;
    }

    public void setSubset(List<SubsetUserModel> subset) {
        this.subset = subset;
    }

    public String getSubset_link() {
        return subset_link;
    }

    public void setSubset_link(String subset_link) {
        this.subset_link = subset_link;
    }
}
