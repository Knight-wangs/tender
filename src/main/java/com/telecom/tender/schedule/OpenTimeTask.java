package com.telecom.tender.schedule;

import com.alibaba.fastjson.JSONObject;
import com.telecom.tender.model.Project;
import com.telecom.tender.service.DepositService;
import com.telecom.tender.service.ProjectService;
import org.apache.commons.lang.StringUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class OpenTimeTask {
    @Resource
    ProjectService projectService;
    @Resource
    DepositService depositService;
    @Scheduled(fixedRate = 3000)
    public void checkOpenTime(){
        List<Project> projectList = projectService.getAllOpenProject();
        for (Project project : projectList){
            String evID;
            JSONObject openTimeData = JSONObject.parseObject(project.getOpenTimeData());
            if (openTimeData.isEmpty()){
                return;
            }
            else {
               evID = openTimeData.getJSONObject("data").getString("evID");
            }
            JSONObject result = depositService.verifybtime(evID);
            String code =result.getString("code");
            JSONObject data = result.getJSONObject("data");
            if(StringUtils.isNotBlank(code) && code.equals("0")){
                if (data!=null) {
                    String verify = data.getString("verify");
                    if (StringUtils.isNotBlank(verify) && Boolean.valueOf(verify)){
                        projectService.setProjectState(evID,"2");
                    }
                }
            }
        }
    }
}
