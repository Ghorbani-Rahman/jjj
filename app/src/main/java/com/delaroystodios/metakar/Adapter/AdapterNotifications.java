package com.delaroystodios.metakar.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.delaroystodios.metakar.Activities.ReadNotificationsActivity;
import com.delaroystodios.metakar.Model.Notifications;
import com.delaroystodios.metakar.R;
import com.delaroystodios.metakar.helper.ConvertToPersianNumber;
import com.delaroystodios.metakar.helper.DeleteNotification;

import java.util.ArrayList;
import java.util.List;

public class AdapterNotifications extends RecyclerView.Adapter<AdapterNotifications.ViewHolder>
{
    private ArrayList<Notifications> data;
    private Context mContext;
    private View view;
    private DeleteNotification deleteNotification;

    public void setOnClickItemRecyclerListener(DeleteNotification deleteNotification) {
        this.deleteNotification = deleteNotification;
    }

    public AdapterNotifications(Context context, ArrayList<Notifications> array) {

        mContext = context;
        this.data = array;
    }

    @Override
    public AdapterNotifications.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_notifications_recyclerview, parent, false);
        AdapterNotifications.ViewHolder viewHolder = new AdapterNotifications.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final AdapterNotifications.ViewHolder holder, final int position)
    {

        holder.title_message.setText(data.get(position).getTitle());
        holder.title_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext , ReadNotificationsActivity.class);
                intent.putExtra("idNotifications" , data.get(position).getId());
                mContext.startActivity(intent);
            }
        });

        if(data.get(position).getPivot().getHas_been_seen().equals("no")) {
            holder.status.setBackgroundResource(R.drawable.ic_no_read_notification_message);
        }else
        {
            holder.status.setBackgroundResource(R.drawable.ic_read_notification_message);

        }

        holder.date_send.setText(new ConvertToPersianNumber(data.get(position).getCreated_at_jalali()).convertToPersian());

      /*  holder.delete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(position != RecyclerView.NO_POSITION)
                {
                    deleteNotification.onActionClicked(data.get(position).getId());
                }*/
     //       }
    //    });
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title_message;
        private TextView date_send;
        private ImageView status;

        private ViewHolder(View view) {
            super(view);

            title_message = (TextView)view.findViewById(R.id.title_message);
            date_send = (TextView)view.findViewById(R.id.date_send);
            status = (ImageView)view.findViewById(R.id.status);
        }
    }

    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Notifications> list) {
        data.addAll(list);
        notifyDataSetChanged();
    }

}
