package com.privatepe.app.Interface;

import com.privatepe.app.response.newgiftresponse.NewGift;

import java.io.Serializable;

public interface GiftSelectListener extends Serializable {

     void OnGiftSelect(NewGift giftData);

}
