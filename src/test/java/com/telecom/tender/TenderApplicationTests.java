package com.telecom.tender;

import com.alibaba.fastjson.JSONObject;
import com.telecom.tender.model.Project;
import com.telecom.tender.service.DepositService;
import com.telecom.tender.service.ProjectService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;



import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TenderApplicationTests {
	@Autowired
	DepositService depositService;
	@Autowired
	ProjectService projectService;
	@Test
	public void contextLoads() {
		JSONObject jsonObject = new JSONObject();
		Date date = new Date();
		jsonObject.put("date",date.toString());
		jsonObject.put("id","id123");
		JSONObject filejson = new JSONObject();
		filejson.put("file","file");
		System.out.println(depositService.evidencestore("12",null,jsonObject.toString(),null));
//		List<Project> projectList =projectService.getAllOpenProject();
	}

}
