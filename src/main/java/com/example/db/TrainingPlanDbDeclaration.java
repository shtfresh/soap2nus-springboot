/* Copyright © 2016 Oracle and/or its affiliates. All rights reserved. */

package com.example.db;

import java.util.List;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.PreparedStatement;
import org.apache.commons.dbutils.DbUtils;

import com.example.TrainingPlan.TrainingPlan;

public class TrainingPlanDbDeclaration {
    
    List<TrainingPlan> eList = null;
	
	public Connection conn = DBConnectionMysql.getInstance().getConnection();
	
	public List<TrainingPlan> query(String sqlQueryStr) {
		List<TrainingPlan> resultList = new ArrayList<>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sqlQueryStr);
			rs = stmt.executeQuery();
			while (rs.next()) {
				resultList.add(
	                    new TrainingPlan(
	                    		rs.getString("tpId"), rs.getString("tpOwnerId"), rs.getString("tpPublishedAt"), rs.getString("tpUpdateAt"),
	                    		rs.getString("tpOwner"), rs.getString("tpStatus"), rs.getString("tpStart"), rs.getString("tpEnd"), rs.getString("tpTargetType"),
	                    		rs.getString("tpTargetMatchid"), rs.getString("tpTargetMatchName"), rs.getInt("tpVersionNo"), rs.getString("minKilometre"), 
	                    		rs.getString("maxKilometre"), rs.getString("tptId"), rs.getString("tptTile"), rs.getString("tptType"), rs.getString("tptDescrition"),
	                    		rs.getString("weeks"))
	            );
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
		return resultList;
	}
	
    public boolean add(TrainingPlan tpItem) {
		String insertTableSQL = "INSERT INTO t_oracle_tp "
				+ "(tpId, tpOwnerId, tpPublishedAt, tpUpdateAt, tpOwner, "
				+ "tpStatus, tpStart, tpEnd, tpTargetType, tpTargetMatchid, tpTargetMatchName, "
				+ "tpVersionNo, minKilometre, maxKilometre, tptId, tptTile, tptType, tptDescrition, weeks) "
				+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = conn.prepareStatement(insertTableSQL);

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
			preparedStatement.setString(11, tpItem.gettpTargetMatchName().replace("\"", ""));
			preparedStatement.setLong(12, tpItem.gettpVersionNo());
			preparedStatement.setString(13, tpItem.getminKilometre().replace("\"", ""));
			preparedStatement.setString(14, tpItem.getmaxKilometre().replace("\"", ""));
			preparedStatement.setString(15, tpItem.gettptId().replace("\"", ""));
			preparedStatement.setString(16, tpItem.gettptTile().replace("\"", ""));
			preparedStatement.setString(17, tpItem.gettptType().replace("\"", ""));
			preparedStatement.setString(18, tpItem.gettptDescrition().replace("\"", ""));
			preparedStatement.setString(19, tpItem.getWeeks());

			preparedStatement.executeUpdate();
            return true;
		} catch (SQLException e) {
            System.out.println("SQL Add Error: " + e.getMessage());
            return false;
		} catch (Exception e) {
            System.out.println("Add Error: " + e.getMessage());
            return false;
		} finally {
		    DbUtils.closeQuietly(preparedStatement);
		    DbUtils.closeQuietly(conn);
		}
    }
    
    public boolean update(String updateTableSQL) {
    	PreparedStatement preparedStatement = null;
		try {
			preparedStatement = conn.prepareStatement(updateTableSQL);

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
    
    public TrainingPlan getActivePlan(String tpOwnerId){
		String queryStr = "SELECT * FROM t_oracle_tp WHERE tpOwnerId=" + "\""+tpOwnerId+"\"" + "and tpStatus =\"active\"";
		List<TrainingPlan> resultList = this.query(queryStr);
                
		if (resultList.size() > 0) {
			return resultList.get(0);
		} else {
            return null;
        }    
    }
    
    public List<TrainingPlan> getAllPlan(String tpOwnerId){
		String queryStr = "SELECT * FROM t_oracle_tp WHERE tpOwnerId=" + "\""+tpOwnerId+"\"";
		List<TrainingPlan> resultList = this.query(queryStr);
		
		if (resultList.size() > 0) {
	        return resultList;
		} else {
            return null;
        }
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
    
    public List<TrainingPlan> getAllActivePlan(String queryStr){
		List<TrainingPlan> resultList = this.query(queryStr);

		if (resultList.size() > 0) {
			return resultList;
		} else {
            return null;
        }
    }

    public boolean deletePlan(String tpId){
		String deleteRowSQL = "DELETE FROM t_oracle_tp WHERE tpId=?";
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = conn.prepareStatement(deleteRowSQL);
			preparedStatement.setString(1, tpId);
			preparedStatement.executeUpdate();
            return true;

		} catch (SQLException e) {
			System.out.println("SQL Delete Error: " + e.getMessage());
            return false;
		} catch (Exception e) {
			System.out.println("Delete Error: " + e.getMessage());
            return false;
		} finally {
		    DbUtils.closeQuietly(preparedStatement);
		    DbUtils.closeQuietly(conn);
		}
	}
}