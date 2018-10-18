package com.telecom.tender.dao;

import com.telecom.tender.model.*;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Mapper
public interface ProjectMapper {
    //查询所有项目
    @Select("select id,name,assessor,industry,area,opentime,tenderTime,state from project where 1=1 order by tenderTime DESC")
    public List<Project> getAllProject();
    //查询等待所有开标的项目
    @Select("select * from project where state = '1'")
    public List<Project> getAllOpenProject();
    //根据id查询项目
    @Select("select * from project where id = #{id}")
    public Project getProjectById(@Param("id") String id);
    //根据项目id查投标情况
    @Select("select bidderid,bidderName,tenderFile from bidderform left join bidder on bidderform.bidderid=bidder.userid " +
            "where projectid = #{projectid}")
    public List<BidderForm> getBidderFormByProjectId(@Param("projectid") String projectid);
    //根据项目id查项目审批情况
    @Select("select view,detail from approvalform left join approver on approvalform.approvalid = approver.userid " +
            "where projectid = #{projectid}")
    public ApprovalForm getApprovalFormByProjectId(@Param("projectid") String projectid);
    //创建项目
    @Insert("insert into project(name,assessor,industry,area,opentime,tenderTime,state) values(#{name},#{assessor},#{industry},#{area},#{opentime},#{tenderTime},#{state})")
    @Options(useGeneratedKeys=true, keyProperty="id")//添加该行id将被自动添加
    public int newProject(Project project);
//    public int newProject(@Param("name") String name, @Param("assessor") String assessor,
//                          @Param("industry") String industry, @Param("area") String area, @Param("opentime")  Date opentime,@Param("tenderTime") Date tenderTime,@Param("state") String state);
    //更新项目信息
    @Update("update project set name=#{name},assessor=#{assessor},industry=#{industry},area=#{area},opentime=#{opentime},state=#{state} where id=#{id} ")
    public int updateProject(@Param("id") String id, @Param("name") String name, @Param("assessor") String assessor,
                          @Param("industry") String industry, @Param("area") String area, @Param("opentime")  Date opentime,@Param("state") String state);
    //保存项目开标时间上链数据
    @Insert("update project set openTimeData = #{openTimeData} where id = #{id}")
    public int updateOpenTimeData(@Param("id") String id,@Param("openTimeData") String openTimeData);
    //获取项目当前状态
    @Select("select state from project where id=#{id}")
    public String getProjectState(@Param("id") String id);
    //设置项目状态
    @Update("update project set state = #{state} where id = #{id}")
    public int setProjectState(@Param("id") String id,@Param("state") String state);
    //更新投标方信息
    @Update("update bidder set companyname={companyname}，phonenumber=#{phonenumber}，info=#{info} where id =#{id}")
    public int updateBidderInfo(@Param("companyname") String companyname,@Param("phonenumber") String phonenumber,
                                @Param("info") String info,@Param("id") String id);
    //设置招标公告文档
    @Update("update project set introFile = #{introFile} where id=#{id}")
    public int setIntroFile(@Param("introFile") String assessorFile,@Param("id") String id);
    //查询招标公告文件存储地址
    @Select("select introFile from project where id=#{id}")
    public String getIntroFilePath(@Param("id") String id);
    //设置公告文档的hash和上链数据
    @Update("update project set introFileHash = #{filehash},introFileData = #{introFileData} where id=#{id}")
    public int saveIntroFileHash(@Param("filehash") String filehash,@Param("id") String id,@Param("introFileData") String introFileData);
    @Select("select introFileHash from project where id=#{id}")
    public String getintroFileHash(@Param("id") String id);

    //设置招标说明文件存储地址
    @Update("update project set assessorFile = #{assessorFile} where id=#{id}")
    //设置招标说明文件hash
    public int setAssessorFile(@Param("assessorFile") String assessorFile,@Param("id") String id);
    //查询招标说明文件存储地址
    @Select("select assessorFile from project where id=#{id}")
    public String getAssessorFilePath(@Param("id") String id);
    //设置招标说明文件hash和上链数据
    @Update("update project set assessorFileHash = #{filehash} , assessorFileData = #{assessorFileHash} where id=#{id}")
    public int saveAssessoFileHash(@Param("filehash") String filehash,@Param("id") String id,@Param("assessorFileHash")String assessorFileHash);
    @Select("select assessorFileHash from project where id=#{id}")
    public String getAssessorFileHash(@Param("id") String id);

    //设置招标结果文件位置
    @Update("update project set resultsFile = #{filehash} where id=#{id}")
    public int setResultsFile(@Param("filehash") String filehash,@Param("id") String id);
    //查询招标结果文件位置
    @Select("select resultsFile from project where id=#{id}")
    public String getresultsFilePath(@Param("id") String id);
    //设置招标结果文件存储hash和上链数据
    @Update("update project set resultsFileHash = #{filehash},resultsFileData = #{resultsFileData} where id=#{id}")
    public int saveResultsFileHash(@Param("filehash") String filehash,@Param("id") String id,@Param("resultsFileData")String resultsFileData);
    @Select("select resultsFileHash from project where id=#{id}")
    public String getResultsFileHash(@Param("id") String id);

    //设置招标结果文件位置
    @Update("update project set contractFile = #{filehash} where id=#{id}")
    public int saveContractFile(@Param("filehash") String filehash,@Param("id") String id);
    //查询招标结果文件位置
    @Select("select contractFile from project where id=#{id}")
    public String getcontractFilePath(@Param("id") String id);
    //设置招标结果文件hash和上链数据
    @Update("update project set contractFileHash = #{fileposition},contractFileData = #{contractFileData} where id=#{id}")
    public int saveContractFileHash(@Param("fileposition") String fileposition,@Param("id") String id,@Param("contractFileData")String contractFileData);
    @Select("select contractFileHash from project where id=#{id}")
    public String getContractFileHash(@Param("id") String id);

    //检查投标人该项目标书上传
    @Select("select tenderFile from bidderform where projectid=#{projectid} and bidderid=#{bidderid}")
    String getTenderFile(@Param("projectid") String projectid,@Param("bidderid") String bidderid);

    //上传标书
    @Insert("insert into bidderform (projectid,bidderid,tenderFile,tenderFileHash,tenderFileData) values(#{projectid},#{bidderid},#{tenderFile},#{tenderFileHash},#{tenderFileData})")
    int uploadBidderForm(@Param("projectid") String projectid,@Param("bidderid") String bidderid,
                         @Param("tenderFile") String tenderFile,@Param("tenderFileHash") String tenderFileHash,@Param("tenderFileData")String tenderFileData);
    //更新标书信息
    @Update("update bidderform set tenderFile=#{tenderFile},tenderFileHash=#{tenderFileHash} where projectid=#{projectid} and bidderid=#{bidderid}")
    int uploadTenderFile(@Param("projectid") String projectid,@Param("bidderid") String bidderid,
                         @Param("tenderFile") String tenderFile,@Param("tenderFileHash") String tenderFileHash);
    //评委评估
    @Insert("insert into approvalform (approvalid,projectid,techScore,bussScore,serverScore,totalScore,comment,opinionData) values (#{approvalid},#{projectid},#{techScore},#{bussScore},#{serverScore},#{totalScore},#{comment},#{opinionData})")
    int saveApprovalOption(@Param("approvalid") String approvalid,@Param("projectid") String projectid, @Param("techScore") String techScore,
                           @Param("bussScore") String bussScore,@Param("serverScore") String serverScore,@Param("totalScore")String totalScore,@Param("comment")String comment,@Param("opinionData") String opinionData);
    //查询是否评委已经评估
    @Select("select count(*) from approvalform where approvalid= #{approvalid} and projectid= #{projectid}")
    int checkIsApproval(@Param("approvalid") String approvalid,@Param("projectid") String projectid);
    //更新评估意见
    @Update("update approvalform set techScore = #{techScore},bussScore = #{bussScore},serverScore = #{serverScore},totalScore = #{totalScore},comment = #{comment},opinionData = #{opinionData} " +
            "where approvalid=#{approvalid} and projectid=#{projectid}")
    int updateevaluation(@Param("approvalid") String approvalid,@Param("projectid") String projectid, @Param("techScore") String techScore,
                         @Param("bussScore") String bussScore,@Param("serverScore") String serverScore,@Param("totalScore")String totalScore,@Param("comment")String comment,@Param("opinionData") String opinionData);
    //根据项目id查询所有投标公司
    @Select("select bidderid,companyname,tenderFile,info from bidderform left join bidder on bidderform.bidderid=bidder.userId where projectid = #{projectid}")
    List<BidderInfo> getAllBidderByProjectId(@Param("projectid") String projectid);

    //查询评委对项目的观点
    @Select("select * from approvalform where approvalid= #{approvalid} and projectid= #{projectid}")
   ApprovalForm getApprovalForm(@Param("approvalid") String approvalid,@Param("projectid") String projectid);
    //根据项目id查询所有评委意见
    @Select("select * from approvalform where projectid= #{projectid}")
    List<ApprovalForm> getAllOpinionByProjectId(@Param("projectid") String projectid);
    //查询所有评委
    @Select("select userid,username from approver")
    List<Approver> getAllApprover();
}
