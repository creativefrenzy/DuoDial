package com.privatepe.host.model.gift;

import com.privatepe.host.response.newgiftresponse.NewGift;

public class GiftAnimData {

    int GiftResourceId;
    String peerName;
    String peerProfilePic;
    NewGift newGift;

    public GiftAnimData(int giftResourceId, String peerName, String peerProfilePic, NewGift newGift) {
        GiftResourceId = giftResourceId;
        this.peerName = peerName;
        this.peerProfilePic = peerProfilePic;
        this.newGift = newGift;
    }

    public int getGiftResourceId() {
        return GiftResourceId;
    }

    public void setGiftResourceId(int giftResourceId) {
        GiftResourceId = giftResourceId;
    }

    public String getPeerName() {
        return peerName;
    }

    public void setPeerName(String peerName) {
        this.peerName = peerName;
    }

    public String getPeerProfilePic() {
        return peerProfilePic;
    }

    public void setPeerProfilePic(String peerProfilePic) {
        this.peerProfilePic = peerProfilePic;
    }

    public NewGift getNewGift() {
        return newGift;
    }

    public void setNewGift(NewGift newGift) {
        this.newGift = newGift;
    }


}
