/* Copyright © 2016 Oracle and/or its affiliates. All rights reserved. */

package com.example.db;

import java.util.List;

import org.json.JSONArray;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.example.TrainingPlanTemplate.TrainingPlanTemplate;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class TrainingPlanTemplateDbDeclaration {
    
    List<TrainingPlanTemplate> eList = null;
	
	private final Connection conn = DBConnectionMysql.getInstance().getConnection();
	    
	public List<TrainingPlanTemplate> query(String sqlQueryStr) {
		List<TrainingPlanTemplate> resultList = new ArrayList<>();
		try (PreparedStatement stmt = conn.prepareStatement(sqlQueryStr)) {
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				//JSONArray weeks = new JSONArray(rs.getString("weeks"));
				//System.out.print(weeks);
				resultList.add(
	                    new TrainingPlanTemplate(rs.getString("tptId"), rs.getString("tptTile"), 
	                        rs.getString("tptType"), rs.getString("tptDescrition"), rs.getString("publishedAt"),rs.getString("weeks"))
	            );
			}
		} catch (SQLException e) {
            System.out.println("SQL Query Error: " + e.getMessage());
		} catch (Exception e) {
            System.out.println("Query Error: " + e.getStackTrace());
		}
		return resultList;
	}

    public boolean add(TrainingPlanTemplate tptItem){
		String insertTableSQL = "INSERT INTO t_oracle_tpt "
				+ "(tptId, tptTile, tptType, tptDescrition, publishedAt, weeks) "
				+ "VALUES(?,?,?,?,?,?)";

		try (PreparedStatement preparedStatement = this.conn
				.prepareStatement(insertTableSQL)) {

			preparedStatement.setString(1, tptItem.gettptId().replace("\"", ""));
			preparedStatement.setString(2, tptItem.gettptTile().replace("\"", ""));
			preparedStatement.setString(3, tptItem.gettptType().replace("\"", ""));
			preparedStatement.setString(4, tptItem.gettptDescrition().replace("\"", ""));
			preparedStatement.setString(5, tptItem.getPublishedAt().replace("\"", ""));
			preparedStatement.setString(6, tptItem.getWeeks());

			preparedStatement.executeUpdate();
            return true;
		} catch (SQLException e) {
            System.out.println("SQL Add Error: " + e.getMessage());
            return false;
            
		} catch (Exception e) {
            System.out.println("Add Error: " + e.getMessage());
            return false;
		}
    }
    
    public boolean update(String tptId, TrainingPlanTemplate tptItem){
		String updateTableSQL = "UPDATE t_oracle_tpt SET tptTile=?, tptType=?, tptDescrition=?, publishedAt=?, weeks=? WHERE tptId=?";
		try (PreparedStatement preparedStatement = this.conn
				.prepareStatement(updateTableSQL);) {
			preparedStatement.setString(1, tptItem.gettptTile().replace("\"", ""));
			preparedStatement.setString(2, tptItem.gettptType().replace("\"", ""));
			preparedStatement.setString(3, tptItem.gettptDescrition().replace("\"", ""));
			preparedStatement.setString(4, tptItem.getPublishedAt().replace("\"", ""));
			preparedStatement.setString(5, tptItem.getWeeks());
			preparedStatement.setString(6, tptId);

			preparedStatement.executeUpdate();
            return true;
		} catch (SQLException e) {
            System.out.println("SQL Update Error: "	+ e.getMessage());
            return false;            
		} catch (Exception e) {
            System.out.println("Update Error: "	+ e.getMessage());
            return false;            
		}
    
    }
    
    public TrainingPlanTemplate getPlanTemplate(String tptId){
		String queryStr = "SELECT * FROM t_oracle_tpt WHERE tptId=" + "\""+tptId+"\"";
		//System.out.println(queryStr);
		List<TrainingPlanTemplate> resultList = this.query(queryStr);
                
		if (resultList.size() > 0) {
			//System.out.println(resultList.get(0));
			return resultList.get(0);
		} else {
            return null;
        }    
    }
    
    public List<TrainingPlanTemplate> listAllPlanTemplate(){
		String queryStr = "SELECT * FROM t_oracle_tpt";
		List<TrainingPlanTemplate> resultList = this.query(queryStr);
        return resultList;
    }
    
    public boolean deletePlanTemplate(String tptId){
		String deleteRowSQL = "DELETE FROM t_oracle_tpt WHERE tptId=?";
		try (PreparedStatement preparedStatement = this.conn
				.prepareStatement(deleteRowSQL)) {
			preparedStatement.setString(1, tptId);
			preparedStatement.executeUpdate();
            return true;

		} catch (SQLException e) {
			System.out.println("SQL Delete Error: " + e.getMessage());
            return false;
		} catch (Exception e) {
			System.out.println("Delete Error: " + e.getMessage());
            return false;
		}
	}
}
