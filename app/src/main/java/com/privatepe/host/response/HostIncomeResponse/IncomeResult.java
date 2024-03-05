package com.privatepe.host.response.HostIncomeResponse;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IncomeResult {
    @SerializedName("weeks")
    @Expose
    private List<ThisWeekData> weeks = null;
    @SerializedName("footer_data")
    @Expose
    private List<AllWeeklyData> footerData = null;

    public List<ThisWeekData> getWeeks() {
        return weeks;
    }

    public void setWeeks(List<ThisWeekData> weeks) {
        this.weeks = weeks;
    }

    public List<AllWeeklyData> getFooterData() {
        return footerData;
    }

    public void setFooterData(List<AllWeeklyData> footerData) {
        this.footerData = footerData;
    }

}
