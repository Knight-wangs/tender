package com.telecom.tender.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.telecom.tender.model.*;
import com.telecom.tender.service.impl.AccountServiceImpl;
import com.telecom.tender.service.impl.DepositServiceImpl;
import com.telecom.tender.service.impl.ProjectServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

//0建项 1招标 2资格预审 3投标 4开标 5评标 6定标
@Controller
@RequestMapping("/project")
public class ProjectControl {
//    private final String INTRODUCTFILEPATH="E:\\电信研究所\\test";
//    private final String ASSESSORFILEPATH = "E:\\电信研究所\\test";
//    private final String CONTRACTFILEPATH = "E:\\电信研究所\\test";
//    private final String RESULTFILEPATH = "E:\\电信研究所\\test";
//    private final String QUALIFICATIONFILEPATH = "E:\\电信研究所\\test";
//    private final String BIDDERFORM = "E:\\电信研究所\\test";

//    private final String INTRODUCTFILEPATH="D:\\电信研究所\\test";
//    private final String ASSESSORFILEPATH = "D:\\电信研究所\\test";
//    private final String CONTRACTFILEPATH = "D:\\电信研究所\\test";
//    private final String RESULTFILEPATH = "D:\\电信研究所\\test";
//    private final String QUALIFICATIONFILEPATH = "D:\\电信研究所\\test";
//    private final String BIDDERFORM = "D:\\电信研究所\\test";

    private final String INTRODUCTFILEPATH="/root/tender/introfile";
    private final String ASSESSORFILEPATH = "/root/tender/assessorfile";
    private final String CONTRACTFILEPATH = "/root/tender/contractfile";
    private final String RESULTFILEPATH = "/root/tender/resultfile";
    private final String QUALIFICATIONFILEPATH = "/root/tender/qualifacationfile";
    private final String BIDDERFORM = "/root/tender/bidderfile";

    //0建项 1招标 2资格预审 3投标 4开标 5评标 6定标
    private final static String CREATE = "0";
    private final static String INIVITE = "1";
    private final static String QUALIFY = "2";
    private final static String BIDDER = "3";
    private final static String OPEN = "4";
    private final static String EVALUATION = "5";
    private final static String RESULT = "6";
    private final static String SUCCESS = "success";
    private final static String FAIL = "fail";
    @Resource
    private PlatformTransactionManager transactionManager;
    @Resource
    private ProjectServiceImpl projectService;
    @Resource
    private DepositServiceImpl depositService;
    @Resource
    private AccountServiceImpl accountService;
    //新建项目
    @RequestMapping("/newProject")
    @ResponseBody
    public String newProject(String name, String assessor, String industry, String area, String opentime, String tenderTime){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date openDate =sdf.parse(opentime);
            Date tenderDate = sdf.parse(tenderTime);
            if (tenderDate.after(openDate)){
                return FAIL+"招标时间大于开标时间";
            }
            Project project = new Project(name, assessor, industry, area, openDate, tenderDate,CREATE);//建项 0
            DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
            defaultTransactionDefinition
                    .setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
            TransactionStatus status = transactionManager
                    .getTransaction(defaultTransactionDefinition);
            try {
                projectService.newProject(project);
                JSONObject openTimeData = depositService.setBTime(openDate.getTime()/1000,project.getId().toString());
                projectService.saveOpenTime(String.valueOf(project.getId()),openTimeData.toString());
                String code = String.valueOf(openTimeData.get("code"));
                if (StringUtils.isNotBlank(code)&&code.equals("0")) {
                    transactionManager.commit(status);
                    return SUCCESS;
                }
                else {
                    transactionManager.rollback(status);
                    return FAIL+"开标时间上传失败";
                }
            }
            catch (Exception e){
                transactionManager.rollback(status);
                e.printStackTrace();
                return FAIL;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return FAIL;
    }
    //更新项目
    @RequestMapping("/updateProject")
    @ResponseBody
    public String updateProject(String id, String name, String assessor, String industry, String area, String opentime, String state){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date =sdf.parse(opentime);
            if(projectService.updateProject(id, name, assessor, industry, area, date, state)>0){
                return SUCCESS;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return FAIL;
    }
    //上传项目介绍文档
    @RequestMapping("/uploadIntroFile")
    @ResponseBody
    public String uploadIntroFile(String id,MultipartFile multipartFile){
        //保存文件
        String filename = multipartFile.getOriginalFilename();
        String PATH = INTRODUCTFILEPATH +File.separator+id;
        try {
            File file = new File(PATH);
            if (!file.exists()){
                file.mkdirs();
            }
            PATH = PATH + File.separator + filename;
            multipartFile.transferTo(new File(PATH));
            System.out.println(PATH);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return FAIL;
        } catch (IOException e) {
            e.printStackTrace();
            return FAIL;
        }

        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        defaultTransactionDefinition
                .setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager
                .getTransaction(defaultTransactionDefinition);
        JSONObject uploadresult = null;
        try {
            projectService.saveProjectFile(PATH,id,ProjectFileType.INTRODUCE);
            FileInputStream fileInputStream = new FileInputStream(new File(PATH));
            MockMultipartFile multipartFileOndisk = new MockMultipartFile(filename,filename,"text/plain",fileInputStream);
            uploadresult = depositService.getHashcCode(multipartFileOndisk);
        }
        catch (Exception e){
            transactionManager.rollback(status);
            return FAIL;
        }

        if (null!=uploadresult&&uploadresult.getString("msg").equals("success")){
            String hash = uploadresult.getJSONObject("data").getString("md5");
            String fileData = projectService.getFileData(uploadresult.getString("data"));
            projectService.saveProjectFileHash(hash,fileData,id,ProjectFileType.INTRODUCE);
            transactionManager.commit(status);
            return SUCCESS;
        }
        else {
            transactionManager.rollback(status);
            return FAIL;
        }
    }
    //上传项目公告文件  上传公告
    @RequestMapping("/uploadAssessorFile")
    @ResponseBody
    public String uploadAssessorFile(String id,MultipartFile multipartFile){
        //保存文件
        String filename = multipartFile.getOriginalFilename();
        String PATH = ASSESSORFILEPATH +File.separator+ id;
        try {
            File file = new File(PATH);
            if (!file.exists()){
                file.mkdirs();
            }
            PATH = PATH + File.separator + filename;
            multipartFile.transferTo(new File(PATH));
            System.out.println(PATH);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return FAIL;
        } catch (IOException e) {
            e.printStackTrace();
            return FAIL;
        }
        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        defaultTransactionDefinition
                .setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager
                .getTransaction(defaultTransactionDefinition);
        JSONObject uploadresult = null;
        try {
            projectService.saveProjectFile(PATH,id,ProjectFileType.ASSESSOR);
            FileInputStream fileInputStream = new FileInputStream(new File(PATH));
            MockMultipartFile multipartFileOndisk = new MockMultipartFile(filename,filename,"text/plain",fileInputStream);
            uploadresult = depositService.getHashcCode(multipartFileOndisk);
        }
        catch (Exception e){
            transactionManager.rollback(status);
            return FAIL;
        }

        if (null!=uploadresult&&uploadresult.getString("msg").equals("success")){
            String hash = uploadresult.getJSONObject("data").getString("md5");
            String fileData = projectService.getFileData(uploadresult.getString("data"));
            projectService.saveProjectFileHash(hash,fileData,id,ProjectFileType.ASSESSOR);
            projectService.setProjectState(id,INIVITE); //上传招标公告后，修改状态为招标
            transactionManager.commit(status);
            return SUCCESS;
        }
        else {
            transactionManager.rollback(status);
            return FAIL;
        }
    }
    //定标上传项目结果文件
    @RequestMapping("/uploadResultFile")
    @ResponseBody
    public String uploadResultFile(String id,MultipartFile multipartFile){
        //保存文件
        String filename = multipartFile.getOriginalFilename();
        String PATH = RESULTFILEPATH +File.separator+ id;
        try {
            File file = new File(PATH);
            if (!file.exists()){
                file.mkdirs();
            }
            PATH = PATH + File.separator + filename;
            multipartFile.transferTo(new File(PATH));
            System.out.println(PATH);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return FAIL;
        } catch (IOException e) {
            e.printStackTrace();
            return FAIL;
        }

        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        defaultTransactionDefinition
                .setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager
                .getTransaction(defaultTransactionDefinition);
        JSONObject uploadresult = null;
        try {
            projectService.saveProjectFile(PATH,id,ProjectFileType.RESULT);
            FileInputStream fileInputStream = new FileInputStream(new File(PATH));
            MockMultipartFile multipartFileOndisk = new MockMultipartFile(filename,filename,"text/plain",fileInputStream);
            uploadresult = depositService.getHashcCode(multipartFileOndisk);
        }
        catch (Exception e){
            transactionManager.rollback(status);
            return FAIL;
        }

        if (null!=uploadresult&&uploadresult.getString("msg").equals("success")){
            String hash = uploadresult.getJSONObject("data").getString("md5");
            String fileData = projectService.getFileData(uploadresult.getString("data"));
            projectService.saveProjectFileHash(hash,fileData,id,ProjectFileType.RESULT);
            projectService.setProjectState(id,RESULT); //上传招标结果后，修改状态为定标
            transactionManager.commit(status);
            return SUCCESS;
        }
        else {
            transactionManager.rollback(status);
            return FAIL;
        }

    }
    //定标上传合同
    @RequestMapping("/uploadContractFile")
    @ResponseBody
    public String uploadContractFile(String id,MultipartFile multipartFile){
        //保存文件
        String filename = multipartFile.getOriginalFilename();
        String PATH = CONTRACTFILEPATH +File.separator+ id;
        try {
            File file = new File(PATH);
            if (!file.exists()){
                file.mkdirs();
            }
            PATH = PATH + File.separator + filename;
            multipartFile.transferTo(new File(PATH));
            System.out.println(PATH);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return FAIL;
        } catch (IOException e) {
            e.printStackTrace();
            return FAIL;
        }

        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        defaultTransactionDefinition
                .setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager
                .getTransaction(defaultTransactionDefinition);
        JSONObject uploadresult = null;
        try {
            projectService.saveProjectFile(PATH,id,ProjectFileType.CONTRACT);
            FileInputStream fileInputStream = new FileInputStream(new File(PATH));
            MockMultipartFile multipartFileOndisk = new MockMultipartFile(filename,filename,"text/plain",fileInputStream);
            uploadresult = depositService.getHashcCode(multipartFileOndisk);
        }
        catch (Exception e){
            transactionManager.rollback(status);
            return FAIL;
        }

        if (null!=uploadresult&&uploadresult.getString("msg").equals("success")){
            String hash = uploadresult.getJSONObject("data").getString("md5");
            String fileData = projectService.getFileData(uploadresult.getString("data"));
            projectService.saveProjectFileHash(hash,fileData,id,ProjectFileType.CONTRACT);
            projectService.setProjectState(id,RESULT); //上传招标结果后，修改状态为定标
            transactionManager.commit(status);
            return SUCCESS;
        }
        else {
            transactionManager.rollback(status);
            return FAIL;
        }

    }
    //查询项目状态
    @RequestMapping("/getState")
    @ResponseBody
    public String getProjectState(String id){
        return projectService.getProjectState(id);
    }
    //修改项目状态
    @RequestMapping("/setState")
    @ResponseBody
    public String setProjectState(String id,String state){
        try {
            projectService.setProjectState(id,state);
            return projectService.setProjectState(id,state)>0?SUCCESS:FAIL;
        }catch (Exception e){
            System.out.println(e);
            return FAIL;
        }
    }
    @RequestMapping("/updateBidderInfo")
    @ResponseBody
    public String updateBidderInfo(String id,String companyname,String phonenumber,String info){
        int num = accountService.updateBidderInfo(companyname, phonenumber, info,id);
        if (num>0){
            return SUCCESS;
        }else {
            return FAIL;
        }
    }
    //上传资质审核文件
    @RequestMapping("/uploadQualificationFile")
    @ResponseBody
    public String uploadQualificationFile(String projectId,String bidderId,MultipartFile multipartFile){
        String filename = multipartFile.getOriginalFilename();
        String PATH = QUALIFICATIONFILEPATH + File.separator + bidderId + File.separator + projectId;
        try {
            File file = new File(PATH);
            if (!file.exists()){
                file.mkdirs();
            }
            PATH = PATH + File.separator + filename;
            multipartFile.transferTo(new File(PATH));
            System.out.println(PATH);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return FAIL;
        } catch (IOException e) {
            e.printStackTrace();
            return FAIL;
        }
        accountService.saveCompanyFile(PATH,projectId,bidderId);
        JSONObject uploadresult = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(PATH));
            MockMultipartFile multipartFileOndisk = new MockMultipartFile(filename,filename,"text/plain",fileInputStream);
            uploadresult = depositService.getHashcCode(multipartFileOndisk);
        }
        catch (Exception e){
            return FAIL;
        }
        if (null!=uploadresult && uploadresult.getString("msg").equals("success")){
            String hash = uploadresult.getJSONObject("data").getString("md5");
            String fileData = projectService.getFileData(uploadresult.getString("data"));
            projectService.setProjectState(projectId,QUALIFY);
            accountService.saveCompanyFileHash(hash,fileData,projectId,bidderId);
            return SUCCESS;
        }
        return FAIL;
    }
    //更新评委信息（弃用）
    @RequestMapping("/updateApproverInfo")
    @ResponseBody
    public String updateApproverInfo(String id,String info){
        if (accountService.updateApprovalInfo(info,id)>0){
            return SUCCESS;
        }
        else {
            return FAIL;
        }
    }
    //上传标书
    @RequestMapping("/uploadBidderForm")
    @ResponseBody
    public String uploadBidderForm(String projectId,String bidderId,MultipartFile multipartFile){
        String filename = multipartFile.getOriginalFilename();
        String PATH = BIDDERFORM + File.separator + bidderId;
        try {
            File file = new File(PATH);
            if (!file.exists()){
                file.mkdirs();
            }
            PATH = PATH + File.separator + filename;
            multipartFile.transferTo(new File(PATH));
            System.out.println(PATH);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return FAIL;
        } catch (IOException e) {
            e.printStackTrace();
            return FAIL;
        }
        JSONObject uploadresult = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(PATH));
            MockMultipartFile multipartFileOndisk = new MockMultipartFile(filename,filename,"text/plain",fileInputStream);
            uploadresult = depositService.getHashcCode(multipartFileOndisk);
        }
        catch (Exception e){
            return FAIL;
        }
        if (null!=uploadresult&&uploadresult.getString("msg").equals("success")){
            String hash = uploadresult.getJSONObject("data").getString("md5");
            String fileData = projectService.getFileData(uploadresult.getString("data"));
            projectService.setProjectState(projectId,BIDDER);
            projectService.uploadBidderForm(projectId,bidderId,PATH,hash,fileData);
            return SUCCESS;
        }
        return FAIL;
    }
    //评标
    @RequestMapping("/evaluation")
    @ResponseBody
    public String evaluation(String approvalId, String projectId,  String techScore,
                             String bussScore, String serverScore,String totalScore ,String comment){
        String projectState = projectService.getProjectState(projectId);
        if (!projectState.equals(EVALUATION) && !projectState.equals(OPEN)){
            return FAIL+"项目状态不可评标";
        }
        if (StringUtils.isBlank(approvalId) || StringUtils.isBlank(projectId) ||StringUtils.isBlank(techScore) ||
                StringUtils.isBlank(bussScore) || StringUtils.isBlank(serverScore) ||
                StringUtils.isBlank(totalScore) || StringUtils.isBlank(comment)){
            return FAIL+"评分不能为空";
        }
        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        defaultTransactionDefinition
                .setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus transactionstatus = transactionManager
                .getTransaction(defaultTransactionDefinition);
        try {
            int state = 0;//0是新建1是更新
            if (projectService.checkIsApproval(approvalId, projectId) > 0) {
                state = 1;
            }
            if(projectService.evaluation(approvalId, projectId, techScore, bussScore, serverScore, totalScore, comment, state) > 0){
                projectService.setProjectState(projectId,EVALUATION);//进入评标状态
                transactionManager.commit(transactionstatus);
                return SUCCESS;
            }
            else {
                transactionManager.rollback(transactionstatus);
                return FAIL;
            }
        }
        catch (Exception e){
            transactionManager.rollback(transactionstatus);
            return FAIL;
        }
    }
    @RequestMapping("/checkProjectFileHash")
    @ResponseBody
    public JSONArray checkProjectFileHash(String id, String fileType){
        JSONArray result = new JSONArray();
        result.add(projectService.checkFileHash(id,fileType));
        return result;
    }
    //检查投标方资质审核文件hash
    @RequestMapping("/checkBidderFileHash")
    @ResponseBody
    public JSONArray checkBidderFileHash(String projectId,String bidderId){
        JSONArray result = new JSONArray();
        result.add(projectService.checkBidderFile(projectId,bidderId));
        return result;
    }
    @RequestMapping("/checkApprovalViewHash")
    @ResponseBody
    public JSONArray checkApprovalViewHash(String approvalId,String projectId){
        JSONArray result = new JSONArray();
        result.add(projectService.checkApprovalHash(projectId,approvalId));
        return result;
    }
    @RequestMapping("checkTenderFile")
    @ResponseBody
    public JSONArray checkTenderFileHash(String projectId,String bidderId){
        JSONArray result = new JSONArray();
        result.add(projectService.checkTenderFile(projectId,bidderId));
        return result;
    }
    //查询所有项目
    @RequestMapping("/getAllProject")
    @ResponseBody
    public List<Project> getAllProject(){
        return projectService.getAllProject();
    }
    @RequestMapping("/getBidder")
    @ResponseBody
    public List<Bidder> getProjectInfo(String bidderId){
        Bidder bidder = accountService.getBidderInfoById(bidderId);
        List<Bidder> bidders = new ArrayList<>();
        bidders.add(bidder);
        return bidders;
    }
    //按照项目id查询投标方信息
    @RequestMapping("/getBidderInfo")
    @ResponseBody
    public List<BidderInfo> getBidderInfo(String projectId){
        return projectService.getAllBidderByProjectId(projectId);
    }

    //查询评委打分
    @RequestMapping("/getApprovalEvaluation")
    @ResponseBody
    public List<ApprovalForm> getApprovalEvaluation(String projectId, String approvalId){
        ApprovalForm approvalForm = projectService.getApprovalForm(approvalId,projectId);
        List<ApprovalForm> result = new ArrayList<>();
        result.add(approvalForm);
        return result;
    }
    @RequestMapping("/getAllOpinionByProjectId")
    @ResponseBody
    public List<ApprovalForm> getAllOpinionByProjectId(String projectId){
        return projectService.getAllOpinionByProjectId(projectId);
    }
    @RequestMapping("/downProjectFile")
    @ResponseBody
    public ResponseEntity<byte[]> downProjectFile(String id,String fileType){
        return projectService.downProjectFile(id,fileType);
    }
    @RequestMapping("/downCompanyFile")
    @ResponseBody
    public ResponseEntity<byte[]> downCompanyFile(String projectId,String bidderId){
        return accountService.downloadCompanyFile(projectId,bidderId);
    }
    @RequestMapping("/downTenderFile")
    @ResponseBody
    public ResponseEntity<byte[]> downTenderFile(String projectId,String bidderId){
        return projectService.downloadBidderFile(projectId,bidderId);
    }

    @RequestMapping("/allprofessor")
    @ResponseBody
    public String[] allprofessor(String projectId){
        try {
            JSONObject result = depositService.allprofessor(Integer.valueOf(projectId));
            String data = result.getString("data");
            String[]  professorList = data.split(",");
            return professorList;
        }
        catch (Exception e){
            return null;
        }
    }
    @RequestMapping("/makeprofessor")
    @ResponseBody
    public JSONObject makeprofessor(String projectId,Integer num){
        JSONObject results = new JSONObject();
        results.put("state","1");
        if (num!=null && num<1){
            return results;
        }
        JSONArray data = new JSONArray();
        SelectedApprover selectedApprover = projectService.getSelectedApprover(projectId);
        if (selectedApprover == null || StringUtils.isBlank(selectedApprover.getProfessorList())){
            List<String> approverIDs = new ArrayList<>();
            List<Approver> allApprover = accountService.getAllApprover();
            for (Approver approver:allApprover){
                approverIDs.add(approver.getUserid());
            }
            JSONObject makeprofessor = depositService.makeprofessor(num,Integer.valueOf(projectId),StringUtils.strip(approverIDs.toString(),"[]"));
            if (makeprofessor.getInteger("code") == 0) {
                projectService.setSelectedApprover(projectId, makeprofessor);
                selectedApprover = projectService.getSelectedApprover(projectId);
            }
        }
        if(selectedApprover!=null && StringUtils.isNotBlank(selectedApprover.getProfessorList())) {
            String[] professorList = StringUtils.strip(selectedApprover.getProfessorList(), "[]").split(",");
            for (int i = 0; i < professorList.length; i++) {
                JSONObject detail = new JSONObject();
                detail.put("progectID", projectId);
                Approver approver = accountService.getApprover(professorList[i].trim());
                detail.put("professors", approver);
                data.add(detail);
            }
        }
        results.put("data",data);
        return results;
    }
    @RequestMapping("/makeProfessorChainData")
    @ResponseBody
    public JSONArray makeProfessorChainData(String projectId){
        JSONArray result = new JSONArray();
        result.add(projectService.checkMakeProfessor(projectId));
        return result;
    }
    @RequestMapping("/getAllApprover")
    @ResponseBody
    public List<Approver> getAllApprover(String projectId) {
        return projectService.getAllApprover();
    }
    //开标结果
    @RequestMapping("/getOpenTimeResult")
    @ResponseBody
    public JSONArray getOpenTimeResult(String projectId){
        JSONArray result = new JSONArray();
        JSONObject chainData = projectService.getOpenTimeResult(projectId);
        String projectState = projectService.getProjectState(projectId);
        if (Integer.valueOf(projectState) < Integer.valueOf(OPEN)){
            JSONObject data = chainData.getJSONObject("data");
            if (!data.isEmpty()){
                data.put("verify","false");
            }
        }
        result.add(chainData);
        return result;
    }
    //开标时间上链信息
    @RequestMapping("/getOpenTimeChainData")
    @ResponseBody
    public JSONArray getOpenTimeChainData(String projectId){
        JSONArray result = new JSONArray();
        result.add(projectService.getOpenTimeTranChainData(projectId));
        return result;
    }

    //查询所有项目的资质审核信息
    @RequestMapping("/getAllQualifyData")
    @ResponseBody
    public List<ProjectBidderQualify> getAllQualifyData(){
        List<ProjectBidderQualify> projectBidderQualifyList = new ArrayList<>();
        List<BidderFile> bidderFileList = accountService.getAllBidderFile();
        for (BidderFile bidderFile:bidderFileList){
            String projectId = bidderFile.getProjectId();
            Project project = projectService.getProjectById(projectId);
            ProjectBidderQualify projectBidderQualify = new ProjectBidderQualify();
            projectBidderQualify.setProjectId(projectId);
            projectBidderQualify.setArea(project.getArea());
            projectBidderQualify.setIndustry(project.getIndustry());
            projectBidderQualify.setName(project.getName());
            projectBidderQualify.setOpentime(project.getOpentime());
            projectBidderQualify.setState(project.getState());
            projectBidderQualify.setTenderTime(project.getTenderTime());
            projectBidderQualify.setBidderFile(bidderFile);
            projectBidderQualifyList.add(projectBidderQualify);
        }
        return projectBidderQualifyList;
    }
    //按照投标方id查询所有的项目资质审核文件
    @RequestMapping("/getQualifyDataByBidderId")
    @ResponseBody
    public List<ProjectBidderQualify> getQualifyDataByBidderId(String bidderId){
        List<ProjectBidderQualify> projectBidderQualifyList = new ArrayList<>();
        List<Project> projectList = projectService.getAllProject();
        for (Project project:projectList){
            String projectId = project.getId().toString();
            ProjectBidderQualify projectBidderQualify = new ProjectBidderQualify();
            projectBidderQualify.setProjectId(projectId);
            projectBidderQualify.setArea(project.getArea());
            projectBidderQualify.setIndustry(project.getIndustry());
            projectBidderQualify.setName(project.getName());
            projectBidderQualify.setOpentime(project.getOpentime());
            projectBidderQualify.setState(project.getState());
            projectBidderQualify.setTenderTime(project.getTenderTime());
            BidderFile bidderFile = accountService.getBidderQuaFile(projectId,bidderId);
            if (bidderFile == null){
                projectBidderQualify.setIsQualify("0");
            }
            else {
                projectBidderQualify.setIsQualify("1");
            }
            projectBidderQualify.setBidderFile(bidderFile);
            projectBidderQualifyList.add(projectBidderQualify);
        }
        return projectBidderQualifyList;
    }

    //按照投标方id查询所有的项目投标文件
    @RequestMapping("/getTenderFileByBidderId")
    @ResponseBody
    public List<ProjectBidderTenderFile> getTenderFileByBidderId(String bidderId){
        List<ProjectBidderTenderFile> ProjectBidderTenderFileList = new ArrayList<>();
        List<Project> projectList = projectService.getAllProject();
        for (Project project:projectList){
            String projectId = project.getId().toString();
            ProjectBidderTenderFile ProjectBidderTenderFile = new ProjectBidderTenderFile();
            ProjectBidderTenderFile.setProjectId(projectId);
            ProjectBidderTenderFile.setArea(project.getArea());
            ProjectBidderTenderFile.setIndustry(project.getIndustry());
            ProjectBidderTenderFile.setName(project.getName());
            ProjectBidderTenderFile.setOpentime(project.getOpentime());
            ProjectBidderTenderFile.setState(project.getState());
            ProjectBidderTenderFile.setTenderTime(project.getTenderTime());
            BidderForm bidderForm = projectService.getBidderForm(projectId,bidderId);
            if (bidderForm == null){
                ProjectBidderTenderFile.setIsTender("0");
            }
            else {
                ProjectBidderTenderFile.setIsTender("1");
            }
            ProjectBidderTenderFile.setBidderForm(bidderForm);
            ProjectBidderTenderFileList.add(ProjectBidderTenderFile);
        }
        return ProjectBidderTenderFileList;
    }
    //删除项目
    @RequestMapping("/deleteProject")
    @ResponseBody
    public String deleteProject(String projectId){
        if (projectService.deleteProjectById(projectId)>0){
            return SUCCESS;
        }
        return FAIL;
    }

}
