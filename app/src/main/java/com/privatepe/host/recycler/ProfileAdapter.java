package com.privatepe.host.recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.privatepe.host.Interface.ViewProfIleImagePosition;
import com.privatepe.host.R;
import com.privatepe.host.model.HostUserPicNew;
import com.privatepe.host.model.UserListResponse;

import java.util.List;
import java.util.Objects;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ProfileAdapterHolder> {
    Context context;
    List<UserListResponse.UserPics> arrayList;
    List<com.privatepe.host.response.metend.UserListResponseNew.FemaleImage> arrayList2;
    List<HostUserPicNew> arrayList3;
    ViewProfIleImagePosition ViewPPosition;
    String Screen;
    public ProfileAdapter(Context context, List<UserListResponse.UserPics> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }
    public ProfileAdapter(Context context, List<com.privatepe.host.response.metend.UserListResponseNew.FemaleImage> arrayList, String Screen) {
        this.arrayList2 = arrayList;
        this.context = context;
        this.Screen = Screen;
    }
    public ProfileAdapter(Context context, List<HostUserPicNew> arrayList, String Screen, Boolean value) {
        this.arrayList3 = arrayList;
        this.context = context;
        this.Screen = Screen;
    }
    public ProfileAdapter(Context context, List<com.privatepe.host.response.metend.UserListResponseNew.FemaleImage> arrayList, String Screen, ViewProfIleImagePosition ViewProfIleImagePosition) {
        this.arrayList2 = arrayList;
        this.context = context;
        this.Screen = Screen;
        this.ViewPPosition = ViewProfIleImagePosition;
    }


    @NonNull
    @Override
    public ProfileAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView;
        if(Objects.equals(Screen, "ViewProfileMet")){
            layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_image_item_viewprofile, parent, false);
        }else if(Objects.equals(Screen, "ExtendedProfileImages") || Objects.equals(Screen, "EditProfileActivity")){
            layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_image_item_viewprofile_extended, parent, false);
        }else{
            layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_image_item, parent, false);
        }
        return new ProfileAdapterHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProfileAdapterHolder holder, final int position) {
        if(Objects.equals(Screen, "ViewProfileMet")){
            Glide.with(context)
                    .load(arrayList2.get(position).getImageName())
                    .into(holder.iv);

            holder.iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewPPosition.setImagePositionView(holder.getAbsoluteAdapterPosition());
                }
            });

        }else if(Objects.equals(Screen, "ExtendedProfileImages")){
            Glide.with(context)
                    .load(arrayList2.get(position).getImageName())
                    .into(holder.iv);
        }else if(Objects.equals(Screen, "EditProfileActivity")){
            Glide.with(context)
                    .load(arrayList3.get(position).getImage_name())
                    .into(holder.iv);
        }
        else {
            Glide.with(context)
                    .load(arrayList.get(position).getImage_name())
                    .into(holder.iv);
        }
    }

    @Override
    public int getItemCount() {
        if(Objects.equals(Screen, "ViewProfileMet")){
            return arrayList2.size();
        }else if(Objects.equals(Screen, "ExtendedProfileImages")){
            return arrayList2.size();
        }else if(Objects.equals(Screen, "EditProfileActivity")){
            return arrayList3.size();
        }else{
            return arrayList.size();
        }
    }

    public class ProfileAdapterHolder extends RecyclerView.ViewHolder {
        ImageView iv;
        public ProfileAdapterHolder(View view) {
            super(view);
            iv = view.findViewById(R.id.userImage);

        }
    }
}
