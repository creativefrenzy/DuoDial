package com.privatepe.host.response.PaymentGateway;

public class PaymentGatewayModel {
    public boolean success;
    public Result result;
    public Object error;

    public static class Result {
        public int paytm;
        public int haoda;
        public int nippy;
    }
}
