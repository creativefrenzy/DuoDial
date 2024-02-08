package com.privatepe.app.nGiftSec.giftAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.zeeplive.app.R;
import com.zeeplive.app.response.N_Gift.Gift;
import com.zeeplive.app.utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class GiftAdapter extends RecyclerView.Adapter<GiftAdapter.ViewHolder> {

    private List<Gift> alGifts = new ArrayList<>();
    private GiftSelectListner listner;
    private String giftcount = "1";


    public GiftAdapter(List<Gift> alGifts, GiftSelectListner listner) {
        this.alGifts = alGifts;
        this.listner = listner;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.giftdata_adapter, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        //viewHolder.tvSelectedCoins.setText(alGifts.get(position).getTotalCounts());
        //Log.e("aniLog", "aniType => " + alGifts.get(position).getIsAnimated() + " ID => " + alGifts.get(position).getId());

        /*Log.e("giftLogadap", alGifts.get(position).getGiftName());

        if (alGifts.get(position).getGiftName().equals("Bramastra")) {
            Log.e("giftLogadap", "m here");
        }*/

     //   if (alGifts.get(position).getIsAnimated() == 0) {
            Utils.imageLoaderView(viewHolder.giftIconImg.getContext(), viewHolder.giftIconImg, alGifts.get(position).getImage());
            //  Log.e("imageUrlLog", alGifts.get(position).getImage() + " ID => " + alGifts.get(position).getId());

       /* } else if (alGifts.get(position).getIsAnimated() == 1) {
            Utils.imageLoaderSvg(viewHolder.giftIconImg.getContext(), viewHolder.giftIconImg, alGifts.get(position).getImage());
            // Log.e("imageUrlSVGLog", alGifts.get(position).getImage() + " ID => " + alGifts.get(position).getId());
        }*/
        viewHolder.coins.setText(alGifts.get(position).getAmount() + "");
        viewHolder.giftName.setText(alGifts.get(position).getGiftName());
        //viewHolder.giftName.setText("Gift name");
        viewHolder.tvSelectedCoins.setVisibility(View.INVISIBLE);

        if (alGifts.get(position).isSelected()) {
            if (!giftcount.equals("1")) {
                viewHolder.tvSelectedCoins.setVisibility(View.VISIBLE);
                viewHolder.tvSelectedCoins.setText("x" + giftcount);
                viewHolder.coins.setText(alGifts.get(position).getAmount() + "x" + giftcount);
            }

            viewHolder.rootLayout.setBackgroundResource(R.drawable.bg_gift_selected);
        } else {
            viewHolder.rootLayout.setBackground(null);
        }

        viewHolder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listner.onSelectedPosition(viewHolder.getAdapterPosition());
            }
        });
        /*if("Lucky".equals(alGifts.get(position).getCategory())) {
            viewHolder.giftbox.setVisibility(View.VISIBLE);
        }*/
    }

    @Override
    public int getItemCount() {
        return alGifts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private AppCompatImageView giftIconImg, giftbox;
        private ConstraintLayout rootLayout;
        private AppCompatTextView tvSelectedCoins, giftName, coins;

        public ViewHolder(View view) {
            super(view);
            giftIconImg = view.findViewById(R.id.giftIconImg);
            giftbox = view.findViewById(R.id.giftbox);
            giftName = view.findViewById(R.id.giftName);
            coins = view.findViewById(R.id.coins);
            rootLayout = view.findViewById(R.id.rootLayout);
            tvSelectedCoins = view.findViewById(R.id.tvSelectedCoins);
        }
    }

    public interface GiftSelectListner {
        void onSelectedPosition(int position);
    }


    public void callUpdatePrice(String count) {
        giftcount = count;
    }
}
