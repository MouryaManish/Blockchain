package com.rudramanish.dumpster.daoObjects;

import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
			//this.daoMain.commitConnection(con);
		}catch(SQLException e){
			//this.daoMain.rollbackCommit(con);
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
			//this.daoMain.commitConnection(con);
		}catch(SQLException e){
			//this.daoMain.rollbackCommit(con);
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
		String sql ="select count(*) from imageList imgList where " +
		"imgList.address = ? and imgList.category = ? and "+
				"imgList.subcat = ? and imgList.image=?";	
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
		String sql ="select count(*) from priceList plist " +
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
	public ArrayList<ImageInfoDao> getSingleImage(ImageInfoDao imageInfo,int distance){
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet set = null;
		ArrayList<ImageInfoDao> regionalData = new ArrayList<ImageInfoDao>();
		//***** try with distinct later*****//
		String sql = "select user.address,list.category,list.subcat,plist.price,plist.description,list.image "
				+ "from userrecord as user join imageList as list on user.address=list.address join pricelist as plist"
				+ " on list.address = plist.address and list.category = plist.category and list.subcat=plist.subcat "
				+"where list.category=? and list.state=? and user.pincode between ? and ? "
				+ "group by list.address,list.category,list.subcat order by plist.price";
		try{
			con = this.daoMain.getConnection();
			stmt = con.prepareStatement(sql);
			System.out.println("  "+
			imageInfo.getCategory()+"  " + imageInfo.getState() +"  "+(imageInfo.getZipCode() - distance)+
			"  "+(imageInfo.getZipCode() - distance) );
			stmt.setString(1,imageInfo.getCategory());
			stmt.setString(2,imageInfo.getState());
			stmt.setInt(3,(imageInfo.getZipCode() - distance));
			stmt.setInt(4,(imageInfo.getZipCode() + distance));
			System.out.println("sql executing for single image");
			set = stmt.executeQuery();
			while(set.next()){
				ImageInfoDao image = new ImageInfoDao();
				image.setAddress(set.getString(1));
				image.setCategory(set.getString(2));
				image.setPrice(set.getBigDecimal(4));
				image.setSubSection(set.getInt(3));
				image.setDescription(set.getString(5));
				image.setImg(set.getString(6));
				regionalData.add(image);
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
	public ArrayList<ImageInfoDao> getSubImages(ImageInfoDao imageInfo){
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet set = null;
		ArrayList<ImageInfoDao> chosenImage = new ArrayList<ImageInfoDao>();
		//***** try with distinct later*****//
		String sql ="select plist.address,plist.category,plist.subcat,plist.price,plist.description,imgL.image from "+
				"imageList as imgL join priceList as plist on imgL.address=plist.address and imgL.category = plist.category "
				+" and imgL.subcat = plist.subcat where plist.address = ? and plist.category =? and plist.subcat = ?";
		try{
			con = this.daoMain.getConnection();
			stmt = con.prepareStatement(sql);
			stmt.setString(1,imageInfo.getAddress());
			stmt.setString(2,imageInfo.getCategory());
			stmt.setInt(3,imageInfo.getSubSection());
			//stmt.setBigDecimal(4,imageInfo.getPrice());
			set = stmt.executeQuery();
			while(set.next()){
				ImageInfoDao image = new ImageInfoDao();
				image.setAddress(set.getString(1));
				image.setCategory(set.getString(2));
				image.setSubSection(set.getInt(3));
				image.setPrice(set.getBigDecimal(4));
				image.setDescription(set.getString(5));
				image.setImg(set.getString(6));
				chosenImage.add(image);
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
	
	public ArrayList<String> ipfsUpload(String category,Integer section,String address){
		ArrayList<String> hash = new ArrayList<String>();
		try{
			System.out.println("ipfs const will be called /n");
		IPFS ipfs = new IPFS("/ip4/127.0.0.1/tcp/5001");
				//IPFS(new MultiAddress("/ip4/127.0.0.1/tcp/5001"));
		System.out.println("ipfs local will be called /n");
		ipfs.refs.local();
		String path = "E:/CodeBase/Pro.Duc_Tran/Blockchain/imageFiles/"+address+"/"+
		category+"/"+section;
		File fileRead = new File(path);
		System.out.println(fileRead.exists());
		System.out.println("namestream will be created /n");
		NamedStreamable.FileWrapper file = new NamedStreamable.FileWrapper(fileRead);
		System.out.println("markel nod will be cretaed/n");
		List<MerkleNode> addResult = ipfs.add(file);   //.get(0);
		System.out.println("markel node list testing /n");
		for(int i=0;i < (addResult.size() -1);i++){
			hash.add(addResult.get(i).hash.toString());
			System.out.println(hash.get(i));
		}
	/*	for(MerkleNode node: addResult){
			System.out.println("node details");
			System.out.println(node.hash);
		}*/
		System.out.println("all cmd have worked so far/n");
		}catch(Exception ex){
			logger.info("********error from ipfs******\n");
			logger.info("error ",ex);
		}
		return hash;
	}
	
	public String orderRecord(ImageInfoDao imageInfo){
		
		Connection con = null;
		PreparedStatement stmt = null;
		String sql ="update imageList set state = 'sold' "+ 
				"where address = ? and category = ? and subcat =?";
		int a =0;
	try{
		con = this.daoMain.getConnection();
		stmt =  con.prepareStatement(sql);
		stmt.setString(1,imageInfo.getAddress());
		stmt.setString(2,imageInfo.getCategory());
		stmt.setInt(3,imageInfo.getSubSection());
		a = stmt.executeUpdate();
		//this.daoMain.commitConnection(con);
	}catch(SQLException e){
		//this.daoMain.rollbackCommit(con);
		System.out.println("******error from ImageDatabaseDao.addImage*******");
		System.out.println(e);
	}finally{
		this.daoMain.stmtClose(stmt);
		this.daoMain.releaseConnection(con);
	}
	if(a >= 1)
		return "success";
	else
		return "failed";
		
	}
	
}
