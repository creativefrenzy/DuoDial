package com.privatepe.app.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.privatepe.app.R;
import com.privatepe.app.response.UserListResponse;
import com.privatepe.app.utils.PaginationAdapterCallback;

import java.util.ArrayList;
import java.util.List;


public class HostListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<UserListResponse.Data> list;

    private PaginationAdapterCallback mCallback;
    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;
    private String errorMsg;

    public HostListAdapter(Context context, PaginationAdapterCallback mCallback) {
        this.context = context;
        this.mCallback = mCallback;
        this.list = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case ITEM:
                View v1 = null;
                v1 = inflater.inflate(R.layout.host_list, parent, false);
                viewHolder = new myViewHolder(v1);
                break;

            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder hld, int position) {

        switch (getItemViewType(position)) {
            case ITEM:
                try {
                    final myViewHolder holder = (myViewHolder) hld;
                    int id = list.get(position).getProfileId();
                    holder.host_name.setText(list.get(position).getName());
                    holder.host_id.setText(String.valueOf("ID: " + id));
                    String getIsAdminBlock = String.valueOf(list.get(position).getIsAdminBlock());
                    Log.e("HostListAdapter", "getIsAdminBlock " + getIsAdminBlock);
                    if (getIsAdminBlock.equals("0")) {
                        holder.success.setText("Success");
                        holder.success.setTextColor((Color.GRAY));
                        Glide.with(context)
                                .load(R.drawable.ic_check)
                                .apply(new RequestOptions().placeholder(R.drawable.ic_check))
                                .into(holder.img_check);
                    } else {
                        holder.success.setText("Blocked");
                        holder.success.setTextColor(context.getResources().getColor(R.color.colorRedNew));
                        Glide.with(context)
                                .load(R.drawable.ic_block)
                                .apply(new RequestOptions().placeholder(R.drawable.ic_block))
                                .into(holder.img_check);
                    }

                    break;

                } catch (Exception e) {
                }

            case LOADING:
                if (retryPageLoad) {
                } else {

                }
                break;
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ImageView user_image;
        user_image = holder.itemView.findViewById(R.id.user_image);
        Log.e("listnew", String.valueOf(list.size()));
        String ImageUrl = "";
        UserListResponse.Data data = list.get(holder.getPosition());
        if (data != null) {
            List<UserListResponse.ProfileImage> profileImagelist = data.getProfileImages();
            if (profileImagelist != null && profileImagelist.size() > 0) {
                UserListResponse.ProfileImage profileImg = profileImagelist.get(0);
                if (profileImg != null) {
                    ImageUrl = profileImg.getImageName();
                }
            }
        }
        if (TextUtils.isEmpty(ImageUrl)) {
            Log.e("iURLA", ImageUrl);
            if (user_image != null) {
                Glide.with(context).load(R.drawable.default_profile).apply(new RequestOptions()).into(user_image);
            }
        } else {
            Glide.with(context).load(ImageUrl).apply(new RequestOptions().placeholder(R.drawable.default_profile).circleCrop()).into(user_image);
        }
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        ImageView user_image;
        user_image = holder.itemView.findViewById(R.id.user_image);
        if (user_image != null ) {
            Glide.with(context).load(R.drawable.default_profile).apply(new RequestOptions().placeholder(R.drawable.default_profile).circleCrop()).into(user_image);
        }

    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == list.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }


    public class myViewHolder extends RecyclerView.ViewHolder {

        ImageView user_image, img_check;
        TextView host_name, host_id, success;
        LinearLayout main_container;

        public myViewHolder(View itemView) {
            super(itemView);
            user_image = itemView.findViewById(R.id.user_image);
            img_check = itemView.findViewById(R.id.img_check);
            host_name = itemView.findViewById(R.id.host_name);
            host_id = itemView.findViewById(R.id.host_id);
            success = itemView.findViewById(R.id.success);
            main_container = itemView.findViewById(R.id.main_container);
        }
    }

    protected class LoadingVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ProgressBar mProgressBar;
        private ImageButton mRetryBtn;
        private TextView mErrorTxt;
        private LinearLayout mErrorLayout;

        public LoadingVH(View itemView) {
            super(itemView);

            mProgressBar = itemView.findViewById(R.id.loadmore_progress);
            mRetryBtn = itemView.findViewById(R.id.loadmore_retry);
            mErrorTxt = itemView.findViewById(R.id.loadmore_errortxt);
            mErrorLayout = itemView.findViewById(R.id.loadmore_errorlayout);

            mRetryBtn.setOnClickListener(this);
            mErrorLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.loadmore_retry:
                    // Show loader here
                    if(isLoadingAdded){
                        mProgressBar.setVisibility(View.VISIBLE);
                    }
                case R.id.loadmore_errorlayout:
                    mProgressBar.setVisibility(View.INVISIBLE);
                    showRetry(false, null);
                    mCallback.retryPageLoad();
                    break;
            }
        }

    }

           /*
   Helpers
   _________________________________________________________________________________________________
    */

    public void add(UserListResponse.Data results) {
        list.add(results);
        notifyItemInserted(list.size() - 1);

    }

    public void addAll(List<UserListResponse.Data> moveResults) {
        for (UserListResponse.Data result : moveResults) {
            add(result);
        }
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new UserListResponse.Data());
    }


    public void removeLoadingFooter() {
        isLoadingAdded = false;
        int position = list.size() - 1;
        UserListResponse.Data result = getItem(position);

        if (result != null) {
            list.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void showRetry(boolean show, @Nullable String errorMsg) {
        retryPageLoad = show;
        notifyItemChanged(list.size() - 1);
        if (errorMsg != null) this.errorMsg = errorMsg;
    }

    public UserListResponse.Data getItem(int position) {
        return list.get(position);
    }






}