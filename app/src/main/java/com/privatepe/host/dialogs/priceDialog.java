package com.privatepe.host.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.privatepe.host.R;
import com.privatepe.host.adapter.ChatPriceRecyclerAdapter;
import com.privatepe.host.model.PriceList.PriceDataModel;

import java.util.ArrayList;

public class priceDialog extends Dialog {


    private RecyclerView ChatPriceRecycler;
    private ChatPriceRecyclerAdapter chatPriceRecyclerAdapter;
    private String selectedPrice;

    public priceDialog pDialog = this;


    SelectedPriceCallback selectedPriceCallback;

    public priceDialog(@NonNull Context context, ArrayList<PriceDataModel> priceDataModelArrayList, int selectedChatPrice, int selectedLevel, int SelectedPosition, SelectedPriceCallback selectedPriceCallback) {
        super(context);
        this.selectedPriceCallback = selectedPriceCallback;
        init(context, priceDataModelArrayList, selectedChatPrice, selectedLevel, SelectedPosition);
    }

    void init(Context context, ArrayList<PriceDataModel> priceDataModelArrayList, int selectedChatPrice, int selectedLevel, int SelectedPosition) {
        try {
            this.setContentView(R.layout.chat_price);
            this.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            //this.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
            this.getWindow().setGravity(Gravity.BOTTOM);
            this.setCancelable(true);


            ChatPriceRecycler = findViewById(R.id.chatpricerEcycler);
            chatPriceRecyclerAdapter = new ChatPriceRecyclerAdapter(context, priceDataModelArrayList, selectedChatPrice, selectedLevel, SelectedPosition, new ChatPriceRecyclerAdapter.PriceCallbackInterface() {
                @Override
                public void getSelectedPrice(String Price) {
                    selectedPrice = Price;
                    selectedPriceCallback.GetSelectedPrice(selectedPrice, pDialog);
                }
            });
            ChatPriceRecycler.setHasFixedSize(true);
            ChatPriceRecycler.setLayoutManager(new LinearLayoutManager(context));
            ChatPriceRecycler.setAdapter(chatPriceRecyclerAdapter);


            show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public interface SelectedPriceCallback {

        void GetSelectedPrice(String price, priceDialog pDialog);

    }

}