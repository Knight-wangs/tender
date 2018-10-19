package com.telecom.tender.model;

public class SelectedApprover {
    private Integer projectId;
    private String professorList;
    private String chainData;

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getProfessorList() {
        return professorList;
    }

    public void setProfessorList(String professorList) {
        this.professorList = professorList;
    }

    public String getChainData() {
        return chainData;
    }

    public void setChainData(String chainData) {
        this.chainData = chainData;
    }
}
