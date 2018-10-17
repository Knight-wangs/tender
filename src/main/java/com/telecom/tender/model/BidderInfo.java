package com.telecom.tender.model;

public class BidderInfo {
    private String bidderid;
    private String companyname;
    private String tenderFile;
    private String info;

    public String getBidderid() {
        return bidderid;
    }

    public void setBidderid(String bidderid) {
        this.bidderid = bidderid;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public String getTenderFile() {
        return tenderFile;
    }

    public void setTenderFile(String tenderFile) {
        this.tenderFile = tenderFile;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
