/* Copyright © 2016 Oracle and/or its affiliates. All rights reserved. */

package com.example.db;

import java.util.List;

import org.apache.commons.dbutils.DbUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.TrainingPlanTemplate.TrainingPlanTemplate;
import com.google.gson.JsonObject;


public class TrainingPlanTemplateEnumDbDeclaration {
    
    List<TrainingPlanTemplate> eList = null;
	
	public Connection conn = DBConnectionMysql.getInstance().getConnection();
	
	public JsonObject query(String sqlQueryStr) {
		JsonObject tptEnumJsonObject = new JsonObject();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sqlQueryStr);
			rs = stmt.executeQuery();
			while (rs.next()) {				
		    	
				tptEnumJsonObject.addProperty("tpStatus", rs.getString("tpStatus"));
				tptEnumJsonObject.addProperty("status", rs.getString("status"));
				tptEnumJsonObject.addProperty("task", rs.getString("task"));
				tptEnumJsonObject.addProperty("tpTargetType", rs.getString("tpTargetType"));
				tptEnumJsonObject.addProperty("tptType", rs.getString("tptType"));
				tptEnumJsonObject.addProperty("run", rs.getString("pace_run"));
				tptEnumJsonObject.addProperty("walk", rs.getString("pace_walk"));
				tptEnumJsonObject.addProperty("easy", rs.getString("pace_easy"));
				tptEnumJsonObject.addProperty("brisk", rs.getString("pace_brisk"));
				tptEnumJsonObject.addProperty("jog", rs.getString("pace_jog"));
				tptEnumJsonObject.addProperty("fast", rs.getString("pace_fast"));
				tptEnumJsonObject.addProperty("recovery", rs.getString("pace_recovery"));
			}
		} catch (SQLException e) {
            System.out.println("SQL Query Error: " + e.getMessage());
		} catch (Exception e) {
            System.out.println("Query Error: " + e.getStackTrace());
		} finally {
		    DbUtils.closeQuietly(stmt);
		    DbUtils.closeQuietly(rs);
		    DbUtils.closeQuietly(conn);
		}
		return tptEnumJsonObject;
	}
	
    public boolean update(String tpOwnerId, String weeks){
		String updateTableSQL = "UPDATE t_oracle_tp SET weeks=? WHERE tpOwnerId=?";
		PreparedStatement preparedStatement = null;

		try {
			preparedStatement = this.conn.prepareStatement(updateTableSQL);
			preparedStatement.setString(1, weeks);
			preparedStatement.setString(2, tpOwnerId);

			preparedStatement.executeUpdate();
            return true;
		} catch (SQLException e) {
            System.out.println("SQL Update Error: "	+ e.getMessage());
            return false;            
		} catch (Exception e) {
            System.out.println("Update Error: "	+ e.getMessage());
            return false;            
		} finally {
		    DbUtils.closeQuietly(preparedStatement);
		    DbUtils.closeQuietly(conn);
		}
    }
    
    public boolean updateNew(String updateTableSQL){

    	PreparedStatement preparedStatement = null;
		try {
			preparedStatement = this.conn.prepareStatement(updateTableSQL);
			preparedStatement.executeUpdate();
            return true;
		} catch (SQLException e) {
            System.out.println("SQL Update Error: "	+ e.getMessage());
            return false;            
		} catch (Exception e) {
            System.out.println("Update Error: "	+ e.getMessage());
            return false;            
		} finally {
		    DbUtils.closeQuietly(preparedStatement);
		    DbUtils.closeQuietly(conn);
		}
    }
    
    public JsonObject getPlanTemplateEnum() {
		String queryStr = "SELECT * FROM t_oracle_tptenum";
		return this.query(queryStr);
    }
}
