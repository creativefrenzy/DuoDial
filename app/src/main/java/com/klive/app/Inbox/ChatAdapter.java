package com.klive.app.Inbox;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.klive.app.R;
import com.klive.app.response.newgiftresponse.NewGift;
import com.klive.app.utils.SessionManager;

import java.util.HashMap;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatAdapterHolder> {

    List<ChatBean> arrayList;
    Context context;
    public ChatAdapter(Context context, List<ChatBean> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_other, parent, false);
        return new ChatAdapterHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ChatAdapterHolder holder, final int position) {
        if (arrayList.get(position).getChat_sent().equals("")) {

            if(arrayList.get(position).getChatType().equals("TEXT"))
            {
                holder.card_receive.setVisibility(View.VISIBLE);
                holder.giftImage.setVisibility(View.GONE);
                holder.receive.setText(arrayList.get(position).getChat_receive());
                holder.card_receive.setBackgroundResource(R.drawable.rounded_relativemsgout);
                holder.videoCallHostImg.setVisibility(View.GONE);
                holder.videoCallImg.setVisibility(View.GONE);
            }
            if (arrayList.get(position).getChatType().equals("GIFT"))
            {
                HashMap<Integer, NewGift> giftImgList = new SessionManager(context).getEmployeeAllGiftList();
                NewGift gift = giftImgList.get(Integer.parseInt(arrayList.get(position).getChat_receive()));
                if (null!= gift) {
                    Glide.with(context)
                            .load(gift.getImage())
                            .into(holder.giftImage);
                    holder.receive.setText(gift.getGift_name()+" Received");
                }
                Log.e("giftInINBOX"," chat gift  id: "+arrayList.get(position).getChat_receive());
                holder.card_receive.setVisibility(View.VISIBLE);
                holder.giftImage.setVisibility(View.VISIBLE);
                holder.card_receive.setBackgroundResource(R.drawable.rounded_relativemsgout);
                holder.videoCallHostImg.setVisibility(View.GONE);
                holder.videoCallImg.setVisibility(View.GONE);
//                holder.giftImage.setImageResource(getGiftResourceId(Integer.parseInt(arrayList.get(position).getChat_receive())));
            } else if (arrayList.get(position).getChatType().equals("video_call_event")){
                holder.card_receive.setVisibility(View.VISIBLE);
                holder.giftImage.setVisibility(View.GONE);
                holder.receive.setText(arrayList.get(position).getChat_receive());
                holder.card_receive.setBackgroundResource(R.drawable.rounded_relativemsgout);
                holder.videoCallHostImg.setVisibility(View.VISIBLE);
                holder.videoCallImg.setVisibility(View.GONE);
            }

            holder.sent.setText("");
            holder.card_sent.setBackgroundResource(R.drawable.white_back);
        }  //it is condition for message sent by employee
        else {
            if(arrayList.get(position).getChatType().equals("TEXT"))
            {
                holder.sent.setText(arrayList.get(position).getChat_sent());
                holder.receive.setText("");
                holder.giftImage.setVisibility(View.GONE);
                holder.card_receive.setBackgroundResource(R.drawable.white_back);
                holder.card_sent.setBackgroundResource(R.drawable.rounded_relativemsgin);
                holder.videoCallHostImg.setVisibility(View.GONE);
                holder.videoCallImg.setVisibility(View.GONE);
            }
            else if (arrayList.get(position).getChatType().equals("GIFT"))
            {
                holder.videoCallHostImg.setVisibility(View.GONE);
                holder.videoCallImg.setVisibility(View.GONE);
               // Log.e("giftInINBOX"," chat gift  id: "+arrayList.get(position).getChat_sent());
            } else if (arrayList.get(position).getChatType().equals("video_call_event")){
                holder.sent.setText(arrayList.get(position).getChat_sent());
                holder.receive.setText("");
                holder.card_receive.setBackgroundResource(R.drawable.white_back);
                holder.card_receive.setVisibility(View.GONE);
                holder.card_sent.setBackgroundResource(R.drawable.rounded_relativemsgin);
                holder.videoCallHostImg.setVisibility(View.GONE);
                holder.videoCallImg.setVisibility(View.VISIBLE);
            }

        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ChatAdapterHolder extends RecyclerView.ViewHolder {
        public TextView receive;
        public TextView sent;
        public TextView sentTime, receiveTime;
        public CardView card_sent, card_receive;
//        public CardView giftReceive;
        public ImageView giftImage,videoCallHostImg,videoCallImg;

        public ChatAdapterHolder(View view) {
            super(view);
            card_receive = view.findViewById(R.id.card_gchat_message_other);
            card_sent = view.findViewById(R.id.card_gchat_message_me);
            sent = view.findViewById(R.id.text_gchat_message_me);
            receive = view.findViewById(R.id.text_gchat_message_other);
//            giftReceive = view.findViewById(R.id.cv_r);
            giftImage = view.findViewById(R.id.img_r);
            sentTime = view.findViewById(R.id.sent_time);
            receiveTime = view.findViewById(R.id.receive_time);
            videoCallHostImg = (ImageView) itemView.findViewById(R.id.video_call_host_img);
            videoCallImg = (ImageView) itemView.findViewById(R.id.video_call_img);
        }
    }


    private int getGiftResourceId(int Pos) {
        int ResourceId = 0;

        switch (Pos) {
            case 19:
                ResourceId = R.drawable.candy;
                break;
            case 2:
                ResourceId = R.drawable.lucky;
                break;
            case 3:
                ResourceId = R.drawable.bell;
                break;
            case 4:
                ResourceId = R.drawable.leaves;
                break;
            case 5:
                ResourceId = R.drawable.kiss;
                break;
            case 6:
                ResourceId = R.drawable.candy_1;
                break;
            case 7:
                ResourceId = R.drawable.rose;
                break;
            case 8:
                ResourceId = R.drawable.heart;
                break;
            case 9:
                ResourceId = R.drawable.lipstik;
                break;
            case 10:
                ResourceId = R.drawable.perfume;
                break;
            case 11:
                ResourceId = R.drawable.necklace;
                break;
            case 12:
                ResourceId = R.drawable.panda;
                break;
            case 13:
                ResourceId = R.drawable.hammer;
                break;
            case 14:
                ResourceId = R.drawable.rocket;
                break;
            case 15:
                ResourceId = R.drawable.ship;
                break;
            case 16:
                ResourceId = R.drawable.ring;
                break;
            case 17:
                ResourceId = R.drawable.disney;
                break;
            case 18:
                ResourceId = R.drawable.hot_ballon;
                break;
        }
        return ResourceId;

    }


}
