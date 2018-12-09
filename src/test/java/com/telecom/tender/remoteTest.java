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
             openDate =sdf.parse("2018-10-26 16:55:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }

//        JSONObject result = depositService.setBTime(openDate.getTime()/1000,"4");
//        System.out.println(result);

        JSONObject btime = depositService.getBTime("114");
        System.out.println(btime);

//        JSONObject verify = depositService.verifybtime("4");
//        System.out.println(verify);
    }
}
