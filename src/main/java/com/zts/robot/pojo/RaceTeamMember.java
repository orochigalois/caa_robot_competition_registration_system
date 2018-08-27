package com.zts.robot.pojo;

public class RaceTeamMember extends RaceTeamMemberKey {
    private String mid;

    private String signuid;

    private String infostatus;

    private String feedback;

    private String upduid;

    private String upddate;

    private String reviewuid;

    private String reviewdate;

    private String feestatus;

    private String createdate;

    private Integer serialnum;

    private String entryno;

    private String entryurl;

    private Integer unitprice;
    
    private String tcode;
    
    private String stlogurl;
    
    private String ndlogurl;
    
    private String rdlogurl;

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid == null ? null : mid.trim();
    }

    public String getSignuid() {
        return signuid;
    }

    public void setSignuid(String signuid) {
        this.signuid = signuid == null ? null : signuid.trim();
    }

    public String getInfostatus() {
        return infostatus;
    }

    public void setInfostatus(String infostatus) {
        this.infostatus = infostatus == null ? null : infostatus.trim();
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback == null ? null : feedback.trim();
    }

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

    public String getReviewuid() {
        return reviewuid;
    }

    public void setReviewuid(String reviewuid) {
        this.reviewuid = reviewuid == null ? null : reviewuid.trim();
    }

    public String getReviewdate() {
        return reviewdate;
    }

    public void setReviewdate(String reviewdate) {
        this.reviewdate = reviewdate == null ? null : reviewdate.trim();
    }

    public String getFeestatus() {
        return feestatus;
    }

    public void setFeestatus(String feestatus) {
        this.feestatus = feestatus == null ? null : feestatus.trim();
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate == null ? null : createdate.trim();
    }

    public Integer getSerialnum() {
        return serialnum;
    }

    public void setSerialnum(Integer serialnum) {
        this.serialnum = serialnum;
    }

    public String getEntryno() {
        return entryno;
    }

    public void setEntryno(String entryno) {
        this.entryno = entryno == null ? null : entryno.trim();
    }

    public String getEntryurl() {
        return entryurl;
    }

    public void setEntryurl(String entryurl) {
        this.entryurl = entryurl == null ? null : entryurl.trim();
    }

    public Integer getUnitprice() {
        return unitprice;
    }

    public void setUnitprice(Integer unitprice) {
        this.unitprice = unitprice;
    }
    
    public void setTcode(String tcode) {
        this.tcode = tcode == null ? null : tcode.trim();
    }
    
    public String getStlogurl() {
        return stlogurl;
    }

    public void setStlogurl(String stlogurl) {
        this.stlogurl = stlogurl == null ? null : stlogurl.trim();
    }
    
    public String getNdlogurl() {
        return ndlogurl;
    }

    public void setNdlogurl(String ndlogurl) {
        this.ndlogurl = ndlogurl == null ? null : ndlogurl.trim();
    }
    
    public String getRdlogurl() {
        return rdlogurl;
    }

    public void setRdlogurl(String rdlogurl) {
        this.rdlogurl = rdlogurl == null ? null : rdlogurl.trim();
    }
}