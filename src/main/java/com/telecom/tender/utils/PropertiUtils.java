package com.telecom.tender.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 读取配置文件的
 */ 
public class PropertiUtils {
    public static String getValue(String key) {
        Properties pro = new Properties();
        InputStream in = new PropertiUtils().getClass().getResourceAsStream("resources/blockchain.properties");
        try {
            pro.load(in);
        } catch (IOException e) {
            // TODO Auto-generated catch block 
            e.printStackTrace();
        }

        return pro.getProperty(key);
    }
}
