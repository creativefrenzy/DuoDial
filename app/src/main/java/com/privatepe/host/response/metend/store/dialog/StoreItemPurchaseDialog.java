package com.privatepe.host.response.metend.store.dialog;


import static com.privatepe.host.response.metend.newgift.utils.Utils.svgaImageViewFromUrl;
import static com.privatepe.host.utils.Constant.BUY_STORE_ITEM;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.privatepe.host.R;
import com.privatepe.host.databinding.StoreItemPurchaseDialogBinding;
import com.privatepe.host.dialogs.InsufficientCoinsMyaccount;
import com.privatepe.host.response.metend.store.adapters.StoreItemPurchaseValidityAdapter;
import com.privatepe.host.response.metend.store.interfaces.OnValiditySelectListener;
import com.privatepe.host.response.metend.store.response.purchase.BuyStoreItemResponse;
import com.privatepe.host.response.metend.store_list.StoreItemModel;
import com.privatepe.host.response.metend.store_list.StorePlanModel;
import com.privatepe.host.retrofit.ApiManager;
import com.privatepe.host.retrofit.ApiResponseInterface;
import com.privatepe.host.utils.SessionManager;

import java.util.ArrayList;


public class StoreItemPurchaseDialog extends Dialog implements OnValiditySelectListener, ApiResponseInterface {
    private StoreItemPurchaseDialogBinding binding;
    private StoreItemModel storeItemModel;
    // private String testUrl = "https://ringlive2022.oss-ap-south-1.aliyuncs.com/ringliveGiftImagesAnimation/2022/11/16/1668587108.svga";
    private String TAG = "StoreItemPurchaseDialog";
    private ArrayList<StorePlanModel> validity_list = new ArrayList<>();
    private ApiManager apiManager;
    private SessionManager sessionManager;
    private StorePlanModel SelectedStorePlan;

    public StoreItemPurchaseDialog(@NonNull Context context, StoreItemModel itemModel) {
        super(context);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.store_item_purchase_dialog, null, false);
        setContentView(binding.getRoot());
        apiManager = new ApiManager(getContext(), this);
        sessionManager = new SessionManager(getContext());
        storeItemModel = itemModel;
        //   Log.e(TAG, "init: " + new Gson().toJson(storeItemModel));
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        init();
        show();
    }

    private void init() {
        initValidityRecycler();
        clickListener();
        binding.itemName.setText(storeItemModel.getStore_name());
        if (storeItemModel.getStoreplan().size() > 0) {
            String firstPlan = "" + storeItemModel.getStoreplan().get(0).getCoin();
            binding.itemCostInCoin.setText(firstPlan);
            SelectedStorePlan = storeItemModel.getStoreplan().get(0);
        }
        //play svga animation
        binding.svgaPlayer.stopAnimation();
        binding.svgaPlayer.setVisibility(View.VISIBLE);
        svgaImageViewFromUrl(storeItemModel.getAnimation_file(), binding.svgaPlayer);


        //   playSvga();

    }

 /*   private void playSvga() {
        binding.svgaPlayer.stopAnimation();
        binding.svgaPlayer.setVisibility(View.VISIBLE);
        SVGAParser parser=new SVGAParser(getContext());

        try {
            URL url=new URL(storeItemModel.getAnimation_file());
            parser.decodeFromURL(url, new SVGAParser.ParseCompletion() {
                @Override
                public void onComplete(@NonNull SVGAVideoEntity svgaVideoEntity) {
                    Log.e(TAG, " playSvga onComplete: " );
                    SVGADrawable drawable=new SVGADrawable(svgaVideoEntity);
                    binding.svgaPlayer.setImageDrawable(drawable);
                    binding.svgaPlayer.startAnimation();
                }

                @Override
                public void onError() {

                }
            },null);
        } catch (MalformedURLException e) {
            Log.e(TAG, " playSvga Exception: "+e.getMessage() );
        }

    }*/

    private void clickListener() {
        binding.upperLay.setOnClickListener(v -> {
            dismiss();
        });

        binding.buyBtn.setOnClickListener(v -> {
            Log.e(TAG, "clickListener: " + SelectedStorePlan.getValidity_in_days());
            Log.e(TAG, "clickListener: buybtn user wallet " + sessionManager.getUserWallet() + "  selected item price  " + SelectedStorePlan.getCoin());
            if (sessionManager.getUserWallet() > SelectedStorePlan.getCoin()) {
                apiManager.buyStoreItem(String.valueOf(SelectedStorePlan.getStore_id()), String.valueOf(storeItemModel.getStore_category_id()), String.valueOf(SelectedStorePlan.getCoin()), String.valueOf(SelectedStorePlan.getValidity_in_days()));
            }
            else {
                new InsufficientCoinsMyaccount(getContext(), 2, sessionManager.getUserWallet());
            }
        });
    }

    private void initValidityRecycler() {
        //  Log.e(TAG, "init: validity_list "+validity_list.size());
        validity_list.addAll(storeItemModel.getStoreplan());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        binding.validityRecycler.setLayoutManager(layoutManager);
        StoreItemPurchaseValidityAdapter adapter = new StoreItemPurchaseValidityAdapter(getContext(), validity_list, this);
        binding.validityRecycler.setAdapter(adapter);
    }



    @Override
    public void isError(String errorCode) {
        Log.e(TAG, "isError: " + errorCode);
    }

    @Override
    public void isSuccess(Object response, int ServiceCode) {

        if (ServiceCode == BUY_STORE_ITEM) {
            BuyStoreItemResponse rsp = (BuyStoreItemResponse) response;
            long result = rsp.getResult();
            new SessionManager(getContext()).setUserWall((int) result);

         //   Log.e(TAG, "isSuccess: "+result );

            Intent intent=new Intent("UPDATE-PURCHASE-LIST");
            intent.putExtra("action","update");
            getContext().sendBroadcast(intent);


            Toast.makeText(getContext(),"Purchased successfully.",Toast.LENGTH_SHORT).show();
            dismiss();


        }


    }

    @Override
    public void onValiditySelect(StorePlanModel storePlanModel) {
        SelectedStorePlan = storePlanModel;
        binding.itemCostInCoin.setText(String.valueOf(storePlanModel.getCoin()));
    }
}
