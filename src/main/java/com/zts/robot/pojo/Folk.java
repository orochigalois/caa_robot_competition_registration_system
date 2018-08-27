package com.zts.robot.pojo;

public class Folk {
    private String folkid;

    private String folk;

    public String getFolkid() {
        return folkid;
    }

    public void setFolkid(String folkid) {
        this.folkid = folkid == null ? null : folkid.trim();
    }

    public String getFolk() {
        return folk;
    }

    public void setFolk(String folk) {
        this.folk = folk == null ? null : folk.trim();
    }
}