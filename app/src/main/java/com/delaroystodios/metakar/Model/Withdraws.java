package com.delaroystodios.metakar.Model;


public class Withdraws
{
    private String user_id;
    private String amount;
    private String withdraw_at;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getWithdraw_at() {
        return withdraw_at;
    }

    public void setWithdraw_at(String withdraw_at) {
        this.withdraw_at = withdraw_at;
    }
}
