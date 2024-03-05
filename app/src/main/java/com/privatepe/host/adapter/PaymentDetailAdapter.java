package com.privatepe.host.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.privatepe.host.R;
import com.privatepe.host.model.PaymentRequestResponce.PaymentRequestAccount;
import com.privatepe.host.model.PaymentRequestResponce.PaymentRequestResult;

import java.util.List;

public class PaymentDetailAdapter extends RecyclerView.Adapter<PaymentDetailAdapter.PaymentDetailAdapterHolder> {

    List<PaymentRequestResult> arrayList;
    PaymentRequestAccount payment;
    Context context;

    public PaymentDetailAdapter(Context context, List<PaymentRequestResult> arrayList) {
        this.arrayList = arrayList;
        this.context = context;

    }

    @NonNull
    @Override
    public PaymentDetailAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(context).inflate(R.layout.adapter_paymentstatus, parent, false);
        return new PaymentDetailAdapterHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull final PaymentDetailAdapterHolder holder, final int position) {
        holder.setIsRecyclable(false);


        try {
          /*  Log.e("PaymentDeatilAdapter", "PaymentDeatilAdapter " + arrayList.get(position).getPaymentRequestAccount().getType());
            if (!arrayList.get(position).getPaymentRequestAccount().toString().equals("null")) {
            }*/
            int typeVar = arrayList.get(position).getPaymentRequestAccount().getType();

            if (typeVar == 1) {
                holder.tv_account.setText("Account: " + arrayList.get(position).getPaymentRequestAccount().getAccountNumber());
            } else if (typeVar == 2) {
                holder.tv_account.setText("Upi: " + arrayList.get(position).getPaymentRequestAccount().getUpiId());
            }

            holder.tv_amount.setText("₹ " + arrayList.get(position).getAmountInr());
            holder.tv_dateandtime.setText(arrayList.get(position).getCreatedAt());
            //holder.tv_paystatus.setText(list.get(position).getTotalCoins());

            if (arrayList.get(position).getRazorpayStatus().equals("")) {
                holder.rl_paystatus.setBackground(context.getResources().getDrawable(R.drawable.round_status_applied));
                holder.img_paystatus.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_outline_pay_status));
                holder.tv_paystatus.setText("Applied");
            } else if (arrayList.get(position).getRazorpayStatus().equals("processed")) {
                holder.rl_paystatus.setBackground(context.getResources().getDrawable(R.drawable.round_status_sucess));
                holder.img_paystatus.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_check_pay_status));
                holder.tv_paystatus.setText("Success");
            } else {
                holder.rl_paystatus.setBackground(context.getResources().getDrawable(R.drawable.round_status_applied));
                holder.img_paystatus.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_outline_pay_status));
                holder.tv_paystatus.setText("Applied");
            }
        } catch (Exception e) {
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class PaymentDetailAdapterHolder extends RecyclerView.ViewHolder {
        TextView tv_account, tv_amount, tv_dateandtime, tv_paystatus;
        RelativeLayout rl_paystatus;
        ImageView img_paystatus;

        public PaymentDetailAdapterHolder(View view) {
            super(view);
            img_paystatus = itemView.findViewById(R.id.img_paystatus);
            rl_paystatus = itemView.findViewById(R.id.rl_paystatus);
            tv_account = itemView.findViewById(R.id.tv_account);
            tv_amount = itemView.findViewById(R.id.tv_amount);
            tv_dateandtime = itemView.findViewById(R.id.tv_dateandtime);
            tv_paystatus = itemView.findViewById(R.id.tv_paystatus);

        }
    }
}
