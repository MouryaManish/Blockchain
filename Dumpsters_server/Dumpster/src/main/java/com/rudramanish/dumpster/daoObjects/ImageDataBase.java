package com.rudramanish.dumpster.daoObjects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.rudramanish.dumpster.dao.ImageInfoDao;

@Repository
public class ImageDataBase {

	private DatabaseDaoQuery daoMain;
	
	@Autowired
	public void setDatabaseDaoQuery(DatabaseDaoQuery dao){
		this.daoMain = dao;
	}
	
	// add image
	public String addImages(String state,ImageInfoDao imageInfo){
			Integer section = getSubSection(imageInfo);
			Connection con = null;
			PreparedStatement stmt = null;
			String sql ="insert into imageList values(?,?,?,?,?,?,?)";	
			int a =0;
		try{
			con = this.daoMain.getConnection();
			stmt =  con.prepareStatement(sql);
			stmt.setString(1,imageInfo.getAddress());
			stmt.setString(2,imageInfo.getCategory());
			stmt.setInt(3,section);
			stmt.setString(4,state);
			stmt.setString(5,imageInfo.getImg());
			stmt.setInt(6,imageInfo.getPrice());
			stmt.setString(7,imageInfo.getDescription());
			a = stmt.executeUpdate();
			this.daoMain.commitConnection(con);
		}catch(SQLException e){
			this.daoMain.rollbackCommit(con);
			System.out.println("******error from ImageDatabaseDao.addImage*******");
			System.out.println(e);
		}finally{
			this.daoMain.stmtClose(stmt);
			this.daoMain.releaseConnection(con);
		}
		if(a >= 1)
			return "sucess";
		else
			return "Failure";
	}
	
	// getting subSection
	public Integer getSubSection(ImageInfoDao imageInfo ){
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet set = null;
		Integer section= null;
		String sql ="select sub.subcat from subCatalog sub,"
				+ "imageList imgL where sub.subcat <> ? and"
				+ " imgL.address =? order by sub.subcat";
		try{
			con = this.daoMain.getConnection();
			stmt =  con.prepareStatement(sql);
			stmt.setString(1,imageInfo.getCategory());
			stmt.setString(2,imageInfo.getAddress());
			set = stmt.executeQuery();
			while(set.next()){
				section = set.getInt(1);
				break;
			}
			
		}catch(SQLException e){
			System.out.println("********sql error from getting subsection :");
			System.out.println(e);
		}finally{
			this.daoMain.resultSetClose(set);
			this.daoMain.stmtClose(stmt);
			this.daoMain.releaseConnection(con);
		}
		return section;
	}
	
	// getOne Image
	public Map<Integer,ImageInfoDao> getSingleImage(ImageInfoDao imageInfo){
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet set = null;
		Map<Integer,ImageInfoDao> regionalData = new HashMap<Integer,ImageInfoDao>();
		//***** try with distinct later*****//
		String sql ="select user.address,imgL.category,imgL.subcat,imgL.price,imgL.image from "
				+ "userrecord user join imageList imgL on user.address = imgL.address join category cat on"
				+ "imgL.category=cat.typeList where user.pincode = ? and cat.typeList =? and imgL.state = 'active' order by "
				+ "imgL.price group by user.address,imgL.category,imgL.subcat order by imgL.price";
		try{
			con = this.daoMain.getConnection();
			stmt = con.prepareStatement(sql);
			stmt.setInt(1,imageInfo.getZipCode());
			stmt.setString(2,imageInfo.getCategory());
			set = stmt.executeQuery();
			int count = 0;
			while(set.next()){
				count++;
				ImageInfoDao image = new ImageInfoDao();
				image.setAddress(set.getString(1));
				image.setCategory(set.getString(2));
				image.setPrice(set.getInt(4));
				image.setImg(set.getString(5));
				regionalData.put(count,image);
			}
						
		}catch(SQLException e){
			System.out.println("********sql error from getting single image :");
			System.out.println(e);
		}finally{
			this.daoMain.resultSetClose(set);
			this.daoMain.stmtClose(stmt);
			this.daoMain.releaseConnection(con);
		}
		return regionalData;
	}
	
	//get chosen Images;
	public Map<Integer,ImageInfoDao> getSubImages(ImageInfoDao imageInfo){
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet set = null;
		Map<Integer,ImageInfoDao> chosenImage = new HashMap<Integer,ImageInfoDao>();
		//***** try with distinct later*****//
		String sql ="select imgL.address,imgL.category,imgL.subcat,imgL.price,imgL.image,imgL.description from imageList imgL where imgL.address = ? and"
				+ "imgL.category =? and umgL.subcat =? and imgL.price=?";
		try{
			con = this.daoMain.getConnection();
			stmt = con.prepareStatement(sql);
			stmt.setString(1,imageInfo.getAddress());
			stmt.setString(2,imageInfo.getCategory());
			stmt.setInt(3,imageInfo.getSubSection());
			stmt.setInt(4,imageInfo.getPrice());
			set = stmt.executeQuery();
			int count = 0;
			while(set.next()){
				count++;
				ImageInfoDao image = new ImageInfoDao();
				image.setAddress(set.getString(1));
				image.setCategory(set.getString(2));
				image.setPrice(set.getInt(4));
				image.setImg(set.getString(5));
				image.setDescription(set.getString(6));
				chosenImage.put(count,image);
			}
						
		}catch(SQLException e){
			System.out.println("********sql error from getting single image :");
			System.out.println(e);
		}finally{
			this.daoMain.resultSetClose(set);
			this.daoMain.stmtClose(stmt);
			this.daoMain.releaseConnection(con);
		}
		return chosenImage;
	}
	
}
