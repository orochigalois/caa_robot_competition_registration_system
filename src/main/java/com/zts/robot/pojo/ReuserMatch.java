package com.zts.robot.pojo;

public class ReuserMatch extends ReuserMatchKey {
    private String createtime;

    private String upuid;

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime == null ? null : createtime.trim();
    }

    public String getUpuid() {
        return upuid;
    }

    public void setUpuid(String upuid) {
        this.upuid = upuid == null ? null : upuid.trim();
    }
}