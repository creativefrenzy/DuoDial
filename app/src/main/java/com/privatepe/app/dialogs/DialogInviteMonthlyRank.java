package com.privatepe.app.dialogs;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.privatepe.app.R;
import com.privatepe.app.databinding.DialogInviteMonthlyRankBinding;
import com.privatepe.app.utils.SessionManager;

public class DialogInviteMonthlyRank extends Dialog {
    Context context;

    DialogInviteMonthlyRankBinding binding;

    TextView text_title;
    String referralUrl = null;
    TextView tv_referUrl;

    RelativeLayout copyLinklay, btn_whatsapp, btn_messenger;


    public DialogInviteMonthlyRank(Context context) {
        super(context);
        this.context = context;
        init();

    }

    //13-19
    void init() {

        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_invite_monthly_rank, null, false);
        setContentView(binding.getRoot());
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        show();

        text_title = findViewById(R.id.text_title);
        copyLinklay = findViewById(R.id.copyLinklay);
        tv_referUrl = findViewById(R.id.tv_referralUrl);
        btn_whatsapp = findViewById(R.id.btn_whatsapp);

        btn_messenger = findViewById(R.id.btn_messenger);
        referralUrl = new SessionManager(getContext()).getReferralUrl();
        if (referralUrl != null) {
            tv_referUrl.setText(referralUrl);
        }

        btn_whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inviteViaWhatsApp(getCurrentFocus());
            }
        });
        btn_messenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inviteViaMessenger(getCurrentFocus());
            }
        });
        copyLinklay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (referralUrl != null) {
                    ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("label", referralUrl);
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(getContext(), "Copied", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void inviteViaWhatsApp(View view) {

        PackageManager pm = getContext().getPackageManager();
        try {

            Intent waIntent = new Intent(Intent.ACTION_SEND);
            waIntent.setType("text/plain");
            String text =context.getResources().getString(R.string.tap_on_my_referral_link)+ referralUrl;

            PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
            //Check if package exists or not. If not then code
            //in catch block will be called
            waIntent.setPackage("com.whatsapp");

            waIntent.putExtra(Intent.EXTRA_TEXT, text);
            context.startActivity(Intent.createChooser(waIntent, "Share with"));

        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(getContext(), "WhatsApp not Installed", Toast.LENGTH_SHORT).show();
        }

    }

    public void inviteViaMessenger(View view) {

        PackageManager pm = getContext().getPackageManager();
        try {

            Intent fbIntent = new Intent(Intent.ACTION_SEND);
            fbIntent.setType("text/plain");
            String text = context.getResources().getString(R.string.tap_on_my_referral_link)+referralUrl;

            PackageInfo info = pm.getPackageInfo("com.facebook.orca", PackageManager.GET_META_DATA);
            //Check if package exists or not. If not then code
            //in catch block will be called
            fbIntent.setPackage("com.facebook.orca");

            fbIntent.putExtra(Intent.EXTRA_TEXT, text);
            context.startActivity(Intent.createChooser(fbIntent, "Share with"));

        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(getContext(), "Facebook Messenger not Installed", Toast.LENGTH_SHORT).show();
        }

    }
}
