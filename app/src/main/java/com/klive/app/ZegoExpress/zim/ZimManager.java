package com.klive.app.ZegoExpress.zim;

import static com.klive.app.utils.AppLifecycle.ZEGOTOKEN;
import static com.klive.app.utils.AppLifecycle.getActivity;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;


import com.klive.app.Zego.CallNotificationDialog;
import com.klive.app.activity.IncomingCallScreen;
import com.klive.app.dialogs.MessageNotificationDialog;
import com.klive.app.utils.AppLifecycle;
import com.klive.app.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/*

import im.zego.zim.ZIM;
import im.zego.zim.callback.ZIMCallAcceptanceSentCallback;
import im.zego.zim.callback.ZIMCallCancelSentCallback;
import im.zego.zim.callback.ZIMCallInvitationSentCallback;
import im.zego.zim.callback.ZIMCallRejectionSentCallback;
import im.zego.zim.callback.ZIMEventHandler;
import im.zego.zim.callback.ZIMLoggedInCallback;
import im.zego.zim.callback.ZIMMessageSentCallback;
import im.zego.zim.entity.ZIMCallAcceptConfig;
import im.zego.zim.entity.ZIMCallCancelConfig;
import im.zego.zim.entity.ZIMCallInvitationAcceptedInfo;
import im.zego.zim.entity.ZIMCallInvitationCancelledInfo;
import im.zego.zim.entity.ZIMCallInvitationReceivedInfo;
import im.zego.zim.entity.ZIMCallInvitationRejectedInfo;
import im.zego.zim.entity.ZIMCallInvitationSentInfo;
import im.zego.zim.entity.ZIMCallInviteConfig;
import im.zego.zim.entity.ZIMCallRejectConfig;
import im.zego.zim.entity.ZIMConversationChangeInfo;
import im.zego.zim.entity.ZIMError;
import im.zego.zim.entity.ZIMGroupAttributesUpdateInfo;
import im.zego.zim.entity.ZIMGroupFullInfo;
import im.zego.zim.entity.ZIMGroupMemberInfo;
import im.zego.zim.entity.ZIMGroupOperatedInfo;
import im.zego.zim.entity.ZIMMessage;
import im.zego.zim.entity.ZIMMessageSendConfig;
import im.zego.zim.entity.ZIMRoomAttributesUpdateInfo;
import im.zego.zim.entity.ZIMTextMessage;
import im.zego.zim.entity.ZIMUserInfo;
import im.zego.zim.enums.ZIMConnectionEvent;
import im.zego.zim.enums.ZIMConnectionState;
import im.zego.zim.enums.ZIMGroupEvent;
import im.zego.zim.enums.ZIMGroupMemberEvent;
import im.zego.zim.enums.ZIMGroupMemberState;
import im.zego.zim.enums.ZIMGroupState;
import im.zego.zim.enums.ZIMMessageType;
import im.zego.zim.enums.ZIMRoomEvent;
import im.zego.zim.enums.ZIMRoomState;
*/

public class ZimManager {

    private static final String TAG = "ZimManager";

 //   private static ZimManager zimManager;
   // private List<ZimEventListener> listeners = new ArrayList<>();
 //   public ZIM zim;
 /*   private String mCallId;
    private Application mApp;
    private Activity mContext;
    private UserInfo mLocalUser;
    private UserInfo mRemoteUser;
    private CallType mCallType;

    private String FirstCallerId;

    public static boolean busyOnCall = false;
    private String callerUserId;
    private SessionManager sessionManager;
    private MessageNotificationDialog messageNotiDialog;*/

  /*  public static ZimManager sharedInstance() {
        if (zimManager == null) {
            synchronized (ZimManager.class) {
                if (zimManager == null) {
                    zimManager = new ZimManager();
                }
            }
        }
        return zimManager;
    }*/

   /* public void init(long appID, Application application) {
        Log.d(TAG, "init: ");
        zim = ZIM.create(appID, application);
        mApp = application;
        sessionManager = new SessionManager(mApp);
        mContext = AppLifecycle.getActivity();
        setEventHandler();
    }*/


   /* public String getCallId() {
        return mCallId;
    }*/

 /*   public CallType getCallType() {
        return mCallType;
    }*/

   /* public UserInfo getRemoteUser() {
        return mRemoteUser;
    }*/

   /* public UserInfo getLocalUser() {
        return mLocalUser;
    }*/

  /*  public void addListener(ZimEventListener listener) {
        listeners.add(listener);
    }*/


   /* public void removeListener(ZimEventListener listener) {
        listeners.remove(listener);
    }
*/
/*
    public void loginZim(String userID, String userName, String icon, String token, ResultCallback callback) {
        ZIMUserInfo zimUserInfo = new ZIMUserInfo();
        zimUserInfo.userID = userID;
        zimUserInfo.userName = userName;
        mLocalUser = new UserInfo(userID, userName, icon);
        zim.login(zimUserInfo, token, new ZIMLoggedInCallback() {
            @Override
            public void onLoggedIn(ZIMError error) {
                Log.d(TAG, "onLoggedIn: " + error.message);
                if (new SessionManager(mApp).getFirstRun()) {
                    new SessionManager(mApp).setLoginTime(System.currentTimeMillis());
                }
                callback.onZimCallback(error.code, error.message);
            }
        });
    }*/


 /*   public void logoutZim() {
        zim.logout();
    }*/

/*
    public void destroyZim() {
        logoutZim();
        zim.destroy();

    }
*/

  /*
    public void sendMessage(ZIMMessage msg, String toUserID) {
        ZIMMessageSendConfig config = new ZIMMessageSendConfig();
        zim.sendPeerMessage(msg, toUserID, config, new ZIMMessageSentCallback() {
            @Override
            public void onMessageAttached(ZIMMessage message) {

            }

            @Override
            public void onMessageSent(ZIMMessage message, ZIMError errorInfo) {
                Log.d(TAG, "onMessageSent: 96");
            }
        });
    }*/


  /*  public void callInvite(String inviteeId, String extendedData, ResultCallback callback) {
        List<String> invitees = new ArrayList<>();  // Invitee list
        invitees.add(inviteeId);       // invitee user id
        ZIMCallInviteConfig config = new ZIMCallInviteConfig();
        config.timeout = 200; //Invitation timeout, in seconds, range 1-600
        config.extendedData = extendedData;

        zim.callInvite(invitees, config, new ZIMCallInvitationSentCallback() {
            @Override
            public void onCallInvitationSent(String callID, ZIMCallInvitationSentInfo info, ZIMError errorInfo) {
                Log.d(TAG, "onCallInvitationSent: " + callID);
                mCallId = callID;
                callback.onZimCallback(errorInfo.code, errorInfo.message);
            }
        });
    }*/


/*    public void callAccept(ResultCallback callback) {
        ZIMCallAcceptConfig config = new ZIMCallAcceptConfig();
        zim.callAccept(mCallId, config, new ZIMCallAcceptanceSentCallback() {
            @Override
            public void onCallAcceptanceSent(String callID, ZIMError errorInfo) {
                Log.d(TAG, "onCallAcceptanceSent: ");
                Log.e(TAG, "onCallInvitationReceived: zim_manager mCallId " + mCallId);

                callback.onZimCallback(errorInfo.code, errorInfo.message);
            }
        });
    }*/

  /*  public void callEnd(String toUserId) {
        ZIMTextMessage zimMessage = new ZIMTextMessage();
        zimMessage.message = "command_end_call";
        sendMessage(zimMessage, toUserId);
    }*/

/*

    public void callCancel(String userId, ResultCallback callback) {
        List<String> invitees = new ArrayList<>();
        invitees.add(userId);
        ZIMCallCancelConfig config = new ZIMCallCancelConfig();

        zim.callCancel(invitees, mCallId, config, new ZIMCallCancelSentCallback() {
            @Override
            public void onCallCancelSent(String callID, ArrayList<String> errorInvitees, ZIMError errorInfo) {
                Log.d(TAG, "onCallCancelSent: ");
                callback.onZimCallback(errorInfo.code, errorInfo.message);
            }
        });
    }
*/

/*

    public void callReject(ResultCallback callback) {

        ZIMCallRejectConfig config = new ZIMCallRejectConfig();
        zim.callReject(mCallId, config, new ZIMCallRejectionSentCallback() {
            @Override
            public void onCallRejectionSent(String callID, ZIMError errorInfo) {
                Log.d(TAG, "onCallRejectionSent: ");
                callback.onZimCallback(errorInfo.code, errorInfo.message);
            }
        });
    }
*/

  /*  private UserInfo parseCallInvitationData(String msg) {
        Log.d(TAG, "parseCallInvitationData: " + mCallType);
        UserInfo userInfo = null;
        JSONObject msgObj = null;
        try {
            msgObj = new JSONObject(msg);
            String userName = msgObj.getString("userName");
            String userId = msgObj.getString("userId");
            int type = msgObj.getInt("callType");
            mCallType = CallType.init(type);
            Log.d(TAG, "parseCallInvitationData: " + mCallType);
            String userIcon = msgObj.getString("userIcon");
            userInfo = new UserInfo(userId, userName, userIcon);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return userInfo;
    }*/




    /*    private void parseZimMessage(ZIMMessage message, String fromUserID) {
        if (new SessionManager(mApp).getLoginTime() < message.getTimestamp()) {
            if (message.getType() == ZIMMessageType.TEXT) {
                ZIMTextMessage txtMsg = (ZIMTextMessage) message;
                String msg = txtMsg.message;
                if (msg.equals("command_end_call")) {
                    // Log.e("parseZimMessage", "ZimManager: command_end_call ");

                    Log.e("zimManagerError", "parseZimMessage: fromUserID " + fromUserID + " callerUserId  " + callerUserId + "  FirstCallerId " + FirstCallerId);

                    if (fromUserID.equals(FirstCallerId)) {
                        for (ZimEventListener listener : listeners) {
                            listener.onReceiveCallEnded();
                        }
                    }H

                } else {
                    for (ZimEventListener listener : listeners) {
                        listener.onReceiveZIMPeerMessage(message, fromUserID);
                    }
                }
            } else {
                for (ZimEventListener listener : listeners) {
                    listener.onReceiveZIMPeerMessage(message, fromUserID);
                }
            }
        }
    }*/

/*

    private void getZimMsg(ZIMMessage zimMsg, String fromUserID) {

        if (sessionManager.getLoginTime() < zimMsg.getTimestamp()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (zimMsg.getType() == ZIMMessageType.TEXT) {
                        ZIMTextMessage txtMsg = (ZIMTextMessage) zimMsg;
                        String msg = txtMsg.message;
                        if (msg.equals("command_end_call")) {

                            if (fromUserID.equals(FirstCallerId)) {

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        for (ZimEventListener listener : listeners) {
                                            //listener.onReceiveCallEnded();
                                        }
                                    }
                                }, 200);
                            }
                        } else {

                            for (ZimEventListener listener : listeners) {
                                listener.onReceiveZIMPeerMessage(zimMsg, fromUserID);
                               */
/* if (messageNotiDialog != null) {
                                    messageNotiDialog.dismiss();
                                }
                                messageNotiDialog = new MessageNotificationDialog(getActivity(), ((ZIMTextMessage) zimMsg).message, fromUserID, "ZEGO", null);
*//*

                                Log.e("MessageNotiDialog", "run: " + ((ZIMTextMessage) zimMsg).message);
                            }
                        }
                    } else {
                        for (ZimEventListener listener : listeners) {
                            listener.onReceiveZIMPeerMessage(zimMsg, fromUserID);
                        }
                    }
                }
            }, 500);


        }

    }
*/


    private void setEventHandler() {

/*        zim.setEventHandler(new ZIMEventHandler() {
            @Override
            public void onConnectionStateChanged(ZIM zim, ZIMConnectionState state, ZIMConnectionEvent event, JSONObject extendedData) {
                super.onConnectionStateChanged(zim, state, event, extendedData);
                Log.d(TAG, "onConnectionStateChanged: ");
                Log.e(TAG, "onConnectionStateChanged: state " + state + " event  " + event);
                for (ZimEventListener listener : listeners) {
                    listener.onConnectionStateChanged(state, event);
                }
            }

            @Override
            public void onError(ZIM zim, ZIMError errorInfo) {
                super.onError(zim, errorInfo);
                Log.d(TAG, "onError: ");
            }

            @Override
            public void onTokenWillExpire(ZIM zim, int second) {
                super.onTokenWillExpire(zim, second);
                Log.d(TAG, "onTokenWillExpire: ");
            }

            @Override
            public void onConversationChanged(ZIM zim, ArrayList<ZIMConversationChangeInfo> conversationChangeInfoList) {
                super.onConversationChanged(zim, conversationChangeInfoList);
                Log.d(TAG, "onConversationChanged: ");
            }

            @Override
            public void onConversationTotalUnreadMessageCountUpdated(ZIM zim, int totalUnreadMessageCount) {
                super.onConversationTotalUnreadMessageCountUpdated(zim, totalUnreadMessageCount);
                Log.d(TAG, "onConversationTotalUnreadMessageCountUpdated: ");
            }

            @Override
            public void onReceivePeerMessage(ZIM zim, ArrayList<ZIMMessage> messageList, String fromUserID) {
                super.onReceivePeerMessage(zim, messageList, fromUserID);
                Log.d(TAG, "onReceivePeerMessage: ");
                for (ZIMMessage msg : messageList) {
                    // parseZimMessage(msg, fromUserID);
                  //  getZimMsg(msg, fromUserID);
                }
            }

            @Override
            public void onReceiveRoomMessage(ZIM zim, ArrayList<ZIMMessage> messageList, String fromRoomID) {
                super.onReceiveRoomMessage(zim, messageList, fromRoomID);
                Log.d(TAG, "onReceiveRoomMessage: ");
            }

            @Override
            public void onReceiveGroupMessage(ZIM zim, ArrayList<ZIMMessage> messageList, String fromGroupID) {
                super.onReceiveGroupMessage(zim, messageList, fromGroupID);
                Log.d(TAG, "onReceiveGroupMessage: ");
            }

            @Override
            public void onRoomMemberJoined(ZIM zim, ArrayList<ZIMUserInfo> memberList, String roomID) {
                super.onRoomMemberJoined(zim, memberList, roomID);
                Log.d(TAG, "onRoomMemberJoined: ");
            }

            @Override
            public void onRoomMemberLeft(ZIM zim, ArrayList<ZIMUserInfo> memberList, String roomID) {
                super.onRoomMemberLeft(zim, memberList, roomID);
                Log.d(TAG, "onRoomMemberLeft: ");
            }

            @Override
            public void onRoomStateChanged(ZIM zim, ZIMRoomState state, ZIMRoomEvent event, JSONObject extendedData, String roomID) {
                super.onRoomStateChanged(zim, state, event, extendedData, roomID);
                Log.d(TAG, "onRoomStateChanged: ");
            }

            @Override
            public void onRoomAttributesUpdated(ZIM zim, ZIMRoomAttributesUpdateInfo info, String roomID) {
                super.onRoomAttributesUpdated(zim, info, roomID);
                Log.d(TAG, "onRoomAttributesUpdated: ");
            }

            @Override
            public void onRoomAttributesBatchUpdated(ZIM zim, ArrayList<ZIMRoomAttributesUpdateInfo> infos, String roomID) {
                super.onRoomAttributesBatchUpdated(zim, infos, roomID);
                Log.d(TAG, "onRoomAttributesBatchUpdated: ");
            }

            @Override
            public void onGroupStateChanged(ZIM zim, ZIMGroupState state, ZIMGroupEvent event, ZIMGroupOperatedInfo operatedInfo, ZIMGroupFullInfo groupInfo) {
                super.onGroupStateChanged(zim, state, event, operatedInfo, groupInfo);
                Log.d(TAG, "onGroupStateChanged: ");
            }

            @Override
            public void onGroupNameUpdated(ZIM zim, String groupName, ZIMGroupOperatedInfo operatedInfo, String groupID) {
                super.onGroupNameUpdated(zim, groupName, operatedInfo, groupID);
                Log.d(TAG, "onGroupNameUpdated: ");
            }

            @Override
            public void onGroupNoticeUpdated(ZIM zim, String groupNotice, ZIMGroupOperatedInfo operatedInfo, String groupID) {
                super.onGroupNoticeUpdated(zim, groupNotice, operatedInfo, groupID);
                Log.d(TAG, "onGroupNoticeUpdated: ");
            }

            @Override
            public void onGroupAttributesUpdated(ZIM zim, ArrayList<ZIMGroupAttributesUpdateInfo> infos, ZIMGroupOperatedInfo operatedInfo, String groupID) {
                super.onGroupAttributesUpdated(zim, infos, operatedInfo, groupID);
                Log.d(TAG, "onGroupAttributesUpdated: ");
            }

            @Override
            public void onGroupMemberStateChanged(ZIM zim, ZIMGroupMemberState state, ZIMGroupMemberEvent event, ArrayList<ZIMGroupMemberInfo> userList, ZIMGroupOperatedInfo operatedInfo, String groupID) {
                super.onGroupMemberStateChanged(zim, state, event, userList, operatedInfo, groupID);
                Log.d(TAG, "onGroupMemberStateChanged: ");
            }

            @Override
            public void onGroupMemberInfoUpdated(ZIM zim, ArrayList<ZIMGroupMemberInfo> userList, ZIMGroupOperatedInfo operatedInfo, String groupID) {
                super.onGroupMemberInfoUpdated(zim, userList, operatedInfo, groupID);
                Log.d(TAG, "onGroupMemberInfoUpdated: ");
            }

            @Override
            public void onCallInvitationReceived(ZIM zim, ZIMCallInvitationReceivedInfo info, String callID) {
                super.onCallInvitationReceived(zim, info, callID);
                // Log.d(TAG, "onCallInvitationReceived = " + info.extendedData);

                Log.d(TAG, "onCallInvitationReceived = " + "");

                String callerUserName = "";
*//*
                if (!info.extendedData.equals("") && (info.extendedData != null)) {
                    // firsttime SF val
                    Log.d(TAG, "onCallInvitationReceived = " + info.extendedData);


                    JSONObject MessageWithCallJson = null;
                    try {
                        MessageWithCallJson = new JSONObject(info.extendedData);
                        //  Log.e(TAG, "goToIncomingCallScreen: " + MessageWithCallJson.toString() + "                 datawithCall :  " + datawithCall);

                        if (MessageWithCallJson.get("isMessageWithCall").toString().equals("yes")) {
                            JSONObject CallMessageBody = new JSONObject(MessageWithCallJson.get("CallMessageBody").toString());
                            callerUserId = CallMessageBody.get("UserId").toString();
                            callerUserName = CallMessageBody.get("UserName").toString();

                            Log.e(TAG, "onCallInvitationReceived: caller id " + callerUserId + " busyOnCall => " + busyOnCall + " callID=> " + callID);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    Log.e("zimamagerError", "onCallInvitationReceived: " + mCallId);


                    if (!busyOnCall) {
                        mCallId = callID;
                        Log.e("zimamagerError", "onCallInvitationReceived: not busy on call " + mCallId + "  user name " + callerUserName);

                        busyOnCall = true;
                        FirstCallerId = callerUserId;

                        if (AppLifecycle.AppInBackground) {
                            goToIncomingCallScreen(info.extendedData);
                            Log.e(TAG, "onCallInvitationReceived: " + "App in Background");
                        } else {
                            new CallNotificationDialog(AppLifecycle.getActivity(), info.extendedData);
                            Log.e(TAG, "onCallInvitationReceived: " + "App in Foreground");
                        }

                    } else {

                        Log.e("zimamagerError", "onCallInvitationReceived: busy on call " + mCallId + "  user name " + callerUserName);
                        Log.e(TAG, "onCallInvitationReceived: " + "busy on call");
                        //Toast.makeText(getActivity(),"busy on call",Toast.LENGTH_SHORT).show();
       *//**//*
                           ZIMTextMessage zimTextMessage=new ZIMTextMessage();
                           zimTextMessage.message="User busy on call";

                           if(!callerUserId.equals(""))
                            sendMessage(zimTextMessage,callerUserId);*//**//*
                    }
                    //
                } else {
                    Log.d(TAG, "onCallInvitationReceived: can't parse remote user info.");

                }*//*

            }

            @Override
            public void onCallInvitationCancelled(ZIM zim, ZIMCallInvitationCancelledInfo info, String callID) {
                super.onCallInvitationCancelled(zim, info, callID);
                Log.d(TAG, "onCallInvitationCancelled: ");
                Toast.makeText(mApp, "call cancelled!", Toast.LENGTH_SHORT).show();

                *//*
                   if (dialog != null){
                    dialog.dismiss();
                   }
                *//*

                for (ZimEventListener listener : listeners) {
                    listener.onCallInvitationCancelled(mRemoteUser, mCallType);
                }

            }

            @Override
            public void onCallInvitationAccepted(ZIM zim, ZIMCallInvitationAcceptedInfo info, String callID) {
                super.onCallInvitationAccepted(zim, info, callID);
                Log.d(TAG, "onCallInvitationAccepted: = " + info.extendedData + "  data   " + mRemoteUser);
                for (ZimEventListener listener : listeners) {
                    listener.onCallInvitationAccepted(mRemoteUser);
                }
            }

            @Override
            public void onCallInvitationRejected(ZIM zim, ZIMCallInvitationRejectedInfo info, String callID) {
                super.onCallInvitationRejected(zim, info, callID);
                Log.d(TAG, "onCallInvitationRejected: ");
                for (ZimEventListener listener : listeners) {
                    listener.onCallInvitationRejected(mRemoteUser);
                }
            }

            @Override
            public void onCallInvitationTimeout(ZIM zim, String callID) {
                super.onCallInvitationTimeout(zim, callID);
                Log.d(TAG, "onCallInvitationTimeout: ");
                for (ZimEventListener listener : listeners) {
                    listener.onCallInvitationTimeout();
                }
            }

            @Override
            public void onCallInviteesAnsweredTimeout(ZIM zim, ArrayList<String> invitees, String callID) {
                super.onCallInviteesAnsweredTimeout(zim, invitees, callID);
                Log.d(TAG, "onCallInviteesAnsweredTimeout: ");
                for (ZimEventListener listener : listeners) {
                    listener.onCallInviteesAnsweredTimeout();
                }
            }


        });*/
    }


    private void goToIncomingCallScreen(String datawithCall) {

      /*  JSONObject MessageWithCallJson = null;
        try {
            MessageWithCallJson = new JSONObject(datawithCall);
            Log.e(TAG, "goToIncomingCallScreen: " + MessageWithCallJson.toString() + "                 datawithCall :  " + datawithCall);

            if (MessageWithCallJson.get("isMessageWithCall").toString().equals("yes")) {

                JSONObject CallMessageBody = new JSONObject(MessageWithCallJson.get("CallMessageBody").toString());

                Intent incoming = new Intent(mApp, IncomingCallScreen.class);
                incoming.putExtra("receiver_id", CallMessageBody.get("UserId").toString());
                incoming.putExtra("username", CallMessageBody.get("UserName").toString());
                incoming.putExtra("unique_id", CallMessageBody.get("UniqueId").toString());
                incoming.putExtra("token", ZEGOTOKEN);
                incoming.putExtra("callType", CallMessageBody.get("CallType").toString());
                incoming.putExtra("is_free_call", CallMessageBody.get("IsFreeCall").toString());
                incoming.putExtra("name", CallMessageBody.get("Name").toString());
                incoming.putExtra("image", CallMessageBody.get("ProfilePicUrl").toString());
                incoming.putExtra("CallEndTime", Long.parseLong(CallMessageBody.get("CallAutoEnd").toString()));

                incoming.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mApp.startActivity(incoming);

                //  Log.e(TAG, "goToIncomingCallScreen: " + "  Activity Started  " + Integer.parseInt(CallMessageBody.get("CallAutoEnd").toString()));
            } else {


            }


        } catch (JSONException e) {
            e.printStackTrace();
        }*/


    }


}