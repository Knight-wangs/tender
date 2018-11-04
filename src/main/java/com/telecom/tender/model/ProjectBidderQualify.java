package com.telecom.tender.model;

import java.util.Date;

public class ProjectBidderQualify {
    private String projectId;
    private String name;
    private String area;
    private Date opentime;
    private Date tenderTime;
    private String state;
    private String isQualify;
    private BidderFile bidderFile;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Date getOpentime() {
        return opentime;
    }

    public void setOpentime(Date opentime) {
        this.opentime = opentime;
    }

    public Date getTenderTime() {
        return tenderTime;
    }

    public void setTenderTime(Date tenderTime) {
        this.tenderTime = tenderTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public BidderFile getBidderFile() {
        return bidderFile;
    }

    public void setBidderFile(BidderFile bidderFile) {
        this.bidderFile = bidderFile;
    }

    public String getIsQualify() {
        return isQualify;
    }

    public void setIsQualify(String isQualify) {
        this.isQualify = isQualify;
    }
}
