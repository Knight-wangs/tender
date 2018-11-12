package com.telecom.tender.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class ProjectBidderTenderFile {
    private String projectId;
    private String bidderId;
    private String name;
    private String industry;
    private String area;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date opentime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date tenderTime;
    private String state;
    private String isTender;
    private BidderForm bidderForm;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getBidderId() {
        return bidderId;
    }

    public void setBidderId(String bidderId) {
        this.bidderId = bidderId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
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

    public String getIsTender() {
        return isTender;
    }

    public void setIsTender(String isTender) {
        this.isTender = isTender;
    }

    public BidderForm getBidderForm() {
        return bidderForm;
    }

    public void setBidderForm(BidderForm bidderForm) {
        this.bidderForm = bidderForm;
    }
}
