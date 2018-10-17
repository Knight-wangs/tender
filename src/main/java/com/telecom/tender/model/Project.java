package com.telecom.tender.model;

import java.util.Date;

public class Project {
    Integer id;
    String name;
    private String introductFile;
    private String IntroductFileHash;
    String assessor;
    String industry;
    String area;
    String assessorFile;
    String assessorFileHash;
    Date opentime;
    String openTimeData;
    Date tenderTime;
    String state;
    String resultsFile;
    String resultsFileHash;
    String contractFile;
    String contractFileHash;

    public Project() {
    }

    public Project(Integer id, String name, String introductFile, String introductFileHash, String assessor, String industry, String area, String assessorFile, String assessorFileHash, Date opentime, String openTimeData, Date tenderTime, String state, String resultsFile, String resultsFileHash, String contractFile, String contractFileHash) {
        this.id = id;
        this.name = name;
        this.introductFile = introductFile;
        IntroductFileHash = introductFileHash;
        this.assessor = assessor;
        this.industry = industry;
        this.area = area;
        this.assessorFile = assessorFile;
        this.assessorFileHash = assessorFileHash;
        this.opentime = opentime;
        this.openTimeData = openTimeData;
        this.tenderTime = tenderTime;
        this.state = state;
        this.resultsFile = resultsFile;
        this.resultsFileHash = resultsFileHash;
        this.contractFile = contractFile;
        this.contractFileHash = contractFileHash;
    }

    public Project(String name, String assessor, String industry, String area, Date opentime, Date tenderTime, String state) {
        this.name = name;
        this.assessor = assessor;
        this.industry = industry;
        this.area = area;
        this.opentime = opentime;
        this.tenderTime = tenderTime;
        this.state = state;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAssessor() {
        return assessor;
    }

    public void setAssessor(String assessor) {
        this.assessor = assessor;
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

    public String getAssessorFile() {
        return assessorFile;
    }

    public void setAssessorFile(String assessorFile) {
        this.assessorFile = assessorFile;
    }

    public String getAssessorFileHash() {
        return assessorFileHash;
    }

    public void setAssessorFileHash(String assessorFileHash) {
        this.assessorFileHash = assessorFileHash;
    }

    public String getResultsFile() {
        return resultsFile;
    }

    public void setResultsFile(String resultsFile) {
        this.resultsFile = resultsFile;
    }

    public String getResultsFileHash() {
        return resultsFileHash;
    }

    public void setResultsFileHash(String resultsFileHash) {
        this.resultsFileHash = resultsFileHash;
    }

    public String getContractFile() {
        return contractFile;
    }

    public void setContractFile(String contractFile) {
        this.contractFile = contractFile;
    }

    public String getContractFileHash() {
        return contractFileHash;
    }

    public void setContractFileHash(String contractFileHash) {
        this.contractFileHash = contractFileHash;
    }

    public Date getOpentime() {
        return opentime;
    }

    public void setOpentime(Date opentime) {
        this.opentime = opentime;
    }

    public String getIntroductFile() {
        return introductFile;
    }

    public void setIntroductFile(String introductFile) {
        this.introductFile = introductFile;
    }

    public String getIntroductFileHash() {
        return IntroductFileHash;
    }

    public void setIntroductFileHash(String introductFileHash) {
        IntroductFileHash = introductFileHash;
    }

    public String getOpenTimeData() {
        return openTimeData;
    }

    public void setOpenTimeData(String openTimeData) {
        this.openTimeData = openTimeData;
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
}
