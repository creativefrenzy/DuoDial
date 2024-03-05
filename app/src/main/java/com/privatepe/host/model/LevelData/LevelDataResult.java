package com.privatepe.host.model.LevelData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LevelDataResult {
    @SerializedName("level")
    @Expose
    private List<Level> level = null;
    @SerializedName("current_week-report")
    @Expose
    private CurrentWeekReport currentWeekReport;
    @SerializedName("last_week-report")
    @Expose
    private LastWeekReport lastWeekReport;
    @SerializedName("agent_id")
    @Expose
    private String agentId;

    public List<Level> getLevel() {
        return level;
    }

    public void setLevel(List<Level> level) {
        this.level = level;
    }

    public CurrentWeekReport getCurrentWeekReport() {
        return currentWeekReport;
    }

    public void setCurrentWeekReport(CurrentWeekReport currentWeekReport) {
        this.currentWeekReport = currentWeekReport;
    }

    public LastWeekReport getLastWeekReport() {
        return lastWeekReport;
    }

    public void setLastWeekReport(LastWeekReport lastWeekReport) {
        this.lastWeekReport = lastWeekReport;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }
}
