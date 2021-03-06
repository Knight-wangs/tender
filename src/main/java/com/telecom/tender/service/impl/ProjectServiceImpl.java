package com.telecom.tender.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.telecom.tender.dao.AccountMapper;
import com.telecom.tender.dao.ProjectMapper;
import com.telecom.tender.model.*;
import com.telecom.tender.service.ProjectService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;
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
        List<Project> projectList = projectMapper.getAllProject();
        for (Project project:projectList){
            project.setIntroFileState("0");
            project.setAssessorFileState("0");
            project.setContractFileState("0");
            project.setResultsFileState("0");
            if (StringUtils.isNotBlank(project.getIntroFile())){
                project.setIntroFileState("1");
            }
            if (StringUtils.isNotBlank(project.getAssessorFile())){
                project.setAssessorFileState("1");
            }
            if (StringUtils.isNotBlank(project.getContractFile())){
                project.setContractFileState("1");
            }
            if (StringUtils.isNotBlank(project.getResultsFile())){
                project.setResultsFileState("1");
            }
        }
        return projectList;
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

    @Override
    public int deleteProjectById(String id) {
        return projectMapper.deleteProjectById(id);
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
        ev_json.put(EvaluationDataType.projectId,projectId);
        ev_json.put(EvaluationDataType.approvalId,approvalId);
        ev_json.put(EvaluationDataType.techScore,techScore);
        ev_json.put(EvaluationDataType.bussScore,bussScore);
        ev_json.put(EvaluationDataType.serverScore,serverScore);
        ev_json.put(EvaluationDataType.totalScore,totalScore);
        ev_json.put(EvaluationDataType.comment,comment);
        file_json.put("files",new ArrayList<String>());
        JSONObject result = depositService.evidencestore("评委意见","招投标平台",ev_json.toString(),file_json.toString());
        if (state == 0) {
            return projectMapper.saveApprovalOption(approvalId,projectId,techScore,bussScore,serverScore,totalScore,comment,result.toString());
        }
        return projectMapper.updateevaluation(approvalId,projectId,techScore,bussScore,serverScore,totalScore,comment,result.toString());

    }

    @Override
    public JSONObject checkFileHash(String id, String fileType) {
        String PATH ;
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
    public JSONObject checkBidderFile(String projectId,String bidderId) {
        String PATH = accountMapper.getBidderFile(projectId, bidderId);
        try {
            FileInputStream fileInputStream = new FileInputStream(PATH);
            File file = new File(PATH);
            try {
                MultipartFile multipartFile = new MockMultipartFile(file.getName(),fileInputStream);
                String storeHash = accountMapper.getBidderFileHash(projectId,bidderId);
                String fileData = accountMapper.getBidderFileData(projectId,bidderId);
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
        if (evidenceData!=null ){
            JSONObject verifyData = depositService.verify(id,evidenceData.getString("evJson"));
            if ((("success").equals(verifyData.getString("msg"))) && checkEvaluationData(approvalForm,evidenceData.getJSONObject("evJson"))) {
                result.put("notChanged", true);
            }
            else {
                result.put("notChanged",false);
            }
        }
        else {
            result.put("notChanged",false);
        }
        if (StringUtils.isNotBlank(hash)) {
            JSONObject chainData = depositService.chainblock(hash).getJSONObject("data");
            JSONObject showChainData = new JSONObject();
            showChainData.put("number", chainData.getString("number"));
            showChainData.put("evidenceaddress", "http://123.207.167.245/evchain/app/evidence/store");
            showChainData.put("timestamp", chainData.getString("timestamp"));
            showChainData.put("transactionshash", chainData.getJSONArray("transactions").getJSONObject(0).getString("hash"));
            result.put("chaindata", showChainData);
        }
        return result;
    }

    private boolean checkEvaluationData(ApprovalForm approvalForm,JSONObject evidenceData){
        if (approvalForm.getProjectId().equals(evidenceData.getString(EvaluationDataType.projectId)) &&
                (approvalForm.getApprovalid().equals(evidenceData.getString(EvaluationDataType.approvalId))) &&
                (approvalForm.getTechScore().equals(evidenceData.getString(EvaluationDataType.techScore))) &&
                (approvalForm.getBussScore().equals(evidenceData.getString(EvaluationDataType.bussScore)))  &&
                (approvalForm.getServerScore().equals(evidenceData.getString(EvaluationDataType.serverScore))) &&
                (approvalForm.getTotalScore().equals(evidenceData.getString(EvaluationDataType.totalScore))) &&
                (approvalForm.getComment().equals(evidenceData.getString(EvaluationDataType.comment)))){
            return true;
        }
        return false;

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
            downloadFielName = URLEncoder.encode(file.getName(),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        headers.setContentDispositionFormData("attachment", downloadFielName);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        List<String> exposeHeaders = new ArrayList<>();
        exposeHeaders.add("Content-Disposition");
        headers.setAccessControlExposeHeaders(exposeHeaders);
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
            downloadFielName = URLEncoder.encode(file.getName(),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        headers.setContentDispositionFormData("attachment", downloadFielName);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        List<String> exposeHeaders = new ArrayList<>();
        exposeHeaders.add("Content-Disposition");
        headers.setAccessControlExposeHeaders(exposeHeaders);
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
        if (StringUtils.isNotBlank(evidenceData)) {
            JSONObject evidenceJSON = JSONObject.parseObject(evidenceData);
            String transactionId = evidenceJSON.getJSONObject("data").getString("transactionId");
            JSONObject chainData = depositService.chainblock(transactionId).getJSONObject("data");
            JSONObject showChainData = new JSONObject();
            showChainData.put("number", chainData.getString("number"));
            showChainData.put("evidenceaddress", "http://123.207.167.245/storage/api/files/upload");
            showChainData.put("timestamp", chainData.getString("timestamp"));
            showChainData.put("transactionshash", chainData.getJSONArray("transactions").getJSONObject(0).getString("hash"));
            result.put("chaindata", showChainData);
        }
        return result;
    }

    @Override
    public List<Approver> getAllApprover() {
        return projectMapper.getAllApprover();
    }

    @Override
    public int setSelectedApprover(String projectId,JSONObject makeprofess) {

        JSONObject selectResult = depositService.getselectprofessor(Integer.valueOf(projectId));
        if (selectResult!=null) {
            JSONArray professorList = selectResult.getJSONArray("data");
            while (professorList == null){
                try {
                    Thread.sleep(1*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                selectResult = depositService.getselectprofessor(Integer.valueOf(projectId));
                professorList = selectResult.getJSONArray("data");
            }
            List<String> professors = new ArrayList<>();
            for (int i=0;i<professorList.size();i++) {
                professors.add(professorList.getString(i));
            }
            return projectMapper.setSelectedApprover(projectId, professors.toString(), makeprofess.toJSONString());
        }

        return 0;
    }

    @Override
    public JSONObject checkMakeProfessor(String projectId) {
        JSONObject result = new JSONObject();
        JSONObject showChainData = new JSONObject();
        SelectedApprover selectedApprover = projectMapper.getSelectedApprover(projectId);
        if (StringUtils.isNotBlank(selectedApprover.getChainData())) {
            JSONObject makeProfessor = JSONObject.parseObject(selectedApprover.getChainData());
            JSONObject data  = makeProfessor.getJSONObject("data");
            JSONObject chainData = depositService.chainblock(data.getString("transactionId")).getJSONObject("data");
            showChainData.put("number", chainData.getString("number"));
            showChainData.put("evidenceaddress", "http://123.207.167.245/evchain/app/evidence/makeprofessor");
            showChainData.put("timestamp", chainData.getString("timestamp"));
            showChainData.put("transactionshash", chainData.getJSONArray("transactions").getJSONObject(0).getString("hash"));
        }
        result.put("projectId",projectId);
        result.put("chainData",showChainData);
        return result;
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
        if(StringUtils.isNotBlank(project.getOpenTimeData())) {
            JSONObject openTimeData = JSONObject.parseObject(project.getOpenTimeData());
            String transactionId = openTimeData.getJSONObject("data").getString("transactionId");
            JSONObject chainData = depositService.chainblock(transactionId).getJSONObject("data");
            JSONObject showChainData = new JSONObject();
            showChainData.put("number", chainData.getString("number"));
            showChainData.put("evidenceaddress", "http://123.207.167.245/evchain/app/evidence/setbtime");
            showChainData.put("timestamp", chainData.getString("timestamp"));
            showChainData.put("transactionshash", chainData.getJSONArray("transactions").getJSONObject(0).getString("hash"));
            result.put("chaindata", showChainData);
        }
        return result;
    }

    @Override
    public JSONObject checkTenderFile(String projectId, String bidderId) {
        JSONObject result = new JSONObject();
        BidderForm bidderForm = projectMapper.getBidderForm(projectId,bidderId);
        String PATH = bidderForm.getTenderFile();
        if (StringUtils.isNotBlank(PATH))
        try {
            FileInputStream fileInputStream = new FileInputStream(PATH);
            File file = new File(PATH);
            try {
                MultipartFile multipartFile = new MockMultipartFile(file.getName(),fileInputStream);
                String tenderFileHash = bidderForm.getTenderFileHash();
                String tenderFileData = bidderForm.getTenderFileData();
                return checkFile(multipartFile,tenderFileHash,tenderFileData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public BidderForm getBidderForm(String projectId, String bidderid) {
        return projectMapper.getBidderForm(projectId,bidderid);
    }
}
