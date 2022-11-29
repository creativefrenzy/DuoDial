package com.klive.app.Interface;

import com.klive.app.response.newgiftresponse.NewGift;

import java.io.Serializable;

public interface GiftSelectListener extends Serializable {

     void OnGiftSelect(NewGift giftData);

}
