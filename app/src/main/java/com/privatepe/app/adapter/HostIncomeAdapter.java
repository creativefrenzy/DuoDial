package com.privatepe.app.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.privatepe.app.R;
import com.privatepe.app.activity.IncomeDetailActivity;
import com.privatepe.app.response.HostIncomeResponse.AllWeeklyData;

import java.util.List;

public class HostIncomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<AllWeeklyData> list;
    private static final int ITEM = 0;

    public HostIncomeAdapter(Context context, List<AllWeeklyData> list) {
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
                v1 = inflater.inflate(R.layout.host_weekly_incone_layout, parent, false);
                viewHolder = new myViewHolder(v1);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder hld, @SuppressLint("RecyclerView") int position) {
        switch (getItemViewType(position)) {
            case ITEM:
                try {
                    final myViewHolder holder = (myViewHolder) hld;
                    String[] date = String.valueOf(list.get(position).getDate()).split("-");
                    String y1 = date[0];
                    String m1 = date[1];
                    String d1 = date[2];
                    String finalDate = d1 + "-" + m1;
                    holder.tv_date.setText(finalDate);
                    holder.tv_user_total_input.setText(String.valueOf(list.get(position).getTotalCoins()));
                    holder.tv_video_input.setText(String.valueOf(list.get(position).getTotalCallCoins()));
                    holder.tv_gift_income_input.setText(String.valueOf(list.get(position).getTotalGiftCoins()));
                    holder.tv_reward_income_input.setText(String.valueOf(list.get(position).getTotalRewardCoins()));

                    holder.cardView_user.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(context, IncomeDetailActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("select_date", list.get(position).getDate());
                            intent.putExtras(bundle);
                            context.startActivity(intent);

                        }
                    });

                    break;

                } catch (Exception e) {
                }

        }


    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        TextView  tv_date,tv_user_total_input, tv_video_input, tv_gift_income_input,tv_reward_income_input;
        CardView cardView_user;

        public myViewHolder(View itemView) {
            super(itemView);
            cardView_user = itemView.findViewById(R.id.cardView_user);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_user_total_input = itemView.findViewById(R.id.tv_user_total_input);
            tv_video_input = itemView.findViewById(R.id.tv_video_input);
            tv_gift_income_input = itemView.findViewById(R.id.tv_gift_income_input);
            tv_reward_income_input = itemView.findViewById(R.id.tv_reward_income_input);

        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }


}
