package com.zts.robot.pojo;

public class Race {
    private String rid;

    private String mid;

    private String frname;

    private String rname;

    private String introduce;

    private String description;

    private String startdate;

    private String enddate;

    private String rules;

    private String attachurl;

    private String delflg;

    private String createdate;

    private String createuid;

    private String rcode;
    
    private String islog;
    
    private String stend;
    
    private String ndend;
    
    private String rdend;

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid == null ? null : rid.trim();
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid == null ? null : mid.trim();
    }

    public String getFrname() {
        return frname;
    }

    public void setFrname(String frname) {
        this.frname = frname == null ? null : frname.trim();
    }

    public String getRname() {
        return rname;
    }

    public void setRname(String rname) {
        this.rname = rname == null ? null : rname.trim();
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce == null ? null : introduce.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate == null ? null : startdate.trim();
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate == null ? null : enddate.trim();
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules == null ? null : rules.trim();
    }

    public String getAttachurl() {
        return attachurl;
    }

    public void setAttachurl(String attachurl) {
        this.attachurl = attachurl == null ? null : attachurl.trim();
    }

    public String getDelflg() {
        return delflg;
    }

    public void setDelflg(String delflg) {
        this.delflg = delflg == null ? null : delflg.trim();
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate == null ? null : createdate.trim();
    }

    public String getCreateuid() {
        return createuid;
    }

    public void setCreateuid(String createuid) {
        this.createuid = createuid == null ? null : createuid.trim();
    }

    public String getRcode() {
        return rcode;
    }

    public void setRcode(String rcode) {
        this.rcode = rcode == null ? null : rcode.trim();
    }
    
    public String getIslog() {
        return islog;
    }
    
    public void setIslog(String islog) {
        this.islog = islog == null ? "0" : islog.trim();
    }
    
    public String getstend() {
        return stend;
    }
    
    public void setstend(String stend) {
        this.stend = stend == null ? null : stend.trim();
    }
    
    public String getndend() {
        return ndend;
    }
    
    public void setndend(String ndend) {
        this.ndend = ndend == null ? null : ndend.trim();
    }
    
    public String getrdend() {
        return rdend;
    }
    
    public void setrdend(String rdend) {
        this.rdend = rdend == null ? null : rdend.trim();
    }
}