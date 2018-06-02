package com.delaroystodios.metakar.Adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.delaroystodios.metakar.Model.Earnings;
import com.delaroystodios.metakar.R;
import com.delaroystodios.metakar.helper.ConvertToPersianNumber;

import java.util.ArrayList;
import java.util.List;

public class AdapterEarnings extends RecyclerView.Adapter<AdapterEarnings.ViewHolder>
{
    private ArrayList<Earnings> data;
    private View view;

    public AdapterEarnings(ArrayList<Earnings> array) {

        this.data = array;
    }

    @Override
    public AdapterEarnings.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_earnings_recyclerview, parent, false);
        AdapterEarnings.ViewHolder viewHolder = new AdapterEarnings.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final AdapterEarnings.ViewHolder holder, final int position)
    {

        holder.userId.setText(new ConvertToPersianNumber(data.get(position).getUserId()).convertToPersian());
        holder.amount.setText(afterTextChangedFunction(data.get(position).getAmount()));

       /* Roozh jCalCreate = new Roozh();
        String created_at = data.get(position).getCreated_at();
        int c_year = Integer.parseInt(created_at.substring(0 , 4));
        int c_Month = Integer.parseInt(created_at.substring(6 , 7));
        int c_day = Integer.parseInt(created_at.substring(9 , 10));
        jCalCreate.GregorianToPersian(c_year, c_Month, c_day);

        String time = created_at.substring(12);

        ConvertToPersianNumber convertToPersianNumber = new ConvertToPersianNumber(time + "   " +jCalCreate.toString());
        holder.createdAt.setText(convertToPersianNumber.convertToPersian());

        holder.description.setText(new ConvertToPersianNumber(data.get(position).getDescription()).convertToPersian());*/

    }

    private String afterTextChangedFunction(String str) {
        String s;

        s = String.format("%,d", Long.parseLong(str));

        return new ConvertToPersianNumber(s).convertToPersian() + " تومان";

    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {


        private TextView userId;
        private TextView amount;

        private ViewHolder(View view) {
            super(view);

            userId = view.findViewById(R.id.id_user_earning);
            amount = view.findViewById(R.id.amount_earning);

        }
    }

    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Earnings> list) {
        data.addAll(list);
        notifyDataSetChanged();
    }

}
