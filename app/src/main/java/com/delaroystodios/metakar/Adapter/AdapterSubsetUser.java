package com.delaroystodios.metakar.Adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.delaroystodios.metakar.Model.SubsetUserModel;
import com.delaroystodios.metakar.R;
import com.delaroystodios.metakar.helper.ConvertToPersianNumber;

import java.util.ArrayList;
import java.util.List;

public class AdapterSubsetUser extends RecyclerView.Adapter<AdapterSubsetUser.ViewHolder>
{
    private ArrayList<SubsetUserModel> data;
    private Context mContext;
    View view;

    public AdapterSubsetUser(Context context, ArrayList<SubsetUserModel> array) {

        mContext = context;
        this.data = array;
    }

    @Override
    public AdapterSubsetUser.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_subset_recycler_view, parent, false);
        AdapterSubsetUser.ViewHolder viewHolder = new AdapterSubsetUser.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final AdapterSubsetUser.ViewHolder holder, final int position)
    {

        ConvertToPersianNumber convertToPersianNumber = new ConvertToPersianNumber(String.valueOf(position+1));
        holder.numberRowSubset.setText(convertToPersianNumber.convertToPersian());

        holder.nameFamilySubsetUser.setText(data.get(position).getName() + " " + data.get(position).getFamily());

    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView numberRowSubset;
        private TextView nameFamilySubsetUser;


        private ViewHolder(View view) {
            super(view);

            numberRowSubset = view.findViewById(R.id.number_row_subset);
            nameFamilySubsetUser = view.findViewById(R.id.name_family_subset_user);
        }
    }

    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<SubsetUserModel> list) {
        data.addAll(list);
        notifyDataSetChanged();
    }

}
