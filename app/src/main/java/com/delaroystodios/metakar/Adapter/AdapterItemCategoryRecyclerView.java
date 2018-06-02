package com.delaroystodios.metakar.Adapter;


import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.delaroystodios.metakar.MainActivity;
import com.delaroystodios.metakar.Model.Category;
import com.delaroystodios.metakar.R;
import com.delaroystodios.metakar.fragment.SubCategoryFragment;

import java.util.ArrayList;
import java.util.List;

public class AdapterItemCategoryRecyclerView
        extends RecyclerView.Adapter<AdapterItemCategoryRecyclerView.ViewHolder>
{

    private ArrayList<Category> dataCategory;
    private Context mContext;
    private RecyclerView recyclerView;
    View view;


    public AdapterItemCategoryRecyclerView(Context context, ArrayList<Category> array , RecyclerView itemview) {

        mContext = context;
        dataCategory = array;
        recyclerView = itemview;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_category_recycler_view, parent , false );
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final AdapterItemCategoryRecyclerView.ViewHolder holder, final int position) {


        holder.tile_category.setText(dataCategory.get(position).getTitle());


        holder.tile_category.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SubCategoryFragment subCategoryFragment = new SubCategoryFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("titleCategory", dataCategory.get(position).getTitle());
                    bundle.putString("idCategory", dataCategory.get(position).getId());
                    subCategoryFragment.setArguments(bundle);


                    MainActivity.ft = MainActivity.fragmentManager.beginTransaction();
                    MainActivity.ft.add(R.id.viewpager, subCategoryFragment , "sub").addToBackStack("sub");
                    MainActivity.ft.commit();
                }
            });
    }

    @Override
    public int getItemCount() {
        return dataCategory == null ? 0 : dataCategory.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tile_category;


        private ViewHolder(View view) {
            super(view);

            tile_category = (TextView)view.findViewById(R.id.title_category);

        }
    }

    public void clear() {
        dataCategory.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Category> listCategory) {
        dataCategory.addAll(listCategory);
        notifyDataSetChanged();
    }

}