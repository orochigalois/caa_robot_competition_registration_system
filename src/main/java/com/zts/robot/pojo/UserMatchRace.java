package com.zts.robot.pojo;

public class UserMatchRace extends UserMatchRaceKey {
    private String upduid;

    private String upddate;

    public String getUpduid() {
        return upduid;
    }

    public void setUpduid(String upduid) {
        this.upduid = upduid == null ? null : upduid.trim();
    }

    public String getUpddate() {
        return upddate;
    }

    public void setUpddate(String upddate) {
        this.upddate = upddate == null ? null : upddate.trim();
    }
}