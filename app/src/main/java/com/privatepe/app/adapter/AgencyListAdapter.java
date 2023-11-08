package com.privatepe.app.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.privatepe.app.R;
import com.privatepe.app.response.Agency.Commission;
import com.privatepe.app.response.Agency.SubagencyCommission;

import java.util.List;


public class AgencyListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<Commission> list;
    List<SubagencyCommission> sublist;
    String type;
    private static final int ITEM = 0;


    public AgencyListAdapter(Context context, List<Commission> list, List<SubagencyCommission> sublist, String type) {
        this.context = context;
        this.list = list;
        this.sublist = sublist;
        this.type = type;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case ITEM:
                View v1 = null;
                v1 = inflater.inflate(R.layout.agency_list_layout, parent, false);
                viewHolder = new myViewHolder(v1);
                break;


        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder hld, int position) {
        switch (getItemViewType(position)) {
            case ITEM:
                try {
                    final myViewHolder holder = (myViewHolder) hld;
                    if (type.equals("agency_policy")) {
                        if (position % 6 == 0) {
                            holder.tv_weekly_income.setText("<$" + String.valueOf(list.get(position).getAmount()));
                        } else {
                            holder.tv_weekly_income.setText("$" + String.valueOf(list.get(position).getAmount()));
                        }
                        holder.tv_commission_ratio.setText(String.valueOf(list.get(position).getCommissionRatio()) + "%");

                    } else if (type.equals("sub_agency_policy")) {
                        if (position % 6 == 0) {
                            holder.tv_weekly_income.setText("<$" + String.valueOf(sublist.get(position).getAmount()));
                        } else {
                            holder.tv_weekly_income.setText("$" + String.valueOf(sublist.get(position).getAmount()));
                        }
                        holder.tv_commission_ratio.setText(String.valueOf(sublist.get(position).getCommissionRatio()) + "%");
                    }

                    break;

                } catch (Exception e) {
                }

        }


    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        TextView tv_weekly_income, tv_commission_ratio;
        LinearLayout main_container;

        public myViewHolder(View itemView) {
            super(itemView);
            tv_weekly_income = itemView.findViewById(R.id.tv_weekly_income);
            tv_commission_ratio = itemView.findViewById(R.id.tv_commission_ratio);
            main_container = itemView.findViewById(R.id.main_container);
        }
    }

    @Override
    public int getItemCount() {
        Log.e("agencyActivity", "adapter3 " + list.size());
        int count = 0;
        if (type.equals("agency_policy")) {
            if (list != null) {
                count = list.size();
            }
        } else if (type.equals("sub_agency_policy")) {
            if (sublist != null) {
                count = sublist.size();
            }
        }
        return count;
    }


}