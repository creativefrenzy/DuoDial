package com.klive.app.adapter;

import static com.klive.app.new_gift.utils.Utils.svgaImageViewFromUrl;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.klive.app.R;
import com.klive.app.model.gift.GiftAnimData;
import com.opensource.svgaplayer.SVGACallback;
import com.opensource.svgaplayer.SVGAImageView;

import java.util.List;


public class GiftAnimationRecyclerAdapter extends RecyclerView.Adapter<GiftAnimationRecyclerAdapter.ViewHolder> {
    private final Handler giftRequestDismissHandler = new Handler();

    List<GiftAnimData> giftdataList;
    Context context;
    OnItemInvisibleListener onItemInvisibleListener;

    public int itemNeedToRemove;
    public boolean isAnimation = true;
    private MediaPlayer mp;


    public GiftAnimationRecyclerAdapter(List<GiftAnimData> giftdataList, Context context, OnItemInvisibleListener listner) {
        this.giftdataList = giftdataList;
        this.context = context;
        onItemInvisibleListener = listner;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gift_animation_recycler_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Log.e("svga_gift_test", "onBindViewHolder: " + giftdataList.get(position).getNewGift().getAnimation_file());
        Log.e("svga_gift_test", "onBindViewHolder: " + giftdataList.get(position).getNewGift().getGift_name());

        holder.svgaImageView.stopAnimation();
        holder.svgaImageView.setVisibility(View.VISIBLE);
        svgaImageViewFromUrl(giftdataList.get(position).getNewGift().getAnimation_file(), holder.svgaImageView);


        holder.svgaImageView.setCallback(new SVGACallback() {
            @Override
            public void onStep(int i, double v) {

            }

            @Override
            public void onRepeat() {

            }

            @Override
            public void onPause() {

            }

            @Override
            public void onFinished() {
                holder.svgaImageView.setVisibility(View.GONE);
            }
        });


        // holder.GiftPic.setImageResource(giftdataList.get(position).getGiftResourceId());
        Glide.with(context).load(giftdataList.get(position).getPeerProfilePic()).into(holder.giftSenderPic);
        holder.giftSenderName.setText(giftdataList.get(position).getPeerName());

        Log.e("GiftAnimationBug", "Adapter Position onBindViewHolder in " + "GiftAnimationRecyclerAdapter " + position);

        AnimateGift(holder.itemView, position);
        //   Log.e("NewZegoGift","position "+position);


    }


    @Override
    public int getItemCount() {
        return giftdataList.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView giftSenderPic;
        TextView giftSenderName;
        ImageView GiftPic;
        SVGAImageView svgaImageView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            GiftPic = itemView.findViewById(R.id.img_recgift);
            giftSenderPic = itemView.findViewById(R.id.img_gifsendpic);
            giftSenderName = itemView.findViewById(R.id.tv_giftsendname);
            svgaImageView = itemView.findViewById(R.id.svga_gift);
        }
    }

    private void AnimateGift(View itemView, int pos) {
        //  Log.e("GiftAnimationBug", "Animation Perform in " + "GiftAnimationRecyclerAdapter "+"true");

        Animation animFadeIn = AnimationUtils.loadAnimation(itemView.getContext(), R.anim.left_to_right);
        ((RelativeLayout) itemView.findViewById(R.id.rl_newgiftui)).setVisibility(View.VISIBLE);

        Log.e("GiftAnimationBug", "Recycler Item Visibility in " + "GiftAnimationRecyclerAdapter " + itemView.findViewById(R.id.rl_newgiftui).getVisibility() + "\n\n\n" + "   ");

        giftRequestDismissHandler.postDelayed(() -> {
            ((RelativeLayout) itemView.findViewById(R.id.rl_newgiftui)).setVisibility(View.INVISIBLE);
            // onItemInvisibleListener.onItemInvisible(0);
            // Log.e("GiftAnimationBug", "Recycler Item Visibility After Animation in " + "GiftAnimationRecyclerAdapter "+itemView.findViewById(R.id.rl_newgiftui).getVisibility());

        }, 3000);

        animFadeIn.reset();
        ((RelativeLayout) itemView.findViewById(R.id.rl_newgiftui)).clearAnimation();
        ((RelativeLayout) itemView.findViewById(R.id.rl_newgiftui)).startAnimation(animFadeIn);


    }


    public interface OnItemInvisibleListener {
        void onItemInvisible(int adapterposition);
    }

}
