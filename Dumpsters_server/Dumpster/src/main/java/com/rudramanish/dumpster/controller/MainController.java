package com.rudramanish.dumpster.controller;



import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.rudramanish.dumpster.Application;
import com.rudramanish.dumpster.dao.ImageInfoDao;
import com.rudramanish.dumpster.dao.UserInfoDao;
import com.rudramanish.dumpster.daoObjects.AccountRelatedDao;

@Controller()
@RequestMapping("/rudramanish")
public class MainController {
	private static final Logger logger = LoggerFactory.getLogger(Application.class);
	
	private Application app;
	
	@Autowired
	private ImageInfoDao imageInfo;

	
	@Autowired
	private AccountRelatedDao accountDao;
	
	@Autowired
	public void setApplication(Application app){
		this.app = app;
	} 
	
	@RequestMapping(value = "/dumpsters",method = RequestMethod.GET)
	public ModelAndView dumsterRouter(){
		
		ModelAndView mav = new ModelAndView("dumpsters");
	   // ((DatabaseDaoQuery)this.app.ctx.getBean(DatabaseDaoQuery.class)).test1();
		return mav;
	}
	
	
	
	@RequestMapping(value = "/authenticate",method = RequestMethod.POST)
	@ResponseBody
	public ArrayList<String> userAccess(@RequestBody UserInfoDao userInfo,
			HttpServletResponse httpResponse){
		ArrayList<String> message = new ArrayList<String>();
		String address = null;
		try{
			address = accountDao.authenticate(userInfo);
			Cookie userAddress = new Cookie("userAddress",address);
			userAddress.setMaxAge(-1);
			httpResponse.addCookie(userAddress);
		}catch(Exception e){
			System.out.println("error in user authentication ");
			System.out.println(e);
		}
	
		if(address != null){
				logger.info("sending success for authentication");
				message.add("success");
				message.add(address);
				message.add("/addImages");
				return message;
			}else{
				logger.info("sending failour for authentication");
				message.add("failed");
				return message;
			}
	}
	
	@RequestMapping(value = "/register",method = RequestMethod.POST)
	@ResponseBody
	public ArrayList<String> registerUser(@RequestBody UserInfoDao userInfo,
			HttpServletResponse httpResponse){
		ArrayList<String> message = new ArrayList<String>();
		String state = "failed";
		try{
			Cookie userAddress = new Cookie("userAddress",userInfo.getAddress());
			userAddress.setMaxAge(-1);
			httpResponse.addCookie(userAddress);
			state = accountDao.addUser(userInfo);
		}catch(Exception e){
			System.out.println("error in user registeration ");
		}
	
		if(state == "sucess"){
				logger.info("sending success for registeration");
				message.add(state);
				message.add(userInfo.getAddress());
				message.add("/addImages");
				return message;
			}else{
				logger.info("sending failour for authentication");
				message.add(state);
				return message;
			}
	}

	
	
/*
	@RequestMapping(value = "/authenticate",method = RequestMethod.POST)
	//(@RequestBody UserInfoDao userInfo,BindingResult result)
	public String userAccess(@RequestParam("clue") String clue,
			@RequestParam("address") String address,@RequestParam("pinCode") int zip,
			UserInfoDao userInfo){
		String state = null;
		try{
			userInfo.setAddress(address);
			userInfo.setClue(clue);
			userInfo.setPinCode(zip);
			state = accountDao.authenticate(userInfo);
		}catch(Exception e){
			System.out.println("error in ");
		}
			if(state == "success"){
				System.out.println("sending redirect");
				return "seller";
			}else{
				logger.info("user not present");
				return "AuthenticateFailed";
			}
	}
	*/
	/*

	
	
	
	@RequestMapping(value="/addUser",method=RequestMethod.POST)
	public String addUser(@RequestParam("clue") String clue,
			@RequestParam("address") String address,@RequestParam("pinCode") int zip,
			UserInfoDao userInfo){
		String state = null;
		try{
			userInfo.setAddress(address);
			userInfo.setClue(clue);
			userInfo.setPinCode(zip);
			state = accountDao.addUser(userInfo);
		}catch(Exception e){
			System.out.println("error in ");
		}
			if(state == "success"){
				System.out.println("sending redirect");
				return "userAdded";
			}else{
				logger.info("user not present");
				return "AuthenticateFailed";
			}
	}
	*/
	
	@RequestMapping(value="/addImages",method = RequestMethod.POST)
	@ResponseBody                           
	public String imageUpload(@RequestParam("price") Number price,
			@RequestParam("category") String categorey,
			@RequestParam("description") String description,
			@RequestParam("img") MultipartFile file){
			if(!file.isEmpty()){
			imageInfo.setPrice(price.intValue());
			imageInfo.setCategory(categorey);
			imageInfo.setDescription(description);
			try{ 
			/*String path = "E:/CodeBase/Pro.Duc_Tran/imageFiles/"+
					""+imageInfo.getAddress()+"/"+
					imageInfo.getCategory()+ "/";*/
			
			String path = "E:/CodeBase/Pro.Duc_Tran/imageFiles/";
			File dir = new File(path);
			if(!dir.exists()){
				dir.mkdirs();
				
				}
			File image = new File(path+file.getOriginalFilename());
			file.transferTo(image);
				
			}catch(IOException ex){
				logger.info("IO exception from imageUpload controller");
			}
			return "uploadSuccess";
			
			}else{
				return "file is empty";
			}
	}
	
	@RequestMapping(value="/ipfsUpload",method = RequestMethod.GET)
	public void ipfsUpload(){
		try{
			System.out.println("ipfs const will be called /n");
		IPFS ipfs = new IPFS("/ip4/127.0.0.1/tcp/5001");
				//IPFS(new MultiAddress("/ip4/127.0.0.1/tcp/5001"));
		System.out.println("ipfs local will be called /n");
		ipfs.refs.local();
		String path = "E:/CodeBase/Pro.Duc_Tran/imageFiles/new.jpeg";
		File fileRead = new File(path);
		System.out.println(fileRead.exists());
		System.out.println("namestream will be created /n");
		NamedStreamable.FileWrapper file = new NamedStreamable.FileWrapper(fileRead);
		System.out.println("markel nod will be cretaed/n");
		MerkleNode addResult = ipfs.add(file).get(0);
		System.out.println("jsonstring will be called /n");
		System.out.println(addResult.toJSONString());
		System.out.println("all cmd have worked so far/n");
		
		}catch(Exception ex){
			logger.info("********error from ipfs******\n");
			logger.info("error ",ex);
		}
	}
}
