package com.privatepe.app.fragments.metend;


import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.privatepe.app.R;
import com.privatepe.app.activity.CardActivity;
import com.privatepe.app.activity.EditProfileActivityNew;
import com.privatepe.app.activity.LevelUpActivity;
import com.privatepe.app.activity.MaleWallet;
import com.privatepe.app.activity.PrivacyPolicyActivity;
import com.privatepe.app.activity.SettingActivity;
import com.privatepe.app.databinding.FragmentMyAccountBinding;
import com.privatepe.app.dialogs.AccountInfoDialog;
import com.privatepe.app.dialogs.ComplaintDialog;
import com.privatepe.app.dialogs.DialogInviteMonthlyRank;
import com.privatepe.app.dialogs.InsufficientCoinsMyaccount;
import com.privatepe.app.model.ProfileDetailsResponse;
import com.privatepe.app.model.WalletBalResponse;
import com.privatepe.app.retrofit.ApiManager;
import com.privatepe.app.retrofit.ApiResponseInterface;
import com.privatepe.app.utils.Constant;
import com.privatepe.app.utils.SessionManager;
import com.opensource.svgaplayer.SVGAImageView;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 **/
public class MyAccountFragment extends Fragment implements ApiResponseInterface {

    ApiManager apiManager;
    FragmentMyAccountBinding binding;
    public int onlineStatus;
    String username = "";
    SessionManager sessionManager;
    String guestPassword;
    DatabaseReference chatRef;
    public static String availableCoins;
    private SVGAImageView svgaImageView;

    public MyAccountFragment() {
        // Required empty public constructor
    }

    BroadcastReceiver updateGuestInfo = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String action = intent.getStringExtra("action");
                if (action.equals("update")) {
                    apiManager.getProfileDetails();
                    Log.e("MyAccount", "onReceive: " + "update");
                }

            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_account, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sessionManager = new SessionManager(getContext());
        guestPassword = sessionManager.getGuestPassword();
        binding.setClickListener(new EventHandler(getContext()));


        //   svgaImageView=view.findViewById(R.id.onGoingGiftsvga);

        if (sessionManager.getGender().equals("male")) {
            //binding.incomeReport.setVisibility(View.GONE);
            //binding.onlineStatus.setVisibility(View.GONE);
            //binding.vIn1.setVisibility(View.VISIBLE);
            //binding.delete.setVisibility(View.VISIBLE);
            //binding.myWallet.setVisibility(View.GONE);
        } else {
            // binding.myWallet.setVisibility(View.GONE);
            //binding.purchaseCoins.setVisibility(View.GONE);
            //binding.onlineStatus.setVisibility(View.GONE);
            // binding.vIn1.setVisibility(View.GONE);
        }

        apiManager = new ApiManager(getContext(), this);
        apiManager.getWalletAmount();
        //chatRef = FirebaseDatabase.getInstance().getReference().child("Users");

       /* binding.rlFollowers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).showFollowers();
            }
        });*/


        binding.userIdCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                String[] copyString = binding.userId.getText().toString().split(":");
                ClipData clip = ClipData.newPlainText("id", copyString[1].trim());
                clipboard.setPrimaryClip(clip);
                Toast toast = Toast.makeText(getContext(), "Copied", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
            }
        });

        binding.rlCenterNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.setUserLocation("India");
                binding.rlCenterNew.setEnabled(false);
                new InsufficientCoinsMyaccount(requireActivity(), 2, Long.parseLong(binding.availableCoins.getText().toString()));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        binding.rlCenterNew.setEnabled(true);
                    }
                }, 1000);
            }
        });

        Log.e("MyAccountFragment", "onCreate: " + "called");
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.e("MyAccountFragment", "onResume: " + "called");

        apiManager.getProfileDetails();
        // apiManager.getRechargeList();
        if (new SessionManager(getContext()).getUserAddress().equals("null")) {
            binding.userLocation.setVisibility(View.GONE);
        } else {
            if (binding.userLocation.getVisibility() == View.GONE) {
                binding.userLocation.setVisibility(View.VISIBLE);
            }
            binding.userLocation.setText(new SessionManager(getContext()).getUserAddress());
        }


        try {
            getContext().registerReceiver(updateGuestInfo, new IntentFilter("UPDATE-GUEST-INFO"));
        } catch (Exception e) {
            Log.e("MyAccountFragment", "onResume: Exception " + e.getMessage());
        }


    }

    public class EventHandler {
        Context mContext;

        public EventHandler(Context mContext) {
            this.mContext = mContext;
        }

        public void editProfile() {
          /*  if (new SessionManager(getContext()).getHostAutopickup().equals("yes")) {
                Toast.makeText(getContext(), "Option is disable while in Face detection mode.", Toast.LENGTH_SHORT).show();
            } else {*/
          /*  Intent intent = new Intent(getActivity(), EditProfile.class);
            startActivity(intent);*/


             Intent intent = new Intent(getActivity(), EditProfileActivityNew.class);
             startActivity(intent);

           // Intent intent = new Intent(getActivity(), KotlinTestActivity.class);
           // startActivity(intent);


            // apiManager.getGiftList();


            //       ((MainActivity) getActivity()).reStartEngine();
            // }
        }
        public void Invitation() {
            DialogInviteMonthlyRank dialogInviteMonthlyRank = new DialogInviteMonthlyRank(getContext());
            dialogInviteMonthlyRank.show();
        }

        public void purchaseCoins() {
           /* Intent recharge = new Intent(getActivity(), PurchaseCoins.class);
            startActivity(recharge);*/
           /*if (sessionManager.getGender().equals("male")) {
                ((MainActivity) getActivity()).enableLocationSettings();
            }*/
            binding.rlCoin.setEnabled(false);
            sessionManager.setUserLocation("India");
            new InsufficientCoinsMyaccount(requireActivity(), 2, Long.parseLong(binding.availableCoins.getText().toString()));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    binding.rlCoin.setEnabled(true);
                }
            }, 1000);
        }

        public void myLevel() {
            Intent level = new Intent(getActivity(), LevelUpActivity.class);
            level.putExtra("Profile_Id", uid);
            startActivity(level);
        }

        public void incomeReport() {
           /* Intent income = new Intent(getActivity(), IncomeReport.class);
            startActivity(income);
            ((MainActivity) getActivity()).reStartEngine();*/
            //((MainActivity) getActivity()).showMyEarning();

            if (sessionManager.getGender().equals("male")) {
            } else {

             /*   FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().addToBackStack(null);
                IncomeReportFragment incomeReportFragment = new IncomeReportFragment();
                fragmentTransaction.add(R.id.fragment_view, incomeReportFragment);
                ((MainActivity) getActivity()).active = incomeReportFragment;
                ((MainActivity) getActivity()).hideMenu();
                fragmentTransaction.commit();*/
            }
        }

        public void myCard() {
            Intent intent = new Intent(getActivity(), CardActivity.class);
            startActivity(intent);

        }

        public void maleWallet() {
            Intent my_wallet = new Intent(getActivity(), MaleWallet.class);
            startActivity(my_wallet);
        }

        public void onlineStatus() {
            //  new OnlineStatusDialog(MyAccountFragment.this, onlineStatus);
        }

        public void complaint() {
            new ComplaintDialog(getContext());
        }

        public void viewTicket() {
         /*   Intent my_wallet = new Intent(getActivity(), ViewTicketActivity.class);
            startActivity(my_wallet);*/
        }

        public void privacyPolicy() {
            Intent intent = new Intent(getActivity(), PrivacyPolicyActivity.class);
            startActivity(intent);
        }

        public void changePassword() {
            //  new ChangePasswordDialog(getContext());
        }

        public void accountInfo() {
            new AccountInfoDialog(getContext(), username, guestPassword);
        }

        public void upGraded() {
            // show upgraded dialog here
            // new UpGradedLevelDialog(getContext());
        }

        public void logout() {
            //  new ExitDialog(MyAccountFragment.this);
            logoutDialog();
        }

        public void support() {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:")); // only email apps should handle this
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"zeepliveofficial@gmail.com"});
            intent.putExtra(Intent.EXTRA_SUBJECT, "Regarding Coin User ID " + new SessionManager(getContext()).getUserId());
            if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                getContext().startActivity(intent);
            }
        }

        public void settingMenu() {

         /*   if (new SessionManager(getContext()).getHostAutopickup().equals("yes")) {
                Toast.makeText(getContext(), "Option is disable while in Face detection mode.", Toast.LENGTH_SHORT).show();
            } else {*/


            startActivity(new Intent(getActivity(), SettingActivity.class));

            //   startActivity(new Intent(getActivity(), VideoFilterMainUI.class));


            //} /* ((MainActivity) getActivity()).reStartEngine();*/

           /* if (sessionManager.getGender().equals("male")) {
                startActivity(new Intent(getActivity(), SettingActivity.class));
            } else {
                ((MainActivity) getActivity()).hideMenu();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().addToBackStack(null);
                SettingFragment settingFragment = new SettingFragment();
                fragmentTransaction.add(R.id.fragment_view, settingFragment);
                ((MainActivity) getActivity()).active = settingFragment;
                fragmentTransaction.commit();
            }*/
        }

        public void deleteAccount() {
            //  new ExitDialog(MyAccountFragment.this);
            //  startActivity(new Intent(getActivity(), DeleteActivity.class));
            //  startActivity(new Intent(getActivity(), VideoHistoryActivity.class));
            accountDeleteDialog();
        }
    }

    void logoutDialog() {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_exit);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();

        TextView closeDialog = dialog.findViewById(R.id.close_dialog);
        TextView logout = dialog.findViewById(R.id.logout);

        closeDialog.setOnClickListener(view -> dialog.dismiss());
        logout.setOnClickListener(view -> {
            dialog.dismiss();
            String cName = new SessionManager(getContext()).getUserLocation();
            String eMail = new SessionManager(getContext()).getUserEmail();
            String passWord = new SessionManager(getContext()).getUserPassword();
            new SessionManager(getContext()).logoutUser();
            apiManager.getUserLogout();
            //checkOnlineAvailability(uid, nme, image);
            new SessionManager(getContext()).setUserLocation(cName);
            new SessionManager(getContext()).setUserEmail(eMail);
            new SessionManager(getContext()).setUserPassword(passWord);
            new SessionManager(getContext()).setUserAskpermission();
            //    sessionManager.setUserLocation("null");

            getActivity().finish();
        });
    }

    void accountDeleteDialog() {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_exit);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.show();

        TextView closeDialog = dialog.findViewById(R.id.close_dialog);
        TextView tv_msg = dialog.findViewById(R.id.tv_msg);
        TextView logout = dialog.findViewById(R.id.logout);

        tv_msg.setText("After deleting account, the balance and earning will be unable to use. your personal information will be removed. your account will be removed permanently and unable to be recovered again. Please consider carefully!");
        tv_msg.setGravity(Gravity.START);
        logout.setText("OK");
        closeDialog.setText("Cancel");
        tv_msg.setTextColor(getActivity().getResources().getColor(R.color.black));

        closeDialog.setOnClickListener(view -> dialog.dismiss());

        logout.setOnClickListener(view -> {
            dialog.dismiss();
            String cName = new SessionManager(getContext()).getUserLocation();
            String eMail = new SessionManager(getContext()).getUserEmail();
            String passWord = new SessionManager(getContext()).getUserPassword();
            new SessionManager(getContext()).logoutUser();
            apiManager.getAccountDelete();
            //checkOnlineAvailability(uid, nme, image);
            new SessionManager(getContext()).setUserLocation(cName);
            new SessionManager(getContext()).setUserEmail(eMail);
            new SessionManager(getContext()).setUserPassword(passWord);
            new SessionManager(getContext()).setUserAskpermission();
            //    sessionManager.setUserLocation("null");
            getActivity().finish();
        });
    }

    String uid, nme, image;

    void checkOnlineAvailability(String uid, String name, String image) {
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);

                if (connected) {
                    // Change online status when user comes back on app
                    try {
                        HashMap<String, String> details = new HashMap<>();
                        details.put("uid", uid);
                        details.put("name", name);
                        details.put("image", image);
                        details.put("status", "Offline");
                        chatRef.child(uid).setValue(details);

                    } catch (Exception e) {
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("fireeBase", "Listener was cancelled");
            }
        });
    }


    @Override
    public void isError(String errorCode) {
        Toast.makeText(getContext(), errorCode, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void isSuccess(Object response, int ServiceCode) {
        try {
           /* if (ServiceCode == Constant.GET_GIFT_LIST) {

                ResultGift gift=(ResultGift)response;

                GiftBottomSheetDialog bottomSheet = new GiftBottomSheetDialog(gift);
                bottomSheet.show(getActivity().getSupportFragmentManager(), "ModalBottomSheet");
            } */

            if (ServiceCode == Constant.WALLET_AMOUNT) {
                WalletBalResponse rsp = (WalletBalResponse) response;
                binding.availableCoins.setText(String.valueOf(rsp.getResult().getTotal_point()));
                availableCoins = String.valueOf(rsp.getResult().getTotal_point());

                Log.e("MyAccountFragment", "isSuccess: " + availableCoins);
            }
            if (ServiceCode == Constant.PROFILE_DETAILS) {
                ProfileDetailsResponse rsp = (ProfileDetailsResponse) response;

                if (rsp.getSuccess().getProfile_images() != null && rsp.getSuccess().getProfile_images().size() > 0) {
                    try {
                        if (!rsp.getSuccess().getProfile_images().get(0).getImage_name().equals("")) {
                            Glide.with(getContext()).load(rsp.getSuccess().getProfile_images().get(0).getImage_name())
                                    .circleCrop().placeholder(R.drawable.default_profile).into(binding.userImage);
                        }

                        String img = "";
                        if (rsp.getSuccess().getProfile_images() != null && rsp.getSuccess().getProfile_images().size() > 0) {
                            img = rsp.getSuccess().getProfile_images().get(0).getImage_name();
                            Log.e("PROFILE_PIC", "isSuccess: IMG" + img);
                        }
                        uid = String.valueOf(rsp.getSuccess().getProfile_id());
                        nme = rsp.getSuccess().getName();

                        sessionManager.setUserProfilepic(img);

                        Log.e("PROFILE_PIC", "isSuccess: image" + img);

                        sessionManager.setUserName(nme);


                        Log.e("MyAccountActivity", "isSuccess: " + nme);
                        this.image = img;

                        sessionManager.setHostLevel(String.valueOf(rsp.getSuccess().getRich_level()));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                //binding.followers.setText(String.valueOf(rsp.getSuccess().getFavorite_count()));

                String user_name = rsp.getSuccess().getName();


                if (user_name.length() > 12) {
                    user_name = user_name.substring(0, 11) + "...";
                }

                binding.name.setText(user_name);


                binding.userId.setText("ID : " + rsp.getSuccess().getProfile_id());
                markOnlineStatus(rsp.getSuccess().getIs_online());

                if (rsp.getSuccess().getLogin_type().equals("manualy") && new SessionManager(getContext()).getGender().equals("male")) {
                    // binding.changePassword.setVisibility(View.VISIBLE);
                    // binding.passwordSeprator.setVisibility(View.VISIBLE);
                }

                if (rsp.getSuccess().getUsername().startsWith("guest")) {
                    //binding.changePassword.setVisibility(View.GONE);
                    //    binding.accountInfo.setVisibility(View.VISIBLE);
                    username = rsp.getSuccess().getUsername();
                }

                apiManager.getWalletAmount();
            }
        } catch (Exception e) {
        }
    }


    public void markOnlineStatus(int is_online) {
        onlineStatus = is_online;

        if (is_online == 1) {
            //binding.status.setText("Online");
        } else {
            //binding.status.setText("Offline");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (updateGuestInfo != null) {
                getContext().unregisterReceiver(updateGuestInfo);
            }
        } catch (Exception e) {
            Log.e("MyAccountFragment", "onDestroy: Exception " + e.getMessage());
        }


    }
}