package com.privatepe.app.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.privatepe.app.R;
import com.privatepe.app.model.AgencyResponse;
import com.privatepe.app.retrofit.ApiManager;
import com.privatepe.app.retrofit.ApiResponseInterface;
import com.privatepe.app.utils.Constant;
import com.privatepe.app.utils.NetworkCheck;

public class ConfirmAgency extends AppCompatActivity implements ApiResponseInterface {
    private NetworkCheck networkCheck;
    EditText agency_id;
    ApiManager apiManager;
    TextView confirm_agency;
    private String blockCharacterSet = "~#^|$%&!\\/!@#$%^&*(){}_[]|\\?/<>,.:-'';§£¥.+\\ ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_confirm_agency);
        networkCheck = new NetworkCheck();
        agency_id = findViewById(R.id.agency);
        confirm_agency = findViewById(R.id.confirm_agency);
        apiManager = new ApiManager(ConfirmAgency.this, this);


        if (agency_id == null) {
            if (agency_id.getText().toString().equals("")) {
                agency_id.setText("");
            }

        }


        agency_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() == 0 || s.toString().trim().length() < 4) {
                    confirm_agency.setEnabled(false);
                    confirm_agency.setBackground(getApplicationContext().getDrawable(R.drawable.inactive));
                } else {
                    confirm_agency.setBackground(getApplicationContext().getDrawable(R.drawable.active));
                    confirm_agency.setEnabled(true);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });

        InputFilter filter1 = new InputFilter.LengthFilter(15);
        agency_id.setFilters(new InputFilter[]{filter, filter1});

    }

    public InputFilter filter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (source != null && blockCharacterSet.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };

    public void confirm(View view) {
        if (networkCheck.isNetworkAvailable(getApplicationContext())) {
            if (!agency_id.getText().toString().trim().equals("")) {

                SharedPreferences prefs = getSharedPreferences("OTP_DATA", MODE_PRIVATE);
                String token = prefs.getString("token", "");
                apiManager.getAgencyInfo(token, agency_id.getText().toString());
            } else {
                Toast.makeText(ConfirmAgency.this, "Empty or Invalid Agency ID", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Check your connection.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void isError(String errorCode) {
        Toast.makeText(ConfirmAgency.this, errorCode, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void isSuccess(Object response, int ServiceCode) {
        if (ServiceCode == Constant.CHECK_AGENCY) {
            AgencyResponse agencyResponse = (AgencyResponse) response;
            if (!agencyResponse.getResult().equals("Invalid Agency Id")) {
                SharedPreferences.Editor editor = getSharedPreferences("AGENCY_DATA", MODE_PRIVATE).edit();
                editor.clear();
                editor.putString("agency", agency_id.getText().toString());
                editor.putString("agency_name", agencyResponse.getResult().getName());
                editor.apply();
                startActivity(new Intent(getApplicationContext(), BasicInformation.class));
            } else {
                //  Toast.makeText(ConfirmAgency.this, "Invalid Agency", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (agency_id == null) {
            if (agency_id.getText().toString().equals("")) {
                agency_id.setText("");
            }

        }
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}