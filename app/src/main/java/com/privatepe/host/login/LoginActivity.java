package com.privatepe.host.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.privatepe.host.R;
import com.privatepe.host.main.Home;
import com.privatepe.host.model.LoginResponse;
import com.privatepe.host.retrofit.ApiManager;
import com.privatepe.host.retrofit.ApiResponseInterface;
import com.privatepe.host.utils.Constant;
import com.privatepe.host.utils.SessionManager;

public class LoginActivity extends AppCompatActivity implements ApiResponseInterface {
    EditText _id, _pass;
    Button login;
    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int AUDIO_PERMISSION_CODE = 101;
    ApiManager apiManager;
    SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        apiManager = new ApiManager(this, this);
        session = new SessionManager(this);
        login = findViewById(R.id.login);
        _id = findViewById(R.id._id);
        _pass = findViewById(R.id._pass);
        checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
        checkPermission(Manifest.permission.RECORD_AUDIO, CAMERA_PERMISSION_CODE);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = _id.getText().toString();
                String pass = _pass.getText().toString();
                if(!_id.getText().toString().equals("") && !_pass.getText().toString().equals("")){
                    //Authentication(id, pass);
                    apiManager.login(id, pass,"hash");
                }
            }
        });
    }
    @Override
    public void isError(String errorCode) {
        Toast.makeText(this, errorCode, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void isSuccess(Object response, int ServiceCode) {
        if (ServiceCode == Constant.LOGIN) {
            LoginResponse rsp = (LoginResponse) response;
            new SessionManager(this).saveGuestStatus(Integer.parseInt(rsp.getAlready_registered()));
            session.createLoginSession(rsp);
            session.setUserEmail(_id.getText().toString());
            session.setUserPassword(_pass.getText().toString());
            Intent intent = new Intent(this, Home.class);
            finishAffinity();
            startActivity(intent);
        }
    }
    /*public void Authentication(String id, String pass){
        //final Authenticate login = new Authenticate(id, pass);
        JsonPlaceHolderApi jsonPlaceHolderApi = RetrofitInstance.getRetrofitInstance().create(JsonPlaceHolderApi.class);
        Call<Login> call = jsonPlaceHolderApi.loginUser(id, pass);
        call.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                response.body().getInfo();
                int code = response.code();
                if (code==404)
                {
                    Toast.makeText(getApplicationContext(), "User Not Found", Toast.LENGTH_LONG).show();
                }
                else if(code == 200) {
                    if(response.body().isSuccess()) {
                        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.shared_preference_login), Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("name", response.body().getInfo().getName());
                        editor.putString("gender", response.body().getInfo().getGender());
                        editor.putInt("is_online", response.body().getInfo().getIs_online());
                        editor.putString("profile_id", response.body().getInfo().getProfile_id());
                        editor.putString("username", response.body().getInfo().getUsername());
                        editor.putString("login_type", response.body().getInfo().getLogin_type());
                        editor.putInt("allow_in_app_purchase", response.body().getInfo().getAllow_in_app_purchase());
                        editor.putString("country", response.body().getInfo().getCountry());
                        editor.putString("user_city", response.body().getInfo().getUser_city());
                        editor.putString("token", response.body().getInfo().getToken());
                        editor.commit();

                        Intent i = new Intent(LoginActivity.this, Home.class);
                        startActivity(i);
                    }
                }
            }
            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Something went wrong...", Toast.LENGTH_LONG).show();
            }
        });
    }*/

    // Function to check and request permission.
    public void checkPermission(String permission, int requestCode)
    {
        if (ContextCompat.checkSelfPermission(LoginActivity.this, permission) == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(LoginActivity.this, new String[] { permission }, requestCode);
        }
        else {
            Toast.makeText(LoginActivity.this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode,
                permissions,
                grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(LoginActivity.this, "Camera Permission Granted", Toast.LENGTH_SHORT) .show();
            }
            else {
                Toast.makeText(LoginActivity.this, "Camera Permission Denied", Toast.LENGTH_SHORT) .show();
            }
        }
        else if (requestCode == AUDIO_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(LoginActivity.this, "Audio Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(LoginActivity.this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}