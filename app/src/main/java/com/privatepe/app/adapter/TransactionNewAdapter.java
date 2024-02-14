package com.privatepe.app.adapter;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.privatepe.app.R;
import com.privatepe.app.response.NewWallet.WalletHistoryDataNew;

import java.util.ArrayList;


public class TransactionNewAdapter extends RecyclerView.Adapter<TransactionNewAdapter.myViewHolder> {
    Context context;

    ArrayList<WalletHistoryDataNew> list;

    private SparseBooleanArray expandState = new SparseBooleanArray();

    public TransactionNewAdapter(Context context, ArrayList<WalletHistoryDataNew> walletDataNew) {

        this.context = context;
        this.list = walletDataNew;

    }
    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_history_item_view, parent, false);
        return new myViewHolder(v);

    }
    @Override
    public void onBindViewHolder(myViewHolder holder, int position) {

        try {
            holder.transaction_name.setText(list.get(position).getTransactionDes());
            holder.date.setText(list.get(position).getCreatedAt());

            if(list.get(position).getCredit()!=0) {
                holder.amount.setText("+ " + list.get(position).getCredit().toString());
            }else{
                holder.amount.setText("- " + list.get(position).getDebit().toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }
    public class myViewHolder extends RecyclerView.ViewHolder {
        TextView transaction_name, amount, date;

        public myViewHolder(View itemView) {
            super(itemView);

            transaction_name = itemView.findViewById(R.id.text_transaction_name);
            date = itemView.findViewById(R.id.trasaction_date);
            amount = itemView.findViewById(R.id.trasaction_amount);
        }
    }

}