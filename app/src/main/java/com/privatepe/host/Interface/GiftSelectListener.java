package com.privatepe.host.Interface;

import com.privatepe.host.response.newgiftresponse.NewGift;

import java.io.Serializable;

public interface GiftSelectListener extends Serializable {

     void OnGiftSelect(NewGift giftData);

}
