package com.privatepe.app.response.metend.store.dialog;


import static com.privatepe.app.utils.Constant.USE_OR_REMOVE_ITEM;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;


import com.privatepe.app.R;
import com.privatepe.app.databinding.UseOrRemoveItemDialogBinding;
import com.privatepe.app.response.metend.store_list.StoreItemModel;
import com.privatepe.app.response.metend.store_list.StorePlanModel;
import com.privatepe.app.retrofit.ApiManager;
import com.privatepe.app.retrofit.ApiResponseInterface;

import java.util.List;

public class UseOrRemoveItemDialog extends Dialog implements ApiResponseInterface {

    private final ApiManager apiManager;
    UseOrRemoveItemDialogBinding binding;
    private StoreItemModel storeItemModel;

    public UseOrRemoveItemDialog(@NonNull Context context, StoreItemModel itemModel) {
        super(context);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.use_or_remove_item_dialog, null, false);
        setContentView(binding.getRoot());
        storeItemModel = itemModel;
        apiManager = new ApiManager(getContext(), this);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        init();
        show();
    }

    private void init() {


        if (storeItemModel.getIs_using() == 0) {
            binding.textButton.setText("Use");
        } else if (storeItemModel.getIs_using() == 1) {
            binding.textButton.setText("Remove");
        }
        listeners();
    }

    private void listeners() {
        binding.mainLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        binding.textButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (storeItemModel.getIs_using() == 0) {
                    apiManager.useOrRemoveItem(String.valueOf(getStoreId(storeItemModel.getStoreplan(),storeItemModel.getCurrent_plan_coin())),String.valueOf(storeItemModel.getStore_category_id()),"use");
                    binding.textButton.setText("Remove");
                } else if (storeItemModel.getIs_using() == 1) {
                    apiManager.useOrRemoveItem(String.valueOf(getStoreId(storeItemModel.getStoreplan(),storeItemModel.getCurrent_plan_coin())),String.valueOf(storeItemModel.getStore_category_id()),"remove");
                    binding.textButton.setText("Use");
                }
            }
        });
        binding.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }


    @Override
    public void isError(String errorCode) {
        // Log.e(TAG, "isError: " + errorCode);
    }

    @Override
    public void isSuccess(Object response, int ServiceCode) {

        if (ServiceCode == USE_OR_REMOVE_ITEM) {
            // BuyStoreItemResponse rsp = (BuyStoreItemResponse) response;
            // long result = rsp.getResult();


            //   Log.e(TAG, "isSuccess: "+result );

            Intent intent = new Intent("UPDATE-PURCHASE-LIST");
            intent.putExtra("action", "update1");
            getContext().sendBroadcast(intent);


           // Toast.makeText(getContext(), "Purchased successfully.", Toast.LENGTH_SHORT).show();
            dismiss();


        }


    }


    private int getStoreId(List<StorePlanModel> storeplan, int current_plan_coin) {

        int storeId=0;
        for (int i = 0; i < storeplan.size(); i++) {

            if (storeplan.get(i).getCoin()==current_plan_coin)
            {
                storeId=storeplan.get(i).getStore_id();
                return storeId;
            }

        }
        return storeId;
    }
}
