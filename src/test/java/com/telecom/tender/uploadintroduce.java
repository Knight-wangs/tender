package com.telecom.tender;


import com.telecom.tender.model.ProjectFileType;
import com.telecom.tender.service.ProjectService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
public class uploadintroduce {
    @Autowired
    private WebApplicationContext wac;

    private MockMvc mvc;
    private MockHttpSession session;
    @Autowired
    ProjectService projectService;

    @Before
    public void setupMockMvc(){
        mvc = MockMvcBuilders.webAppContextSetup(wac).build(); //初始化MockMvc对象
        session = new MockHttpSession();
    }
    @Test
    public  void test() throws Exception {
//        String path = "E:\\电信研究所\\test.txt";
        File file = new File("E:\\电信研究所\\存证平台应用接口文档V2.0.doc");
        FileInputStream fileInputStream = new FileInputStream(file);
        MockMultipartFile multipartFile = new MockMultipartFile("multipartFile","存证平台应用接口文档V2.0.doc","text/plain",fileInputStream);
        mvc.perform(MockMvcRequestBuilders.fileUpload("/project/uploadQualificationFile").
                file( multipartFile).param("id","001")).
                andExpect(status().is(200)).
                andExpect(content().string("success"));
    }

    @Test
    public void testsavefile(){
        String PATH = "E:\\电信研究所\\test.txt";
        String id = "001";
        projectService.saveProjectFile(PATH,id,ProjectFileType.ASSESSOR);
    }
    @Test
    public void uploadBidderForm() throws IOException {
        File file = new File("E:\\电信研究所\\test.txt");
        FileInputStream fileInputStream = new FileInputStream(file);
        MockMultipartFile multipartFile = new MockMultipartFile("multipartFile","test.text","text/plain",fileInputStream);
        try {
            mvc.perform(MockMvcRequestBuilders.fileUpload("/project/uploadBidderForm")
                    .file(multipartFile)
                    .param("projectId","001")
                    .param("bidderId","001"))
                    .andExpect(status().is(200)).
                    andExpect(content().string("success"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void createFile(){
        String INTRODUCTFILEPATH="E:/电信研究所/test/1234/";
        File file = new File("E:\\电信研究所\\test.txt");
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            MockMultipartFile multipartFile = new MockMultipartFile("multipartFile","test.text","text/plain",fileInputStream);
            File dir = new File(INTRODUCTFILEPATH);
            if (!dir.exists()){
                dir.mkdirs();
            }
            String path =INTRODUCTFILEPATH+"/1";
            multipartFile.transferTo(new File(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }






}
