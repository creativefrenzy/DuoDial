package com.privatepe.host.retrofit;

public class EncryptedRequest {

    String countrycode;
    String mobile;
    String unique_device_id;
    String myhaskey;

    public EncryptedRequest(String countrycode, String mobile, String unique_device_id, String myhaskey) {
        this.countrycode = countrycode;
        this.mobile = mobile;
        this.unique_device_id = unique_device_id;
        this.myhaskey = myhaskey;
    }
}