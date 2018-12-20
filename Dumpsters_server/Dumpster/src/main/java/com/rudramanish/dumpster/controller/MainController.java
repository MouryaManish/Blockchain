package com.rudramanish.dumpster.controller;



import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
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
	
	@RequestMapping(value = "/userInfo",method = RequestMethod.POST)
	@ResponseBody
	public UserInfoDao userAccess(@RequestBody UserInfoDao userInfo,BindingResult
	result){
		try{
			if(result.hasErrors()){
				System.out.println("********error from /userInfo request******");
				throw new Exception();
			}
		}catch(Exception e){
			System.out.println("error in ");
		}
		return userInfo;
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
	@RequestMapping(value = "/authenticate",method = RequestMethod.POST,produces = "application/json")
	@ResponseBody
	public String userAccess(@RequestBody UserInfoDao userInfo){
		String state = null;
		try{
			state = accountDao.authenticate(userInfo);
		}catch(Exception e){
			System.out.println("error in user authentication ");
		}
			if(state == "success"){
				logger.info("sending success for authentication");
				return state;
			}else{
				logger.info("sending failour for authentication");
				state = "AuthenticateFailed";
				return state;
			}
	}
	
	
	
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
	
	@RequestMapping(value="/imageInfo",method = RequestMethod.POST)
	@ResponseBody                           
	public String imageUpload(@RequestParam("price") Number price,
			@RequestParam("categorey") String categorey,
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
			File image = new File(path+file.getOriginalFilename()+".jpeg");
			file.transferTo(image);
				
			}catch(IOException ex){
				logger.info("IO exception from imageUpload controller");
			}
			return "redirect:uploadSuccess";
			
			}else{
				return "redirect:uploadFailed";
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
