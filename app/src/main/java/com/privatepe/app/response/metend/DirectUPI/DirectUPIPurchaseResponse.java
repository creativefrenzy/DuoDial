package com.privatepe.app.response.metend.DirectUPI;

public class DirectUPIPurchaseResponse {
    public boolean success;
    public Result result;
    public Object error;

    public static class Result {
        public int total_point;
        public int redemablePoints;
        public String total_points_till_now;
    }
}
