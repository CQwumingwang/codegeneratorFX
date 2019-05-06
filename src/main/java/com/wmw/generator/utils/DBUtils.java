package com.wmw.generator.utils;

import com.wmw.generator.build.Parameters;

import java.sql.*;

/**
 * @ClassName DBUtils
 * @Description TODO 连接工具
 * @Author wumingwang
 * @Date 2019/5/6 9:53
 * @Version 1.0
 */
public class DBUtils {

    private  static Connection conn = null;

    private static Statement stmt = null;

    private static ResultSet rs =null;

    public static Connection getConnection() throws Exception {
        if(conn == null){
            synchronized (DBUtils.class) {
                if(conn == null){
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    conn = DriverManager.getConnection("jdbc:mysql://"+ Parameters.DB_IP+":"+Parameters.DB_PORT+"/"+Parameters.DB_DATABASE+"?useUnicode=true&characterEncoding=utf-8&useSSL=false"
                            , Parameters.DB_USER, Parameters.DB_PASSWORD);
                }
            }
        }
        return conn;
    }

    public static ResultSet query(String sql) throws Exception {
        getConnection();
        stmt = conn.createStatement();
        rs = stmt.executeQuery(sql);
        return rs;
    }

    public static void insert(String sql){
        try{
            getConnection();
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(stmt != null){
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void closeConnection(){
        if(conn != null){
            try {
                conn.close();
                conn = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public static void close(){
        if(rs != null){
            try {
                rs.close();
                rs = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(stmt != null){
            try {
                stmt.close();
                stmt = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(conn != null){
            try {
                conn.close();
                conn = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
