package com.privatepe.host.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.privatepe.host.R;
import com.privatepe.host.response.HostIncomeDetail.IncomeDetailWalletHistory;

import java.util.List;

public class HostIncomeDetailAdapter extends RecyclerView.Adapter<HostIncomeDetailAdapter.ViewHolder> {
    Context context;
    List<IncomeDetailWalletHistory> list;

    public HostIncomeDetailAdapter(Context context, List<IncomeDetailWalletHistory> list) {
        this.context = context;
        this.list = list;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.income_detail_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (String.valueOf(list.get(position).getStatus()).equals("4")) {
            holder.tv_call_income.setText("Call income");
        } else if (String.valueOf(list.get(position).getStatus()).equals("45")) {
            holder.tv_call_income.setText("Bonus");
        } else {
            holder.tv_call_income.setText("Gift income");
        }

        holder.tv_credit.setText("+" + String.valueOf(list.get(position).getCredit()));
        holder.tv_updated_at.setText(String.valueOf(list.get(position).getUpdatedAt()));
        holder.tv_caller_profile_id.setText("ID : " + String.valueOf(list.get(position).getCallerProfileId()));

    }


    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_call_income, tv_credit, tv_updated_at, tv_caller_profile_id;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_call_income = itemView.findViewById(R.id.tv_call_income);
            tv_credit = itemView.findViewById(R.id.tv_credit);
            tv_updated_at = itemView.findViewById(R.id.tv_updated_at);
            tv_caller_profile_id = itemView.findViewById(R.id.tv_caller_profile_id);


        }
    }
}