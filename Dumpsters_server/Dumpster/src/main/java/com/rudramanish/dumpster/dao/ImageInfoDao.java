package com.rudramanish.dumpster.dao;

import java.math.BigDecimal;

import org.springframework.stereotype.Repository;

@Repository
public class ImageInfoDao {
	public String address;
	public String category;
	public Integer subSection;
	public String img;
	public BigDecimal price;
	public int zipCode;
	public String state;
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
	
	public String getState(){
		return this.state;
	}
	
	public void setState(String state){
		this.state = state;
	}
	
	public BigDecimal getPrice(){
		return this.price;
	}
	
	public void setPrice(BigDecimal price ){
		this.price = price;
	}
	
}
	

