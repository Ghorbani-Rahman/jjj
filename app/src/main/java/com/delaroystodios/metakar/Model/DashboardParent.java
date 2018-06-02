package com.delaroystodios.metakar.Model;

import java.util.List;

public class DashboardParent {

    private List<Advertisement> advertisements;
    private String balance;

    public List<Advertisement> getAdvertisements() {
        return advertisements;
    }

    public void setAdvertisements(List<Advertisement> advertisements) {
        this.advertisements = advertisements;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
}
