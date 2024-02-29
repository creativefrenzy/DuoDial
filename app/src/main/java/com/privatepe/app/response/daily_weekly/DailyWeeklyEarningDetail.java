package com.privatepe.app.response.daily_weekly;

import java.io.Serializable;
import java.util.List;

public class DailyWeeklyEarningDetail {
    boolean success;
    DailyWeeklyEarningDetail.Result result;
    Object error;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }

    public static class Result implements Serializable {
        int call_rate, weekly_earning, today_total_calls, today_total_earning,today_call_earning,today_gift_earning,today_other_earning,referal_earning;
        String next_week_date;

        public int getReferal_earning() {
            return referal_earning;
        }

        public void setReferal_earning(int referal_earning) {
            this.referal_earning = referal_earning;
        }

        public int getCall_rate() {
            return call_rate;
        }

        public void setCall_rate(int call_rate) {
            this.call_rate = call_rate;
        }

        public int getWeekly_earning() {
            return weekly_earning;
        }

        public void setWeekly_earning(int weekly_earning) {
            this.weekly_earning = weekly_earning;
        }

        public int getToday_total_calls() {
            return today_total_calls;
        }

        public void setToday_total_calls(int today_total_calls) {
            this.today_total_calls = today_total_calls;
        }

        public int getToday_total_earning() {
            return today_total_earning;
        }

        public void setToday_total_earning(int today_total_earning) {
            this.today_total_earning = today_total_earning;
        }

        public int getToday_call_earning() {
            return today_call_earning;
        }

        public void setToday_call_earning(int today_call_earning) {
            this.today_call_earning = today_call_earning;
        }

        public int getToday_gift_earning() {
            return today_gift_earning;
        }

        public void setToday_gift_earning(int today_gift_earning) {
            this.today_gift_earning = today_gift_earning;
        }

        public int getToday_other_earning() {
            return today_other_earning;
        }

        public void setToday_other_earning(int today_other_earning) {
            this.today_other_earning = today_other_earning;
        }

        public String getNext_week_date() {
            return next_week_date;
        }

        public void setNext_week_date(String next_week_date) {
            this.next_week_date = next_week_date;
        }
    }
}
