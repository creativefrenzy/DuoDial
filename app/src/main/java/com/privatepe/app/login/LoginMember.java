package com.privatepe.app.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.privatepe.app.R;


public class LoginMember extends AppCompatActivity {
    private EditText edtPhone;

    private Button next;
    //View view = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_member);


        edtPhone = findViewById(R.id.number);
        next = findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edtPhone.getText().toString()) || edtPhone.getText().toString().length()<9) {
                    //new bioDialog(getApplicationContext());
                    Toast.makeText(LoginMember.this, "Please enter a valid phone number.", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(LoginMember.this, OTPVerify.class).putExtra("number", edtPhone.getText().toString()));
                }
                //view = v;
            }
        });
    }

    public void chooseNumber(){
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View viewPopupwindow = layoutInflater.inflate(R.layout.choose_number, null);
        final PopupWindow popupWindow = new PopupWindow(viewPopupwindow, 900, 500, true);

        viewPopupwindow.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }
}