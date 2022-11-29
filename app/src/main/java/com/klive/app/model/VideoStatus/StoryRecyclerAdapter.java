package com.klive.app.model.VideoStatus;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.klive.app.R;
import com.klive.app.main.Home;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class StoryRecyclerAdapter extends RecyclerView.Adapter<StoryRecyclerAdapter.ViewHolder> {

    Context context;
    List<ResourceVideoModel> resourcelist;

    LayoutInflater layoutInflater;

    FragmentActivity thisFragmentActivity;

    SharedPreferences sharedPreferences;


    public StoryRecyclerAdapter(Context context, List<ResourceVideoModel> resourcelist, FragmentActivity fragmentActivity) {
        this.context = context;
        this.resourcelist = resourcelist;
        this.thisFragmentActivity = fragmentActivity;
        // this.layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        sharedPreferences = context.getSharedPreferences("VideoApp", parent.getContext().MODE_PRIVATE);
        this.layoutInflater = LayoutInflater.from(parent.getContext());
        View storiesView = layoutInflater.inflate(R.layout.story_recycler_item, parent, false);
        return new ViewHolder(storiesView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.circularStatusView.setPortionsCount(resourcelist.get(position).getVideoLinkLists().size());
        holder.UserName.setText(resourcelist.get(position).getProfileName());

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM hh:mm a");
        String datetime = sdf.format(resourcelist.get(position).getTimeNDate());

        holder.DateNTime.setText(datetime);

        // holder.circleImageView.setImageBitmap(resourcelist.get(position).getThumbPath());

        if (getAllViewed(String.valueOf(resourcelist.get(position).getId()))) {
            //Log.i("isviewed",);
            holder.circularStatusView.setPortionsColor(context.getResources().getColor(R.color.viewedColor));
        } else {

            if (resourcelist.get(position).getVideoLinkLists().size() == getVideoNumFromSharedPref(String.valueOf(resourcelist.get(position).getId()))) {

                holder.circularStatusView.setPortionsColor(context.getResources().getColor(R.color.viewedColor));

            } else {

                for (int i = -1; i <= getVideoNumFromSharedPref(String.valueOf(resourcelist.get(position).getId())); i++) {
                    holder.circularStatusView.setPortionColorForIndex(i, context.getResources().getColor(R.color.viewedColor));
                }
            }

        }


    }

    private boolean getAllViewed(String id) {
        boolean isAllViewed = false;

        String ss = id + "bool";

        if (sharedPreferences.contains(ss)) {
            isAllViewed = sharedPreferences.getBoolean(ss, false);

        }

        return isAllViewed;
    }


    @Override
    public int getItemCount() {
        return resourcelist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircularStatusView circularStatusView;


        CircleImageView circleImageView;
        TextView UserName;
        TextView DateNTime;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            circleImageView = itemView.findViewById(R.id.circleImageView);
            circularStatusView = itemView.findViewById(R.id.circular_progress);

            UserName = itemView.findViewById(R.id.ProfileName);
            DateNTime = itemView.findViewById(R.id.datentime);





        }
    }


    private int getVideoNumFromSharedPref(String UserId) {
        int video_num = -1;
        if (sharedPreferences.contains(UserId)) {
            video_num = sharedPreferences.getInt(UserId, 0);
        }


        return video_num;
    }
}
