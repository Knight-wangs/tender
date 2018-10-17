package com.telecom.tender;

import com.telecom.tender.service.impl.DepositServiceImpl;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.crypto.Data;
import java.io.File;
import java.io.FileInputStream;
import java.util.Date;

public class uploadfiletest {
    public static void main(String[] args) throws Exception{
        Date data =new Date();
        DepositServiceImpl depositService = new DepositServiceImpl();
        File file = new File("E:\\电信研究所\\test.txt");
        FileInputStream fileInputStream = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("test",fileInputStream);
        System.out.println(depositService.getHashcCode(multipartFile));
        System.out.println(depositService.getHashcCode(multipartFile).toString());
//        System.out.println(depositService.getHashcCode(multipartFile).getJSONObject("data").getString("md5"));
    }
}
