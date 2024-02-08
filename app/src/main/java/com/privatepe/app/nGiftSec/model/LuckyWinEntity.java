package com.privatepe.app.nGiftSec.model;

public class LuckyWinEntity extends Gift {
    public String giftName;
    public String giftUrl;
    public String headUrl;
    public String winEnergy;
    public String fromLevel;

    public LuckyWinEntity(String str, String str2, String str3, String str4, String str5 , int str6) {
        this.headUrl = str;
        this.giftUrl = str2;
        this.giftName = str3;
        this.winEnergy = str4;
        this.fromLevel = str5;
        this.fromUserSex = str6;
    }

    @Override
    public boolean equals(Object obj) {
        return false;
    }

    public String getGiftName() {
        return this.giftName;
    }

    public String getGiftUrl() {
        return this.giftUrl;
    }

    public String getHeadUrl() {
        return this.headUrl;
    }

    public String getWinEnergy() {
        return this.winEnergy;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public void setGiftUrl(String giftUrl) {
        this.giftUrl = giftUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public void setWinEnergy(String winEnergy) {
        this.winEnergy = winEnergy;
    }

    public String getFromLevel() {
        return fromLevel;
    }

    public void setFromLevel(String fromLevel) {
        this.fromLevel = fromLevel;
    }
}