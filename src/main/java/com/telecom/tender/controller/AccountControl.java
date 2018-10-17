package com.telecom.tender.controller;

import com.telecom.tender.model.*;
import com.telecom.tender.service.impl.AccountServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


@Controller
@RequestMapping("/account")
public class AccountControl {
    private static String BIDDER = "1";
    private static String ASSESSOR = "2";
    private static String APPROVER = "3";
    private static  final String USEDID = "当前账户已经注册";
    @Resource
    AccountServiceImpl accountService;
//    private Logger log = new ;
    @RequestMapping("/login")
    @ResponseBody
    public ResponeseDto login(String userid,String password,String role, HttpServletRequest request) throws Exception {
//        log.info("登陆验证");
        ResponeseDto responeseDto = new ResponeseDto();
        boolean login = false;
        if(role.equals(BIDDER)){
            Bidder bidder = accountService.getBidderByUserIdAndpwd(userid,password);
            if (bidder!=null){
                login=true;
                request.getSession().setAttribute("username",bidder.getUsername());
            }
        }
        else if (role.equals(ASSESSOR)){
            Assessor assessor = accountService.getAssessorByUserIdAndpwd(userid,password);
            if (assessor!= null){
                login = true;
                request.getSession().setAttribute("username",assessor.getUsername());
            }
        }
        else if (role.equals(APPROVER)){
            Approver approver = accountService.getApproverByUserIdAndpwd(userid,password);
            if (approver!=null){
                login = true;
                request.getSession().setAttribute("username",approver.getUsername());
            }
        }
        // 判断用户是否存在
        if (login) {
            request.getSession().setAttribute("userid",userid);
            request.getSession().setAttribute("role",role);
            responeseDto.setCode(ResponseCode.SUCCESS);
        } else {
            responeseDto.setCode(ResponseCode.FAIL);
        }
        return responeseDto;
    }

    @RequestMapping("/register")
    @ResponseBody
    public ResponeseDto register(String userid,String password,String role,String username) {
        ResponeseDto responeseDto = new ResponeseDto();
        if (role.equals(BIDDER)) {
            int num =accountService.getBidderByUserId(userid);
            if (num == 0) {
                if (accountService.regiserBidder(userid, username, password) > 0) {
                    responeseDto.setCode(ResponseCode.SUCCESS);
                } else {
                    responeseDto.setCode(ResponseCode.SUCCESS);
                }
            } else {
                responeseDto.setCode(ResponseCode.FAIL);
                responeseDto.setMsg(USEDID);
            }

        }
        if (role.equals(APPROVER)) {
            if (accountService.getApproverByUserId(userid) == 0) {
                if (accountService.regiserApprover(userid, username, password) > 0) {
                    responeseDto.setCode(ResponseCode.SUCCESS);
                } else {
                    responeseDto.setCode(ResponseCode.SUCCESS);
                }
            } else {
                responeseDto.setCode(ResponseCode.FAIL);
                responeseDto.setMsg(USEDID);
            }
        }
        if (role.equals(ASSESSOR)) {
            if (accountService.getAssessorByUserId(userid) == 0) {
                if (accountService.regiserAssessor(userid, username, password) > 0) {
                    responeseDto.setCode(ResponseCode.SUCCESS);
                } else {
                    responeseDto.setCode(ResponseCode.SUCCESS);
                }
            } else {
                responeseDto.setCode(ResponseCode.FAIL);
                responeseDto.setMsg(USEDID);
            }
        }
        return responeseDto;
    }


}
