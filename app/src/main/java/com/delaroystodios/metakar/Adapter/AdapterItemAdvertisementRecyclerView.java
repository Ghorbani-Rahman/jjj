package com.delaroystodios.metakar.Adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.delaroystodios.metakar.Activities.AdvertisementDetailsActivity;
import com.delaroystodios.metakar.Model.Advertisement;
import com.delaroystodios.metakar.R;
import com.delaroystodios.metakar.helper.ConvertToPersianNumber;
import com.delaroystodios.metakar.ui.BannerImageView;

import java.util.ArrayList;
import java.util.List;

public class AdapterItemAdvertisementRecyclerView
        extends RecyclerView.Adapter<AdapterItemAdvertisementRecyclerView.ViewHolder>
{

    private ArrayList<Advertisement> data;
    private Context mContext;

    public AdapterItemAdvertisementRecyclerView(Context context, ArrayList<Advertisement> array) {

        mContext = context;
        data = array;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_advertisement_recycler_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final AdapterItemAdvertisementRecyclerView.ViewHolder holder, final int position) {

        if(data.get(position).getType().equals("visit"))
        {
            holder.img_type_advertisements.setBackgroundResource(R.drawable.ic_type_advertisement_visit);
        }
        else if(data.get(position).getType().equals("text"))
        {
            holder.img_type_advertisements.setBackgroundResource(R.drawable.ic_type_advertisement_text);
        }
        else if(data.get(position).getType().equals("tizer"))
        {
            holder.img_type_advertisements.setBackgroundResource(R.drawable.ic_visit_tizer);
        }
        else if(data.get(position).getType().equals("social"))
        {
            holder.img_type_advertisements.setBackgroundResource(R.drawable.ic_visit_social_network);
        }


        holder.cost_advertisement.setText(new ConvertToPersianNumber(data.get(position).getCost()).convertToPersian());

        holder.progress.getIndeterminateDrawable().setColorFilter(mContext.getResources().getColor(R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);

        Glide.with(mContext)
                .load(data.get(position).getImage_url())
                .error(R.drawable.metakar)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

                        holder.progress.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        holder.progress.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(holder.imgAdvertisement);


        holder.titleAdvertisement.setText(new ConvertToPersianNumber(data.get(position).getTitle()).convertToPersian());
        holder.total_visits_count.setText(new ConvertToPersianNumber(data.get(position).getTotal_visits_count()).convertToPersian());
        holder.total_user_visit_count.setText(new ConvertToPersianNumber(data.get(position).getTotal_user_visit_count()).convertToPersian());
        holder.durationVisit.setText(new ConvertToPersianNumber(data.get(position).getDuration_farsi()).convertToPersian());
        holder.remaning_visit.setText(new ConvertToPersianNumber(data.get(position).getRemaning_visit()).convertToPersian());


        holder.linearLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, AdvertisementDetailsActivity.class);
                intent.putExtra("Advertisement_ID", data.get(position).getId());
                intent.putExtra("Advertisement_Type", data.get(position).getType());

                mContext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView img_type_advertisements;
        private TextView cost_advertisement;
        private BannerImageView imgAdvertisement;
        private TextView titleAdvertisement;
        private TextView total_visits_count;
        private TextView total_user_visit_count;
        private TextView durationVisit;
        private TextView remaning_visit;
        private LinearLayout linearLayout;
        private ProgressBar progress;


        private ViewHolder(View view) {
            super(view);

            img_type_advertisements =  view.findViewById(R.id.type_advertisment);
            cost_advertisement = view.findViewById(R.id.cost_advertisment);
            imgAdvertisement = view.findViewById(R.id.imgAdvertisement);
            titleAdvertisement = view.findViewById(R.id.title_advertisement);
            total_user_visit_count = view.findViewById(R.id.total_user_visit_count);
            total_visits_count = view.findViewById(R.id.total_visits_count);
            durationVisit = view.findViewById(R.id.durationVisit);
            remaning_visit = view.findViewById(R.id.remaning_visit);
            linearLayout = view.findViewById(R.id.cardlist_item);
            progress =  view.findViewById(R.id.progress);
        }
    }

    public void clear() {
        data.clear();
        notifyDataSetChanged();

    }

    public void addAll(List<Advertisement> list) {
        data.addAll(list);
        notifyDataSetChanged();

    }

}
