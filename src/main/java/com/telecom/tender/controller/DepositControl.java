package com.telecom.tender.controller;

import com.alibaba.fastjson.JSONObject;
import com.telecom.tender.service.impl.DepositServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;

@Controller
@RequestMapping("/deposit")
public class DepositControl {
    @Resource
    DepositServiceImpl depositService;
    @RequestMapping("/upload")
    public JSONObject upload(MultipartFile file){
        return depositService.upload(file);
    }
    @RequestMapping("/download")
    public File download(String uri, String filePath){
        return depositService.download(uri,filePath);
    }
    @RequestMapping("/getHashCode")
    public JSONObject getHashcCode(MultipartFile file){
        return depositService.getHashcCode(file);
    }
    @RequestMapping("/evidencestore")
    public JSONObject evidencestore(String discription,String label,String ev_json,String files_json){
        return depositService.evidencestore(discription,label,ev_json,files_json);
    }
    @RequestMapping("/getEvidengceList")
    public JSONObject getEvidengceList(String key,String currPage,String pageSize){
        return depositService.getEvidengceList(key, currPage, pageSize);
    }
    @RequestMapping("/getEvidengceDetail")
    public JSONObject getEvidengceDetail(String id){
        return depositService.getEvidengceDetail(id);
    }
    @RequestMapping("/verify")
    public JSONObject verify(String evid,String evjson){
        return depositService.verify(evid,evjson);
    }
}
