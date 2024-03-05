package com.privatepe.host.recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.privatepe.host.R;
import com.privatepe.host.model.UserListResponse;

import java.util.List;

public class EditProfileAdapter extends RecyclerView.Adapter<EditProfileAdapter.EditProfileAdapterHolder> {
    Context context;
    List<UserListResponse.UserPics> arrayList;
    public EditProfileAdapter(Context context, List<UserListResponse.UserPics> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public EditProfileAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_image_item, parent, false);
        return new EditProfileAdapterHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull final EditProfileAdapterHolder holder, final int position) {
        Glide.with(context)
                .load(arrayList.get(position).getImage_name())
                .into(holder.iv);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class EditProfileAdapterHolder extends RecyclerView.ViewHolder {
        ImageView iv;
        public EditProfileAdapterHolder(View view) {
            super(view);
            iv = view.findViewById(R.id.userImage);

        }
    }
}
