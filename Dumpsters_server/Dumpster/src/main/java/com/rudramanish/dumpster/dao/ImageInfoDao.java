package com.rudramanish.dumpster.dao;

import org.springframework.stereotype.Repository;

@Repository
public class ImageInfoDao {
	public String address;
	public String category;
	public Integer subSection;
	public String img;
	public int price;
	public int zipCode;
	public String description;

	
	public String getCategory(){
		return this.category;
	}
	
	public void setCategory(String category ){
		this.category = category;
	}
	
	public String getImg(){
		return this.img;
	}
	
	public void setImg(String imgA ){
		this.img = imgA ;
	}
	
	public String getAddress(){
		return this.address ;
	}
	
	public void setAddress(String address){
		this.address = address;
	}
	
	public int getZipCode(){
		return this.zipCode ;
	}
	
	public void setZipCode(int zipCode){
		this.zipCode = zipCode ;
	}
	
	public Integer getSubSection(){
		return this.subSection;
	}
	
	public void setSubSection(Integer section){
		this.subSection = section;
	}
	
	public String getDescription(){
		return this.description;
	}
	
	public void setDescription(String description){
		this.description = description;
	}
/*	
	public String getImgB(){
		return this.imgB;
	}
	
	public void setImgB(String imgB ){
		this.imgB = imgB;
	}
	
	public String getImgC(){
		return this.imgC;
	}
	
	public void setImgC(String imgC ){
		this.imgC = imgC;
	}
	
	public String getImgD(){
		return this.imgD;
	}
	
	public void setImgD(String imgD ){
		this.imgD = imgD;
	}
	*/
	public int getPrice(){
		return this.price;
	}
	
	public void setPrice(int price ){
		this.price = price;
	}
	
}
	

