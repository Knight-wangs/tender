package com.telecom.tender.model;

public class ApprovalForm {
    private int id;
    private String approvalid;
    private String projectId;
    private String techScore;
    private String bussScore;
    private String serverScore;
    private String totalScore;
    private String comment;
    private String opinionData;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getApprovalid() {
        return approvalid;
    }

    public void setApprovalid(String approvalid) {
        this.approvalid = approvalid;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getTechScore() {
        return techScore;
    }

    public void setTechScore(String techScore) {
        this.techScore = techScore;
    }

    public String getBussScore() {
        return bussScore;
    }

    public void setBussScore(String bussScore) {
        this.bussScore = bussScore;
    }

    public String getServerScore() {
        return serverScore;
    }

    public void setServerScore(String serverScore) {
        this.serverScore = serverScore;
    }

    public String getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(String totalScore) {
        this.totalScore = totalScore;
    }

    public String getOpinionData() {
        return opinionData;
    }

    public String getOption() {
        return comment;
    }

    public void setOption(String comment) {
        this.comment = comment;
    }

    public void setOpinionData(String opinionData) {
        this.opinionData = opinionData;
    }
}
