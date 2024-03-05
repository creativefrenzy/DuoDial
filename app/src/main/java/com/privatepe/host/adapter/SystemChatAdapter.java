package com.privatepe.host.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.privatepe.host.Inbox.ChatBean;
import com.privatepe.host.R;

import java.util.List;

public class SystemChatAdapter extends RecyclerView.Adapter<SystemChatAdapter.SystemChatAdapterHolder> {

    List<ChatBean> arrayList;
    Context context;
    public SystemChatAdapter(Context context, List<ChatBean> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public SystemChatAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.system_chat_screen, parent, false);
        return new SystemChatAdapterHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull final SystemChatAdapterHolder holder, final int position) {
        if (arrayList.get(position).getChat_sent().equals("")) {
            holder.receive.setText(arrayList.get(position).getChat_receive());
            holder.card_receive.setBackgroundResource(R.drawable.rounded_relativemsgout);
            holder.date.setText(arrayList.get(position).getDate());
        }else {
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class SystemChatAdapterHolder extends RecyclerView.ViewHolder {
        public TextView receive;
        public TextView date;
        public CardView card_receive;
        public SystemChatAdapterHolder(View view) {
            super(view);
            card_receive = view.findViewById(R.id.card_gchat_message_other);
            date = view.findViewById(R.id.date);
            receive = view.findViewById(R.id.text_gchat_message_other);

        }
    }
}
