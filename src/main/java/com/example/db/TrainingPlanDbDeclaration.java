/* Copyright © 2016 Oracle and/or its affiliates. All rights reserved. */

package com.example.db;

import java.util.List;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.example.TrainingPlan.TrainingPlan;


public class TrainingPlanDbDeclaration {
    
    List<TrainingPlan> eList = null;
	
	private final Connection conn = DBConnectionMysql.getInstance().getConnection();
	
	public List<TrainingPlan> query(String sqlQueryStr) {
		List<TrainingPlan> resultList = new ArrayList<>();
		try (PreparedStatement stmt = conn.prepareStatement(sqlQueryStr)) {
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				//JSONArray weeks = new JSONArray(rs.getString("weeks"));
				//System.out.print(weeks);
				resultList.add(
	                    new TrainingPlan(
	                    		rs.getString("tpId"), rs.getString("tpOwnerId"), rs.getString("tpPublishedAt"), rs.getString("tpUpdateAt"),
	                    		rs.getString("tpOwner"), rs.getString("tpStatus"), rs.getString("tpStart"), rs.getString("tpEnd"),
	                    		rs.getString("tpTargetType"), rs.getString("tpTargetMatchid"), rs.getInt("tpVersionNo"),
	                    		rs.getString("tptId"), rs.getString("tptTile"), rs.getString("tptType"), rs.getString("tptDescrition"), rs.getString("weeks"))
	            );
			}
		} catch (SQLException e) {
            System.out.println("SQL Query Error: " + e.getMessage());
		} catch (Exception e) {
            System.out.println("Query Error: " + e.getStackTrace());
		}
		return resultList;
	}

    public boolean add(TrainingPlan tpItem){
		String insertTableSQL = "INSERT INTO t_oracle_tp "
				+ "(tpId, tpOwnerId, tpPublishedAt, tpUpdateAt, tpOwner, "
				+ "tpStatus, tpStart, tpEnd, tpTargetType, tpTargetMatchid, tpVersionNo,"
				+ "tptId, tptTile, tptType, tptDescrition, weeks) "
				+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		try (PreparedStatement preparedStatement = this.conn
				.prepareStatement(insertTableSQL)) {

			preparedStatement.setString(1, tpItem.gettpId().replace("\"", ""));
			preparedStatement.setString(2, tpItem.gettpOwnerId().replace("\"", ""));
			preparedStatement.setString(3, tpItem.gettpPublishedAt().replace("\"", ""));
			preparedStatement.setString(4, tpItem.gettpUpdateAt().replace("\"", ""));
			preparedStatement.setString(5, tpItem.gettpOwner().replace("\"", ""));
			preparedStatement.setString(6, tpItem.gettpStatus().replace("\"", ""));
			preparedStatement.setString(7, tpItem.gettpStart().replace("\"", ""));
			preparedStatement.setString(8, tpItem.gettpEnd().replace("\"", ""));
			preparedStatement.setString(9, tpItem.gettpTargetType().replace("\"", ""));
			preparedStatement.setString(10, tpItem.gettpTargetMatchid().replace("\"", ""));
			preparedStatement.setLong(11, tpItem.gettpVersionNo());
			preparedStatement.setString(12, tpItem.gettptId().replace("\"", ""));
			preparedStatement.setString(13, tpItem.gettptTile().replace("\"", ""));
			preparedStatement.setString(14, tpItem.gettptType().replace("\"", ""));
			preparedStatement.setString(15, tpItem.gettptDescrition().replace("\"", ""));
			preparedStatement.setString(16, tpItem.getWeeks());

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
    /*
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
    
    }*/
    
    public TrainingPlan getActivePlan(String tpOwnerId){
		String queryStr = "SELECT * FROM t_oracle_tp WHERE tpOwnerId=" + "\""+tpOwnerId+"\"" + "and tpStatus =\"active\"";
		//System.out.println(queryStr);
		List<TrainingPlan> resultList = this.query(queryStr);
                
		if (resultList.size() > 0) {
			//System.out.println(resultList.get(0));
			return resultList.get(0);
		} else {
            return null;
        }    
    }
    
    public List<TrainingPlan> getAllPlan(String tpOwnerId){
		String queryStr = "SELECT * FROM t_oracle_tp WHERE tpOwnerId=" + "\""+tpOwnerId+"\"";
		List<TrainingPlan> resultList = this.query(queryStr);
        return resultList;
    }
    
    public TrainingPlan getPlanOnTP(String tpId){
		String queryStr = "SELECT * FROM t_oracle_tp WHERE tpId=" + "\""+tpId+"\"";
		List<TrainingPlan> resultList = this.query(queryStr);
		
		if (resultList.size() > 0) {
			return resultList.get(0);
		} else {
            return null;
        }  
    }
    
    public boolean deletePlan(String tpId){
		String deleteRowSQL = "DELETE FROM t_oracle_tp WHERE tpId=?";
		try (PreparedStatement preparedStatement = this.conn
				.prepareStatement(deleteRowSQL)) {
			preparedStatement.setString(1, tpId);
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
