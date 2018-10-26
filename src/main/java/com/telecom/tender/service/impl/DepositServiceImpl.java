package com.telecom.tender.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.telecom.tender.service.DepositService;
import com.telecom.tender.utils.HttpClientUtils;
import net.dongliu.requests.Requests;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service("depositservice")
public class DepositServiceImpl implements DepositService {
    private final static String URL = "http://123.207.167.245";
    private final static String APPID = "b26d80f01146482e9942fc5624297bb6";
    private final static String TOKEN = "c3a206b1a7be4b9fa5daf8553e8bf466";

    @Override
    public JSONObject upload(MultipartFile file) {
        String url = URL+":8099/storage/api/files/upload";
        Map<String,String> params=new HashMap<>();
        params.put("token",TOKEN);
        String result=HttpClientUtils.httpClientUploadFile(file,url,params);
        return JSONObject.parseObject(result);
    }

    @Override
    public File download(String uri,String filepath) {
        String url= URL+":8099/storage/api/files/"+uri;
        File file = HttpClientUtils.saveUrlAs(url,filepath,"post");
        return file;
    }

    @Override
    public JSONObject getHashcCode(MultipartFile file) {
        String url=URL+":8099/storage/api/files/gethashcode";
        Map<String,String> params=new HashMap<>();
        params.put("token",TOKEN);
        String result=HttpClientUtils.httpClientUploadFile(file,url,params);
        return JSONObject.parseObject(result);
    }

    @Override
    public JSONObject evidencestore(String discription, String label, String ev_json, String files_json) {
        String url = URL+":8081/evchain/app/evidence/store";
        Map<String,String> param = new HashMap<>();
        Map<String,Object> header = new HashMap<>();
        header.put("appId",APPID);
        header.put("appKey",TOKEN);
        param.put("discription",discription);
        param.put("label",label);
        param.put("ev_json",ev_json);
        param.put("files_json",files_json);
        String result = Requests.post(url).headers(header).params(param).send().readToText();
        return JSONObject.parseObject(result);
    }

    @Override
    public JSONObject getEvidengceList(String key, String currPage, String pageSize) {
        String url = URL+":8081/evchain/app/evidence/list";
        Map<String,String> param = new HashMap<>();
        Map<String,Object> header = new HashMap<>();
        header.put("appId",APPID);
        header.put("appKey",TOKEN);
        param.put("key",key);
        param.put("currPage",currPage);
        param.put("pageSize",pageSize);
        String result = Requests.post(url).headers(header).params(param).send().readToText();
        return JSONObject.parseObject(result);
    }

    @Override
    public JSONObject getEvidengceDetail(String id) {
        String url = URL+":8081/evchain/app/evidence/detail";
        Map<String,String> param = new HashMap<>();
        Map<String,Object> header = new HashMap<>();
        header.put("appId",APPID);
        header.put("appKey",TOKEN);
        param.put("id",id);
        String result = Requests.post(url).headers(header).params(param).send().readToText();
        return JSONObject.parseObject(result);
    }

    @Override
    public JSONObject verify(String evid, String evjson) {
        String url = URL+":8081/evchain/app/evidence/verify";
        Map<String,String> param = new HashMap<>();
        Map<String,Object> header = new HashMap<>();
        header.put("appId",APPID);
        header.put("appKey",TOKEN);
        param.put("evid",evid);
        param.put("evjson",evjson);
        String result = Requests.post(url).headers(header).params(param).send().readToText();
        return JSONObject.parseObject(result);
    }

    @Override
    public JSONObject setBTime(long timeStamp,String evID) {
        String url = URL+"/evchain/app/evidence/setbtime";
        Map<String,Object> param = new HashMap<>();
        Map<String,Object> header = new HashMap<>();
        header.put("appId",APPID);
        header.put("appKey",TOKEN);
        param.put("evID",evID);
        param.put("timestamp",timeStamp);
        String result = Requests.post(url).headers(header).params(param).send().readToText();
        return JSONObject.parseObject(result);
    }

    @Override
    public JSONObject getBTime(String evID) {
        String url = URL+"/evchain/app/evidence/getbtime";
        Map<String,String> param = new HashMap<>();
        Map<String,Object> header = new HashMap<>();
        header.put("appId",APPID);
        header.put("appKey",TOKEN);
        param.put("evID",evID);
        String result = Requests.post(url).headers(header).params(param).send().readToText();
        return JSONObject.parseObject(result);
    }

    @Override
    public JSONObject verifybtime(String evID) {
        String url = URL+"/evchain/app/evidence/verifybtime";
        Map<String,String> param = new HashMap<>();
        Map<String,Object> header = new HashMap<>();
        header.put("appId",APPID);
        header.put("appKey",TOKEN);
        param.put("evID",evID);
        String result = Requests.post(url).headers(header).params(param).send().readToText();
        return JSONObject.parseObject(result);
    }

    @Override
    public JSONObject makeprofessor(Integer num, Integer progectID, String experts) {
        String url = URL+"/evchain/app/evidence/makeprofessor";
        Map<String, Object> param = new HashMap<>();
        Map<String, String> header = new HashMap<>();
        header.put("appId",APPID);
        header.put("appKey",TOKEN);
        param.put("num",num);
        param.put("experts",experts);
        param.put("progectID",progectID);
        String result = Requests.post(url).headers(header).params(param).send().readToText();
        return JSONObject.parseObject(result);
    }

    @Override
    public JSONObject getselectprofessor(Integer progectID) {
        String url = URL+"/evchain/app/evidence/getselectprofessor";
        Map<String, Object> param = new HashMap<>();
        Map<String, String> header = new HashMap<>();
        header.put("appId",APPID);
        header.put("appKey",TOKEN);
        param.put("progectID",progectID);
        String result = Requests.post(url).headers(header).params(param).send().readToText();
        return JSONObject.parseObject(result);
    }

    @Override
    public JSONObject addprofessor(String expertId,Integer progectID) {
        String url = URL+"/evchain/app/evidence/addprofessor";
        Map<String, Object> param = new HashMap<>();
        Map<String, String> header = new HashMap<>();
        header.put("appId",APPID);
        header.put("appKey",TOKEN);
        param.put("expert",expertId);
        param.put("progectID",progectID);
        String result = Requests.post(url).headers(header).params(param).send().readToText();
        return JSONObject.parseObject(result);
    }

    @Override
    public JSONObject delprofessor(String expertId,Integer progectID) {
        String url = URL+"/evchain/app/evidence/delprofessor";
        Map<String, Object> param = new HashMap<>();
        Map<String, String> header = new HashMap<>();
        header.put("appId",APPID);
        header.put("appKey",TOKEN);
        param.put("expert",expertId);
        param.put("progectID",progectID);
        String result = Requests.post(url).headers(header).params(param).send().readToText();
        return JSONObject.parseObject(result);
    }

    @Override
    public JSONObject selectprofessor(Integer num,Integer progectID) {
        String url = URL+"/evchain/app/evidence/selectprofessor";
        Map<String, Integer> param = new HashMap<>();
        Map<String, String> header = new HashMap<>();
        header.put("appId",APPID);
        header.put("appKey",TOKEN);
        param.put("num",num);
        param.put("progectID",progectID);
        String result = Requests.post(url).headers(header).params(param).send().readToText();
        return JSONObject.parseObject(result);
    }

    @Override
    public JSONObject allprofessor(Integer progectID) {
        String url = URL+"/evchain/app/evidence/allprofessor";
        Map<String, Integer> param = new HashMap<>();
        Map<String, String> header = new HashMap<>();
        header.put("appId",APPID);
        header.put("appKey",TOKEN);
        param.put("progectID",progectID);
        String result = Requests.post(url).headers(header).params(param).send().readToText();
        return JSONObject.parseObject(result);
    }

    @Override
    public JSONObject chainblock(String transactionId) {
        String url = URL+"/evchain/app/evidence/chainblock";
        Map<String, String> param = new HashMap<>();
        Map<String, String> header = new HashMap<>();
        header.put("appId",APPID);
        header.put("appKey",TOKEN);
        param.put("transactionId",transactionId);
        String result = Requests.post(url).headers(header).params(param).send().readToText();
        return JSONObject.parseObject(result);
    }
}
