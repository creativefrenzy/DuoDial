package com.klive.app.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.klive.app.R;
import com.klive.app.response.AgencyHostWeekly.GiftByHostsRecord;

import java.util.List;

public class AgencyHostWeeklyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<GiftByHostsRecord> list;
    private static final int ITEM = 0;

    public AgencyHostWeeklyAdapter(Context context, List<GiftByHostsRecord> list) {
        this.context = context;
        this.list = list;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case ITEM:
                View v1 = null;
                v1 = inflater.inflate(R.layout.agency_host_weekly_layout, parent, false);
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
                    float value = Float.parseFloat(list.get(position).getPayoutDollar());
                    holder.tv_payout_dollar.setTextColor(context.getResources().getColor(R.color.match_color));

                    holder.host_name.setText(list.get(position).getUsername());
                    holder.host_id.setText("ID:" + String.valueOf(list.get(position).getProfileId()));
                    holder.tv_payout_dollar.setText(String.valueOf("$"+String.format("%.2f", value)));
                    holder.tv_user_total_input.setText(String.valueOf(list.get(position).getTotalCoins()));
                    holder.tv_video_input.setText(String.valueOf(list.get(position).getTotalCallCoins()));
                    holder.tv_gift_income_input.setText(String.valueOf(list.get(position).getTotalGiftCoins()));
                    break;

                } catch (Exception e) {
                }

        }


    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        ImageView host_user_image;
        TextView host_name, host_id, tv_payout_dollar, tv_user_total_input, tv_video_input, tv_gift_income_input;

        public myViewHolder(View itemView) {
            super(itemView);
            host_user_image = itemView.findViewById(R.id.host_user_image);
            host_name = itemView.findViewById(R.id.host_name);
            host_id = itemView.findViewById(R.id.host_id);
            tv_payout_dollar = itemView.findViewById(R.id.tv_payout_dollar);
            tv_user_total_input = itemView.findViewById(R.id.tv_user_total_input);
            tv_video_input = itemView.findViewById(R.id.tv_video_input);
            tv_gift_income_input = itemView.findViewById(R.id.tv_gift_income_input);

        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }


}