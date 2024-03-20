package com.privatepe.app.response.Stripe.key;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StripeKeyData {

    @SerializedName("publishable-key")
    @Expose
    private String publishableKey;
    @SerializedName("secret-key")
    @Expose
    private String secretKey;

    public String getPublishableKey() {
        return publishableKey;
    }

    public void setPublishableKey(String publishableKey) {
        this.publishableKey = publishableKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

}
