package com.delaroystodios.metakar.Adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.delaroystodios.metakar.Model.Withdraws;
import com.delaroystodios.metakar.R;
import com.delaroystodios.metakar.helper.ConvertToPersianNumber;
import java.util.ArrayList;
import java.util.List;

public class AdapterWithdraws extends RecyclerView.Adapter<AdapterWithdraws.ViewHolder>
{
    private ArrayList<Withdraws> data;
    View view;

    public AdapterWithdraws(ArrayList<Withdraws> array) {

        this.data = array;
    }

    @Override
    public AdapterWithdraws.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_withdraws_recyclerview, parent, false);
        AdapterWithdraws.ViewHolder viewHolder = new AdapterWithdraws.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final AdapterWithdraws.ViewHolder holder, final int position)
    {

        holder.idUser.setText(new ConvertToPersianNumber(data.get(position).getUser_id()).convertToPersian());
        holder.costWithdraws.setText(afterTextChangedFunction(data.get(position).getAmount()));
/*

        Roozh jCalCreate = new Roozh();
        String created_at = data.get(position).getWithdraw_at();
        int c_year = Integer.parseInt(created_at.substring(0 , 4));
        int c_Month = Integer.parseInt(created_at.substring(6 , 7));
        int c_day = Integer.parseInt(created_at.substring(9 , 10));
        jCalCreate.GregorianToPersian(c_year, c_Month, c_day);

        String time = created_at.substring(12);

        ConvertToPersianNumber conCreateToPersion = new ConvertToPersianNumber(time + "   " +jCalCreate.toString());
        holder.dateWithdraws.setText(conCreateToPersion.convertToPersian());
*/

    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView idUser;
        private TextView costWithdraws;


        private ViewHolder(View view) {
            super(view);

            idUser = view.findViewById(R.id.id_user);
            costWithdraws = view.findViewById(R.id.cost_withdraws);

        }
    }

    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }

    private String afterTextChangedFunction(String str) {
        String s;

        s = String.format("%,d", Long.parseLong(str));

        return new ConvertToPersianNumber(s).convertToPersian() + " تومان";

    }

    public void addAll(List<Withdraws> list) {
        data.addAll(list);
        notifyDataSetChanged();
    }

}
