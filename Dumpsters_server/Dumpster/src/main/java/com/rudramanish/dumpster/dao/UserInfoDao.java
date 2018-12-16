package com.rudramanish.dumpster.dao;

import org.springframework.stereotype.Component;

@Component
public class UserInfoDao {
	
	private String clue;
	private String address;
	private int pinCode;
	
	public String getClue(){
		return this.clue;
	}
	
	public void setClue(String clue){
		this.clue = clue;
	}
	
	public String getAddress(){
		return this.address;
	}
	
	public void setAddress(String address){
		this.address = address;
	}
	
	public void setPinCode(int zip){
		this.pinCode = zip;
	}
	
	public int getPinCode(){
		return this.pinCode;
	}
}
