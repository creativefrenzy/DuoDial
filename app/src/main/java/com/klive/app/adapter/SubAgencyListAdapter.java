package com.klive.app.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.klive.app.R;

import com.klive.app.response.sub_agency.SubAgencyData;
import com.klive.app.response.sub_agency.SubAgencyResult;
import com.klive.app.utils.PaginationAdapterCallback;

import java.util.ArrayList;
import java.util.List;

public class SubAgencyListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<SubAgencyData> list;

    private PaginationAdapterCallback mCallback;
    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;
    private String errorMsg;


    public SubAgencyListAdapter(Context context, PaginationAdapterCallback mCallback) {
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

                    //Glide.with(context).load(list.get(position).getProfileImages().get(0).getImageName()).placeholder(R.drawable.default_profile).circleCrop().into(((myViewHolder) hld).user_image);
                    int id = list.get(position).getProfileId();
                    holder.host_name.setText(list.get(position).getName());
                    holder.host_id.setText(String.valueOf("ID: " + id));
                    Log.e("SubListAdapter", "getIsAdminBlockName " + list.get(position).getName());
                    Log.e("SubListAdapter", "getIsAdminBlockId " + list.get(position).getProfileId());

                    String getIsAdminBlock = String.valueOf(list.get(position).getIsAdminBlock());
                    Log.e("SubListAdapter", "getIsAdminBlock " + getIsAdminBlock);
                    if (getIsAdminBlock.equals("0")) {
                        holder.success.setText("Success");
                        holder.success.setTextColor((Color.GRAY));
                    } else {
                        holder.success.setText("Block");
                        holder.success.setHintTextColor(Color.RED);
                    }
                    //Glide.with(context).load(list.get(position).getProfileImages().get(0).getImageName()).placeholder(R.drawable.default_profile).circleCrop().into(((myViewHolder) hld).user_image);
                   /* if (!list.get(position).get().get(0).getImageName().equals("")) {
                        if (list.get(position).getProfileImages().size() > 0) {
                            Glide.with(context).load(list.get(position).getProfileImages().get(0).getImageName())
                                    .apply(new RequestOptions().placeholder(R.drawable.default_profile).error
                                            (R.drawable.default_profile).circleCrop()).into(holder.user_image);
                        } else {
                            Glide.with(context).load(R.drawable.default_profile).apply(new RequestOptions()).into(holder.user_image);
                        }

                    }*/
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
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == list.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }


    public class myViewHolder extends RecyclerView.ViewHolder {

        ImageView user_image;
        TextView host_name, host_id, success;
        LinearLayout main_container;

        public myViewHolder(View itemView) {
            super(itemView);

            user_image = itemView.findViewById(R.id.user_image);
            host_name = itemView.findViewById(R.id.host_name);
            host_id = itemView.findViewById(R.id.host_id);
            success = itemView.findViewById(R.id.success);
            main_container = itemView.findViewById(R.id.main_container);
        }
    }

    protected class LoadingVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        //  private ProgressBar mProgressBar;
        private ImageButton mRetryBtn;
        private TextView mErrorTxt;
        private LinearLayout mErrorLayout;

        public LoadingVH(View itemView) {
            super(itemView);

            //mProgressBar = itemView.findViewById(R.id.loadmore_progress);
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
                case R.id.loadmore_errorlayout:

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

    public void add(SubAgencyData results) {
        //list.clear();
        list.add(results);
        notifyItemInserted(list.size() - 1);
    }

    public void addAll(List<SubAgencyData> moveResults) {
        for (SubAgencyData result : moveResults) {
            add(result);
        }
    }

    public void updateItem(int position, SubAgencyData data) {
        list.set(position, data);
        notifyItemChanged(position);
    }

    public void remove(SubAgencyResult r) {
        int position = list.indexOf(r);
        if (position > -1) {
            list.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void removeAll() {
        if (list != null && list.size() > 0) {
            list.clear();
        }
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new SubAgencyData());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = list.size() - 1;
        SubAgencyData result = getItem(position);

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

    public SubAgencyData getItem(int position) {
        return list.get(position);
    }
}