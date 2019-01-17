package com.telecom.tender;

import com.alibaba.fastjson.JSONObject;
import com.telecom.tender.service.impl.DepositServiceImpl;

public class evidenceStoreTest {
    public static void main(String[] args) {
        DepositServiceImpl depositService = new DepositServiceImpl();
        JSONObject result = depositService.getEvidengceDetail("2474");
        System.out.println(result);
    }


}
