package com.telecom.tender.service;

import com.telecom.tender.model.Approver;
import com.telecom.tender.model.Assessor;
import com.telecom.tender.model.Bidder;
import org.springframework.http.ResponseEntity;

public interface AccountService {
    Approver getApproverByUserIdAndpwd(String userid, String password);
    Bidder getBidderByUserIdAndpwd(String userid, String password);
    Assessor getAssessorByUserIdAndpwd(String userid, String password);
    int getApproverByUserId(String userid);
    int getBidderByUserId(String userId);
    int getAssessorByUserId(String userId);
    int regiserApprover(String userId,String username,String password);
    int regiserBidder(String userId,String username,String password);
    int regiserAssessor(String userId,String username,String password);
    //更新招标方信息
    int updateBidderInfo(String companyname,String phonenumber,String info,String id);
    //设置招标方公司
    int saveCompanyFile(String fileposition,String id);
    //设置招标方文件信息文件hash
    int saveCompanyFileHash(String filehash,String id);
    //更新评委信息
    int updateApprovalInfo(String info,String id);
    //查询投标方信息
    Bidder getBidderInfoById(String userid);
    //下载投标方资质审核文件
    ResponseEntity<byte[]> downloadCompanyFile(String id);

}
