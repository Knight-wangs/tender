package com.telecom.tender.utils;


import java.sql.Connection;
import java.sql.DriverManager;

public class Dbutils {
    public Connection getCon() throws Exception{
        //加载驱动程序
        Class.forName(PropertiUtils.getValue("jdbc.driver"));
        //获取连接
        Connection con= DriverManager.getConnection(PropertiUtils.getValue("jdbc.url"), PropertiUtils.getValue("jdbc.username"), PropertiUtils.getValue("jdbc.password"));
        return con;


    }
    public void closeCon(Connection con)throws Exception{
        if(con!=null){
            con.close();
        }
    }
}
