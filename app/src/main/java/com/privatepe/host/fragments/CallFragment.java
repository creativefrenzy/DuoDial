package com.privatepe.host.fragments;


import static com.privatepe.host.main.Home.cardView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.privatepe.host.R;
import com.privatepe.host.model.Authenticate;
import com.privatepe.host.retrofit.ApiInterface;
import com.privatepe.host.retrofit.RetrofitInstance;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallFragment extends Fragment{
    private String appId = "";
    private String AppID;

    // Fill the channel name.
    private String channelName = "";
    // Fill the temp token generated on Agora Console.
    private String token = "";


    private int is_online, allow_in_app_purchase;
    private String username, receiver_id, unique_id, is_free_call, call_type;

    FrameLayout frameLayoutLocal, frameLayoutRemote;
    private static final int PERMISSION_REQ_ID = 22;

    private static final String[] REQUESTED_PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA
    };

    private boolean checkSelfPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(getContext(), permission) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), REQUESTED_PERMISSIONS, requestCode);
            return false;
        }
        return true;
    }

    public CallFragment(){
        // require a empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.home_fragment, container, false);

        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        cardView.setVisibility(View.VISIBLE);
        frameLayoutLocal = v.findViewById(R.id.local_video_view_container);
        frameLayoutRemote = v.findViewById(R.id.remote_video_view_container);
        appId = getString(R.string.app_id);

        username= getArguments().getString("username");
        receiver_id = getArguments().getString("receiver_id");
        unique_id = getArguments().getString("unique_id");
        channelName = getArguments().getString("channel_name");
        token = getArguments().getString("token");
        is_free_call = getArguments().getString("is_free_call");
        call_type = getArguments().getString("call_type");

        if (checkSelfPermission(REQUESTED_PERMISSIONS[0], PERMISSION_REQ_ID) &&
                checkSelfPermission(REQUESTED_PERMISSIONS[1], PERMISSION_REQ_ID)) {

        }
        return v;
    }

    public void coin_per_second_user(){
        final Authenticate authenticate = new Authenticate(receiver_id, unique_id, call_type, false);
        ApiInterface apiInterface = RetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.coin_per_second_user_busy(authenticate, "Bearer "+token);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                /*response.body().toString();
                int code = response.code();
                if(code == 200){}*/
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //Toast.makeText(getContext(), "Something went wrong...", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void onDestroy() {
        super.onDestroy();
        coin_per_second_user();

    }
}