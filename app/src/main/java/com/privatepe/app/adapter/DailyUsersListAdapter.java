package com.privatepe.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.privatepe.app.R;
import com.privatepe.app.response.daily_weekly.DailyUserListResponse;
import com.privatepe.app.utils.SessionManager;

import java.util.List;

public class DailyUsersListAdapter extends RecyclerView.Adapter<DailyUsersListAdapter.myViewHolder> {

    Context context;
    List<DailyUserListResponse.Result> list;
    SessionManager sessionManager;
    Activity activity;
    //ProfileDetailBottomSheetDialog.ZegoLivePagerListener zegoLivePagerListener;
    String hostId;
    public DailyUsersListAdapter(Context context, List<DailyUserListResponse.Result> list, Activity activity) { // ProfileDetailBottomSheetDialog.ZegoLivePagerListener listener,,  String hostId

        this.context = context;
        this.list = list;
        this.activity = activity;
        sessionManager = new SessionManager(context);
        //this.zegoLivePagerListener = listener;
//        this.hostId = hostId;
    }

    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.daily_user_list_item, parent, false);
        return new myViewHolder(v);

    }

    @Override
    public void onBindViewHolder(myViewHolder holder, int position) {

        //Log.e("position====>",position+"");
        holder.tvPosition.setText("#"+String.valueOf(position + 4));

        holder.tvCount.setText(list.get(position).getTotal_coin_earned() + "");
        holder.tvUserName.setText(list.get(position).getName().toLowerCase());
        holder.tvCharmLevel.setText(list.get(position).getCharm_level()+"");

        if(list.get(position).getCharm_level() ==0){
            holder.rlBg.setBackground(context.getResources().getDrawable(R.drawable.charm_lv0));
        }else if(list.get(position).getCharm_level() >=1 && list.get(position).getCharm_level() <=5){
            holder.rlBg.setBackground(context.getResources().getDrawable(R.drawable.charm_lv1_5));
        }else if(list.get(position).getCharm_level() >=6 && list.get(position).getCharm_level() <=10){
            holder.rlBg.setBackground(context.getResources().getDrawable(R.drawable.charm_lv6_10));
        }else if(list.get(position).getCharm_level() >=11 && list.get(position).getCharm_level() <=15){
            holder.rlBg.setBackground(context.getResources().getDrawable(R.drawable.charm_lv11_15));
        }else if(list.get(position).getCharm_level() >=16 && list.get(position).getCharm_level() <=20){
            holder.rlBg.setBackground(context.getResources().getDrawable(R.drawable.charm_lv16_20));
        }else if(list.get(position).getCharm_level() >=21 && list.get(position).getCharm_level() <=25){
            holder.rlBg.setBackground(context.getResources().getDrawable(R.drawable.charm_lv21_25));
        }else if(list.get(position).getCharm_level() >=26 && list.get(position).getCharm_level() <=30){
            holder.rlBg.setBackground(context.getResources().getDrawable(R.drawable.charm_lv26_30));
        }else if(list.get(position).getCharm_level() >=31 && list.get(position).getCharm_level() <=35){
            holder.rlBg.setBackground(context.getResources().getDrawable(R.drawable.charm_lv31_35));
        }else if(list.get(position).getCharm_level() >=36 && list.get(position).getCharm_level() <=40){
            holder.rlBg.setBackground(context.getResources().getDrawable(R.drawable.charm_lv36_40));
        }else if(list.get(position).getCharm_level() >=41 && list.get(position).getCharm_level() <=45){
            holder.rlBg.setBackground(context.getResources().getDrawable(R.drawable.charm_lv41_45));
        }else if(list.get(position).getCharm_level() >=46 && list.get(position).getCharm_level() <=50){
            holder.rlBg.setBackground(context.getResources().getDrawable(R.drawable.charm__lv46_50));
        }

        try {
            if(list.get(position).getProfile_images() != null && list.get(position).getProfile_images().size()>0) {
                Glide.with(context)
                        .load(list.get(position).getProfile_images().get(0).getImage_name())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(R.drawable.fake_user_icon)
                        .placeholder(R.drawable.fake_user_icon)
                        .dontTransform()
                        .override(200, 200)
                        .into(holder.ivUserImage);
            }
        }catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }

        holder.llParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ProfileDetailBottomSheetDialog detailDialog = new ProfileDetailBottomSheetDialog(new ContextThemeWrapper(context,  R.style.DialogSlideAnim), list.get(position).getProfile_id()+"", list.get(position).getId()+"",position+3, activity,zegoLivePagerListener,"other", hostId);
//                detailDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {

        TextView tvPosition, tvUserName, tvCount, tvCharmLevel;
        LinearLayout llParent;
        ImageView ivPosition, ivUserImage;
        RelativeLayout rlBg;
        public myViewHolder(View itemView) {
            super(itemView);

            ivPosition = itemView.findViewById(R.id.ivPosition);
            ivUserImage = itemView.findViewById(R.id.ivUserImage);
            tvPosition = itemView.findViewById(R.id.tvPosition);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvCount = itemView.findViewById(R.id.tvCount);
            tvCharmLevel = itemView.findViewById(R.id.tvCharmLevel);
            llParent = itemView.findViewById(R.id.llParent);
            rlBg = itemView.findViewById(R.id.rl_bg);

        }
    }

}