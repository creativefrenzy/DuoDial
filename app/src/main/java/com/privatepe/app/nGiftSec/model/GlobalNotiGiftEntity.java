package com.privatepe.app.nGiftSec.model;

public final class GlobalNotiGiftEntity {
    public  long fromUsedrID;
    public  String fromNickName;
    public  int fromSex;
    public  String fromUserPic;
    public  String fromVLevel;
    public  int giftCount;
    public  String giftName;
    public  String giftType;
    public  String giftUrl;
    public  String gifticontype;
    public  String giftid;
    public  boolean isGo;
    public  long roomId;
    public  String roomName;
    public  long toUserID;
    public  String toNickName;
    public  int toSex;
    public  String toUserPic;
    public  String toVLevel;
    public  long winEnergy;
    public  String toAge;
    public  String toLanguage;
    public  String toLocation;
    public  String toCallRate;
    public  boolean isLive;




    public GlobalNotiGiftEntity(long fromUsedrID, String fromNickName, int fromSex, String fromUserPic, String fromVLevel, int giftCount, String giftName, String giftType,
                                String giftUrl, String giftid, long toUserID, String toNickName, int toSex, String toUserPic, String toVLevel, long winEnergy, long roomId,
                                String roomName, boolean isGo, String gifticontype, String toAge, String toLanguage, String toLocation, String toCallRate, boolean isLive) {
        this.fromUsedrID = fromUsedrID;
        this.fromNickName = fromNickName;
        this.fromSex = fromSex;
        this.fromUserPic = fromUserPic;
        this.fromVLevel = fromVLevel;
        this.giftCount = giftCount;
        this.giftName = giftName;
        this.giftType = giftType;
        this.giftUrl = giftUrl;
        this.giftid = giftid;
        this.toUserID = toUserID;
        this.toNickName = toNickName;
        this.toSex = toSex;
        this.toUserPic = toUserPic;
        this.toVLevel = toVLevel;
        this.winEnergy = winEnergy;
        this.roomId = roomId;
        this.roomName = roomName;
        this.isGo = isGo;
        this.gifticontype = gifticontype;
        this.toAge = toAge;
        this.toLanguage = toLanguage;
        this.toLocation = toLocation;
        this.toCallRate = toCallRate;
        this.isLive = isLive;
    }



    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof GlobalNotiGiftEntity)) {
            return false;
        }
        GlobalNotiGiftEntity globalWinGiftEntity = (GlobalNotiGiftEntity) obj;
        return  this.fromUsedrID == globalWinGiftEntity.fromUsedrID && a(this.fromNickName, globalWinGiftEntity.fromNickName) && this.fromSex == globalWinGiftEntity.fromSex && a(this.fromUserPic, globalWinGiftEntity.fromUserPic) && this.fromVLevel == globalWinGiftEntity.fromVLevel && this.giftCount == globalWinGiftEntity.giftCount && a(this.giftName, globalWinGiftEntity.giftName) && a(this.giftType, globalWinGiftEntity.giftType) && a(this.giftUrl, globalWinGiftEntity.giftUrl) && a(this.giftid, globalWinGiftEntity.giftid) && this.toUserID == globalWinGiftEntity.toUserID && a(this.toNickName, globalWinGiftEntity.toNickName) && this.toSex == globalWinGiftEntity.toSex && a(this.toUserPic, globalWinGiftEntity.toUserPic) && this.toVLevel == globalWinGiftEntity.toVLevel && this.winEnergy == globalWinGiftEntity.winEnergy && this.roomId == globalWinGiftEntity.roomId  && a(this.roomName, globalWinGiftEntity.roomName)   && this.isGo == globalWinGiftEntity.isGo;
    }


    public final long getFrom() {
        return this.fromUsedrID;
    }

    public final String getFromNickName() {
        return this.fromNickName;
    }

    public final int getFromSex() {
        return this.fromSex;
    }

    public final String getFromUserPic() {
        return this.fromUserPic;
    }

    public final String getFromVLevel() {
        return this.fromVLevel;
    }

    public final int getGiftCount() {
        return this.giftCount;
    }

    public final String getGiftName() {
        return this.giftName;
    }

    public final String getGiftType() {
        return this.giftType;
    }

    public final String getGiftUrl() {
        return this.giftUrl;
    }

    public final String getGiftid() {
        return this.giftid;
    }

    public final long getRoomId() {
        return this.roomId;
    }

    public final String getRoomName() {
        return this.roomName;
    }


    public final long getTo() {
        return this.toUserID;
    }

    public final String getToNickName() {
        return this.toNickName;
    }

    public final int getToSex() {
        return this.toSex;
    }

    public final String getToUserPic() {
        return this.toUserPic;
    }

    public final String getToVLevel() {
        return this.toVLevel;
    }


    public final long getWinEnergy() {
        return this.winEnergy;
    }

    public long getFromUsedrID() {
        return fromUsedrID;
    }

    public long getToUserID() {
        return toUserID;
    }

    public String getToAge() {
        return toAge;
    }

    public String getToLanguage() {
        return toLanguage;
    }

    public String getToLocation() {
        return toLocation;
    }

    public String getToCallRate() {
        return toCallRate;
    }

    public final boolean isGo() {
        return this.isGo;
    }

    public final void setGo(boolean z) {
        this.isGo = z;
    }

    public String getGifticontype() {
        return gifticontype;
    }

    public void setGifticontype(String gifticontype) {
        this.gifticontype = gifticontype;
    }

    public GlobalNotiGiftEntity() {
    }

    public String toString() {
        StringBuilder e2 = e("GlobalWinGiftEntity(bizCode=");
        e2.append(", from=");
        e2.append(this.fromUsedrID);
        e2.append(", fromNickName=");
        e2.append(this.fromNickName);
        e2.append(", fromSex=");
        e2.append(this.fromSex);
        e2.append(", fromUserPic=");
        e2.append(this.fromUserPic);
        e2.append(", fromVLevel=");
        e2.append(this.fromVLevel);
        e2.append(", giftCount=");
        e2.append(this.giftCount);
        e2.append(", giftName=");
        e2.append(this.giftName);
        e2.append(", giftType=");
        e2.append(this.giftType);
        e2.append(", giftUrl=");
        e2.append(this.giftUrl);
        e2.append(", giftid=");
        e2.append(this.giftid);
        e2.append(", to=");
        e2.append(this.toUserID);
        e2.append(", toNickName=");
        e2.append(this.toNickName);
        e2.append(", toSex=");
        e2.append(this.toSex);
        e2.append(", toUserPic=");
        e2.append(this.toUserPic);
        e2.append(", toVLevel=");
        e2.append(this.toVLevel);
        e2.append(", winEnergy=");
        e2.append(this.winEnergy);
        e2.append(", roomId=");
        e2.append(this.roomId);
        e2.append(", roomName=");
        e2.append(this.roomName);
        e2.append(", isGo=");
        return a(e2, this.isGo, ")");
    }

    public static boolean a(Object obj, Object obj2) {
        if (obj == null) {
            return obj2 == null;
        }
        return obj.equals(obj2);
    }

    public static StringBuilder e(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        return sb;
    }

    public static String a(StringBuilder sb, boolean z, String str) {
        sb.append(z);
        sb.append(str);
        return sb.toString();
    }
}
