package com.telecom.tender;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.telecom.tender.service.DepositService;
import com.telecom.tender.service.ProjectService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class professorTest {
    @Autowired
    DepositService depositService;
    @Autowired
    ProjectService projectService;
    @Test
    public void contextLoads() {
       JSONObject add = depositService.addprofessor("1",1);
//       depositService.addprofessor("2",1);
//       depositService.addprofessor("3",1);
        JSONObject all = depositService.allprofessor(1);
//       JSONObject del = depositService.delprofessor("1",1);
//       JSONObject select = depositService.selectprofessor(2,1);
//       JSONArray items = select.getJSONArray("data");
//       for (int i=0;i<items.size();i++){
//           System.out.println(items.get(i).toString());
//       }
    }
}
