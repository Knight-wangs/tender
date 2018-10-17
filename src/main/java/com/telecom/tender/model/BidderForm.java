package com.telecom.tender.model;

public class BidderForm {
    private int id;
    private String projectid;
    private String bidderid;
    private String tenderFile;
    private String tenderFileHash;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProjectid() {
        return projectid;
    }

    public void setProjectid(String projectid) {
        this.projectid = projectid;
    }

    public String getBidderid() {
        return bidderid;
    }

    public void setBidderid(String bidderid) {
        this.bidderid = bidderid;
    }

    public String getTenderFile() {
        return tenderFile;
    }

    public void setTenderFile(String tenderFile) {
        this.tenderFile = tenderFile;
    }

    public String getTenderFileHash() {
        return tenderFileHash;
    }

    public void setTenderFileHash(String tenderFileHash) {
        this.tenderFileHash = tenderFileHash;
    }
}
