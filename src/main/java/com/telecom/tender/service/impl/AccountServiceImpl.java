package com.telecom.tender.service.impl;

import com.telecom.tender.dao.AccountMapper;
import com.telecom.tender.model.*;
import com.telecom.tender.service.AccountService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Service("accountService")
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountMapper accountMapper;
    @Override
    public Approver getApproverByUserIdAndpwd(String userid, String password) {
        return accountMapper.getApproverByUserIdAndpwd(userid,password);
    }

    @Override
    public Bidder getBidderByUserIdAndpwd(String userid, String password) {
        return accountMapper.getBidderByUserIdAndpwd(userid,password);
    }

    @Override
    public Assessor getAssessorByUserIdAndpwd(String userid, String password) {
        return accountMapper.getAssessorByUserIdAndpwd(userid,password);
    }

    @Override
    public int getApproverByUserId(String userid) {
        return accountMapper.getApproverByUserId(userid);
    }

    @Override
    public int getBidderByUserId(String userId) {
        return accountMapper.getBidderByUserId(userId);
    }

    @Override
    public int getAssessorByUserId(String userId) {
        return accountMapper.getAssessorByUserId(userId);
    }

    @Override
    public int regiserApprover(String userId, String username, String password) {
        return accountMapper.regiserApprover(userId,username,password);
    }

    @Override
    public int regiserBidder(String userId, String username, String password) {
        return accountMapper.regiserBidder(userId,username,password);
    }

    @Override
    public int regiserAssessor(String userId, String username, String password) {
        return accountMapper.regiserAssessor(userId,username,password);
    }

    @Override
    public int updateBidderInfo(String companyname, String phonenumber, String info, String id) {
        return accountMapper.updateBidderInfo(companyname,phonenumber,info,id);
    }

    @Override
    public int saveCompanyFile(String fileposition, String projectId,String bidderId) {
        BidderFile bidderFile = accountMapper.getBidderQuaFile(projectId, bidderId);
        if (bidderFile==null){
            return accountMapper.saveCompanyFile(fileposition, projectId,bidderId);
        }
        else {
            return accountMapper.updateCompanyFile(fileposition,projectId,bidderId);
        }
    }

    @Override
    public int saveCompanyFileHash(String filehash, String fileData,String projectId,String bidderId) {
        BidderFile bidderFile = accountMapper.getBidderQuaFile(projectId, bidderId);
        if (bidderFile == null){
            return accountMapper.saveCompanyFileHash(filehash,fileData, projectId,bidderId);
        }
        else {
            return accountMapper.updateCompanyFileHash(filehash,fileData,projectId,bidderId);
        }

    }

    @Override
    public int updateApprovalInfo(String info, String id) {
        return accountMapper.updateApprovalInfo(info, id);
    }

    @Override
    public Bidder getBidderInfoById(String userid) {
        return accountMapper.getBidderInfoById(userid);
    }

    @Override
    public ResponseEntity<byte[]> downloadCompanyFile(String projectId,String bidderId) {
        String PATH = accountMapper.getBidderFile(projectId,bidderId);
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
    public List<Approver> getAllApprover() {
        return accountMapper.getAllApprover();
    }

    @Override
    public Approver getApprover(String userId) {
        return accountMapper.getApprover(userId);
    }

    @Override
    public List<BidderFile> getAllBidderFile() {
        return accountMapper.getAllBidderFile();
    }

    @Override
    public List<BidderFile> getBidderQuaFileByBidderId(String bidderId) {
        return accountMapper.getBidderQuaFileByBidderId(bidderId);
    }

    @Override
    public BidderFile getBidderQuaFile(String projectId, String bidderId) {
        return accountMapper.getBidderQuaFile(projectId,bidderId);
    }
}
