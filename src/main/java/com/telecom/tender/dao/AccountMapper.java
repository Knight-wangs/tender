package com.telecom.tender.dao;

import com.telecom.tender.model.Approver;
import com.telecom.tender.model.Assessor;
import com.telecom.tender.model.Bidder;
import com.telecom.tender.model.BidderFile;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface AccountMapper {
    @Select("select * from approver where userid = #{userid} and password =#{password}")
    Approver getApproverByUserIdAndpwd(@Param("userid") String userid, @Param("password") String password);

    @Select("select * from bidder where userid = #{userid} and password =#{password}")
    Bidder getBidderByUserIdAndpwd(@Param("userid") String userid,@Param("password") String password);

    @Select("select * from assessor where userid = #{userid} and password =#{password}")
    Assessor getAssessorByUserIdAndpwd(@Param("userid") String userid, @Param("password") String password);

    @Select("select count(*)from approver where userid = #{userid} ")
    int getApproverByUserId(@Param("userid") String userid);

    @Select("select count(*) from bidder where userid = #{userid} ")
    int getBidderByUserId(@Param("userid") String userid);

    @Select("select count(*) from assessor where userid = #{userid}")
    int getAssessorByUserId(@Param("userid") String userid);

    //查询资质审核文件是否已存在
    @Select("select * from bidderfile where projectId=#{projectId} and bidderId=#{bidderId} ")
    BidderFile getBidderQuaFile(@Param("projectId") String projectId,@Param("bidderId")String bidderId);
    @Insert("insert into approver (userid,username,password) values (#{userId},#{username},#{password}) ")
    int regiserApprover(@Param("userId") String userId,@Param("username") String username,@Param("password") String password);

    @Insert("insert into bidder (userid,username,password) values (#{userId},#{username},#{password}) ")
    int regiserBidder(@Param("userId") String userId,@Param("username") String username,@Param("password") String password);

    @Insert("insert into assessor (userid,username,password) values(#{userId},#{username},#{password})")
    int regiserAssessor(@Param("userId") String userId,@Param("username") String username,@Param("password") String password);

    //更新投标方信息
    @Update("update bidder set companyname=#{companyname},phonenumber = #{phonenumber},info = #{info} where userid = #{id}")
    int updateBidderInfo(@Param("companyname") String companyname,@Param("phonenumber") String phonenumber,@Param("info") String info,@Param("id") String id);

    //插入投标方资质审核文件地址
    @Insert("Insert bidderfile  (fileposition,projectId,bidderId) values (#{fileposition},#{projectId},#{bidderId}) ")
    int saveCompanyFile(@Param("fileposition") String fileposition,@Param("projectId") String projectId,@Param("bidderId")String bidderId);

    //插入投标方资质审核文件hash
    @Insert("insert bidderfile (filehash,fileData,projectId,bidderId) values (#{filehash},#{fileData},#{projectId},#{bidderId})")
    int saveCompanyFileHash(@Param("filehash") String filehash,@Param("fileData")String fileData,
                            @Param("projectId") String projectId,@Param("bidderId")String bidderId);
    //更新投标方资质审核文件地址
    @Update("update bidderfile set fileposition = #{fileposition}  where projectId=#{projectId} and bidderId=#{bidderId}")
    int updateCompanyFile(@Param("fileposition") String fileposition,@Param("projectId") String projectId,@Param("bidderId")String bidderId);

    //更新投标方资质审核文件hash
    @Update("update bidderfile set filehash = #{filehash},fileData = #{fileData} where projectId=#{projectId} and bidderId=#{bidderId}")
    int updateCompanyFileHash(@Param("filehash") String filehash,@Param("fileData")String fileData,
                            @Param("projectId") String projectId,@Param("bidderId")String bidderId);

    //更新评委信息
    @Update("update approver set info = #{info} where userid = #{id}")
    int updateApprovalInfo(@Param("info") String info,@Param("id") String id);
    //查询投标方信息
    @Select("select userid,username,companyname,phonenumber,info,fileposition from bidder where userid=#{userid}")
    Bidder getBidderInfoById(@Param("userid") String userid);

    //查询投标人资质文件位置
    @Select("select fileposition from bidderfile where projectId=#{projectId} and bidderId=#{bidderId}")
    String getBidderFile(@Param("projectId") String projectId,@Param("bidderId")String bidderId);
    //查询投标人资质文件hash
    @Select("select filehash from bidder where projectId=#{projectId} and bidderId=#{bidderId}")
    String getBidderFileHash(@Param("projectId") String projectId,@Param("bidderId")String bidderId);
    //查询投标方资质审核文件的存在数据
    @Select("select fileData from bidderfile where projectId=#{projectId} and bidderId=#{bidderId}")
    String getBidderFileData(@Param("projectId") String projectId,@Param("bidderId")String bidderId);
    //获取所有的评委
    @Select("select userid,username,info from approver")
    List<Approver> getAllApprover();
    //查询评委信息
    @Select("select userid,username,phonenumber,info from approver where userid=#{userId}")
    Approver getApprover(String userId);
    //查询所有资质审核文件信息
    @Select("select * from bidderfile")
    List<BidderFile> getAllBidderFile();
    //按照投标方id查询项目资质审核文件
    @Select("select * from bidderfile where bidderId=#{bidderId} ")
    List<BidderFile> getBidderQuaFileByBidderId(@Param("bidderId")String bidderId);
}
