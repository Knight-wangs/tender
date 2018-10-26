package com.telecom.tender.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class Project {
    private Integer id;
    private String name;
    private String introFile;
    private String introFileHash;
    private String introFileData;
    private String assessor;
    private String industry;
    private String area;
    private String assessorFile;
    private String assessorFileHash;
    private String assessorFileData;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date opentime;
    String openTimeData;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date tenderTime;
    private String state;
    private String resultsFile;
    private String resultsFileHash;
    private String resultsFileData;
    private String contractFile;
    private String contractFileHash;
    private String contractFileData;

    public Project() {
    }

    public Project(Integer id, String name, String introFile, String introFileHash, String introFileData, String assessor, String industry, String area, String assessorFile, String assessorFileHash, String assessorFileData, Date opentime, String openTimeData, Date tenderTime, String state, String resultsFile, String resultsFileHash, String resultsFileData, String contractFile, String contractFileHash, String contractFileData) {
        this.id = id;
        this.name = name;
        this.introFile = introFile;
        this.introFileHash = introFileHash;
        this.introFileData = introFileData;
        this.assessor = assessor;
        this.industry = industry;
        this.area = area;
        this.assessorFile = assessorFile;
        this.assessorFileHash = assessorFileHash;
        this.assessorFileData = assessorFileData;
        this.opentime = opentime;
        this.openTimeData = openTimeData;
        this.tenderTime = tenderTime;
        this.state = state;
        this.resultsFile = resultsFile;
        this.resultsFileHash = resultsFileHash;
        this.resultsFileData = resultsFileData;
        this.contractFile = contractFile;
        this.contractFileHash = contractFileHash;
        this.contractFileData = contractFileData;
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

    public String getIntroFile() {
        return introFile;
    }

    public void setIntroFile(String introFile) {
        this.introFile = introFile;
    }

    public String getIntroFileHash() {
        return introFileHash;
    }

    public void setIntroFileHash(String introFileHash) {
        this.introFileHash = introFileHash;
    }

    public String getIntroFileData() {
        return introFileData;
    }

    public void setIntroFileData(String introFileData) {
        this.introFileData = introFileData;
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

    public String getAssessorFileData() {
        return assessorFileData;
    }

    public void setAssessorFileData(String assessorFileData) {
        this.assessorFileData = assessorFileData;
    }

    public String getResultsFileData() {
        return resultsFileData;
    }

    public void setResultsFileData(String resultsFileData) {
        this.resultsFileData = resultsFileData;
    }

    public String getContractFileData() {
        return contractFileData;
    }

    public void setContractFileData(String contractFileData) {
        this.contractFileData = contractFileData;
    }
}
