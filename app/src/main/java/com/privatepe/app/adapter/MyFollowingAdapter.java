package com.privatepe.app.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.privatepe.app.R;
import com.privatepe.app.activity.DetailedFansAndFollowers;
import com.privatepe.app.model.DataFollowers;
import com.privatepe.app.model.MyFollowerUserData;
import com.privatepe.app.model.MyTopFansData;
import com.privatepe.app.model.Usertopfans;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyFollowingAdapter extends RecyclerView.Adapter<MyFollowingAdapter.myViewHolder>{

    private final Context mContext;
    private List<MyTopFansData> MyTopFans;
    private List<DataFollowers> MyFollowersData;
    private String Screen;

    public MyFollowingAdapter(@NotNull Context context, @Nullable List<MyTopFansData> usersData) {
        this.mContext = context;
        this.MyTopFans = usersData;

    }
        public MyFollowingAdapter(@NotNull Context context, @Nullable List<DataFollowers> Followers,String screen) {
        this.mContext = context;
        this.MyFollowersData = Followers;
        this.Screen = screen;
            }


    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_following_top_users, parent, false);
        return new MyFollowingAdapter.myViewHolder(v1);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if(MyTopFans != null&& MyTopFans.size() > position){
            MyTopFansData user = MyTopFans.get(position);
            assert user != null;
            Glide.with(mContext).load(user.user_details.profile_images.get(0).getImageName())
                    .error(R.drawable.default_profile)
                    .into(holder.im_profile_pic);
            holder.tv_profile_name.setText(user.user_details.name);
            holder.tv_profile_lvl.setText("Lvl"+user.user_details.rich_level);

            holder.tv_profile_coins.setText(String.valueOf(user.total_beans));
            String formatDate = formatDate(String.valueOf(user.updated_at));
            holder.tv_profile_date.setText(formatDate);
            if(user.user_details.is_online==1) {
                holder.tv_profile_status.setText("Online");
                holder.img_profile_status.setImageResource(R.color.green);
            }else {
                holder.tv_profile_status.setText("Offline");
                holder.img_profile_status.setImageResource(R.color.red);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, DetailedFansAndFollowers.class);
                    intent.putExtra("profile_img",user.user_details.profile_images.get(0).getImageName());
                    intent.putExtra("profile_name",user.user_details.name);
                    intent.putExtra("profile_lvl",String.valueOf(user.user_details.rich_level));
                    intent.putExtra("profile_is_0nline",String.valueOf(user.user_details.is_online));
                    intent.putExtra("favorite_by_you_count",String.valueOf(user.user_details.favorite_by_you_count));
                    intent.putExtra("profile_id",String.valueOf(user.user_details.id));
                    try {
                        intent.putExtra("total_beans",String.valueOf(user.total_beans));
                    }catch (Exception e){
                        intent.putExtra("total_beans","0");
                    }
                    mContext.startActivity(intent);
                }
            });
        }
        if (MyFollowersData != null && MyFollowersData.size() > position) {

                    try {
                        holder.tv_profile_coins.setText(String.valueOf(MyFollowersData.get(position).getUsertopfans().getTotal_beans()));
                        String formatDate = formatDate(String.valueOf(MyFollowersData.get(position).getUsertopfans().getUpdated_at()));
                        holder.tv_profile_date.setText(formatDate);
                    }catch (Exception e) {
                        holder.tv_profile_coins.setText("0");
                        holder.tv_profile_date.setText("");
                    }
                    MyFollowerUserData user = MyFollowersData.get(position).getUser_data_follower();
            // Load profile image using Glide

            Glide.with(mContext)
                    .load(Objects.requireNonNull(user.getProfile_images()).get(0).getImageName())
                    .error(R.drawable.default_profile)
                    .into(holder.im_profile_pic);

            // Set profile name
            holder.tv_profile_name.setText(user.getName());

            // Set profile level
            holder.tv_profile_lvl.setText("Lvl" + user.getLevel());

            // Check if user is online
            if (user.is_online()==1) {
                holder.tv_profile_status.setText("Online");
                holder.img_profile_status.setImageResource(R.color.green);
            } else {
                holder.tv_profile_status.setText("Offline");
                holder.img_profile_status.setImageResource(R.color.red);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, DetailedFansAndFollowers.class);
                    intent.putExtra("profile_img",(user.getProfile_images()).get(0).getImageName());
                    intent.putExtra("profile_name",user.getName());
                    intent.putExtra("profile_lvl",String.valueOf(user.getLevel()));
                    intent.putExtra("profile_is_0nline",String.valueOf(user.is_online()));
                    intent.putExtra("favorite_by_you_count",String.valueOf(user.getFavorite_by_you_count()));
                    intent.putExtra("profile_id",String.valueOf(user.getId()));
                    try {
                        intent.putExtra("total_beans",String.valueOf(MyFollowersData.get(position).getUsertopfans().getTotal_beans()));
                    }catch (Exception e){
                        intent.putExtra("total_beans","0");
                    }

                    mContext.startActivity(intent);
                }
            });
        }


    }
    public static String formatDate(String dateString) {
        try {
            // Define the input date format
            DateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);

            // Parse the input date string
            Date date = inputFormat.parse(dateString);

            // Define the output date format
            DateFormat outputFormat = new SimpleDateFormat("dd-MMM", Locale.ENGLISH);

            // Format the date to "dd-MMM" (e.g., "16-Feb")
            assert date != null;
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return dateString;
        }
    }

    @Override
    public int getItemCount() {
        if(Screen!=null && Screen.equals("MY_FOLLOWERS")){
            return MyFollowersData.size();
        }else {
            return MyTopFans.size();
        }

    }

    public static class myViewHolder extends RecyclerView.ViewHolder{
        CircleImageView im_profile_pic,img_profile_status;
        TextView tv_profile_name,tv_profile_lvl,tv_profile_coins,tv_profile_date,tv_profile_status;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            im_profile_pic = itemView.findViewById(R.id.img_profile_pic);
            img_profile_status = itemView.findViewById(R.id.img_profile_status);
            tv_profile_name = itemView.findViewById(R.id.tv_profile_name);
            tv_profile_lvl = itemView.findViewById(R.id.tv_profile_lvl);
            tv_profile_coins = itemView.findViewById(R.id.tv_profile_coins);
            tv_profile_date = itemView.findViewById(R.id.tv_profile_date);
            tv_profile_status = itemView.findViewById(R.id.tv_profile_status);

        }
    }
}
