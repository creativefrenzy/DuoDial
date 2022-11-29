package com.klive.app.recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.klive.app.R;
import com.klive.app.model.Profile;
import com.klive.app.model.UserListResponse;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ProfileAdapterHolder> {
    Context context;
    List<UserListResponse.UserPics> arrayList;
    public ProfileAdapter(Context context, List<UserListResponse.UserPics> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProfileAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_image_item, parent, false);
        return new ProfileAdapterHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProfileAdapterHolder holder, final int position) {
        Glide.with(context)
                .load(arrayList.get(position).getImage_name())
                .into(holder.iv);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ProfileAdapterHolder extends RecyclerView.ViewHolder {
        ImageView iv;
        public ProfileAdapterHolder(View view) {
            super(view);
            iv = view.findViewById(R.id.userImage);

        }
    }
}
