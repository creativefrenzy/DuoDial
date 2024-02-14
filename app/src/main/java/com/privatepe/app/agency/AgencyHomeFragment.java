package com.privatepe.app.agency;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.privatepe.app.R;
import com.privatepe.app.activity.AgencyCenterActivity;
import com.privatepe.app.activity.AgencyPolicy;
import com.privatepe.app.activity.HostList;
import com.privatepe.app.activity.IncomeReportActivity;
import com.privatepe.app.activity.MyChatPriceActivity;
import com.privatepe.app.activity.SettingActivity;
import com.privatepe.app.activity.SettlementActivity;
import com.privatepe.app.activity.SubAgencyActivity;
import com.privatepe.app.databinding.FragmentAgencyHomeBinding;
import com.privatepe.app.dialogs_agency.PaymentMethod;
import com.privatepe.app.retrofit.ApiManager;
import com.privatepe.app.retrofit.ApiResponseInterface;
import com.privatepe.app.utils.Constant;
import com.privatepe.app.utils.SessionManager;

import java.util.HashMap;

import static com.privatepe.app.utils.SessionManager.NAME;
import static com.privatepe.app.utils.SessionManager.PROFILE_ID;

public class AgencyHomeFragment extends Fragment implements ApiResponseInterface {

    FragmentAgencyHomeBinding binding;
    String authToken;
    ApiManager apiManager;
    SessionManager session;
    PaymentMethod paymentMethod;

    public AgencyHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ((Home) getActivity()).hideStatusBar(getActivity().getWindow(),false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_agency_home, container, false);
        apiManager = new ApiManager(getContext(), this);
        session = new SessionManager(getContext());

        binding.setClickListener(new EventHandler(getContext()));
        HashMap<String, String> data = session.getUserDetails();
        binding.agencyName.setText(data.get(NAME));
        binding.userId.setText("ID : " + data.get(PROFILE_ID));

        Glide.with(getActivity()).load(session.getUserProfilepic()).placeholder(R.drawable.default_profile).into(binding.userImage);
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public class EventHandler {
        Context mContext;

        public EventHandler(Context mContext) {
            this.mContext = mContext;
        }

        public void copy() {
            ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("id", binding.userId.getText().toString());
            clipboard.setPrimaryClip(clip);
            Toast toast = Toast.makeText(getContext(),
                    "Copied", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }

        public void agencyCenter() {
            Intent intent = new Intent(mContext, AgencyCenterActivity.class);
            startActivity(intent);

        }

        public void settlement() {
            Intent intent = new Intent(mContext, SettlementActivity.class);
            startActivity(intent);
        }

        public void agencyPolicy() {
            Intent intent = new Intent(mContext, AgencyPolicy.class);
            startActivity(intent);
        }

        public void hostList() {
            Intent intent = new Intent(mContext, HostList.class);
            startActivity(intent);

        }

        public void sub_Agency() {
            Intent intent = new Intent(mContext, SubAgencyActivity.class);
            startActivity(intent);
        }

        public void showDialog() {
            //   new PaymentMethod(mContext);

            startActivity(new Intent(getContext(), IncomeReportActivity.class));


        }

        public void settingMenu() {
            startActivity(new Intent(getActivity(), SettingActivity.class));
        }


    }

    @Override
    public void isError(String errorCode) {

    }

    @Override
    public void isSuccess(Object response, int ServiceCode) {
        try {
            if (ServiceCode == Constant.PROFILE_DETAILS) {
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}