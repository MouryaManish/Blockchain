package com.rudramanish.dumpster.daoObjects;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.stereotype.Repository;

@Repository
public class DatabaseDaoQuery {
	
	public Connection getConnection(){
		Connection con = null;
		try{  
			Class.forName("com.mysql.jdbc.Driver");  
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/rudramanish?useSSL=false","root","bhole");   
			}catch(SQLException e ){
				System.out.println("SQL exception: "+ e);
				}catch(ClassNotFoundException e){
					System.out.println("driver class not found exception:\n"+ e);
				}
		System.out.println("********Connection establised*******");
		return con;
	}
	
	public void releaseConnection(Connection con){
		try{
			con.close();
		}catch(SQLException e){
			System.out.println("connection closing error from sql: \n" + e);
		}
	}
	
	public void rollbackCommit(Connection con){
		try{
			con.rollback();
		}catch(SQLException e){
			System.out.println(" connection rolled back sql exception :\n" + e);
		}
	}
	
	public void commitConnection(Connection con){
		try{
			con.commit();
		}catch(SQLException e){
			System.out.println(" commit sql exception :\n" + e);
		}
	}
	
	public void stmtClose(PreparedStatement stmt){
		try{
			stmt.close();
		}catch(SQLException e){
			System.out.println("statement closing error \n"
					+ e);
		}
	}
	
	public void resultSetClose(ResultSet set){
		try{
			set.close();
		}catch(SQLException e){
			System.out.println("resultset closing error \n"
					+ e);
		}
	}
}
