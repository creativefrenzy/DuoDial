package com.privatepe.app.enumClass;

public enum PaymentTypeEnum {
    NIPPY_GPAY_UPI("nippy_gpay_upi"),
    NIPPY_PHONEPE_UPI("nippy_phonepe_upi"),
    NIPPY_PAYTM_UPI("nippy_paytm_upi"),
    NIPPY_WEB_PAYMENT("nippy_web_payment");

    private String value;
    PaymentTypeEnum(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
