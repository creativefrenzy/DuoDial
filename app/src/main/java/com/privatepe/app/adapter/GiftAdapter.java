package com.privatepe.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.privatepe.app.R;
import com.privatepe.app.model.gift.Gift;

import java.util.List;


public class GiftAdapter extends RecyclerView.Adapter<GiftAdapter.ContactViewHolder> {
    private List<Gift> giftArrayList;
    private int rowLayout;
    private Context context;

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout contactLayout;
        TextView tv_coin;
        ImageView img_gift;

        public ContactViewHolder(View v) {
            super(v);
            contactLayout = v.findViewById(R.id.contact_layout);
            img_gift = v.findViewById(R.id.img_gift);
            tv_coin = v.findViewById(R.id.tv_coin);
        }
    }

    public GiftAdapter(List<Gift> giftArrayList, int rowLayout, Context context) {
        this.giftArrayList = giftArrayList;
        this.rowLayout = rowLayout;
        this.context = context;
    }


    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {

      /*  holder.tv_coin.setTypeface(Typeface.createFromAsset(context.getAssets(),
      "fonts/POPPINS-BOLD_0.TTF"));*/

        holder.tv_coin.setText(String.valueOf(giftArrayList.get(position).getAmount()));
        holder.img_gift.setImageResource(getGiftImage(String.valueOf(giftArrayList.get(position).getId())));

       /* Glide.with(context)
                .load(giftArrayList.get(position).getGiftPhoto())
                .into(holder.img_gift);*/

    }

    int getGiftImage(String id) {
        int imgResource = 0;
        if (id.equals("2")) {
            imgResource = R.drawable.lucky;
        } else if (id.equals("3")) {
            imgResource = R.drawable.bell;
        } else if (id.equals("19")) {
            imgResource = R.drawable.candy;
        } else if (id.equals("4")) {
            imgResource = R.drawable.leaves;
        } else if (id.equals("5")) {
            imgResource = R.drawable.kiss;
        } else if (id.equals("6")) {
            imgResource = R.drawable.candy_1;
        } else if (id.equals("7")) {
            imgResource = R.drawable.rose;
        } else if (id.equals("8")) {
            imgResource = R.drawable.heart;
        } else if (id.equals("9")) {
            imgResource = R.drawable.lipstik;
        } else if (id.equals("10")) {
            imgResource = R.drawable.perfume;
        } else if (id.equals("11")) {
            imgResource = R.drawable.necklace;
        } else if (id.equals("12")) {
            imgResource = R.drawable.panda;
        } else if (id.equals("13")) {
            imgResource = R.drawable.hammer;
        } else if (id.equals("14")) {
            imgResource = R.drawable.rocket;
        } else if (id.equals("15")) {
            imgResource = R.drawable.ship;
        } else if (id.equals("16")) {
            imgResource = R.drawable.ring;
        } else if (id.equals("17")) {
            imgResource = R.drawable.disney;
        } else if (id.equals("18")) {
            imgResource = R.drawable.hot_ballon;
        }
        return imgResource;
    }

    @Override
    public int getItemCount() {
        return giftArrayList.size();
    }
}