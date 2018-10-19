package com.telecom.tender;

import com.alibaba.fastjson.JSONObject;
import com.telecom.tender.service.DepositService;
import com.telecom.tender.service.ProjectService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


@RunWith(SpringRunner.class)
@SpringBootTest
public class remoteTest {
    @Autowired
    DepositService depositService;
    @Autowired
    ProjectService projectService;
    @Test
    public void contextLoads() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date openDate =new Date();
        try {
             openDate =sdf.parse("2018-10-20 18:06:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }

//        JSONObject result = depositService.setBTime(openDate.getTime());
//        JSONObject result = depositService.getBTime("c8d6c8363a21f6928a591240262f4952");
        JSONObject result = depositService.chainblock("0xd7dca7b688de61fe95f12affd8444aac9d84bb5fccbdab33a22c20c0fee81f76");
//        JSONObject verresult = depositService.verifybtime("c8d6c8363a21f6928a591240262f4952");
        System.out.println(result);
    }
}
