package com.privatepe.app.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.privatepe.app.R;
import com.privatepe.app.response.CallDetailData;
import com.privatepe.app.utils.DateFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CallDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<CallDetailData> list;
    private static final int ITEM = 0;

    public CallDetailAdapter(Context context, List<CallDetailData> list) {
        this.context = context;
        this.list = list;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case ITEM:
                View v1 = null;
                v1 = inflater.inflate(R.layout.item_call_detail_layout, parent, false);
                viewHolder = new myViewHolder(v1);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder hld, @SuppressLint("RecyclerView") int position) {
        switch (getItemViewType(position)) {
            case ITEM:
                try {
                    final myViewHolder holder = (myViewHolder) hld;
                    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm a");
                    Date date = inputFormat.parse(list.get(position).getCreated_at());
                    String formattedDate = outputFormat.format(date);


                    int callStatus = list.get(position).getCall_status();
                    long timeSeconds = list.get(position).getDuration();

                    if(callStatus ==0){
                        holder.tvCallDate.setText(DateFormatter.getInstance().getDayFromDate(formattedDate)); //DateFormatter.getInstance().getDayFromDate(formattedDate)
                    }else {
                        holder.tvCallDate.setText(DateFormatter.getInstance().getDayFromDate(formattedDate) + " (" + DateFormatter.getInstance().calculateTime(timeSeconds)+")");
                    }
                    holder.tvUserName.setText(String.valueOf(list.get(position).getCaller_details().getName()));
                    holder.tvUserLevel.setText("Lvl "+list.get(position).getCaller_details().getRich_level());
                    holder.tvCallStatus.setText("A");

                    final int sdk = android.os.Build.VERSION.SDK_INT;
                    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        if(callStatus ==0){
                            holder.ivCallStatus.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.ic_phone_missed) );
                        }else
                            holder.ivCallStatus.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.ic_call_received) );
                    } else {
                        if(callStatus ==0) {
                            holder.ivCallStatus.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_phone_missed));
                        }else
                            holder.ivCallStatus.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_call_received));
                    }
                    String callQuality = list.get(position).getCall_quality();
                    if(callQuality.equalsIgnoreCase("Very Bad")){
                        holder.tvCallQuality.setBackground(ContextCompat.getDrawable(context, R.drawable.rounded_call_quality_bad));
                    }else if(callQuality.equalsIgnoreCase("Bad")){
                        holder.tvCallQuality.setBackground(ContextCompat.getDrawable(context, R.drawable.rounded_cornor_call_quality));
                    }else if(callQuality.equalsIgnoreCase("Good")){
                        holder.tvCallQuality.setBackground(ContextCompat.getDrawable(context, R.drawable.rounded_call_quality_good));
                    }else{
                        holder.tvCallQuality.setBackground(ContextCompat.getDrawable(context, R.drawable.rounded_call_quality_good));
                    }
                    holder.tvCallQuality.setText(String.valueOf(list.get(position).getCall_quality()));
                    holder.cardView_user.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });

                    break;

                } catch (Exception e) {
                }

        }


    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        TextView  tvUserName,tvUserLevel, tvCallQuality, tvCallStatus,tvCallDate;
        CardView cardView_user;
        ImageView ivCallStatus;

        public myViewHolder(View itemView) {
            super(itemView);
            cardView_user = itemView.findViewById(R.id.cardView_user);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvUserLevel = itemView.findViewById(R.id.tvUserLevel);
            tvCallQuality = itemView.findViewById(R.id.tvCallQuality);
            tvCallStatus = itemView.findViewById(R.id.tvCallStatus);
            tvCallDate = itemView.findViewById(R.id.tvCallDate);
            ivCallStatus = itemView.findViewById(R.id.ivCallStatus);

        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }


}
