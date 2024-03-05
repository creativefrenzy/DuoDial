package com.privatepe.host.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.privatepe.host.R;
import com.privatepe.host.databinding.InvitationRewardRulesBinding;

public class DialogInvitationReword extends Dialog {
    Context context;
    InvitationRewardRulesBinding binding;
    public DialogInvitationReword( @NonNull Context context) {
        super(context);
        this.context = context;
        init();

    }
    void init() {

        binding = InvitationRewardRulesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // DialogInvitationRewardRulesAdapter adapter = new DialogInvitationRewardRulesAdapter(context,commissionList);
        //binding.recViewInvitation.setAdapter(adapter);
        // binding.recViewInvitation.setLayoutManager(new LinearLayoutManager(context));
        show();
    }

}
