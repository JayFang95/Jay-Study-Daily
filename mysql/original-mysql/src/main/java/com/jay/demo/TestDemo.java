package com.jay.demo;

import java.sql.*;

/**
 * Copyright(c),2018-2021,合肥市鼎足空间技术有限公司
 *
 * @author jing.fang
 * @date 2022/12/8
 * @description history
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public class TestDemo {

    public static void main(String[] args) throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/monitor21?serverTimezone=GMT%2B8",
                "root", "root");
        // 开启事务
        connection.setAutoCommit(false);

        /**
         * prepareStatement 预编译 执行效率高 可以防sql注入
         */
        String querySql = "select * from user where username = ?";
        PreparedStatement pst = connection.prepareStatement(querySql);
        pst.setString(1, "admin");
        ResultSet rs = pst.executeQuery();

        Statement statement = connection.createStatement();
        ResultSet set = statement.executeQuery("select * from user" );

        while (set.next()){
            System.out.println(set.getString("username") + set.getInt("id"));
        }

        // 事务提交
        connection.commit();
        set.close();
        rs.close();
        statement.close();
        connection.close();
    }

}
