package com.delaroystodios.metakar.Model;

import java.util.List;

public class ListEarnParent {

    private String result;
    private String inventory;
    private List<ListEarn> withdraws;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getInventory() {
        return inventory;
    }

    public void setInventory(String inventory) {
        this.inventory = inventory;
    }

    public List<ListEarn> getWithdraws() {
        return withdraws;
    }

    public void setWithdraws(List<ListEarn> withdraws) {
        this.withdraws = withdraws;
    }
}
