package com.telecom.tender.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;

public interface DepositService {
    JSONObject upload(MultipartFile file);

    File download(String uri, String filePath);

    JSONObject getHashcCode(MultipartFile file);

    JSONObject evidencestore(String discription, String label, String ev_json, String files_json);

    JSONObject getEvidengceList(String key, String currPage, String pageSize);

    JSONObject getEvidengceDetail(String id);

    JSONObject verify(String evid, String evjson);

    JSONObject setBTime(long timeStamp,String evID);

    JSONObject getBTime(String evID);

    JSONObject verifybtime(String evID);

    JSONObject addprofessor(String expertId,Integer progectID);

    JSONObject delprofessor(String expertId,Integer progectID);

    JSONObject selectprofessor(Integer num,Integer progectID);

    JSONObject allprofessor(Integer progectID);

    JSONObject makeprofessor(Integer num, Integer progectID, String experts);

    JSONObject getselectprofessor(Integer progectID);

    JSONObject chainblock(String transactionId);
}
