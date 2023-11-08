package com.privatepe.app.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.privatepe.app.R;
import com.privatepe.app.response.trading_response.TradingHistoryResult;

import java.util.ArrayList;

public class TradingHistoryRecyclerAdapter extends RecyclerView.Adapter<TradingHistoryRecyclerAdapter.ViewHolder> {

    ArrayList<TradingHistoryResult> tradingHistoryResponseList = new ArrayList();
    Context context;
    OnButtonClickListener onButtonClickListener;

    public TradingHistoryRecyclerAdapter(ArrayList<TradingHistoryResult> tradingHistoryResponseList, Context context, OnButtonClickListener listener) {
        this.tradingHistoryResponseList = tradingHistoryResponseList;
        this.context = context;
        onButtonClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trading_history_recycler_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //  Log.e("SendTooo","" + tradingHistoryResponseList.get(position).getTradingHistoryUserDetail().getName());

        if (tradingHistoryResponseList.get(position).getPending() == 0) {
            holder.lower.setVisibility(View.VISIBLE);
            holder.TransactionStatusText.setVisibility(View.GONE);
        } else if (tradingHistoryResponseList.get(position).getPending() == 1) {
            holder.lower.setVisibility(View.GONE);
            holder.TransactionStatusText.setVisibility(View.VISIBLE);
            holder.TransactionStatusText.setText("Activated");
            holder.TransactionStatusText.setTextColor(context.getResources().getColor(R.color.green));
        } else if (tradingHistoryResponseList.get(position).getPending() == 2) {
            holder.lower.setVisibility(View.GONE);
            holder.TransactionStatusText.setVisibility(View.VISIBLE);
            holder.TransactionStatusText.setText("Recalled");
            holder.TransactionStatusText.setTextColor(context.getResources().getColor(R.color.trading_text_color_gray));
        }

        holder.TransferDateNTimeText.setText(tradingHistoryResponseList.get(position).getCreated_at());
        if (tradingHistoryResponseList.get(position).getStatus().equals("Sent")) {
            holder.SendToUserText.setText("Send to " + tradingHistoryResponseList.get(position).getTradingHistoryUserDetail().getName());
            holder.UserIdText.setText(tradingHistoryResponseList.get(position).getTradingHistoryUserDetail().getProfile_id() + " (" + tradingHistoryResponseList.get(position).getType() + ")");
        } else if (tradingHistoryResponseList.get(position).getStatus().equals("Admin Credit")) {
            holder.SendToUserText.setText("Admin Credited");
            holder.UserIdText.setText("");
        } else if (tradingHistoryResponseList.get(position).getStatus().equals("Received")) {
            holder.SendToUserText.setText("Received from " + tradingHistoryResponseList.get(position).getTradingHistoryUserDetail().getName());
            holder.UserIdText.setText(tradingHistoryResponseList.get(position).getTradingHistoryUserDetail().getProfile_id() + " (" + tradingHistoryResponseList.get(position).getType() + ")");
        }


        if(tradingHistoryResponseList.get(position).getCredit()==0)
        {
            holder.DiamondText.setText("-" + tradingHistoryResponseList.get(position).getDebit());
        //  holder.DiamondText.setTextColor(context.getResources().getColor(R.color.trading_text_color_purple_light));
            holder.DiamondText.setTextColor(Color.RED);
        }
        if (tradingHistoryResponseList.get(position).getDebit()==0)
        {
            holder.DiamondText.setText("+" + tradingHistoryResponseList.get(position).getCredit());
            holder.DiamondText.setTextColor(context.getResources().getColor(R.color.green));
        }


    }


    @Override
    public int getItemCount() {
        return tradingHistoryResponseList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView SendToUserText, TransferDateNTimeText, UserIdText, DiamondText, TransactionStatusText;
        Button btnActivate, btnRecall;
        RelativeLayout lower;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            SendToUserText = itemView.findViewById(R.id.sendtotext);
            TransferDateNTimeText = itemView.findViewById(R.id.transferdate_time);
            UserIdText = itemView.findViewById(R.id.transferto_userId);
            DiamondText = itemView.findViewById(R.id.diamonds_amount);

            btnActivate = itemView.findViewById(R.id.btn_activate);
            btnRecall = itemView.findViewById(R.id.btn_recall);
            lower = itemView.findViewById(R.id.lower);
            TransactionStatusText = itemView.findViewById(R.id.transaction_status);

            TransactionStatusText.setVisibility(View.GONE);

            btnActivate.setOnClickListener(v ->
                    onButtonClickListener.onBtuttonClicked(
                          getAdapterPosition(),  btnActivate.getId(), String.valueOf(tradingHistoryResponseList.get(getAdapterPosition()).getId())
                    ));

            btnRecall.setOnClickListener(v ->
                    onButtonClickListener.onBtuttonClicked(
                            getAdapterPosition(),    btnRecall.getId(), String.valueOf(tradingHistoryResponseList.get(getAdapterPosition()).getId())
                    ));


        }
    }

    public interface OnButtonClickListener {
        void onBtuttonClicked(int dapterpos,int btnId, String walletIid);

    }


}
