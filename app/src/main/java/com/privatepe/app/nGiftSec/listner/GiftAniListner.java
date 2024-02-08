package com.privatepe.app.nGiftSec.listner;

import android.view.View;

import com.zeeplive.app.activity.party.model.PartyModel;
import com.zeeplive.app.response.N_Gift.Gift;

import java.io.Serializable;
import java.util.List;

public interface GiftAniListner extends Serializable {
    void onClick( Gift selectedGifts);
    void onSelectClick(Gift selectedGifts, String giftCount, List<PartyModel> selectedUserLists,View view);

}
