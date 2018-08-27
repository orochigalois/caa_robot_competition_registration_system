package com.zts.robot.pojo;

public class RaceTeamScore extends RaceTeamScoreKey {
    private Integer score;

    private String flg;

    private String grantjzstatus;

    private String grantcsstatus;

    private String awards;

    private String cup;

    private String awardsno;

    private String cupno;

    private String awardsurl;

    private String cupurl;

    private String creatstatus;

    private String mid;

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getFlg() {
        return flg;
    }

    public void setFlg(String flg) {
        this.flg = flg == null ? null : flg.trim();
    }

    public String getGrantjzstatus() {
        return grantjzstatus;
    }

    public void setGrantjzstatus(String grantjzstatus) {
        this.grantjzstatus = grantjzstatus == null ? null : grantjzstatus.trim();
    }

    public String getGrantcsstatus() {
        return grantcsstatus;
    }

    public void setGrantcsstatus(String grantcsstatus) {
        this.grantcsstatus = grantcsstatus == null ? null : grantcsstatus.trim();
    }

    public String getAwards() {
        return awards;
    }

    public void setAwards(String awards) {
        this.awards = awards == null ? null : awards.trim();
    }

    public String getCup() {
        return cup;
    }

    public void setCup(String cup) {
        this.cup = cup == null ? null : cup.trim();
    }

    public String getAwardsno() {
        return awardsno;
    }

    public void setAwardsno(String awardsno) {
        this.awardsno = awardsno == null ? null : awardsno.trim();
    }

    public String getCupno() {
        return cupno;
    }

    public void setCupno(String cupno) {
        this.cupno = cupno == null ? null : cupno.trim();
    }

    public String getAwardsurl() {
        return awardsurl;
    }

    public void setAwardsurl(String awardsurl) {
        this.awardsurl = awardsurl == null ? null : awardsurl.trim();
    }

    public String getCupurl() {
        return cupurl;
    }

    public void setCupurl(String cupurl) {
        this.cupurl = cupurl == null ? null : cupurl.trim();
    }

    public String getCreatstatus() {
        return creatstatus;
    }

    public void setCreatstatus(String creatstatus) {
        this.creatstatus = creatstatus == null ? null : creatstatus.trim();
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid == null ? null : mid.trim();
    }
}