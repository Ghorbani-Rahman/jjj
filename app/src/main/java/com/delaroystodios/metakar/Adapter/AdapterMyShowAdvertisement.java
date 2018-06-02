package com.delaroystodios.metakar.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.delaroystodios.metakar.Activities.ShowDetailMyAdvertisements;
import com.delaroystodios.metakar.Model.MyAdvertisementModel;
import com.delaroystodios.metakar.R;
import com.delaroystodios.metakar.helper.ConvertToPersianNumber;

import java.util.ArrayList;
import java.util.List;

public class AdapterMyShowAdvertisement extends RecyclerView.Adapter<AdapterMyShowAdvertisement.ViewHolder>
{
    private ArrayList<MyAdvertisementModel> data;
    private Context mContext;
    private View view;

    public AdapterMyShowAdvertisement(Context context, ArrayList<MyAdvertisementModel> array) {

        mContext = context;
        this.data = array;
    }

    @Override
    public AdapterMyShowAdvertisement.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_show_my_advertisement, parent, false);
        AdapterMyShowAdvertisement.ViewHolder viewHolder = new AdapterMyShowAdvertisement.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final AdapterMyShowAdvertisement.ViewHolder holder, final int position)
    {

        holder.title.setText(new ConvertToPersianNumber(data.get(position).getTitle()).convertToPersian());
        holder.status.setText(new ConvertToPersianNumber(data.get(position).getStatus_farsi()).convertToPersian());
        holder.sectionList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext , ShowDetailMyAdvertisements.class);
                intent.putExtra("id" , data.get(position).getId()) ;
                intent.putExtra("type" , data.get(position).getType()) ;
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView status;
        private LinearLayout sectionList;


        private ViewHolder(View view)
        {
            super(view);

            title = view.findViewById(R.id.title);
            status = view.findViewById(R.id.status);
            sectionList =  view.findViewById(R.id.section_list);
        }
    }

    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<MyAdvertisementModel> list) {
        data.addAll(list);
        notifyDataSetChanged();
    }

}