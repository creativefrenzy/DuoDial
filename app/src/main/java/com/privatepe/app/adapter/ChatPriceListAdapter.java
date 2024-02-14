package com.privatepe.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.privatepe.app.R;
import com.privatepe.app.response.chat_price.PriceDataModel;

import java.util.ArrayList;

public class ChatPriceListAdapter extends RecyclerView.Adapter<ChatPriceListAdapter.ViewHolder> {

    private static final int VIEW_TYPE_FIRST = 0;
    private static final int VIEW_TYPE_INTERMEDIATE = 1;
    private static final int VIEW_TYPE_LAST = 2;

    private ArrayList<PriceDataModel> callpricelist;
    private Context context;

    public ChatPriceListAdapter(ArrayList<PriceDataModel> callpricelist, Context context) {
        this.callpricelist = callpricelist;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutRes;
        switch (viewType) {
            case VIEW_TYPE_FIRST:
                layoutRes = R.layout.chat_price_list_item_first;
                break;
            case VIEW_TYPE_LAST:
                layoutRes = R.layout.chat_price_list_item_last;
                break;
            default:
                layoutRes = R.layout.chat_price_list_item;
                break;
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
        return new ViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.level.setText(callpricelist.get(position).getLevel());
        holder.chatPrice.setText(String.valueOf(callpricelist.get(position).getAmount()));
        holder.beanweekly.setText(String.valueOf(callpricelist.get(position).getLevel_beans()));

    }

    @Override
    public int getItemCount() {
        return callpricelist.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_FIRST;
        } else if (position == callpricelist.size() - 1) {
            return VIEW_TYPE_LAST;
        } else {
            return VIEW_TYPE_INTERMEDIATE;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView level, chatPrice,beanweekly;

        public ViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);
            level = itemView.findViewById(R.id.level);
            chatPrice = itemView.findViewById(R.id.chat_price);
            beanweekly=itemView.findViewById(R.id.weeklybeanTV);

            // You can customize views based on viewType if needed
            switch (viewType) {
                case VIEW_TYPE_FIRST:
                    // Customize for the first item
                    break;
                case VIEW_TYPE_LAST:
                    // Customize for the last item
                    break;
                default:
                    // Customize for intermediate items
                    break;
            }
        }
    }
}
