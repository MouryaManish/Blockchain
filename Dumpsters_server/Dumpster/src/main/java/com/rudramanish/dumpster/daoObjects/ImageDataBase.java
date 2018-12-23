package com.rudramanish.dumpster.daoObjects;

import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.rudramanish.dumpster.dao.ImageInfoDao;

@Repository
public class ImageDataBase {

	private DatabaseDaoQuery daoMain;
	private static final Logger logger = LoggerFactory.getLogger(ImageDataBase.class);
	
	@Autowired
	public void setDatabaseDaoQuery(DatabaseDaoQuery dao){
		this.daoMain = dao;
	}
	
	// add image
	public String addImages(ImageInfoDao imageInfo){
			Connection con = null;
			PreparedStatement stmt = null;
			String sql ="insert into imageList values(?,?,?,?,?)";	
			int a =0;
		try{
			con = this.daoMain.getConnection();
			stmt =  con.prepareStatement(sql);
			stmt.setString(1,imageInfo.getAddress());
			stmt.setString(2,imageInfo.getCategory());
			stmt.setInt(3,imageInfo.getSubSection());
			stmt.setString(4,imageInfo.getState());
			stmt.setString(5,imageInfo.getImg());
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
	
	
	// add image
	public String addPrice(ImageInfoDao imageInfo){
			Connection con = null;
			PreparedStatement stmt = null;
			String sql ="insert into priceList values(?,?,?,?,?)";	
			int a =0;
		try{
			con = this.daoMain.getConnection();
			stmt =  con.prepareStatement(sql);
			stmt.setString(1,imageInfo.getAddress());
			stmt.setString(2,imageInfo.getCategory());
			stmt.setInt(3,imageInfo.getSubSection());
			stmt.setBigDecimal(4, imageInfo.getPrice());
			stmt.setString(5,imageInfo.getDescription());
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
	
	
	public Integer imageCheck(ImageInfoDao imageInfo){
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet set = null;
		Integer count= null;
		String sql ="select count(*) from imageList imgList where" +
		"imgList.address = ? and imgList.category = ? and "+
				"imgList.subcat = ? and imgList.image=?;";	
		try{
			con = this.daoMain.getConnection();
			stmt =  con.prepareStatement(sql);
			stmt.setString(1,imageInfo.getAddress());
			stmt.setString(2,imageInfo.getCategory());
			stmt.setInt(3,imageInfo.getSubSection());
			stmt.setString(4,imageInfo.getImg());
			
			set = stmt.executeQuery();
			while(set.next()){
				count = set.getInt(1);
			}
			
		}catch(SQLException e){
			System.out.println("********sql error from getting image check :");
			System.out.println(e);
		}finally{
			this.daoMain.resultSetClose(set);
			this.daoMain.stmtClose(stmt);
			this.daoMain.releaseConnection(con);
		}
		return count;
		
	}
	
	
	public Integer priceCheck(ImageInfoDao imageInfo){
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet set = null;
		Integer count= null;
		String sql ="select count(*) from priceList plist" +
		"where plist.address =? and "+
		"plist.category = ? and plist.subcat = ?";
		
		try{
			con = this.daoMain.getConnection();
			stmt =  con.prepareStatement(sql);
			stmt.setString(1,imageInfo.getAddress());
			stmt.setString(2,imageInfo.getCategory());
			stmt.setInt(3,imageInfo.getSubSection());	
			set = stmt.executeQuery();
			while(set.next()){
				count = set.getInt(1);
			}
		}catch(SQLException e){
			System.out.println("********sql error from getting pricecheck:");
			System.out.println(e);
		}finally{
			this.daoMain.resultSetClose(set);
			this.daoMain.stmtClose(stmt);
			this.daoMain.releaseConnection(con);
		}
		return count;
		
	}
	
	// getting subSection
	public Integer getSubSection(ImageInfoDao imageInfo ){
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet set = null;
		Integer section= null;
		String sql ="select imgList.subcat from imageList imgList where "+
		"imgList.address= ? and imgList.category= ? order by imgList.subcat "+
				"DESC limit 1";	
		try{
			con = this.daoMain.getConnection();
			stmt =  con.prepareStatement(sql);
			stmt.setString(1,imageInfo.getAddress());
			stmt.setString(2,imageInfo.getCategory());
			set = stmt.executeQuery();
			while(set.next()){
				section = set.getInt(1);
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
	
public Integer getSubSectionCount(){
	Connection con = null;
	PreparedStatement stmt = null;
	ResultSet set = null;
	Integer section_count= null;
	String sql ="select subcat from subCatalog order by subcat desc limit 1";
	try{
		con = this.daoMain.getConnection();
		stmt =  con.prepareStatement(sql);
		set = stmt.executeQuery();
		while(set.next()){
			section_count = set.getInt(1);
		}
		
	}catch(SQLException e){
		System.out.println("********sql error from getting subsection :");
		System.out.println(e);
	}finally{
		this.daoMain.resultSetClose(set);
		this.daoMain.stmtClose(stmt);
		this.daoMain.releaseConnection(con);
	}
	return section_count;	
}
	
	
	// getOne Image
	public Map<Integer,ImageInfoDao> getSingleImage(ImageInfoDao imageInfo){
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet set = null;
		Map<Integer,ImageInfoDao> regionalData = new HashMap<Integer,ImageInfoDao>();
		//***** try with distinct later*****//
		String sql ="select user.address,imgL.category,imgL.subcat,imgL.price,imgL.image from userrecord user join imageList imgL on user.address = imgL.address join priceList plist on"
				+ " plist.address"+ "where imgL.category = ? and imgL.state='active'"
				+ "and user.pincode  between ? and ? order by plist.price asc?";
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
				image.setPrice(set.getBigDecimal(4));
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
			stmt.setBigDecimal(4,imageInfo.getPrice());
			set = stmt.executeQuery();
			int count = 0;
			while(set.next()){
				count++;
				ImageInfoDao image = new ImageInfoDao();
				image.setAddress(set.getString(1));
				image.setCategory(set.getString(2));
				image.setPrice(set.getBigDecimal(4));
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
	
	public String ipfsUpload(Integer section){
		String hash = null;
		try{
			System.out.println("ipfs const will be called /n");
		IPFS ipfs = new IPFS("/ip4/127.0.0.1/tcp/5001");
				//IPFS(new MultiAddress("/ip4/127.0.0.1/tcp/5001"));
		System.out.println("ipfs local will be called /n");
		ipfs.refs.local();
		String path = "E:/CodeBase/Pro.Duc_Tran/Blockchain/imageFiles/"+section;
		File fileRead = new File(path);
		System.out.println(fileRead.exists());
		System.out.println("namestream will be created /n");
		NamedStreamable.FileWrapper file = new NamedStreamable.FileWrapper(fileRead);
		System.out.println("markel nod will be cretaed/n");
		List<MerkleNode> addResult = ipfs.add(file);   //.get(0);
		System.out.println("markel node list testing /n");
		for(MerkleNode node: addResult){
			System.out.println("node details");
			System.out.println(node.hash);
		}
		System.out.println("all cmd have worked so far/n");
		}catch(Exception ex){
			logger.info("********error from ipfs******\n");
			logger.info("error ",ex);
		}
		return hash;
	}
	
}
