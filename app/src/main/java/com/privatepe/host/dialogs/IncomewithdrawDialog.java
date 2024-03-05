package com.privatepe.host.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.privatepe.host.R;
import com.privatepe.host.utils.SessionManager;


public class IncomewithdrawDialog extends Dialog {
    int amount;
    Context context;
    SessionManager sessionManager;
    Button btn_done;
    TextView tv_income_withdraw_input;

    public IncomewithdrawDialog(Context context, int amount) {
        super(context);
        this.context = context;
        this.amount = amount;
        init();
    }

    void init() {
        this.setContentView(R.layout.dialog_income_withdraw);
        this.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.setCancelable(false);

        sessionManager = new SessionManager(context);
        // float count = Float.parseFloat(dollar);
        btn_done = findViewById(R.id.btn_go);
        tv_income_withdraw_input = findViewById(R.id.tv_income_withdraw_input);
        if (amount > 3600) {
            tv_income_withdraw_input.setText(String.valueOf(amount));
            tv_income_withdraw_input.setBackground(context.getResources().getDrawable(R.drawable.bg_withdraw_max));
            btn_done.setText("Withdraw");
        } else {
            sessionManager.setCoinCheck(true);
            tv_income_withdraw_input.setText(String.valueOf(amount));
        }

        btn_done.setOnClickListener(view -> {
            sessionManager.setFirstRun(false);

         //   context.startActivity(new Intent(getContext(), IncomeReportActivity.class));
            dismiss();

        });

        show();
    }
}