package com.delaroystodios.metakar.Adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.delaroystodios.metakar.Model.ShowWallet;
import com.delaroystodios.metakar.R;
import com.delaroystodios.metakar.helper.ConvertToPersianNumber;

import java.util.ArrayList;
import java.util.List;

    public class AdapterShowWallet extends RecyclerView.Adapter<AdapterShowWallet.ViewHolder>
    {
        private ArrayList<ShowWallet> data;
        private Context mContext;
        private View view;

        public AdapterShowWallet(Context context, ArrayList<ShowWallet> array) {

            mContext = context;
            this.data = array;
        }

        @Override
        public AdapterShowWallet.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_show_wallet, parent, false);
            AdapterShowWallet.ViewHolder viewHolder = new AdapterShowWallet.ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final AdapterShowWallet.ViewHolder holder, final int position)
        {

            final String amount = new ConvertToPersianNumber(String.valueOf(data.get(position).getAmount())).convertToPersian();
            holder.cost.setText(amount+" + ");
            final String des = new ConvertToPersianNumber(String.valueOf(data.get(position).getDescription())).convertToPersian();
            holder.description.setText(des);
          final String date = new ConvertToPersianNumber(data.get(position).getCreated_at_jalali()).convertToPersian();

            holder.sectionList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<String> details = new ArrayList<>();
                    details.add("ارزش : " + amount + " تومان ");
                    details.add("نوع : " + data.get(position).getType_farsi());
                    details.add("تاریخ ایجاد : " + date);
                    details.add("شرح : " + des);
                    showDetailWallet(details);
                }
            });
        }

        @Override
        public int getItemCount() {
            return data == null ? 0 : data.size();
        }

        private void showDetailWallet(ArrayList<String> details)
        {
            new MaterialDialog.Builder(mContext)
                    .title("مشاهده جزئیات کیف پول")
                    .titleGravity(GravityEnum.END)
                    .itemsGravity(GravityEnum.END)
                    .items(details)
                    .cancelable(true)
                    .show();

        }

        public static class ViewHolder extends RecyclerView.ViewHolder {

            private LinearLayout sectionList;
            private TextView cost;
            private TextView description;


            private ViewHolder(View view) {
                super(view);

                cost = view.findViewById(R.id.cost);
                description = view.findViewById(R.id.description);
                sectionList = view.findViewById(R.id.section_list);
            }
        }

        public void clear() {
            data.clear();
            notifyDataSetChanged();
        }

        public void addAll(List<ShowWallet> list) {
            data.addAll(list);
            notifyDataSetChanged();
        }

    }
