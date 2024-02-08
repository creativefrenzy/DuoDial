package com.privatepe.app.nGiftSec.model;

import java.io.Serializable;

public class Gift implements Serializable, Cloneable {
    public int count;
    public String fromHead;
    public String fromName;
    public long fromUserId;
    public int fromUserSex;
    public String fromUserVlevel;
    public long giftId;
    public boolean hideGiftNum;
    public String icon;
    public int isCollectiveGift;
    public String name;
    public int shakeTime = 0;
    public int streamerTime;
    public String toHeadUrl;
    public String toName;
    public long toUserId;
    public String unit;
    public String icon_type;
    public String animation_file;
    public Integer animationType;
    public Integer animation;
    public String giftCoin;
    public String soundFile;
    public long fromSeatNo;
    public long toSeatNo;
    public String gift_duration;
    public String fromScreen;

    public Gift(String animation_file, String soundFile, String icon, String icon_type, String fromHead, String toName, String fromUserVlevel, int shakeTime, int count) {
        this.animation_file = animation_file;
        this.soundFile = soundFile;
        this.icon = icon;
        this.icon_type = icon_type;
        this.fromHead = fromHead;
        this.toName = toName;
        this.fromUserVlevel = fromUserVlevel;
        this.shakeTime = shakeTime;
        this.count = count;
    }

    public Gift(String animation_file, String soundFile, String icon, String icon_type, String fromHead, String toName,
                String fromUserVlevel, int shakeTime, int count, int gender) {
        this.animation_file = animation_file;
        this.soundFile = soundFile;
        this.icon = icon;
        this.icon_type = icon_type;
        this.fromHead = fromHead;
        this.toName = toName;
        this.fromUserVlevel = fromUserVlevel;
        this.shakeTime = shakeTime;
        this.count = count;
        this.fromUserSex = gender;
    }
    public Gift(String animation_file, String soundFile, String icon, String icon_type, String fromHead, String toName,
                String fromUserVlevel, int shakeTime, int count, int gender,String fromName,String fromScreen) {
        this.animation_file = animation_file;
        this.soundFile = soundFile;
        this.icon = icon;
        this.icon_type = icon_type;
        this.fromHead = fromHead;
        this.toName = toName;
        this.fromUserVlevel = fromUserVlevel;
        this.shakeTime = shakeTime;
        this.count = count;
        this.fromUserSex = gender;
        this.fromName = fromName;
        this.fromScreen = fromScreen;
    }

    public Gift(int count, String fromHead, String fromName, long fromUserId, int fromUserSex, String fromUserVlevel, long giftId, boolean hideGiftNum,
                String icon, int isCollectiveGift, String name, int shakeTime, int streamerTime, String toHeadUrl, String toName, long toUserId, String unit,
                String icon_type, String animation_file, Integer animationType, Integer animation, String giftCoin, String soundFile) {
        this.count = count;
        this.fromHead = fromHead;
        this.fromName = fromName;
        this.fromUserId = fromUserId;
        this.fromUserSex = fromUserSex;
        this.fromUserVlevel = fromUserVlevel;
        this.giftId = giftId;
        this.hideGiftNum = hideGiftNum;
        this.icon = icon;
        this.isCollectiveGift = isCollectiveGift;
        this.name = name;
        this.shakeTime = shakeTime;
        this.streamerTime = streamerTime;
        this.toHeadUrl = toHeadUrl;
        this.toName = toName;
        this.toUserId = toUserId;
        this.unit = unit;
        this.icon_type = icon_type;
        this.animation_file = animation_file;
        this.animationType = animationType;
        this.animation = animation;
        this.giftCoin = giftCoin;
        this.soundFile = soundFile;
    }

    public Gift(int count, String fromHead, String fromName, long fromUserId, int fromUserSex, String fromUserVlevel, long giftId, boolean hideGiftNum,
                String icon, int isCollectiveGift, String name, int shakeTime, int streamerTime, String toHeadUrl, String toName, long toUserId, String unit,
                String icon_type, String animation_file, Integer animationType, Integer animation, String giftCoin, String soundFile, String gift_duration) {
        this.count = count;
        this.fromHead = fromHead;
        this.fromName = fromName;
        this.fromUserId = fromUserId;
        this.fromUserSex = fromUserSex;
        this.fromUserVlevel = fromUserVlevel;
        this.giftId = giftId;
        this.hideGiftNum = hideGiftNum;
        this.icon = icon;
        this.isCollectiveGift = isCollectiveGift;
        this.name = name;
        this.shakeTime = shakeTime;
        this.streamerTime = streamerTime;
        this.toHeadUrl = toHeadUrl;
        this.toName = toName;
        this.toUserId = toUserId;
        this.unit = unit;
        this.icon_type = icon_type;
        this.animation_file = animation_file;
        this.animationType = animationType;
        this.animation = animation;
        this.giftCoin = giftCoin;
        this.soundFile = soundFile;
        this.gift_duration = gift_duration;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Gift)) {
            return false;
        }
        Gift gift = (Gift) obj;
        return this.fromUserId == gift.getFromUserId() && this.toUserId == gift.getToUserId() && this.giftId == gift.getGiftId();
    }
    public long getFromSeatNo() {
        return this.fromSeatNo;
    }
    public long getToSeatNo() {
        return this.toSeatNo;
    }

    public int getCount() {
        return this.count;
    }

    public String getFromHead() {
        return this.fromHead;
    }

    public String getFromName() {
        return this.fromName;
    }

    public long getFromUserId() {
        return this.fromUserId;
    }

    public int getFromUserSex() {
        return this.fromUserSex;
    }

    public String getFromUserVlevel() {
        return this.fromUserVlevel;
    }

    public long getGiftId() {
        return this.giftId;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getIsCollectiveGift() {
        return this.isCollectiveGift;
    }

    public String getName() {
        return this.name;
    }

    public int getShakeTime() {
        return this.shakeTime;
    }

    public int getStreamerTime() {
        return this.streamerTime;
    }

    public String getToHeadUrl() {
        return this.toHeadUrl;
    }

    public String getToName() {
        return this.toName;
    }

    public long getToUserId() {
        return this.toUserId;
    }

    public String getUnit() {
        return this.unit;
    }

    public boolean isHideGiftNum() {
        return this.hideGiftNum;
    }

    public void mergeGift(Gift gift) {
        this.count = gift.getCount() + this.count;
    }

    public void setFromUserSex(int i) {
        this.fromUserSex = i;
    }

    public void setFromUserVlevel(String i) {
        this.fromUserVlevel = i;
    }

    public void setHideGiftNum(boolean z) {
        this.hideGiftNum = z;
    }

    public void setShakeTime(int i) {
        this.shakeTime = i;
    }

    public void setStreamerTime(int i) {
        this.streamerTime = i;
    }

    public void setFromUserId(long fromUserId) {
        this.fromUserId = fromUserId;
    }
    public void setFromSeatNo(long fromSeatNo) {
        this.fromSeatNo = fromSeatNo;
    }
    public void setToSeatNo(long toSeatNo) {
        this.toSeatNo = toSeatNo;
    }

    public String getIcon_type() {
        return icon_type;
    }

    public void setIcon_type(String icon_type) {
        this.icon_type = icon_type;
    }

    public String getAnimation_file() {
        return animation_file;
    }

    public void setAnimation_file(String animation_file) {
        this.animation_file = animation_file;
    }

    public Integer getAnimationType() {
        return animationType;
    }

    public void setAnimationType(Integer animationType) {
        this.animationType = animationType;
    }

    public Integer getAnimation() {
        return animation;
    }

    public void setAnimation(Integer animation) {
        this.animation = animation;
    }

    public String getGiftCoin() {
        return giftCoin;
    }

    public void setGiftCoin(String giftCoin) {
        this.giftCoin = giftCoin;
    }

    public String getSoundFile() {
        return soundFile;
    }

    public void setSoundFile(String soundFile) {
        this.soundFile = soundFile;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public void setFromHead(String fromHead) {
        this.fromHead = fromHead;
    }

    public String getGift_duration() {
        return gift_duration;
    }

    public void setGift_duration(String gift_duration) {
        this.gift_duration = gift_duration;
    }

    public Gift() {
    }
    public String getFromScreen() {
        return fromScreen;
    }

    public void setFromScreen(String fromScreen) {
        this.fromScreen = fromScreen;
    }
}
