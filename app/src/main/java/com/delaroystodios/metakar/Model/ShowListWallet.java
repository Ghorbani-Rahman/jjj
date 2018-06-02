package com.delaroystodios.metakar.Model;

import java.util.List;

public class ShowListWallet {

    private String balance;
    private List<ShowWallet> wallet;

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public List<ShowWallet> getWallet() {
        return wallet;
    }

    public void setWallet(List<ShowWallet> wallet) {
        this.wallet = wallet;
    }
}
