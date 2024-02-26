package com.privatepe.app.AppsFlyerPackage;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AFInAppEventType;
import com.appsflyer.AppsFlyerLib;
import com.appsflyer.attribution.AppsFlyerRequestListener;
import com.privatepe.app.utils.SessionManager;

import java.util.HashMap;
import java.util.Map;

public class AppsFlyerEvent {

    private static AppsFlyerEvent instance;
    private final Context mcontext;

    private AppsFlyerEvent(Context context) {
        this.mcontext = context;
    }

    public static synchronized AppsFlyerEvent getInstance(Context context) {
        if (instance == null) {
            instance = new AppsFlyerEvent(context);
            instance.initAppsFlyer();
        }
        return instance;
    }

    private void initAppsFlyer() {
        //Log.e("test12345", "initAppsFlyer :");
        AppsFlyerLib.getInstance().init("RSSXPDDXpDY6HqWjQhhA57", null, mcontext);
        AppsFlyerLib.getInstance().start(mcontext);

    }

    //This is being Used to call Custom/In-app events of AppsFlyer .
    public void customLogEvents(Context context, String eventType, Map<String, Object> values) {

        AppsFlyerLib.getInstance().logEvent(context,
                eventType, values, new AppsFlyerRequestListener() {
                    @Override
                    public void onSuccess() {
                        /*Log.e("test12344","onSuccess : " + eventType);*/
                    }

                    @Override
                    public void onError(int i, @NonNull String s) {
                        /*Log.e("test12344", "Event failed to be sent:\n" +
                                "Error code: " + i + "\n"
                                + "Error description: " + s);*/
                    }
                });

    }

    public void trackCoinPurchase(double amount, String currency) {
        Map<String, Object> eventValues = new HashMap<String, Object>();
        eventValues.put(AFInAppEventParameterName.CONTENT_ID, getUserId());
        eventValues.put(AFInAppEventParameterName.CONTENT_TYPE, "Coin Purchase");
        eventValues.put(AFInAppEventParameterName.CURRENCY, currency);
        eventValues.put(AFInAppEventParameterName.REVENUE, amount);
        //Log.e("test12345", "trackInAppPurchase :"+ new SessionManager(mcontext).getUserId());
        customLogEvents(mcontext, AFInAppEventType.PURCHASE, eventValues);
    }

    public void customerIdAndLogSession() {
        AppsFlyerLib.getInstance().waitForCustomerUserId(true);
        AppsFlyerLib.getInstance().setCustomerIdAndLogSession(getUserId(), mcontext);
        //Log.e("test12345", "customerIdAndLogSession :" + new SessionManager(mcontext).getUserId());
    }

    public void trackLogin() {
        Map<String, Object> eventValues = new HashMap<String, Object>();
        eventValues.put(AFInAppEventParameterName.CONTENT_ID, getUserId());
        //Log.e("test12345", "trackLogin : " + eventValues.get(AFInAppEventParameterName.CONTENT_ID));
        customLogEvents(mcontext, AFInAppEventType.LOGIN, eventValues);
    }

    public void trackRegistration() {
        Map<String, Object> eventValues = new HashMap<String, Object>();
        eventValues.put(AFInAppEventParameterName.CONTENT_ID, getUserId());
        //Log.e("test12345", "trackRegistration : " + eventValues.get(AFInAppEventParameterName.CONTENT_ID));
        customLogEvents(mcontext, AFInAppEventType.COMPLETE_REGISTRATION, eventValues);


    }
    public void trackDeviceConfig(){
        Map<String, Object> eventValues = new HashMap<String, Object>();
        eventValues.put(AFInAppEventParameterName.CONTENT_ID, getUserId());
        eventValues.put("architecture", android.os.Build.CPU_ABI);
        customLogEvents(mcontext, "device_architecture", eventValues);
    }
    // created to fetch user id from session manager.
    public String getUserId() {
        return new SessionManager(mcontext).getUserId();
    }

}
