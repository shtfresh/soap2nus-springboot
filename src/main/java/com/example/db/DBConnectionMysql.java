/* Copyright © 2016 Oracle and/or its affiliates. All rights reserved. */

package com.example.db;

import java.sql.DriverManager;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.AITrainer.DBConfigProperties;
//import com.example.AITrainer.DBConfigPropertiesC;
import com.mysql.jdbc.Connection;

public class DBConnectionMysql {
	public static String URL = "";
	public static String DRIVER  = "";
	public static String USERNAME = "";
	public static String PASSWORD = "";

    private static Connection connection = null;
    private static DBConnectionMysql instance = null;

    private DBConnectionMysql() {
        try {
            Class.forName(DRIVER).newInstance();
        } catch (Exception sql) {
            System.out.println(sql.getStackTrace());
        }
    }

    public static DBConnectionMysql getInstance() {
        instance = new DBConnectionMysql();
        return instance;
    }

    public Connection getConnection() {
        try {
        	connection = (Connection)DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.getMessage();
        }
        return connection;
    }
}

