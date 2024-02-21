package com.privatepe.app.adapter.gift;

import static com.bumptech.glide.load.engine.DiskCacheStrategy.ALL;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.ContentInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.privatepe.app.Interface.GiftSelectListener;
import com.privatepe.app.R;
import com.privatepe.app.response.newgiftresponse.NewGift;
import com.privatepe.app.utils.SessionManager;

import java.util.ArrayList;

public class GiftNewRecyclerAdapter extends RecyclerView.Adapter<GiftNewRecyclerAdapter.ViewHolder> {

    private ArrayList<NewGift> giftArrayList;
    private GiftSelectListener giftSelectListener;
    private int SELECTED_POS = -1;
    int mainTabPos, subtabPos;
    private Animation zoomIn, zoomOut;

    private Animation.AnimationListener zoomInListner, zoomOutListener;

    String TAG = "GiftNewRecyclerAdapter";
    private ValueAnimator anim;

    private final int mInterval = 2000;

    private Handler mHandler = new Handler();

    private Runnable runnable;
private Context context;

    public GiftNewRecyclerAdapter(Context context1,ArrayList<NewGift> giftArrayList, GiftSelectListener giftSelectListener, int mTabPos, int tabPos) {
        this.giftArrayList = giftArrayList;
        this.giftSelectListener = giftSelectListener;
        this.mainTabPos = mTabPos;
        this.subtabPos = tabPos;
        Log.e(TAG, "GiftNewRecyclerAdapter: giftArrayList.size " + giftArrayList.size());
        mHandler = new Handler();
        this.context=context1;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.newgift_recycler_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(holder.itemView.getContext())
                .load(giftArrayList.get(position).getImage())
                .thumbnail(0.4f)
                .centerInside()
                .fitCenter()
                .error(R.drawable.ic_baseline_image_not_supported_24)
                .diskCacheStrategy(ALL)
                .into(holder.giftIconImg);
        holder.giftName.setText(giftArrayList.get(position).getGift_name());

        if(new SessionManager(context).getGender().equalsIgnoreCase("female")){
            holder.beansImg.setVisibility(View.VISIBLE);
            holder.coins.setText( " " + (int) giftArrayList.get(position).getGift_beans());

        }else {
            holder.beansImg.setVisibility(View.GONE);
            holder.coins.setText( "\u20B9" + (int) giftArrayList.get(position).getAmount());

        }


        if (SELECTED_POS == position) {
            holder.rootLayout.setBackgroundResource(R.drawable.bg_gift_selected);
            if (anim != null) {
                anim.removeAllListeners();
                anim.cancel();
            }

            anim = ValueAnimator.ofFloat(1.0f, 1.1f);
            anim.setDuration(500);
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    holder.giftIconImg.setScaleX((Float) animation.getAnimatedValue());
                    holder.giftIconImg.setScaleY((Float) animation.getAnimatedValue());
                }
            });

            anim.setRepeatCount(Animation.INFINITE);
            anim.setRepeatMode(ValueAnimator.REVERSE);
            anim.start();

           /*
            Runnable runnable = new Runnable() {
                @Override
                public void run() {

                  holder.giftIconImg.clearAnimation();
                    holder.giftIconImg.setAnimation(zoomIn);


                    zoomInListner=new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            holder.giftIconImg.clearAnimation();
                            holder.giftIconImg.setAnimation(zoomOut);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    };


                    zoomOutListener=new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            holder.giftIconImg.clearAnimation();
                            holder.giftIconImg.setAnimation(zoomIn);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    };

                    zoomIn.setAnimationListener(zoomInListner);
                    zoomOut.setAnimationListener(zoomOutListener);




                    mHandler.postDelayed(this, mInterval);

                }
            };

            runnable.run();*/

        } else {
            holder.rootLayout.setBackgroundResource(0);
            holder.giftIconImg.setScaleX(1.0f);
            holder.giftIconImg.setScaleY(1.0f);
        }

    }

    @Override
    public int getItemCount() {
        return giftArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private AppCompatImageView giftIconImg, giftbox;
        private ConstraintLayout rootLayout;
        private TextView tvSelectedCoins, giftName, coins;
        private View beansImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            giftIconImg = itemView.findViewById(R.id.giftIconImg);
            giftName = itemView.findViewById(R.id.giftName);
            coins = itemView.findViewById(R.id.coins);
            rootLayout = itemView.findViewById(R.id.rootLayout);
beansImg=itemView.findViewById(R.id.beansImg);
            // zoomIn = AnimationUtils.loadAnimation(itemView.getContext(), R.anim.zoom_in);
            // zoomOut = AnimationUtils.loadAnimation(itemView.getContext(), R.anim.zoom_out);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SELECTED_POS = getAdapterPosition();
                    giftSelectListener.OnGiftSelect(giftArrayList.get(getAdapterPosition()));
                    Log.e(TAG, "onCreateView: MainTab " + mainTabPos + " SubTab " + subtabPos);
                    notifyDataSetChanged();
                }
            });


        }
    }


}
