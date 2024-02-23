package com.privatepe.app.recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.privatepe.app.Interface.ViewProfIleImagePosition;
import com.privatepe.app.R;
import com.privatepe.app.activity.ViewProfile;
import com.privatepe.app.model.UserListResponse;
import com.privatepe.app.model.UserListResponseNew.FemaleImage;

import java.util.List;
import java.util.Objects;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ProfileAdapterHolder> {
    Context context;
    List<UserListResponse.UserPics> arrayList;
    List<FemaleImage> arrayList2;
    ViewProfIleImagePosition ViewPPosition;
    String Screen;
    public ProfileAdapter(Context context, List<UserListResponse.UserPics> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }
    public ProfileAdapter(Context context, List<FemaleImage> arrayList, String Screen) {
        this.arrayList2 = arrayList;
        this.context = context;
        this.Screen = Screen;
    }

    public ProfileAdapter(ViewProfile context, List<FemaleImage> arrayList, String Screen, ViewProfIleImagePosition ViewProfIleImagePosition) {
        this.arrayList2 = arrayList;
        this.context = context;
        this.Screen = Screen;
        this.ViewPPosition = ViewProfIleImagePosition;
    }

    @NonNull
    @Override
    public ProfileAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView;
        if(Objects.equals(Screen, "ViewProfile")){
            layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_image_item_viewprofile, parent, false);
        }else if(Objects.equals(Screen, "ExtendedProfileImages")){
            layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_image_item_viewprofile_extended, parent, false);
        }else{
            layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_image_item, parent, false);
        }
        return new ProfileAdapterHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProfileAdapterHolder holder, final int position) {
        if(Objects.equals(Screen, "ViewProfile")){
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
        }else {
            Glide.with(context)
                    .load(arrayList.get(position).getImage_name())
                    .into(holder.iv);
        }
    }

    @Override
    public int getItemCount() {
        if(Objects.equals(Screen, "ViewProfile")){
            return arrayList2.size();
        }else if(Objects.equals(Screen, "ExtendedProfileImages")){
            return arrayList2.size();
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
