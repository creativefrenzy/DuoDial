package com.klive.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.klive.app.R;
import com.klive.app.model.UserListResponse;
import com.klive.app.status_videos.ActivityStatus;

import java.util.ArrayList;
import java.util.List;

public class ProfileVideoAdapter extends RecyclerView.Adapter<ProfileVideoAdapter.ProfileAdapterHolder> {

    Context context;
    ArrayList<UserListResponse.ProfileVideo> arrayList;

    public ProfileVideoAdapter(Context context, ArrayList<UserListResponse.ProfileVideo> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProfileAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_video_item, parent, false);
        return new ProfileAdapterHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProfileAdapterHolder holder, final int position) {
        Glide.with(context)
                .load(arrayList.get(position).getVideoThumbnail())
                .into(holder.videoThumbnailImage);

        holder.videoThumbnailImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ActivityStatus.class);
                intent.putExtra("inWhichActivity","ProfileVideoAdapter");
                intent.putExtra("id",String.valueOf(arrayList.get(position).getUserId()));
                ArrayList<String> list2 = getVideolinksList();
                Log.e("onSingleTap11", "onSingleTap: " + "list size  " + list2);
                intent.putStringArrayListExtra("resoureList", list2);
                intent.putExtra("allListData", new Gson().toJson(arrayList));
                intent.putExtra("clickedUrl",arrayList.get(position).getVideoUrl());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ProfileAdapterHolder extends RecyclerView.ViewHolder {
        ImageView videoThumbnailImage;
        public ProfileAdapterHolder(View view) {
            super(view);
            videoThumbnailImage = view.findViewById(R.id.videoThumbnailImage);

        }
    }

    private ArrayList<String> getVideolinksList() {

        ArrayList<String> videolist = new ArrayList<>();
        videolist.clear();

        for (int i = 0; i < arrayList.size(); i++) {
            videolist.add(arrayList.get(i).getVideoUrl());
        }
        return videolist;
    }
}
