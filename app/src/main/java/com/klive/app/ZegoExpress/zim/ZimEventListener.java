package com.klive.app.ZegoExpress.zim;

import im.zego.zim.entity.ZIMMessage;
import im.zego.zim.enums.ZIMConnectionEvent;
import im.zego.zim.enums.ZIMConnectionState;

public interface ZimEventListener {

    public void onCallInvitationCancelled(UserInfo userInfo, CallType cancelType);

    public void onCallInvitationAccepted(UserInfo userInfo);

    public void onCallInvitationRejected(UserInfo userInfo);

    public void onCallInvitationTimeout();

    public void onCallInviteesAnsweredTimeout();

    public void onReceiveCallEnded();

    public void onConnectionStateChanged(ZIMConnectionState state, ZIMConnectionEvent event);

    public void onReceiveZIMPeerMessage(ZIMMessage zimMessage, String fromUserID);

}
