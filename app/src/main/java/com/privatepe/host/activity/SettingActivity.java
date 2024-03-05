package com.privatepe.host.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.privatepe.host.R;
import com.privatepe.host.databinding.ActivitySettingBinding;
import com.privatepe.host.model.ProfileDetailsResponse;
import com.privatepe.host.retrofit.ApiManager;
import com.privatepe.host.retrofit.ApiResponseInterface;

import com.privatepe.host.utils.BaseActivity;
import com.privatepe.host.utils.SessionManager;

import java.io.File;

public class SettingActivity extends BaseActivity implements ApiResponseInterface {
    ActivitySettingBinding binding;
    String username = "";
    String guestPassword;
    SessionManager sessionManager;
    ApiManager apiManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        hideStatusBar(getWindow(), true);
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting);
        binding.settingBack.setOnClickListener(view -> onBackPressed());
        sessionManager = new SessionManager(this);
        guestPassword = sessionManager.getGuestPassword();
        apiManager = new ApiManager(this, this);
        apiManager.getProfileDetails();

        binding.setClickListener(new EventHandler(this));
        initializeCache();

        // apiManager.getUserLogout();


        if (sessionManager.getGender().equals("male")) {
            //binding.changePassword.setVisibility(View.VISIBLE);
            binding.rlSupport.setVisibility(View.VISIBLE);
            // binding.accountInfo.setVisibility(View.VISIBLE);
            binding.vTicket.setVisibility(View.VISIBLE);
            binding.deleteAccount.setVisibility(View.GONE);
        } else {
            binding.changePassword.setVisibility(View.GONE);
            binding.rlSupport.setVisibility(View.GONE);
            binding.vSupport.setVisibility(View.GONE);
            binding.deleteAccount.setVisibility(View.GONE);
            // binding.accountInfo.setVisibility(View.GONE);
        }

        try {
            String versionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            binding.currentAppVersion.setText("v" + versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


    }


    public void logout() {
        String cName = new SessionManager(SettingActivity.this).getUserLocation();
        String eMail = new SessionManager(SettingActivity.this).getUserEmail();
        String passWord = new SessionManager(SettingActivity.this).getUserPassword();
        new SessionManager(SettingActivity.this).logoutUser();
        apiManager.getUserLogout();
        new SessionManager(SettingActivity.this).setUserLocation(cName);
        new SessionManager(SettingActivity.this).setUserEmail(eMail);
        new SessionManager(SettingActivity.this).setUserPassword(passWord);
        new SessionManager(SettingActivity.this).setUserAskpermission();

        if (new SessionManager(getApplicationContext()).getHostAutopickup().equals("yes")) {
            Intent myIntent = new Intent("KAL-CLOSEME");
            myIntent.putExtra("action", "closeme");
            this.sendBroadcast(myIntent);
        }
        finishAffinity();

    }


    public class EventHandler {
        Context mContext;

        public EventHandler(Context mContext) {
            this.mContext = mContext;
        }

        public void complaint() {
            // new ComplaintDialog(mContext);
        }

        public void viewTicket() {
            /*Intent my_wallet = new Intent(mContext, ViewTicketActivity.class);
            startActivity(my_wallet);*/
        }

        public void aboutUS() {
        }

        public void rateZeeplive() {
        }

        public void clear_cache() {
            clearDialog();
        }

        public void support() {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:")); // only email apps should handle this
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"zeepliveofficial@gmail.com"});
            intent.putExtra(Intent.EXTRA_SUBJECT, "Regarding Coin User ID " + new SessionManager(getApplicationContext()).getUserId());
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }

        public void privacyPolicy() {
            Intent intent = new Intent(mContext, PrivacyPolicyActivity.class);
            intent.putExtra("Policy", "Privacy Policy");
            startActivity(intent);
        }

        public void user_agreement() {
            Intent intent = new Intent(mContext, PrivacyPolicyActivity.class);
            intent.putExtra("Policy", "UserAgreement");
            startActivity(intent);
        }


        public void changePassword() {
            // new ChangePasswordDialog(mContext);
        }

        public void accountInfo() {
            //new AccountInfoDialog(mContext, username, guestPassword);
        }

        public void logout() {
            logoutDialog();
        }

        public void deleteAccount() {
            accountDeleteDialog();
        }

    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void clearDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_exit);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.show();

        TextView closeDialog = dialog.findViewById(R.id.close_dialog);
        TextView tv_msg = dialog.findViewById(R.id.tv_msg);
        TextView logout = dialog.findViewById(R.id.logout);

        tv_msg.setText("Do you want to clear Cache?");
        tv_msg.setGravity(Gravity.START);
        logout.setText("Yes");
        closeDialog.setText("No");
        tv_msg.setTextColor(getResources().getColor(R.color.black));

        closeDialog.setOnClickListener(view -> dialog.dismiss());

        logout.setOnClickListener(view -> {
            dialog.dismiss();
            deleteCache(getApplicationContext());
            initializeCache();
        });
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    private void initializeCache() {
        long size = 0;
        size += getDirSize(this.getCacheDir());
        size += getDirSize(this.getExternalCacheDir());
        binding.tvCacheSize.setText(formatSize(size));
    }

    public long getDirSize(File dir) {
        long size = 0;
        for (File file : dir.listFiles()) {
            if (file != null && file.isDirectory()) {
                size += getDirSize(file);
            } else if (file != null && file.isFile()) {
                size += file.length();
            }
        }
        return size;
    }

    public static String formatSize(long v) {
        if (v < 1024) return v + " B";
        int z = (63 - Long.numberOfLeadingZeros(v)) / 10;
        return String.format("%.1f %sB", (double) v / (1L << (z * 10)), " KMGTPE".charAt(z));
    }

    void logoutDialog() {
        Dialog dialog = new Dialog(SettingActivity.this);
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
            String cName = new SessionManager(SettingActivity.this).getUserLocation();
            String eMail = new SessionManager(SettingActivity.this).getUserEmail();
            String passWord = new SessionManager(SettingActivity.this).getUserPassword();
            new SessionManager(SettingActivity.this).logoutUser();
            apiManager.getUserLogout();
            new SessionManager(SettingActivity.this).setUserLocation(cName);
            new SessionManager(SettingActivity.this).setUserEmail(eMail);
            new SessionManager(SettingActivity.this).setUserPassword(passWord);
            new SessionManager(SettingActivity.this).setUserAskpermission();

            if (new SessionManager(getApplicationContext()).getHostAutopickup().equals("yes")) {
                Intent myIntent = new Intent("KAL-CLOSEME");
                myIntent.putExtra("action", "closeme");
                this.sendBroadcast(myIntent);
            }
            finishAffinity();
            //finish();
        });
    }

    void accountDeleteDialog() {
        Dialog dialog = new Dialog(this);
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
        tv_msg.setTextColor(getResources().getColor(R.color.black));

        closeDialog.setOnClickListener(view -> dialog.dismiss());

        logout.setOnClickListener(view -> {
            dialog.dismiss();
            if (new SessionManager(getApplicationContext()).getHostAutopickup().equals("yes")) {
                Intent myIntent = new Intent("KAL-CLOSEME");
                myIntent.putExtra("action", "closeme");
                this.sendBroadcast(myIntent);
            }
            String cName = new SessionManager(SettingActivity.this).getUserLocation();
            String eMail = new SessionManager(SettingActivity.this).getUserEmail();
            String passWord = new SessionManager(SettingActivity.this).getUserPassword();
            new SessionManager(getApplicationContext()).logoutUser();
            apiManager.getAccountDelete();
            new SessionManager(SettingActivity.this).setUserLocation(cName);
            new SessionManager(SettingActivity.this).setUserEmail(eMail);
            new SessionManager(SettingActivity.this).setUserPassword(passWord);
            new SessionManager(SettingActivity.this).setUserAskpermission();


            finish();
        });
    }

    @Override
    public void isError(String errorCode) {
        Toast.makeText(SettingActivity.this, errorCode, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void isSuccess(Object response, int ServiceCode) {
        ProfileDetailsResponse rsp = (ProfileDetailsResponse) response;
        try {
            if (rsp.getSuccess().getLogin_type().equals("manualy") && new SessionManager(getApplicationContext()).getGender().equals("male")) {
                //   binding.changePassword.setVisibility(View.VISIBLE);
            }
            if (rsp.getSuccess().getUsername().startsWith("guest")) {
                //     binding.changePassword.setVisibility(View.GONE);
                //     binding.accountInfo.setVisibility(View.VISIBLE);
                username = rsp.getSuccess().getAndroid_id();
            }
            if (rsp.getSuccess().getUsername().startsWith("female")) {
                username = rsp.getSuccess().getAndroid_id();
            }
        } catch (Exception e) {

        }
    }
}