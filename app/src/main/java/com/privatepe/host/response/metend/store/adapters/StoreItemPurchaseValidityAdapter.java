package com.privatepe.host.response.metend.store.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.privatepe.host.R;
import com.privatepe.host.response.metend.store.interfaces.OnValiditySelectListener;
import com.privatepe.host.response.metend.store_list.StorePlanModel;

import java.util.ArrayList;

public class StoreItemPurchaseValidityAdapter extends RecyclerView.Adapter<StoreItemPurchaseValidityAdapter.ViewHolder> {
    private Context context;
    private ArrayList<StorePlanModel> validityList;
    private int SELECTED_POS = 0;
    private String TAG="StoreItemPurchageValidityAdapter";
    private OnValiditySelectListener onValiditySelectListener;

    public StoreItemPurchaseValidityAdapter(Context context, ArrayList<StorePlanModel> validityList, OnValiditySelectListener listener) {
        this.context = context;
        this.validityList = validityList;
        this.onValiditySelectListener=listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_validity_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (SELECTED_POS == position) {
            holder.imageSelector.setVisibility(View.VISIBLE);
        } else {
            holder.imageSelector.setVisibility(View.GONE);
        }
        Log.e(TAG, "onBindViewHolder: "+validityList);
        holder.validityText.setText(validityList.get(position).getValidity_in_days()+" days");

    }

    @Override
    public int getItemCount() {
        return validityList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageSelector;
        TextView validityText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageSelector = itemView.findViewById(R.id.selector_image);
            validityText = itemView.findViewById(R.id.validity_text);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SELECTED_POS = getAdapterPosition();
                    notifyDataSetChanged();
                    onValiditySelectListener.onValiditySelect(validityList.get(SELECTED_POS));
                }
            });


        }
    }
}
