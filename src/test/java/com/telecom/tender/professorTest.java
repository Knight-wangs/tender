package com.telecom.tender;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.telecom.tender.service.DepositService;
import com.telecom.tender.service.ProjectService;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class professorTest {
    @Autowired
    DepositService depositService;
    @Autowired
    ProjectService projectService;
    @Test
    public void contextLoads() {
//       JSONObject add = depositService.addprofessor("1",1);
////       depositService.addprofessor("2",1);
////       depositService.addprofessor("3",1);
//        JSONObject all = depositService.allprofessor(1);
////       JSONObject del = depositService.delprofessor("1",1);
////       JSONObject select = depositService.selectprofessor(2,1);
////       JSONArray items = select.getJSONArray("data");
////       for (int i=0;i<items.size();i++){
////           System.out.println(items.get(i).toString());
////       }
//    }
//        ArrayList<Integer> experts = new ArrayList<>();
//        experts.add(1);
//        experts.add(2);
//        experts.add(3);
//        experts.add(4);
//        experts.add(5);
////        String experts = "1,2,3,4,5,6";
//        JSONObject resilt = depositService.makeprofessor(3,2, StringUtils.strip(experts.toString(),"[]"));
//        System.out.println(resilt);

        JSONObject select = depositService.getselectprofessor(20);
        JSONArray list = select.getJSONArray("data");
        List<String> professors = new ArrayList<>();
        for (int i=0;i<list.size();i++) {
            professors.add(list.getString(i));

        }
        System.out.println(professors.toString());
//        JSONObject resilt = depositService.chainblock("0xca60611659203183193aa49cf0aff4532933b76e9e2597e579decbd69c7eca16");
//        System.out.println(resilt);
    }
}
