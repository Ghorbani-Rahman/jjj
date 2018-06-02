package com.delaroystodios.metakar.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.delaroystodios.metakar.MainActivity;
import com.delaroystodios.metakar.Model.SubCategory;
import com.delaroystodios.metakar.R;
import com.delaroystodios.metakar.fragment.ShowSubCategory;
import com.delaroystodios.metakar.helper.ConvertToPersianNumber;

import java.util.ArrayList;
import java.util.List;

import static com.delaroystodios.metakar.MainActivity.ft;

public class AdapterItemSubCategory
        extends RecyclerView.Adapter<AdapterItemSubCategory.ViewHolder>
{

    private ArrayList<SubCategory> dataSubCategory;
    private Context mContext;
    private RecyclerView recyclerView;
    View view;

    public AdapterItemSubCategory(Context context, ArrayList<SubCategory> data , RecyclerView itemview) {

        dataSubCategory = data;
        mContext = context;
        recyclerView = itemview;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_category_recycler_view, parent , false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final AdapterItemSubCategory.ViewHolder holder, final int position) {


        holder.title_category.setText(dataSubCategory.get(position).getTitle() + "   (" + new ConvertToPersianNumber(dataSubCategory.get(position).getAdvertisements_count()).convertToPersian()+")");

        holder.title_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowSubCategory showSubCategory = new ShowSubCategory();
                Bundle bundle = new Bundle();
                bundle.putString("titleSubCategory", dataSubCategory.get(position).getTitle());
                bundle.putInt("idSubCategory", dataSubCategory.get(position).getId());
                showSubCategory.setArguments(bundle);

                ft = MainActivity.fragmentManager.beginTransaction();

                ft.add(R.id.viewpager, showSubCategory , "showSub").addToBackStack("showSub");

                ft.commit();

            }
        });

    }

    @Override
    public int getItemCount() {
        return dataSubCategory == null ? 0 : dataSubCategory.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title_category;


        private ViewHolder(View view) {
            super(view);

            title_category = (TextView)view.findViewById(R.id.title_category);

        }
    }

    public void clear() {
        dataSubCategory.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<SubCategory> listSubCategory) {
        dataSubCategory.addAll(listSubCategory);
        notifyDataSetChanged();
    }

}