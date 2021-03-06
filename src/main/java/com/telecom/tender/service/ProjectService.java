package com.telecom.tender.service;

import com.alibaba.fastjson.JSONObject;
import com.telecom.tender.model.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;

public interface ProjectService {
    //查询所有项目
    List<Project> getAllProject();

    //查询所有等待开标的项目
    List<Project> getAllOpenProject();

    //根据id查询项目的信息
    Project getProjectById(String projectID);

    //根据项目id删除项目
    int deleteProjectById(String id);
    //新建项目
//    public int newProject(String name, String assessor, String industry, String area, Date opentimme, Date tenderTime,String state);
    int newProject(Project project);

    //更新项目
    int updateProject(String id, String name, String assessor, String industry, String area, Date opentimme, String state);

    //保存项目开标时间
    int saveOpenTime(String id, String openTimeData);

    //获取项目状态
    String getProjectState(String id);

    //设置项目状态
    int setProjectState(String id, String state);

    //保存项目文件
    int saveProjectFile(String filePath, String id, String fileType);

    //保存项目文件hash
    int saveProjectFileHash(String fileHash, String fileData, String id, String fileType);

    int uploadBidderForm(String projectid, String bidderid, String tenderFile, String tenderFileHash, String tenderFileData);

    //评委点评
    int evaluation(String approvalId, String projectId, String techScore,
                   String bussScore, String serverScore, String totalScore, String option, int state);

    //检查项目文件hash
    JSONObject checkFileHash(String id, String fileType);

    //检查投标人资质文件hash
    JSONObject checkBidderFile(String projectId,String bidderId);

    //查询是否评委已经评估
    int checkIsApproval(String approvalid, String projectid);

    //根据项目id获取所有投标方
    List<BidderInfo> getAllBidderByProjectId(String projectid);

    //检查投标人意见hash
    JSONObject checkApprovalHash(String projectId, String approvalId);

    //根据项目id查询所有评委意见
    List<ApprovalForm> getAllOpinionByProjectId(String projectid);

    //下载项目文件
    ResponseEntity<byte[]> downProjectFile(String id, String fileType);

    //查询评委对项目的观点
    ApprovalForm getApprovalForm(String approvalid, String projectid);

    //下载投标方项目标书
    ResponseEntity<byte[]> downloadBidderFile(String projectId, String bidderId);

    //查询保存文件上链信息
    String getFileData(String filedata);

    //查询所有的评委
    List<Approver> getAllApprover();

    //设置项目筛选评委信息
    int setSelectedApprover(String projectId, JSONObject makeprofess);

    //查询项目评委信息
    SelectedApprover getSelectedApprover(String projectId);

    //项目筛选评委信息上链
    JSONObject getSelectedApproverChainData(String projectId, String professorList);

    //查询项目开标时间智能合约所自动执行信息
    JSONObject getOpenTimeResult(String projectId);

    //查询项目开标时间智能合约链上信息
    JSONObject getOpenTimeTranChainData(String projectId);

    //检查投标人投标文件
    public JSONObject checkTenderFile(String projectId, String bidderId);

    //查询执行筛选专家合约的链上信息
    JSONObject checkMakeProfessor(String projectId);

    //根据项目id和投标人id,查询项目投标信息
    BidderForm getBidderForm(String projectId,String bidderid);
}
