package com.privatepe.app.response.metend.store.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.privatepe.app.R;
import com.privatepe.app.response.metend.store.interfaces.OnStoreItemClickListener;
import com.privatepe.app.response.metend.store_list.StoreResultModel;

public class StoreItemListAdapter extends RecyclerView.Adapter<StoreItemListAdapter.ViewHolder> {

    Context context;
    private StoreResultModel storeTabResult;
    private OnStoreItemClickListener onStoreItemClickListener;
    private String type;
    private String TAG = "StoreItemListAdapter";

    public StoreItemListAdapter(Context context, StoreResultModel list, OnStoreItemClickListener listener, String type) {
        this.context = context;
        storeTabResult = list;
        onStoreItemClickListener = listener;
        this.type = type;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_catagory_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.e(TAG, "onBindViewHolder: type  " + type);
        Glide.with(context).load(storeTabResult.getStores().get(position).getImage()).into(holder.storeItemImage);
        holder.storeItemName.setText(storeTabResult.getStores().get(position).getStore_name());
        Log.e("BackgroundCrash_Test", "StoreItemListAdapter onBindViewHolder: " );
        if (type.equals("StoreTab")) {
            if (storeTabResult.getStores().get(position).getStoreplan().size() > 0) {
                holder.storeItemCostInCoin.setText(String.valueOf(storeTabResult.getStores().get(position).getStoreplan().get(0).getCoin()));
            }
            Log.e(TAG, "onBindViewHolder: StoreTab ");
        } else if (type.equals("MineTab")) {
            holder.storeItemCostInCoin.setText(String.valueOf(storeTabResult.getStores().get(position).getCurrent_plan_coin()));
            Log.e(TAG, "onBindViewHolder: MineTab item expired " + getItemExpired(storeTabResult.getStores().get(position).getCurrent_time(), storeTabResult.getStores().get(position).getEnd_time()));
            if (getItemExpired(storeTabResult.getStores().get(position).getCurrent_time(), storeTabResult.getStores().get(position).getEnd_time())) {
                holder.ItemStatus.setVisibility(View.VISIBLE);
                holder.ItemStatus.setBackground(context.getResources().getDrawable(R.drawable.item_status_bg_expired));
                holder.ItemStatus.setText("Expired");
            } else {
                if (storeTabResult.getStores().get(position).getIs_using()==1)
                {
                    holder.ItemStatus.setVisibility(View.VISIBLE);
                    holder.ItemStatus.setBackground(context.getResources().getDrawable(R.drawable.item_status_bg_using));
                    holder.ItemStatus.setText("Using");
                }
                else {
                    holder.ItemStatus.setVisibility(View.GONE);
                }

            }
        }

    }

    @Override
    public int getItemCount() {
        return storeTabResult.getStores().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView storeItemImage;
        private TextView storeItemName, storeItemCostInCoin, ItemStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            storeItemImage = itemView.findViewById(R.id.item_image);
            storeItemName = itemView.findViewById(R.id.item_name);
            ItemStatus = itemView.findViewById(R.id.item_status);
            storeItemCostInCoin = itemView.findViewById(R.id.coins);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onStoreItemClickListener.onStoreItemClick(storeTabResult.getStores().get(getAdapterPosition()),type);
                }
            });

        }
    }


    public boolean getItemExpired(long currentTime, long endTime) {
        if (currentTime > endTime) {
            return true;
        }
        return false;
    }


}
