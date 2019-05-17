/* Copyright © 2016 Oracle and/or its affiliates. All rights reserved. */

package com.example.db;

import java.sql.DriverManager;
import java.sql.SQLException;
import com.mysql.jdbc.Connection;

public class DBConnectionMysql {

    private static final String URL = "jdbc:mysql://";
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    public static final String LOCAL_USERNAME = "readygo";
    public static final String LOCAL_PASSWORD = "37eVE9fe0@f73409";
    public static final String LOCAL_DEFAULT_CONNECT_DESCRIPTOR = "10.22.31.100:6033/readygo";

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
        //if (connection == null) {
            instance = new DBConnectionMysql();
        //}
        return instance;
    }

    public Connection getConnection() {
        //if (connection == null) {
            try {     
            	//System.out.println("#### OCCS_DEFAULT_CONNECT_DESCRIPTOR = " +OCCS_DEFAULT_CONNECT_DESCRIPTOR);
        		connection = (Connection)DriverManager.getConnection(URL + LOCAL_DEFAULT_CONNECT_DESCRIPTOR, LOCAL_USERNAME, LOCAL_PASSWORD);
            } catch (SQLException e) {
                e.getMessage();
            }
        //}
        return connection;
    }
}

