package com.delaroystodios.metakar.Adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.delaroystodios.metakar.Model.ListEarn;
import com.delaroystodios.metakar.R;
import com.delaroystodios.metakar.helper.ConvertToPersianNumber;

import java.util.ArrayList;
import java.util.List;

public class AdapterListEarn extends RecyclerView.Adapter<AdapterListEarn.ViewHolder>
{
    private ArrayList<ListEarn> data;
    private Context mContext;
    private View view;

    public AdapterListEarn(Context context, ArrayList<ListEarn> array) {

        mContext = context;
        this.data = array;
    }

    @Override
    public AdapterListEarn.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_show_earns, parent, false);
        AdapterListEarn.ViewHolder viewHolder = new AdapterListEarn.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final AdapterListEarn.ViewHolder holder, final int position)
    {
        holder.amount.setText(new ConvertToPersianNumber(data.get(position).getAmount()).convertToPersian());
        holder.status.setText(new ConvertToPersianNumber(data.get(position).getStatus()).convertToPersian());
        holder.sectionShowListEarn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "sample", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView amount;
        private TextView status;
        private TextView sectionShowListEarn;



        private ViewHolder(View view)
        {
            super(view);

            amount = view.findViewById(R.id.amount);
            status = view.findViewById(R.id.status);
            sectionShowListEarn = view.findViewById(R.id.section_show_list_earn);

        }
    }

    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<ListEarn> list) {
        data.addAll(list);
        notifyDataSetChanged();
    }

}
