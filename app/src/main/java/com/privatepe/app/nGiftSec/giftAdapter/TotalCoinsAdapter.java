package com.privatepe.app.nGiftSec.giftAdapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.zeeplive.app.R;
import com.zeeplive.app.response.N_Gift.CoinSelectionModel;

import java.util.ArrayList;

public class TotalCoinsAdapter extends RecyclerView.Adapter<TotalCoinsAdapter.ViewHolder> {

    private ArrayList<CoinSelectionModel> alTotalCoins;
    private CoinSelectListner listner;

    public TotalCoinsAdapter(ArrayList<CoinSelectionModel> alTotalCoins, CoinSelectListner listner) {
        this.alTotalCoins = alTotalCoins;
        this.listner = listner;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_total_coin, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {
        viewHolder.tv_TotalCoin.setText(alTotalCoins.get(position).getName());
        if(alTotalCoins.get(position).isSelected()) {
            viewHolder.tv_TotalCoin.setTextColor(Color.parseColor("#f02df2"));
            viewHolder.tv_TotalCoin.setBackground(ContextCompat.getDrawable(viewHolder.tv_TotalCoin.getContext(), R.drawable.text_bg));
        } else {
            viewHolder.tv_TotalCoin.setTextColor(Color.parseColor("#FFFFFF"));
            viewHolder.tv_TotalCoin.setBackground(null);
        }

        viewHolder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listner.onSelectedPosition(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return alTotalCoins.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_TotalCoin;
        private LinearLayout mainLayout;
        public ViewHolder(View view) {
            super(view);
            tv_TotalCoin = view.findViewById(R.id.tv_TotalCoin);
            mainLayout = view.findViewById(R.id.mainLayout);
        }
    }
    public interface CoinSelectListner {
        void onSelectedPosition(int position);
    }
}
