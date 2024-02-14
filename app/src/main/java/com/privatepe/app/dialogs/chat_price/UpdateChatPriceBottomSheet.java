package com.privatepe.app.dialogs.chat_price;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.privatepe.app.R;
import com.privatepe.app.activity.MyChatPriceActivity;
import com.privatepe.app.adapter.chat_price.UpdateChatPriceListAdapter;
import com.privatepe.app.databinding.UpdateChatPriceDialogBinding;
import com.privatepe.app.response.chat_price.PriceDataModel;
import com.privatepe.app.utils.SessionManager;

import java.util.ArrayList;

public class UpdateChatPriceBottomSheet extends BottomSheetDialogFragment {
    private UpdateChatPriceDialogBinding binding;
    private MyChatPriceActivity myChatPriceActivity;
    private LinearLayoutManager layoutManager;
    private UpdateChatPriceListAdapter adapter;
    private int SelectedChatPrice;
    private SessionManager sessionManager;
    private ArrayList<PriceDataModel> chatpricelist=new ArrayList<>();


    public UpdateChatPriceBottomSheet(MyChatPriceActivity myChatPriceActivity) {
        this.myChatPriceActivity=myChatPriceActivity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.update_chat_price_dialog, null, false);
        getActivity().getWindow().setBackgroundDrawable(new ColorDrawable());
        sessionManager=new SessionManager(getContext());
        chatpricelist= (ArrayList<PriceDataModel>) sessionManager.getChatPriceListResponse().getResult();
        SelectedChatPrice=sessionManager.getSelectedCallPrice();

        layoutManager=new LinearLayoutManager(getContext());
        adapter=new UpdateChatPriceListAdapter(chatpricelist, SelectedChatPrice,myChatPriceActivity);
        binding.updateCallRateRecycler.setLayoutManager(layoutManager);
        binding.updateCallRateRecycler.setAdapter(adapter);
        return binding.getRoot();
    }


}
