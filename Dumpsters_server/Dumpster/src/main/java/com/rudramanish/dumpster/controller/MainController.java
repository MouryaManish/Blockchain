package com.rudramanish.dumpster.controller;



import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
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
import com.rudramanish.dumpster.daoObjects.ImageDataBase;

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
	private ImageDataBase  imageDatabase;
	
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

	@RequestMapping(value = "/seller",method = RequestMethod.GET)
	public ModelAndView sellerRouter(){
		ModelAndView mav = new ModelAndView("seller");
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
				//message.add(address);
				message.add("/seller");
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
	
		if(state == "success"){
				logger.info("sending success for registeration");
				message.add(state);
				//message.add(userInfo.getAddress());
				message.add("/seller");
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
	/*String path = "E:/CodeBase/Pro.Duc_Tran/imageFiles/"+
	""+imageInfo.getAddress()+"/"+
	imageInfo.getCategory()+ "/";*/
	@RequestMapping(value="/addImages",method = RequestMethod.POST)
	@ResponseBody                           
	public String imageUpload(@RequestParam("price") BigDecimal price,
			@RequestParam("category") String category,
			@RequestParam("description") String description,
			@RequestParam("img") MultipartFile file,
			HttpServletRequest request){
			
		String state = "active";
		Integer section;
		Integer section_count;
		Integer count;
		String hash = null;
		Cookie[] c = request.getCookies();
		System.out.println(c[0].getValue());
		imageInfo.setAddress(c[0].getValue());
		imageInfo.setCategory(category);
		imageInfo.setState(state);
		imageInfo.setDescription(description);
		imageInfo.setPrice(price);
		// checking for sebsection
		section_count = imageDatabase.getSubSectionCount();
		section = imageDatabase.getSubSection(imageInfo);
		System.out.println("section " + section);
		if(section == null || section < section_count){
			if(section == null){
				section = 1;
			}else{
				section +=1; 
			}
			imageInfo.setSubSection(section);
			if(!file.isEmpty()){
				try{ 
				String path = "E:/CodeBase/Pro.Duc_Tran/Blockchain/imageFiles/"+imageInfo.getSubSection();
				File dir = new File(path);
				if(!dir.exists()){
					dir.mkdirs();	
					}
				File image = new File(path+"/"+file.getOriginalFilename());
				file.transferTo(image);
				logger.info("Image created in the directory");
				//*******************************************
				hash = imageDatabase.ipfsUpload(section);
				logger.info("ipfs image has created");
				imageInfo.setImg(hash);
				count = imageDatabase.imageCheck(imageInfo);
				if(count == 0){
					imageDatabase.addImages(imageInfo);
				}else{
					return "image is present";
				}
				count = imageDatabase.priceCheck(imageInfo);
				if(count == 0){
					imageDatabase.addPrice(imageInfo);
				}
				}catch(IOException ex){
					logger.info("IO exception from imageUpload controller");
				}
				}else{
					return "No Image uploaded";
				}
			
		}else{
			return "We allow only limit section"+section_count;
		}
		return "image uploaded!";
	}
	
	//@RequestMapping(value="/ipfsUpload",method = RequestMethod.GET)

}
