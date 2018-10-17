package com.telecom.tender.controller;

import com.telecom.tender.model.Bidder;
import com.telecom.tender.service.impl.AccountServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("/test")
public class test {
    @Resource
    AccountServiceImpl accountService;
    @RequestMapping("/index")
    @ResponseBody
    public Bidder index(){
        return accountService.getBidderByUserIdAndpwd("1","wang");
    }
}
