package com.klive.app.ZegoExpress.zim;

import im.zego.zim.enums.ZIMErrorCode;

public interface ResultCallback {

    void onZimCallback(ZIMErrorCode errorCode, String errMsg);

}
