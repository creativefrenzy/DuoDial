package com.privatepe.host.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.privatepe.host.R;
import com.privatepe.host.activity.ViewProfile;
import com.privatepe.host.activity.ViewProfileMet;
import com.privatepe.host.response.ProfileVideoResponse;


import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class VideoStatusAdapter extends RecyclerView.Adapter<VideoStatusAdapter.ViewHolder> {


    Context context;
    ArrayList<ProfileVideoResponse> videolinklist = new ArrayList<>();
    String dp;
    ViewProfile viewProfile;
    ViewProfileMet viewProfileMet;

    public VideoStatusAdapter(Context context, ArrayList<ProfileVideoResponse> videolist, String dp, ViewProfile viewProfile) {

        this.context = context;
        videolinklist = videolist;
        this.dp = dp;
        this.viewProfile = viewProfile;
    }

    public VideoStatusAdapter(Context context, ArrayList<ProfileVideoResponse> videolist, String dp, ViewProfileMet viewProfile) {

        this.context = context;
        videolinklist = videolist;
        this.dp = dp;
        this.viewProfileMet = viewProfile;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_status_recycler, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //  Log.e("VideoStatusAdapter", "onBindViewHolder: "+dp );
        //  Log.e("VideoStatusAdapter", "onBindViewHolder: video "+videolinklist.get(position).getVideoName() );
        //  Log.e("VideoStatusAdapter", "onBindViewHolder: thumbnail "+videolinklist.get(position).getVideoThumbnail());

        Glide.with(context).load(videolinklist.get(position).getVideoThumbnail()).apply(new RequestOptions().placeholder(R.drawable.female_placeholder).error(R.drawable.female_placeholder)).into(holder.thumbnail);

     /*   if (!dp.equals("")) {
            Glide.with(context).load(dp).apply(new RequestOptions().placeholder(R.drawable.female_placeholder).error(R.drawable.female_placeholder)).into(holder.thumbnail);
        } else {
            Glide.with(context).load(R.drawable.female_placeholder).apply(new RequestOptions()).into(holder.thumbnail);
        }
*/


    }

    @Override
    public int getItemCount() {
        return videolinklist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView thumbnail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            thumbnail = itemView.findViewById(R.id.video_thumbnail);


            thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        viewProfile.goToVideoStatus(getAdapterPosition());
                    } catch (Exception e) {
                        viewProfileMet.goToVideoStatus(getAdapterPosition());
                    }


              /*      Intent vsIntent=new Intent(context, ActivityStatus.class);
                    vsIntent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                    vsIntent.putExtra("videonum",""+getAdapterPosition());
                    Log.e("VideoStatusAdapter1", "onClick: "+getAdapterPosition() );
                    context.startActivity(vsIntent);*/

                }
            });


        }
    }
}
