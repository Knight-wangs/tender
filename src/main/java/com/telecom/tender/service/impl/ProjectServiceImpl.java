package com.telecom.tender.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.telecom.tender.dao.AccountMapper;
import com.telecom.tender.dao.ProjectMapper;
import com.telecom.tender.model.*;
import com.telecom.tender.service.ProjectService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.io.FileUtils;

@Service("projectService")
public class ProjectServiceImpl implements ProjectService {
    @Autowired
    ProjectMapper projectMapper;
    @Autowired
    AccountMapper accountMapper;
    @Autowired
    DepositServiceImpl depositService;

    @Override
    public List<Project> getAllProject() {
        return projectMapper.getAllProject();
    }

    @Override
    public List<Project> getAllOpenProject() {
        List<Project>  projectList= projectMapper.getAllOpenProject();
        return  projectList;
    }

    @Override
    public Project getProjectById(String projectID) {
        return projectMapper.getProjectById(projectID);
    }

//    @Override
//    public int newProject(String name, String assessor, String industry, String area, Date opentime,Date tenderTime, String state) {
//        return projectMapper.newProject(name, assessor, industry, area, opentime, tenderTime, state);
//    }

    @Override
    public int newProject(Project project) {
        return  projectMapper.newProject(project);
    }

    @Override
    public int updateProject(String id, String name, String assessor, String industry, String area, Date opentimme, String state) {
        return projectMapper.updateProject(id, name, assessor, industry, area, opentimme, state);
    }

    @Override
    public int saveOpenTime(String id, String openTimeData) {
        return projectMapper.updateOpenTimeData(id,openTimeData);
    }

    @Override
    public String getProjectState(String id) {
        return projectMapper.getProjectState(id);
    }

    @Override
    public int setProjectState(String id,String state) {
        return projectMapper.setProjectState(id,state);
    }


    @Override
    public int saveProjectFile(String filePath, String id, String fileType ) {
        if (fileType.equals(ProjectFileType.INTRODUCE)){
            return projectMapper.setIntroFile(filePath,id);
        }
        if (fileType.equals(ProjectFileType.ASSESSOR)){
            return projectMapper.setAssessorFile(filePath,id);
        }
        if (fileType.equals(ProjectFileType.RESULT)) {
            return projectMapper.setResultsFile(filePath,id);
        }
        if (fileType.equals(ProjectFileType.CONTRACT)){
            return projectMapper.saveContractFile(filePath,id);
        }
        return 0;
    }

    @Override
    public int saveProjectFileHash(String fileHash, String fileData,String id, String fileType) {
        if (fileType.equals(ProjectFileType.INTRODUCE)){
            return projectMapper.saveIntroFileHash(fileHash,id,fileData);
        }
        if (fileType.equals(ProjectFileType.ASSESSOR)){
            return projectMapper.saveAssessoFileHash(fileHash,id,fileData);
        }
        if (fileType.equals(ProjectFileType.RESULT)) {
            return projectMapper.saveResultsFileHash(fileHash,id,fileData);
        }
        if (fileType.equals(ProjectFileType.CONTRACT)){
            return projectMapper.saveContractFileHash(fileHash,id,fileData);
        }
        return 0;
    }

    @Override
    public int uploadBidderForm(String projectid, String bidderid, String tenderFile, String tenderFileHash,String tenderFileData) {
        if(StringUtils.isNotBlank(projectMapper.getTenderFile(projectid, bidderid))){
            return projectMapper.uploadTenderFile(projectid, bidderid, tenderFile, tenderFileHash);
        }
        return projectMapper.uploadBidderForm(projectid, bidderid, tenderFile, tenderFileHash,tenderFileData);
    }

    @Override
    public int evaluation(String approvalId, String projectId,  String techScore,
                          String bussScore, String serverScore,String totalScore, String comment,int state) {
        JSONObject ev_json = new JSONObject();
        JSONObject file_json = new JSONObject();
        ev_json.put("projectId",projectId);
        ev_json.put("approvalId",approvalId);
        ev_json.put("techScore",techScore);
        ev_json.put("bussScore",bussScore);
        ev_json.put("serverScore",serverScore);
        ev_json.put("totalScore",totalScore);
        ev_json.put("comment",comment);
        file_json.put("files",new ArrayList<String>());
        JSONObject result = depositService.evidencestore("评委意见","招投标平台",ev_json.toString(),file_json.toString());
        if (state == 0) {
            return projectMapper.saveApprovalOption(approvalId,projectId,techScore,bussScore,serverScore,totalScore,comment,result.toString());
        }
        return projectMapper.updateevaluation(approvalId,projectId,techScore,bussScore,serverScore,totalScore,comment,result.toString());

    }

    @Override
    public JSONObject checkFileHash(String id, String fileType) {
        String PATH = "";
        Project project = projectMapper.getProjectById(id);
        if (fileType.equals(ProjectFileType.INTRODUCE)){
            PATH = projectMapper.getIntroFilePath(id);
            try {
                FileInputStream fileInputStream = new FileInputStream(PATH);
                File file = new File(PATH);
                try {
                    MultipartFile multipartFile = new MockMultipartFile(file.getName(),fileInputStream);
                    String storeHash = projectMapper.getintroFileHash(id);
                    String evidengceData = project.getIntroFileData();
                    return checkFile(multipartFile,storeHash,evidengceData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        if (fileType.equals(ProjectFileType.ASSESSOR)){
            PATH = projectMapper.getAssessorFilePath(id);
            try {
                FileInputStream fileInputStream = new FileInputStream(PATH);
                File file = new File(PATH);
                try {
                    MultipartFile multipartFile = new MockMultipartFile(file.getName(),fileInputStream);
                    String storeHash = projectMapper.getAssessorFileHash(id);
                    String evidengceData = project.getAssessorFileData();
                    return checkFile(multipartFile,storeHash,evidengceData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        if (fileType.equals(ProjectFileType.RESULT)) {
            PATH = projectMapper.getresultsFilePath(id);
            try {
                FileInputStream fileInputStream = new FileInputStream(PATH);
                File file = new File(PATH);
                try {
                    MultipartFile multipartFile = new MockMultipartFile(file.getName(),fileInputStream);
                    String storeHash = projectMapper.getResultsFileHash(id);
                    String evidengceData = project.getResultsFileData();
                    return checkFile(multipartFile,storeHash,evidengceData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        if (fileType.equals(ProjectFileType.CONTRACT)){
            PATH = projectMapper.getcontractFilePath(id);
            try {
                FileInputStream fileInputStream = new FileInputStream(PATH);
                File file = new File(PATH);
                try {
                    MultipartFile multipartFile = new MockMultipartFile(file.getName(),fileInputStream);
                    String storeHash = projectMapper.getContractFileHash(id);
                    String evidengceData = project.getContractFileData();
                    return checkFile(multipartFile,storeHash,evidengceData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public JSONObject checkBidderFile(String id) {
        String PATH = accountMapper.getBidderFile(id);
        try {
            FileInputStream fileInputStream = new FileInputStream(PATH);
            File file = new File(PATH);
            try {
                MultipartFile multipartFile = new MockMultipartFile(file.getName(),fileInputStream);
                String storeHash = accountMapper.getBidderFileHash(id);
                String fileData = accountMapper.getBidderFileData(id);
                return checkFile(multipartFile,storeHash,fileData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int checkIsApproval(String approvalid, String projectid) {
        return projectMapper.checkIsApproval(approvalid, projectid);
    }

    @Override
    public List<BidderInfo> getAllBidderByProjectId(String projectid) {
        return projectMapper.getAllBidderByProjectId(projectid);
    }

    @Override
    public JSONObject checkApprovalHash(String projectId, String approvalId) {
        JSONObject result = new JSONObject();
        ApprovalForm approvalForm = projectMapper.getApprovalForm(approvalId,projectId);
        String opinionData = approvalForm.getOpinionData();
        JSONObject opinionJson = JSON.parseObject(opinionData);
        JSONObject data = (JSONObject) opinionJson.get("data");
        String id = data.getString("id");
        String hash = data.getString("transactionId");
        JSONObject evidence = depositService.getEvidengceDetail(id);
        JSONObject evidenceData = (JSONObject) evidence.get("data");
        if (evidence!=null && evidenceData!=null ){
            JSONObject verifyData = depositService.verify(id,evidenceData.getString("evJson"));
            if (verifyData.getString("msg").equals("success")) {
                result.put("notChanged", true);
            }
            else {
                result.put("notChanged",false);
            }
        }
        else {
            result.put("notChanged",false);
        }
        JSONObject chainData = depositService.chainblock(hash).getJSONObject("data");

        JSONObject showChainData = new JSONObject();
        showChainData.put("number",chainData.getString("number"));
        showChainData.put("blockhash",chainData.getString("hash"));
        showChainData.put("timestamp",chainData.getString("timestamp"));
        showChainData.put("transactionshash",chainData.getJSONArray("transactions").getJSONObject(0).getString("hash"));
        result.put("chaindata",showChainData);
        return result;
    }

    @Override
    public List<ApprovalForm> getAllOpinionByProjectId(String projectid) {
        return projectMapper.getAllOpinionByProjectId(projectid);
    }

    @Override
    public ResponseEntity<byte[]> downProjectFile(String id, String fileType) {
        String PATH = "";
        if (fileType.equals(ProjectFileType.INTRODUCE)){
            PATH = projectMapper.getIntroFilePath(id);
        }
        if (fileType.equals(ProjectFileType.ASSESSOR)){
            PATH = projectMapper.getAssessorFilePath(id);
        }
        if (fileType.equals(ProjectFileType.RESULT)) {
            PATH = projectMapper.getresultsFilePath(id);
        }
        if (fileType.equals(ProjectFileType.CONTRACT)){
            PATH = projectMapper.getcontractFilePath(id);
        }
        if (StringUtils.isBlank(PATH)){
            return null;
        }
        File file = new File(PATH);
        HttpHeaders headers = new HttpHeaders();
        String downloadFielName = null;
        try {
            downloadFielName = new String(file.getName().getBytes("UTF-8"),
                    "iso-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        headers.setContentDispositionFormData("attachment", downloadFielName);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        try {
            return new ResponseEntity<byte[]>(
                    FileUtils.readFileToByteArray(file), headers,
                    HttpStatus.CREATED);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ApprovalForm getApprovalForm(String approvalid, String projectid) {
        return projectMapper.getApprovalForm(approvalid,projectid);
    }

    @Override
    public ResponseEntity<byte[]> downloadBidderFile(String projectId, String bidderId) {
        String PATH = projectMapper.getTenderFile(projectId,bidderId);
        if (StringUtils.isBlank(PATH)){
            return null;
        }
        File file = new File(PATH);
        HttpHeaders headers = new HttpHeaders();
        String downloadFielName = null;
        try {
            downloadFielName = new String(file.getName().getBytes("UTF-8"),
                    "iso-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        headers.setContentDispositionFormData("attachment", downloadFielName);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        try {
            return new ResponseEntity<byte[]>(
                    FileUtils.readFileToByteArray(file), headers,
                    HttpStatus.CREATED);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getFileData(String filedata) {
        JSONObject ev_json = new JSONObject();
        JSONObject file_json = new JSONObject();
        ev_json.put("存证对象","文件");
        file_json.put("files",filedata);
        JSONObject result = depositService.evidencestore("文件存证","招投标平台",ev_json.toString(),file_json.toString());
        return result.toJSONString();
    }

    private JSONObject checkFile(MultipartFile multipartFile, String storeHash,String evidenceData){
        JSONObject result = new JSONObject();
        result.put("notChanged",false);
        result.put("originalHash",storeHash);
        JSONObject hashCode = depositService.getHashcCode(multipartFile);
        if (hashCode.getString("msg").equals("success")){
            String hash = hashCode.getJSONObject("data").getString("md5");
            if (hash.equals(storeHash)){
                result.put("notChanged",true);
            }
            result.put("chainHash",hash);
        }
        JSONObject evidenceJSON = JSONObject.parseObject(evidenceData);
        String transactionId = evidenceJSON.getJSONObject("data").getString("transactionId");
        JSONObject chainData = depositService.chainblock(transactionId).getJSONObject("data");
        JSONObject showChainData = new JSONObject();
        showChainData.put("number",chainData.getString("number"));
        showChainData.put("blockhash",chainData.getString("hash"));
        showChainData.put("timestamp",chainData.getString("timestamp"));
        showChainData.put("transactionshash",chainData.getJSONArray("transactions").getJSONObject(0).getString("hash"));
        result.put("chaindata",showChainData);
        return result;
    }

    @Override
    public List<Approver> getAllApprover() {
        return projectMapper.getAllApprover();
    }

    @Override
    public int setSelectedApprover(String projectId, String professorList, String chainData) {
        return projectMapper.setSelectedApprover(projectId, professorList, chainData);
    }

    @Override
    public SelectedApprover getSelectedApprover(String projectId) {
        return projectMapper.getSelectedApprover(projectId);
    }

    @Override
    public JSONObject getSelectedApproverChainData(String projectId, String professorList) {
        JSONObject ev_json = new JSONObject();
        JSONObject file_json = new JSONObject();
        ev_json.put("专家列表",professorList);
        ev_json.put("项目ID",projectId);
        file_json.put("files",new ArrayList<String>());
        JSONObject evidenceresult = depositService.evidencestore("项目专家列表存证","招投标平台",ev_json.toString(),file_json.toString());
//        String transactionId = evidenceresult.getJSONObject("data").getString("transactionId");
//        JSONObject chainData = depositService.chainblock(transactionId).getJSONObject("data");
        return evidenceresult;
    }

    @Override
    public JSONObject getOpenTimeResult(String projectId) {
        Project project = projectMapper.getProjectById(projectId);
        JSONObject openTimeData = JSONObject.parseObject(project.getOpenTimeData());
        String evID = openTimeData.getJSONObject("data").getString("evID");
        return depositService.verifybtime(evID);
    }

    @Override
    public JSONObject getOpenTimeTranChainData(String projectId) {
        JSONObject result = new JSONObject();
        Project project = projectMapper.getProjectById(projectId);
        JSONObject openTimeData = JSONObject.parseObject(project.getOpenTimeData());
        String transactionId = openTimeData.getJSONObject("data").getString("transactionId");
        JSONObject chainData = depositService.chainblock(transactionId);
        JSONObject showChainData = new JSONObject();
        showChainData.put("number",chainData.getString("number"));
        showChainData.put("blockhash",chainData.getString("hash"));
        showChainData.put("timestamp",chainData.getString("timestamp"));
        showChainData.put("transactionshash",chainData.getJSONArray("transactions").getJSONObject(0).getString("hash"));
        result.put("chaindata",showChainData);
        return result;
    }
}
