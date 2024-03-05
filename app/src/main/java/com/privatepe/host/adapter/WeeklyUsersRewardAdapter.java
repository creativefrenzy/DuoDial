package com.privatepe.host.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.privatepe.host.R;
import com.privatepe.host.response.daily_weekly.WeeklyUserRewardResponse;

import java.util.List;

public class WeeklyUsersRewardAdapter extends RecyclerView.Adapter<WeeklyUsersRewardAdapter.myViewHolder> {

    Context context;
    List<WeeklyUserRewardResponse.WeeklyRewardData> list;
    Activity activity;

    public WeeklyUsersRewardAdapter(Context context, List<WeeklyUserRewardResponse.WeeklyRewardData> list, Activity activity) {

        this.context = context;
        this.list = list;
        this.activity = activity;
    }

    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.weekly_user_reward_list_item, parent, false);
        return new myViewHolder(v);

    }

    @Override
    public void onBindViewHolder(myViewHolder holder, int position) {

        if(list.get(position).getRank() ==1)
            holder.tvPosition.setText(String.valueOf(list.get(position).getRank()+"st"));
        else if(list.get(position).getRank() ==2){
            holder.tvPosition.setText(String.valueOf(list.get(position).getRank()+"nd"));
        }else if(list.get(position).getRank() ==3){
            holder.tvPosition.setText(String.valueOf(list.get(position).getRank()+"rd"));
        }else {
            holder.tvPosition.setText(String.valueOf(list.get(position).getRank() + "th"));
        }
        //String aFormatted = getPriceFromNumber(list.get(position).getReward_coin());

        holder.tvCount.setText(list.get(position).getReward_coin() + "");

    }

    public static String getPriceFromNumber(long number){

        if(number >= 1000000000){
            return String.format("%.1fB", number/ 1000000000.0);
        }
        if(number >= 10000000.0){
            return String.format("%.1fCr", number/ 10000000.0);
        }
        if(number >= 1000000){
            return String.format("%.1fM", number/ 1000000.0);
        }

        if(number >= 100000){
            return String.format("%.1fL", number/ 100000.0);
        }

        if(number >=1000){
            return String.format("%.1fK", number/ 1000.0);
        }
        return String.valueOf(number);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        TextView tvPosition,  tvCount;
        View viewLine;

        public myViewHolder(View itemView) {
            super(itemView);
            tvPosition = itemView.findViewById(R.id.tvPosition);
            tvCount = itemView.findViewById(R.id.tvCount);
            viewLine = itemView.findViewById(R.id.viewLine);
        }
    }

}