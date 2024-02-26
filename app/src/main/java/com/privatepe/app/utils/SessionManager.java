package com.privatepe.app.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.privatepe.app.AppsFlyerPackage.AppsFlyerEvent;
import com.privatepe.app.activity.MainActivity;
import com.privatepe.app.activity.SocialLogin;
import com.privatepe.app.activity.addalbum.AddAlbumActivity;
import com.privatepe.app.activity.addalbum.AuditionVideoActivity;
import com.privatepe.app.activity.addalbum.ShotVideoActivity;
import com.privatepe.app.login.OTPVerify;
import com.privatepe.app.main.Home;
import com.privatepe.app.model.EndCallData.EndCallData;
import com.privatepe.app.model.LoginResponse;
import com.privatepe.app.response.chat_price.PriceListResponse;
import com.privatepe.app.response.metend.RechargePlan.RechargePlanResponseNew;
import com.privatepe.app.response.metend.store_list.StoreResponse;
import com.privatepe.app.response.newgiftresponse.NewGift;
import com.privatepe.app.response.newgiftresponse.NewGiftListResponse;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class SessionManager {


    // Shared Preferences
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "PrivatePe";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    private static final String IS_RTM_LOGGED_IN = "isRTMLogin";

    public static final String PROFILE_ID = "profile_id";
    public static final String TOKEN_ID = "token_id";
    public static final String NAME = "name";
    public static final String GENDER = "gender";
    public static final String IS_ONLINE = "is_online";
    public static final String IS_GUEST_STATUS = "is_guest_status_data";

    public static final String PROFILE_PIC = "profile_pic";
    public static final String USER_LEVEL = "user_level";
    public static final String USER_DOB = "user_dob";
    public static final String USER_AGE = "userage";
    public static final String PIC_BASE_URL = "pic_base_url";

    public static final String ACCOUNT_VERIFIED = "account_verified";
    public static final String FCM_TOKEN = "fcm_token";
    public static final String CURRENT_RECEIVER = "current_receiver";

    public static final String LOGIN_TYPE = "login_type";
    public static final String IS_CONSENT_SEEN = "is_consent_seen";

    public static final String GUEST_PASSWORD = "guest_password";

    public static final String LANG_STATE = "language_State";
    public static final String FASTMODE_STATE = "fastmode_State";
    public static final String ONLINE_STATE = "online_State";
    public static final String ONLINE_STATEBACK = "online_Stateback";
    public static final String USER_LOCATION = "user_location";
    public static final String USER_ADDRESS = "user_address";
    public static final String USER_FACEBOOK_NAME = "user_facebook_name";


    public static final String USER_Email = "user_Email";
    public static final String USER_Password = "user_Password";
    public static final String RECENT_CHAT_LIST_UPDATE = "recent_chat_list_update";

    public static final String USER_WALLET = "user_Wall";

    public static final String USER_STRIPEK = "user_Stripek";
    public static final String USER_STRIPES = "user_Stripes";

    public static final String USER_RAZK = "user_Razk";
    public static final String USER_RAZS = "user_Razs";

    public static final String USER_ENDCALLDATA = "user_EndCallData";
    public static final String USER_GETENDCALLDATA = "user_GETEndCallData";

    public static final String USER_BROADTOKEN = "user_broadtoken";
    public static final String USER_BROADTYPE = "user_broadtype";
    public static final String USER_BROADID = "user_broadid";

    public static final String USER_LOADDATA = "userdataforloading";
    public static final String USER_ASKPERMISSION = "user_askpermission";
    public static final String HOST_AUTOPICKUP = "host_autopickup";
    public static final String ROLE = "role";
    public static final String LANGUAGE = "language";
    public static final String BIO = "bio";
    public static final String IS_FIRST_RUN = "is_first_run";
    public static final String COIN_CHECK = "coin_check";


    private static final String IS_WORKED_ON = "isWorkedOn";

    private static final String IS_FIRST_LOGIN = "isFirstLogin";
    public static final String LOGINTIME = "login_time";

    public static final String SYSTEM_MESSAGE_COUNTER = "system_message_counter";
    public static final String USER_NEW_PHOTO = "user_new_photo";

    public static final String CATEGORY_GIFT_LIST_RESPONSE = "gift_category_list";
    //    public static final String CATEGORY_GIFT_EMPLOYEE_LIST_RESPONSE = "gift_category_employee_list";
    public static final String CATEGORY_GIFT_ALL_EMPLOYEE_LIST_RESPONSE = "gift_all_category_employee_list";

    public static final String VIDEO_STATUS_LIST_SIZE = "video_status_list_size";


    public static final String FirstTimeLogin = "first_time_login";

    public static final String CHAT_PRICE_LIST_RESPONSE = "chat_price_list_response";
    public static final String SELECTED_CHAT_PRICE = "selected_chat_price";
    public static final String HOST_LEVEL = "host_level";
    public static final String RES_UPLOAD = "res_upload";


    // Constructor
    public SessionManager(Context context) {
        try {
            this._context = context;
            pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
            editor = pref.edit();
        } catch (Exception e) {
        }
    }


    public void logoutUser() {


        // Logout from Google
        signoutFromGoogle();

        // Clear all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Login Activity
        //Intent i = new Intent(_context, OTPVerify.class);
        Intent i = new Intent(_context, SocialLogin.class);

        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);
    }

    public void createLoginSession(LoginResponse result) {
        //    Log.e("inogin", new Gson().toJson(result));

        // setFirstLogin(true);

        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
        editor.putBoolean(IS_RTM_LOGGED_IN, false);
        editor.putString(TOKEN_ID, result.getResult().getToken());
        editor.putString(NAME, result.getResult().getName());
        editor.putString(GENDER, result.getResult().getGender());
        //Log.e("gengerStatus", result.getResult().getGender());
        editor.putInt(IS_ONLINE, result.getResult().getIs_online());
        editor.putString(PROFILE_ID, result.getResult().getProfile_id());
        editor.putString(USER_LOCATION, result.getResult().getCountry());
        editor.putString(USER_ADDRESS, result.getResult().getUser_city());
        editor.putString(ROLE, String.valueOf(result.getResult().getRole()));
        //editor.putString(USER_LEVEL, result.getResult().getU)

        // commit changes
        editor.commit();
        AppsFlyerLoginEvent();

    }
    private void AppsFlyerLoginEvent() {
        // This will trigger the Login Event Once when the user is logged in.
        AppsFlyerEvent appsFlyerManager = AppsFlyerEvent.getInstance(_context.getApplicationContext());
        appsFlyerManager.trackLogin();
    }

   /* public void setFirstLogin(boolean b) {
        editor.putBoolean(IS_FIRST_LOGIN,b );
        editor.commit();
    }

    public boolean getFirstLogin() {
        return pref.getBoolean(IS_FIRST_LOGIN, false);
    }
*/


    public void saveGuestPassword(String password) {
        editor.putString(GUEST_PASSWORD, password);
        editor.commit();
    }

    public String getGuestPassword() {
        return pref.getString(GUEST_PASSWORD, null);
    }

    public String getUserToken() {
        return pref.getString(TOKEN_ID, null);
    }

    public int isOnline() {
        return pref.getInt(IS_ONLINE, 0);
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     */
    public void checkLogin() {
        // Check login status
        if (this.isLoggedIn()) {
            if (this.getCheckLoginTypeUser().equals("google") || this.getCheckLoginTypeUser().equals("guest")) {
                Intent i = new Intent(_context, MainActivity.class);
                //   Intent i = new Intent(_context, ConfirmAgency.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                _context.startActivity(i);
            } else {

                String resControl = getResUpload();
                Intent i;

                switch (resControl) {
                    case "0":
                        i = new Intent(_context, AddAlbumActivity.class);
                        break;
                    case "1":
                        i = new Intent(_context, ShotVideoActivity.class);
                        break;
                    case "2":
                        i = new Intent(_context, AuditionVideoActivity.class); break;
                    default:
                        i = new Intent(_context, Home.class);
                }

                //Intent i = new Intent(_context, OTPVerify.class);
                //   Intent i = new Intent(_context, ConfirmAgency.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                _context.startActivity(i);
            }
        } else {
            //Intent i = new Intent(_context, OTPVerify.class);
            Intent i = new Intent(_context, SocialLogin.class);
            _context.startActivity(i);
        }
    }

    public void saveFcmToken(String token) {
        editor.putString(FCM_TOKEN, token);
        editor.commit();
    }

    public String getFcmToken() {
        return pref.getString(FCM_TOKEN, null);
    }

    /**
     * Get stored session data
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(TOKEN_ID, pref.getString(TOKEN_ID, null));
        user.put(NAME, pref.getString(NAME, null));
        user.put(GENDER, pref.getString(GENDER, null));
        user.put(PROFILE_ID, pref.getString(PROFILE_ID, null));

        // return user
        return user;
    }


    public String getGender() {
        return pref.getString(GENDER, null);
    }

    /**
     * Clear session details
     */


   /* public void resetFcmToken() {
        new Thread(() -> {
            try {
                FirebaseInstanceId.getInstance().deleteInstanceId();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }*/

    void signoutFromGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.
                Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                build();

        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(_context, gso);
        googleSignInClient.signOut();
    }

    /**
     * Quick check for login
     **/
    // Get Login State
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }


    public boolean getConsent() {
        return pref.getBoolean(IS_CONSENT_SEEN, false);
    }

    public void setConsent(boolean status) {
        editor.putBoolean(IS_CONSENT_SEEN, status);
        editor.apply();
    }


    public void saveCurrentReceiver(String id) {
        editor.putString(CURRENT_RECEIVER, id);
        editor.apply();
    }

    public String getCurrentReceiver() {
        return pref.getString(CURRENT_RECEIVER, "none");
    }

    public void setLangState(int id) {
        editor.putInt(LANG_STATE, id);
        editor.apply();
    }

    public int gettLangState() {
        return pref.getInt(LANG_STATE, 0);
    }

    public void setFastModeState(int id) {
        editor.putInt(FASTMODE_STATE, id);
        editor.apply();
    }

    public void setLanguage(String id) {
        editor.putString(LANGUAGE, id);
        editor.apply();
    }

    public String getLanguage() {
        return pref.getString(LANGUAGE, "");
    }

    public void setBio(String id) {
        editor.putString(BIO, id);
        editor.apply();
    }

    public String getBio() {
        return pref.getString(BIO, "");
    }

    public int getFastModeState() {
        return pref.getInt(FASTMODE_STATE, 0);
    }

    public void setOnlineState(int id) {
        editor.putInt(ONLINE_STATE, id);
        editor.apply();
    }

    public int getOnlineState() {
        return pref.getInt(ONLINE_STATE, 0);
    }

    public void saveGuestStatus(int id) {
        editor.putInt(IS_GUEST_STATUS, id);
        editor.apply();
    }

    public int getGuestStatus() {
        return pref.getInt(IS_GUEST_STATUS, 0);
    }


    public String getUserId() {
        return pref.getString(PROFILE_ID, "");
    }

    public String getUserName() {
        return pref.getString(NAME, "");
    }


    public void setUserProfilepic(String profileUrl) {
        try {
            editor.putString(PROFILE_PIC, profileUrl);
            editor.commit();
        } catch (Exception E) {
        }
    }

    public void setUserLevel(String level) {
        try {
            editor.putString(USER_LEVEL, level);
            editor.commit();
        } catch (Exception E) {
        }
    }

    public String getUserLevel() {
        return pref.getString(USER_LEVEL, "");
    }

    public void setUserAge(String age) {
        try {
            editor.putString(USER_DOB, age);
            editor.commit();
        } catch (Exception E) {
        }
    }

    public String getUserAge() {
        return pref.getString(USER_DOB, "");
    }


    public void setUserDob(String age) {
        try {
            editor.putString(USER_AGE, age);
            editor.commit();
        } catch (Exception E) {
        }
    }

    public String getUserDob() {
        return pref.getString(USER_AGE, "");
    }


    public void setUserName(String name) {
        try {
            editor.putString(NAME, name);
            editor.commit();
        } catch (Exception E) {
        }
    }

    public String getName() {
        return pref.getString(NAME, "");
    }

    public String getRole() {
        return pref.getString(ROLE, "");
    }

    public String getUserProfilepic() {
        return pref.getString(PROFILE_PIC, "");
    }

    public void setOnlineFromBack(int id) {
        editor.putInt(ONLINE_STATEBACK, id);
        editor.apply();
    }

    public int getOnlineFromBack() {
        return pref.getInt(ONLINE_STATEBACK, 0);
    }

    public void setUserLocation(String c_name) {
        editor.putString(USER_LOCATION, c_name);
        editor.apply();
    }

    public String getUserLocation() {
        return pref.getString(USER_LOCATION, "null");
    }

    public void setUserAddress(String address) {
        editor.putString(USER_ADDRESS, address);
        editor.apply();
    }

    public String getUserAddress() {
        return pref.getString(USER_ADDRESS, "null");
    }

    public void setUserEmail(String c_name) {
        editor.putString(USER_Email, c_name);
        editor.apply();
    }

    public String getUserEmail() {
        return pref.getString(USER_Email, "null");
    }


    public void setUserFacebookName(String f_name) {
        editor.putString(USER_FACEBOOK_NAME, f_name);
        editor.apply();
    }

    public String getUserFacebookName() {
        return pref.getString(USER_FACEBOOK_NAME, "null");
    }


    public void setUserPassword(String c_name) {
        editor.putString(USER_Password, c_name);
        editor.apply();
    }

    public void isRecentChatListUpdateNeeded(boolean status) {
        editor.putBoolean(RECENT_CHAT_LIST_UPDATE, status);
        editor.apply();
    }

    public boolean getRecentChatListUpdateStatus() {
        return pref.getBoolean(RECENT_CHAT_LIST_UPDATE, false);
    }

    public String getUserPassword() {
        return pref.getString(USER_Password, "null");
    }

    public void setUserWall(int u_wall) {
        editor.putInt(USER_WALLET, u_wall);
        editor.apply();
    }

    public int getUserWallet() {
        return pref.getInt(USER_WALLET, 0);
    }

    public void setUserStriepKS(String u_StripeK, String u_StripeS) {
        editor.putString(USER_STRIPEK, u_StripeK);
        editor.putString(USER_STRIPES, u_StripeS);
        editor.apply();
    }

    public void setUserRazKS(String u_RAZK, String u_RAZS) {
        editor.putString(USER_RAZK, u_RAZK);
        editor.putString(USER_RAZS, u_RAZS);
        editor.apply();
    }

    public String getUserStriepK() {
        return pref.getString(USER_STRIPEK, "null");
    }

    public String getUserStriepS() {
        return pref.getString(USER_STRIPES, "null");
    }

    public String getUserRazK() {
        return pref.getString(USER_RAZK, "null");
    }

    public String getUserRazs() {
        return pref.getString(USER_RAZS, "null");
    }


    //end call
    public void setUserEndcalldata(ArrayList<EndCallData> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(USER_ENDCALLDATA, json);
        editor.apply();
    }

    public ArrayList<EndCallData> getUserEndcalldata() {
        Gson gson = new Gson();
        String json = pref.getString(USER_ENDCALLDATA, null);
        Type type = new TypeToken<ArrayList<EndCallData>>() {
        }.getType();
        return gson.fromJson(json, type);
    }


    public void setUserGetendcalldata(String U_GETENDCALLDATA) {
        editor.putString(USER_GETENDCALLDATA, U_GETENDCALLDATA);
        editor.apply();
    }

    public String getUserGetendcalldata() {
        return pref.getString(USER_GETENDCALLDATA, "null");
    }

    public void setUserBroadtoken(String U_GETBTOKEN) {
        editor.putString(USER_BROADTOKEN, U_GETBTOKEN);
        editor.apply();
    }

    public String getUserBroadtoken() {
        return pref.getString(USER_BROADTOKEN, "null");
    }

    public void setUserBroadtype(String U_GETBTYPE) {
        editor.putString(USER_BROADTYPE, U_GETBTYPE);
        editor.apply();
    }

    public String getUserBroadtype() {
        return pref.getString(USER_BROADTYPE, "null");
    }

    public void setUserBroadid(String U_GETBID) {
        editor.putString(USER_BROADID, U_GETBID);
        editor.apply();
    }

    public String getUserBroadid() {
        return pref.getString(USER_BROADID, "null");
    }

    public void setUserLoaddata() {
        editor.putString(USER_LOADDATA, "yes");
        editor.apply();
    }

    public void setUserLoaddataNo() {
        editor.putString(USER_LOADDATA, "no");
        editor.apply();
    }


    public String getUserLoaddata() {
        return pref.getString(USER_LOADDATA, "null");
    }

    public void setUserAskpermission() {
        editor.putString(USER_ASKPERMISSION, "no");
        editor.apply();
    }


    public String getUserAskpermission() {
        return pref.getString(USER_ASKPERMISSION, "null");
    }

    public boolean getFirstRun() {
        return pref.getBoolean(IS_FIRST_RUN, true);
    }

    public void setFirstRun(boolean run) {
        editor.putBoolean(IS_FIRST_RUN, run);
        editor.apply();
    }

    public long getLoginTime() {
        return pref.getLong(LOGINTIME, 0);
    }

    public void setLoginTime(long time) {
        editor.putLong(LOGINTIME, time);
        editor.apply();
    }

    public boolean getCoinCheck() {
        return pref.getBoolean(COIN_CHECK, true);
    }

    public void setCoinCheck(boolean check) {
        editor.putBoolean(COIN_CHECK, check);
        editor.apply();
    }


    public void setHostAutopickup(String param) {
        editor.putString(HOST_AUTOPICKUP, param);
        editor.apply();
    }

    public String getHostAutopickup() {
        return pref.getString(HOST_AUTOPICKUP, "null");
    }

    public void setIsRtmLoggedIn(boolean val) {
        editor.putBoolean(IS_RTM_LOGGED_IN, val);
        editor.commit();
    }

    public Boolean getIsRtmLoggedIn() {
        return pref.getBoolean(IS_RTM_LOGGED_IN, false);
    }


    public Boolean getWorkSession() {

        return pref.getBoolean(IS_WORKED_ON, false);

    }

    public void setWorkSession(boolean isWorkOn) {
        editor.putBoolean(IS_WORKED_ON, isWorkOn);
        editor.apply();

    }


    public void setSystemMessageCounter(int counter) {
        editor.putInt(SYSTEM_MESSAGE_COUNTER, counter);
        editor.apply();
    }

    public int getSystemMessageCounter() {
        return pref.getInt(SYSTEM_MESSAGE_COUNTER, 0);
    }

    public void setUserNewPhoto(String param) {
        editor.putString(USER_NEW_PHOTO, param);
        editor.apply();
    }

    public String getUserNewPhoto() {
        return pref.getString(USER_NEW_PHOTO, "null");
    }


    public void setCategoryGiftList(NewGiftListResponse newGiftListResponse) {
        String data = new Gson().toJson(newGiftListResponse);
        Log.e("CATEGORY_GIFT_LIST", "setCategoryGiftList: " + data);
        editor.putString(CATEGORY_GIFT_LIST_RESPONSE, data);
        editor.apply();
    }

    public NewGiftListResponse getCategoryGiftList() {
        String data = pref.getString(CATEGORY_GIFT_LIST_RESPONSE, null);
        NewGiftListResponse newGiftListResponse = new Gson().fromJson(data, NewGiftListResponse.class);
        return newGiftListResponse;
    }

    public void setEmployeeAllGiftList(HashMap<Integer, NewGift> list) {
        String data = new Gson().toJson(list);
        Log.e("EMPLOYEE_GIFT_LIST", "setEmployeeGiftList: " + data);
        editor.putString(CATEGORY_GIFT_ALL_EMPLOYEE_LIST_RESPONSE, data);
        editor.apply();
    }

    public HashMap<Integer, NewGift> getEmployeeAllGiftList() {
        Gson gson = new Gson();
        String json = pref.getString(CATEGORY_GIFT_ALL_EMPLOYEE_LIST_RESPONSE, null);
        Type type = new TypeToken<HashMap<Integer, NewGift>>() {
        }.getType();
        return gson.fromJson(json, type);
    }


    public void setVideoStatusListSize(String size) {
        editor.putString(VIDEO_STATUS_LIST_SIZE, size);
        editor.apply();
    }

    public String getStatusVideoListSize() {
        String data = pref.getString(VIDEO_STATUS_LIST_SIZE, null);
        return data;
    }


    public static final String LOGINCOMPLETE = "login_completed";
    public static final String RECHARGE_LIST = "recharge_list";
    public static final String IS_FIRST_RECHARGE_DONE = "is_first_recharge_done";
    public static final String STORE_TAB_LIST_RESPONSE = "store_tab_list_response";
    public static final String CHECK_LOGIN_TYPE_USER = "check_login_type_user";
    public static final String STORE_PURCHASED_LIST_RESPONSE = "store_purchased_item";
    public static final String LEVEL = "level";


    public void setFirstTimeLogin(boolean isFirstTime) {
        editor.putBoolean(FirstTimeLogin, isFirstTime);
        editor.apply();
    }


    public boolean getFirstTimeLogin() {
        return pref.getBoolean(FirstTimeLogin, true);
    }

    public void setUserLoginCompleted(boolean b) {
        editor.putBoolean(LOGINCOMPLETE, b);
        editor.apply();
    }

    public boolean getUserLoginCompleted() {

        return pref.getBoolean(LOGINCOMPLETE, false);
    }

    public void setRechargeListResponse(RechargePlanResponseNew rechargeRes) {
        String data = new Gson().toJson(rechargeRes);
        editor.putString(RECHARGE_LIST, data);
        editor.apply();
        Log.e("GET_RechargePlanResponse", "getRechargeListResponse: set  " + data);
    }

    public RechargePlanResponseNew getRechargeListResponse() {
        String data = pref.getString(RECHARGE_LIST, "null");
        RechargePlanResponseNew response = new Gson().fromJson(data, RechargePlanResponseNew.class);
        Log.e("GET_RechargePlanResponse", "getRechargeListResponse: get  " + new Gson().toJson(response));
        return response;
    }

    public String getFirstTimeRecharged() {
        return pref.getString(IS_FIRST_RECHARGE_DONE, "0");
    }


    public void setFirstTimeRecharged(String firstRecharge) {
        editor.putString(IS_FIRST_RECHARGE_DONE, firstRecharge);
        editor.apply();
    }

    public String getCheckLoginTypeUser() {
        return pref.getString(CHECK_LOGIN_TYPE_USER, null);
    }


    public void setCheckLoginTypeUser(String loginType) {
        editor.putString(CHECK_LOGIN_TYPE_USER, loginType);
        editor.apply();
    }

    public void setStoreTabList(StoreResponse storeResponse) {
        String data = new Gson().toJson(storeResponse);
        Log.e("STORE_TAB_LIST_RESPONSE", "setStoreList: " + data);
        editor.putString(STORE_TAB_LIST_RESPONSE, data);
        editor.apply();
    }

    public StoreResponse getStoreTabList() {
        String data = pref.getString(STORE_TAB_LIST_RESPONSE, null);
        StoreResponse storeResponse = new Gson().fromJson(data, StoreResponse.class);
        Log.e("STORE_TAB_LIST_RESPONSE", "setStoreList: " + new Gson().toJson(data));
        return storeResponse;
    }

    public void setPurchasedItems(StoreResponse purchasedItemsResp) {
        if (purchasedItemsResp != null) {
            String purchasedItemsRespString = new Gson().toJson(purchasedItemsResp);
            editor.putString(STORE_PURCHASED_LIST_RESPONSE, purchasedItemsRespString);
            editor.apply();
            Log.e("STORE_PURCHASED_LIST_RESPONSE_session", "setPurchasedItems: " + purchasedItemsRespString);
        }
    }

    public StoreResponse getPurchasedItems() {
        StoreResponse purchasedItemListResp = null;
        String purchasedItemsRespString = pref.getString(STORE_PURCHASED_LIST_RESPONSE, null);
        purchasedItemListResp = new Gson().fromJson(purchasedItemsRespString, StoreResponse.class);
        Log.e("STORE_PURCHASED_LIST_RESPONSE_session", "getPurchasedItems: " + new Gson().toJson(purchasedItemListResp));
        return purchasedItemListResp;
    }

    public void setLevel(int level) {
        editor.putInt(LEVEL, level);
        editor.apply();
    }

    public int getLevel() {
        return pref.getInt(LEVEL, 0);
    }

    @SuppressLint("LongLogTag")
    public void setChatPriceListResponse(PriceListResponse priceListResponse) {
        String priceListRespString = new Gson().toJson(priceListResponse);
        editor.putString(CHAT_PRICE_LIST_RESPONSE, priceListRespString);
        editor.apply();
        //  Log.e("CHAT_PRICE_LIST_RESPONSE", "setChatPriceListResponse: " + priceListRespString);
    }

    @SuppressLint("LongLogTag")
    public PriceListResponse getChatPriceListResponse() {
        String priceListRespString = pref.getString(CHAT_PRICE_LIST_RESPONSE, null);
        PriceListResponse response = new Gson().fromJson(priceListRespString, PriceListResponse.class);
        // Log.e("CHAT_PRICE_LIST_RESPONSE", "getChatPriceListResponse: " + new Gson().toJson(response));
        return response;
    }

    public void setSelectedCallPrice(int selectedCallPrice) {
        Log.e("SELECTED_CHAT_PRICE", "setSelectedCallPrice: " + selectedCallPrice);
        editor.putInt(SELECTED_CHAT_PRICE, selectedCallPrice);
        editor.apply();
    }

    public int getSelectedCallPrice() {
        int selectedCallPrice = pref.getInt(SELECTED_CHAT_PRICE, 0);
        Log.e("SELECTED_CHAT_PRICE", "getSelectedCallPrice: " + selectedCallPrice);
        return selectedCallPrice;
    }

    public void setHostLevel(String hostLevel) {
        editor.putString(HOST_LEVEL, hostLevel);
        editor.apply();
    }

    public String getHostLevel() {
        return pref.getString(HOST_LEVEL, "0");
    }

    public void setResUpload(String hostLevel) {
        editor.putString(RES_UPLOAD, hostLevel);
        editor.apply();
    }

    public String getResUpload() {
        return pref.getString(RES_UPLOAD, "0");
    }

}