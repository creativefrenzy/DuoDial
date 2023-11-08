package com.privatepe.app.ZegoExpress.zim;

public enum CallType {

    /// voice: voice call
    Voice(1),
    /// video: video call
    Video(2);

    private final int value;

    public int getValue() {
        return value;
    }

    CallType(int value) {
        this.value = value;
    }

    public static CallType init(int value){
        if (value == 1){
            return Voice;
        }else{
            return Video;
        }
    }

}
