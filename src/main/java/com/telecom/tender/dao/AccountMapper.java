package com.telecom.tender.dao;

import com.telecom.tender.model.Approver;
import com.telecom.tender.model.Assessor;
import com.telecom.tender.model.Bidder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;


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

    @Update("insert into approver (userid,username,password) values (#{userId},#{username},#{password}) ")
    int regiserApprover(@Param("userId") String userId,@Param("username") String username,@Param("password") String password);

    @Update("insert into bidder (userid,username,password) values (#{userId},#{username},#{password}) ")
    int regiserBidder(@Param("userId") String userId,@Param("username") String username,@Param("password") String password);

    @Update("insert into assessor (userid,username,password) values(#{userId},#{username},#{password})")
    int regiserAssessor(@Param("userId") String userId,@Param("username") String username,@Param("password") String password);

    //更新投标方信息
    @Update("update bidder set companyname=#{companyname},phonenumber = #{phonenumber},info = #{info} where userid = #{id}")
    int updateBidderInfo(@Param("companyname") String companyname,@Param("phonenumber") String phonenumber,@Param("info") String info,@Param("id") String id);

    //设置投标方资质审核文件地址
    @Update("update bidder set fileposition = #{fileposition} where userid=#{userid}")
    int saveCompanyFile(@Param("fileposition") String fileposition,@Param("userid") String userid);

    //设置投标方资质审核文件hash
    @Update("update bidder set filehash = #{filehash} where userid=#{userid}")
    int saveCompanyFileHash(@Param("filehash") String filehash,@Param("userid") String userid);

    //更新评委信息
    @Update("update approver set info = #{info} where userid = #{id}")
    int updateApprovalInfo(@Param("info") String info,@Param("id") String id);
    //查询投标方信息
    @Select("select userid,username,companyname,phonenumber,info,fileposition from bidder where userid=#{userid}")
    Bidder getBidderInfoById(@Param("userid") String userid);

    //查询投标人资质文件位置
    @Select("select fileposition from bidder where userid=#{id}")
    String getBidderFile(@Param("id") String id);
    //查询投标人资质文件hash
    @Select("select filehash from bidder where userid=#{id}")
    String getBidderFileHash(@Param("id") String id);
}
