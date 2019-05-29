/* Copyright © 2016 Oracle and/or its affiliates. All rights reserved. */

package com.example.db;

import java.sql.DriverManager;
import java.sql.SQLException;
import com.mysql.jdbc.Connection;

public class DBConnectionMysql {
    private static final String URL = DBConfigProperties.values("URL");
    private static final String DRIVER = DBConfigProperties.values("DRIVER");
    public static final String USERNAME = DBConfigProperties.values("USERNAME");
    public static final String PASSWORD = DBConfigProperties.values("PASSWORD");

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

