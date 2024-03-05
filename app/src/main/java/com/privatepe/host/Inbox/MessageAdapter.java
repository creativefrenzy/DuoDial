package com.privatepe.host.Inbox;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.annotations.NotNull;
import com.google.gson.Gson;

import com.privatepe.host.R;
import com.privatepe.host.response.newgiftresponse.NewGift;
import com.privatepe.host.utils.SessionManager;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<MessageBean> messageBeanList;
    protected final LayoutInflater inflater;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private Context context;
    InboxDetails inboxDetails;

    public MessageAdapter(Context context, List<MessageBean> messageBeanList, InboxDetails inboxDetails) {
        inflater = ((Activity) context).getLayoutInflater();
        this.messageBeanList = messageBeanList;
        this.context = context;
        this.inboxDetails = inboxDetails;

    }

    /*@Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_ITEM) {
            View view = inflater.inflate(R.layout.msg_item_layout, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        } else if (viewType == TYPE_HEADER) {
            View view = inflater.inflate(R.layout.msg_item_layout, parent, false);
            HeaderViewHolder holder = new HeaderViewHolder(view);
            return holder;
        }
       return null;
    }*/

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = inflater.inflate(R.layout.msg_item_layout, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        } else if (viewType == TYPE_HEADER) {
            View view = inflater.inflate(R.layout.msg_header_layout, parent, false);
            HeaderViewHolder holder = new HeaderViewHolder(view);
            return holder;
        }

        throw new RuntimeException("there is no type");
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof HeaderViewHolder) {
            setupView((HeaderViewHolder) holder, position);
        } else if (holder instanceof MyViewHolder) {
            setupView((MyViewHolder) holder, position);
        }
    }

    @Override
    public int getItemCount() {

        return messageBeanList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;

        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        String date = messageBeanList.get(position).getMsgDate();
        if (TextUtils.isEmpty(date)) {
            return false;
        } else {
            return true;
        }
    }

    // MyViewHolder cusHolder;

    private void setupView(MyViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        MessageBean bean = messageBeanList.get(position);
        //Log.e("ChatList", "position="+position+" "+bean.getMessage().getType() +" "+bean.getMessage().getMessage());
        Log.e("chatdbtesttt", "setupView: messageBeanList.size " + messageBeanList.size());

        Log.e("chatdbtesttt", "setupView: messageBeanList  " + new Gson().toJson(messageBeanList));
        Log.e("chatdbtesttt", "setupView: " + new Gson().toJson(bean));

        if (bean.isBeSelf()) {
            try {

                if (bean.getMessage().getType().equals("text")) {
                    if (!TextUtils.isEmpty(bean.getMessage().getFromImage())) {
                        Picasso.get().load(bean.getMessage().getFromImage()).placeholder(R.drawable.default_profile).into(holder.iconSelfName);
                    }
                    holder.textViewSelfMsg.setText(bean.getMessage().getMessage());
                    holder.timeRight.setText(getTimeByTimestamp(bean.getTimestamp()));

                    holder.iconSelfName.setVisibility(View.GONE);
                    holder.ll_r.setVisibility(View.VISIBLE);
                    holder.cv_r.setVisibility(View.GONE);
                    holder.giftImg.setVisibility(View.GONE);
                    holder.videoCallHostImg.setVisibility(View.GONE);
                    holder.videoCallImg.setVisibility(View.GONE);
                } else if (bean.getMessage().getType().equals("gift")) {
                    // added this for the gift image from local preferences fetched from api
//                    HashMap<Integer, NewGift> giftImgList = new SessionManager(context).getEmployeeGiftList();
                    HashMap<Integer, NewGift> giftImgList = new SessionManager(context).getEmployeeAllGiftList();
                    Glide.with(context)
                            .load(giftImgList.get(Integer.parseInt(bean.getMessage().getMessage())).getImage())
                            .into(holder.giftImg);
                    holder.textViewSelfMsg.setText(giftImgList.get(Integer.parseInt(bean.getMessage().getMessage())).getGift_name() + " Sent");
                    //todo will show the pic as per image url
                    holder.iconSelfName.setVisibility(View.GONE);
                    holder.giftImg.setVisibility(View.VISIBLE);
                    holder.ll_r.setVisibility(View.VISIBLE);
                    holder.cv_r.setVisibility(View.GONE); //VISIBLE before KLive integrated
                    holder.videoCallHostImg.setVisibility(View.GONE);
                    holder.videoCallImg.setVisibility(View.GONE);
                    holder.img_r.setImageResource(getGiftImage((bean.getMessage().getMessage())));
                } else if (bean.getMessage().getType().equals("ss")) {
                    holder.iconSelfName.setVisibility(View.GONE);
                    holder.ll_r.setVisibility(View.GONE);
                    holder.cv_ss_r.setVisibility(View.VISIBLE);
                    holder.giftImg.setVisibility(View.GONE);
                    Picasso.get().load(bean.getMessage().getMessage()).into(holder.img_ss_r);
                    holder.videoCallHostImg.setVisibility(View.GONE);
                    holder.videoCallImg.setVisibility(View.GONE);

                    //     holder.img_r.setImageResource(getGiftImage((bean.getMessage().getMessage())));
                } else if (bean.getMessage().getType().equals("video_call_event")) {
                    holder.textViewSelfMsg.setText(bean.getMessage().getMessage());
                    holder.timeRight.setText(getTimeByTimestamp(bean.getTimestamp()));
                    holder.iconSelfName.setVisibility(View.GONE);
                    holder.ll_r.setVisibility(View.VISIBLE);
                    holder.cv_r.setVisibility(View.GONE);
                    holder.giftImg.setVisibility(View.GONE);
                    holder.videoCallHostImg.setVisibility(View.GONE);
                    holder.videoCallImg.setVisibility(View.VISIBLE);
                } /*else if (bean.getMessage().getType().equals("image")) {

                    Log.e("automessageLog","in image area");
                    holder.iconSelfName.setVisibility(View.GONE);
                    holder.ll_r.setVisibility(View.GONE);
                    holder.cv_r.setVisibility(View.VISIBLE);
                    holder.giftImg.setVisibility(View.GONE);
                    holder.videoCallHostImg.setVisibility(View.GONE);
                    holder.videoCallImg.setVisibility(View.GONE);


                } else if (bean.getMessage().getType().equals("audio")) {
                }*/
            } catch (Exception e) {
              /*  holder.textViewSelfName.setText(bean.getAccount());
                holder.textViewSelfMsg.setText(bean.getMessage().getMessage());
                holder.timeRight.setText(getTimeByTimestamp(bean.getTimestamp()));*/
            }
        } else {
            try {
                if (bean.getMessage().getType().equals("text")) {
                    if (!TextUtils.isEmpty(bean.getMessage().getFromImage())) {
                        Picasso.get().load(bean.getMessage().getFromImage()).into(holder.iconOtherName);
                    }
                    holder.textViewOtherMsg.setText(bean.getMessage().getMessage());
                    holder.timeLeft.setText(getTimeByTimestamp(bean.getTimestamp()));

                    holder.iconOtherName.setVisibility(View.GONE);
                    holder.ll_l.setVisibility(View.VISIBLE);
                    holder.cv_l.setVisibility(View.GONE);
                    holder.giftImg.setVisibility(View.GONE);
                    holder.videoCallHostImg.setVisibility(View.GONE);
                    holder.videoCallImg.setVisibility(View.GONE);
                    holder.cv_audio.setVisibility(View.GONE);
                  /*  if (bean.getBackground() != 0) {
                        holder.textViewOtherName.setBackgroundResource(bean.getBackground());
                    }*/
                } else if (bean.getMessage().getType().equals("gift")) {

                    HashMap<Integer, NewGift> giftImgList = new SessionManager(context).getEmployeeAllGiftList();
                    Glide.with(context)
                            .load(giftImgList.get(Integer.parseInt(bean.getMessage().getMessage())).getImage())
                            .into(holder.img_l);

                    holder.textViewOtherMsg.setText("Gift Received");

                    holder.iconOtherName.setVisibility(View.GONE);
                    holder.ll_l.setVisibility(View.GONE);
                    holder.cv_l.setVisibility(View.VISIBLE);
                    //holder.img_l.setImageResource(getGiftImage((bean.getMessage().getMessage())));
                    holder.videoCallHostImg.setVisibility(View.GONE);
                    holder.cv_audio.setVisibility(View.GONE);
                    holder.videoCallImg.setVisibility(View.GONE);
                } else if (bean.getMessage().getType().equals("video_call_event")) {
                    holder.textViewOtherMsg.setText(bean.getMessage().getMessage());
                    holder.timeLeft.setText(getTimeByTimestamp(bean.getTimestamp()));

                    holder.iconOtherName.setVisibility(View.GONE);
                    holder.ll_l.setVisibility(View.VISIBLE);
                    holder.cv_l.setVisibility(View.GONE);
                    holder.giftImg.setVisibility(View.GONE);
                    holder.videoCallHostImg.setVisibility(View.VISIBLE);
                    holder.cv_audio.setVisibility(View.GONE);
                    holder.videoCallImg.setVisibility(View.GONE);
                } else if (bean.getMessage().getType().equals("image")) {

                    //Log.e("automessageLog", "in image area");
                    holder.iconOtherName.setVisibility(View.GONE);
                    holder.ll_l.setVisibility(View.GONE);
                    holder.cv_l.setVisibility(View.VISIBLE);
                    holder.giftImg.setVisibility(View.GONE);
                    holder.videoCallHostImg.setVisibility(View.GONE);
                    holder.videoCallImg.setVisibility(View.GONE);
                    holder.cv_audio.setVisibility(View.GONE);


                    Glide.with(context)
                            .load(bean.getMessage().getMessage())
                            .centerCrop()
                            .into(holder.img_l);

                    holder.img_l.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            inboxDetails.showImage(bean.getMessage().getMessage());
                        }
                    });

                } else if (bean.getMessage().getType().equals("audio")) {
                    holder.iconOtherName.setVisibility(View.GONE);
                    holder.ll_l.setVisibility(View.GONE);
                    holder.cv_l.setVisibility(View.GONE);
                    holder.giftImg.setVisibility(View.GONE);
                    holder.videoCallHostImg.setVisibility(View.GONE);
                    holder.videoCallImg.setVisibility(View.GONE);
                    holder.cv_audio.setVisibility(View.VISIBLE);

                    Glide.with(context)
                            .load(bean.getMessage().getFromImage())
                            .centerCrop()
                            .into(holder.cir_pp);

                    holder.img_playpause.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            MessageBean messageBean = new MessageBean();
                            messageBean.setMessage(messageBeanList.get(position).getMessage());
                            messageBean.setAccount(messageBeanList.get(position).getAccount());
                            messageBean.setTimestamp(messageBeanList.get(position).getTimestamp());
                            messageBean.setBeSelf(messageBeanList.get(position).isBeSelf());
                            if (!messageBeanList.get(position).isPlayingAudio()) {

                                holder.img_playpause.setImageDrawable(context.getResources().getDrawable(R.drawable.pause_audio));
                                messageBean.setPlayingAudio(true);
                                mp = new MediaPlayer();
                                try {
                                    mp.setDataSource(bean.getMessage().getMessage());
                                    mp.prepare();
                                    mp.start();

                                    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                        @Override
                                        public void onCompletion(MediaPlayer mediaPlayer) {
                                            MessageBean messageBean = new MessageBean();
                                            messageBean.setMessage(messageBeanList.get(position).getMessage());
                                            messageBean.setAccount(messageBeanList.get(position).getAccount());
                                            messageBean.setTimestamp(messageBeanList.get(position).getTimestamp());
                                            messageBean.setBeSelf(messageBeanList.get(position).isBeSelf());
                                            holder.img_playpause.setImageDrawable(context.getResources().getDrawable(R.drawable.play_audio));
                                            messageBean.setPlayingAudio(false);
                                            stopChangeUi();
                                            messageBeanList.set(position, messageBean);
                                        }
                                    });
                                } catch (IOException e) {
                                    //  Log.e(LOG_TAG, "prepare() failed");
                                }
                            } /*else {


                                holder.img_playpause.setImageDrawable(context.getResources().getDrawable(R.drawable.pause_audio));
                                messageBean.setPlayingAudio(true);
                            }*/ else {
                                stopChangeUi();
                                holder.img_playpause.setImageDrawable(context.getResources().getDrawable(R.drawable.play_audio));
                                messageBean.setPlayingAudio(false);
                            }
                            messageBeanList.set(position, messageBean);


                            //   inboxDetails.playAudioFile(bean.getMessage().getMessage(), position);
                        }
                    });
                }

                if (!messageBeanList.get(position).isPlayingAudio()) {
                    holder.img_playpause.setImageDrawable(context.getResources().getDrawable(R.drawable.play_audio));
                } else {
                    holder.img_playpause.setImageDrawable(context.getResources().getDrawable(R.drawable.pause_audio));
                }
            } catch (Exception e) {
               /* holder.textViewOtherName.setText(bean.getAccount());
                holder.textViewOtherMsg.setText(bean.getMessage().getMessage());
                holder.timeLeft.setText(getTimeByTimestamp(bean.getTimestamp()));
                if (bean.getBackground() != 0) {
                    holder.textViewOtherName.setBackgroundResource(bean.getBackground());
                }*/

                Log.e("chatdbtesttt", "setupView: Exception " + e.getMessage());
            }
        }

        holder.layoutRight.setVisibility(bean.isBeSelf() ? View.VISIBLE : View.GONE);
        holder.layoutLeft.setVisibility(bean.isBeSelf() ? View.GONE : View.VISIBLE);
    }

    MediaPlayer mp;

    public void stopChangeUi() {
        try {
            mp.release();
            mp = null;
        } catch (Exception e) {
        }
    }

    String getGiftImageTextById(String id) {
        String imgResource = "";
//        6, 18, 8, 5
        if (id.equals("5")) {
            imgResource = "Kiss";
        } else if (id.equals("6")) {
            imgResource = "Candy";
        } else if (id.equals("8")) {
            imgResource = "Heart";
        } else if (id.equals("18")) {
            imgResource = "Hot Balloon";
        }
        return imgResource;
    }

    private void setupView(HeaderViewHolder holder, int position) {

        MessageBean bean = messageBeanList.get(position);
        holder.headerTV.setText(bean.getMsgDate());

        /*if (position > 0) {
            String dateCurr = getDateByTimestamp(messageBeanList.get(position).getTimestamp());
            String dateprev = getDateByTimestamp(messageBeanList.get(position - 1).getTimestamp());
            if (dateCurr.equalsIgnoreCase(dateprev)) {
                holder.headerTV.setVisibility(View.GONE);
            } else {
                holder.headerTV.setVisibility(View.VISIBLE);
            }
        } else {
            holder.headerTV.setVisibility(View.VISIBLE);
        }*/
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private final CircleImageView iconOtherName;
        private final TextView textViewOtherMsg;
        private final CircleImageView iconSelfName;
        CircleImageView cir_pp;
        private final TextView textViewSelfMsg;
        private final RelativeLayout layoutLeft;
        private final RelativeLayout layoutRight;
        private final RelativeLayout cv_audio;
        private final TextView timeLeft;
        private final TextView timeRight;


        private CardView cv_l, cv_r, cv_ss_r;
        private LinearLayout ll_l, ll_r;
        private ImageView img_r, img_ss_r, img_l, giftImg, videoCallHostImg, videoCallImg, img_playpause;

        public MyViewHolder(View itemView) {
            super(itemView);

            iconOtherName = (CircleImageView) itemView.findViewById(R.id.item_icon_l);
            textViewOtherMsg = (TextView) itemView.findViewById(R.id.item_msg_l);
            iconSelfName = (CircleImageView) itemView.findViewById(R.id.item_icon_r);
            cir_pp = (CircleImageView) itemView.findViewById(R.id.cir_pp);
            textViewSelfMsg = (TextView) itemView.findViewById(R.id.item_msg_r);
            layoutLeft = (RelativeLayout) itemView.findViewById(R.id.item_layout_l);
            layoutRight = (RelativeLayout) itemView.findViewById(R.id.item_layout_r);
            timeLeft = (TextView) itemView.findViewById(R.id.item_time_l);
            timeRight = (TextView) itemView.findViewById(R.id.item_time_r);
            cv_l = (CardView) itemView.findViewById(R.id.cv_l);
            cv_r = (CardView) itemView.findViewById(R.id.cv_r);
            cv_ss_r = (CardView) itemView.findViewById(R.id.cv_ss_r);
            cv_audio = (RelativeLayout) itemView.findViewById(R.id.cv_audio);
            ll_l = (LinearLayout) itemView.findViewById(R.id.ll_l);
            ll_r = (LinearLayout) itemView.findViewById(R.id.ll_r);
            img_r = (ImageView) itemView.findViewById(R.id.img_r);
            img_ss_r = (ImageView) itemView.findViewById(R.id.img_ss_r);
            img_l = (ImageView) itemView.findViewById(R.id.img_l);
            giftImg = (ImageView) itemView.findViewById(R.id.gift_img);
            videoCallHostImg = (ImageView) itemView.findViewById(R.id.video_call_host_img);
            videoCallImg = (ImageView) itemView.findViewById(R.id.video_call_img);
            img_playpause = (ImageView) itemView.findViewById(R.id.img_playpause);
        }
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {

        private final TextView headerTV;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            headerTV = itemView.findViewById(R.id.headerTV);
        }
    }

    public static String getTimeByTimestamp(String dateInMilliseconds) {
        String dateFormat = "hh:mm a";
        try {
            return DateFormat.format(dateFormat, Long.parseLong(dateInMilliseconds)).toString().toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getDateByTimestamp(String dateInMilliseconds) {
        String dateFormat = "dd/MM/yyyy";
        try {
            return DateFormat.format(dateFormat, Long.parseLong(dateInMilliseconds)).toString().toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    int getGiftImage(String id) {
        int imgResource = 0;
        if (id.equals("18")) {
            imgResource = R.drawable.heart;

        } else if (id.equals("1")) {
            imgResource = R.drawable.test1;
        } else if (id.equals("2")) {
            imgResource = R.drawable.test2;
        } else if (id.equals("3")) {
            imgResource = R.drawable.test3;
        } else if (id.equals("4")) {
            imgResource = R.drawable.test4;
        } else if (id.equals("21")) {
            imgResource = R.drawable.lips;

        } else if (id.equals("22")) {
            imgResource = R.drawable.bunny;

        } else if (id.equals("23")) {
            imgResource = R.drawable.rose;

        } else if (id.equals("24")) {
            imgResource = R.drawable.boygirl;

        } else if (id.equals("25")) {
            imgResource = R.drawable.sandle;

        } else if (id.equals("26")) {
            imgResource = R.drawable.frock;

        } else if (id.equals("27")) {
            imgResource = R.drawable.car;

        } else if (id.equals("28")) {
            imgResource = R.drawable.ship;

        } else if (id.equals("29")) {
            imgResource = R.drawable.tajmahal;
        } else if (id.equals("30")) {
            imgResource = R.drawable.crown;
        } else if (id.equals("31")) {
            imgResource = R.drawable.bracket;
        } else if (id.equals("32")) {
            imgResource = R.drawable.diamondgift;
        } else if (id.equals("33")) {
            imgResource = R.drawable.lovegift;
        }
        return imgResource;
    }
}
