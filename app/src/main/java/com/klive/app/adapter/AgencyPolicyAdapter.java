package com.klive.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.klive.app.R;
import com.klive.app.response.HostIncomeDetail.IncomeDetailWalletHistory;

import java.util.List;

public class AgencyPolicyAdapter extends RecyclerView.Adapter<AgencyPolicyAdapter.ViewHolder> {
    Context context;
    List<String> list;

    public AgencyPolicyAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.agency_policy_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

       /* if (String.valueOf(list.get(position).getStatus()).equals("4")){
            holder.tv_call_income.setText("Call income");
        }else {
            holder.tv_call_income.setText("Gift income");
        }
        holder.tv_credit.setText("+"+String.valueOf(list.get(position).getCredit()));
        holder.tv_updated_at.setText(String.valueOf(list.get(position).getUpdatedAt()));
        holder.tv_caller_profile_id.setText("ID : "+String.valueOf(list.get(position).getCallerProfileId()));*/


    }


    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_call_income,tv_credit,tv_updated_at,tv_caller_profile_id;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_call_income = itemView.findViewById(R.id.tv_call_income);
            tv_credit = itemView.findViewById(R.id.tv_credit);
            tv_updated_at = itemView.findViewById(R.id.tv_updated_at);
            tv_caller_profile_id = itemView.findViewById(R.id.tv_caller_profile_id);


        }
    }
}