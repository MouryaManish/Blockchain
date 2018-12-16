package com.rudramanish.dumpster.controller;

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

@Controller()
@RequestMapping("/rudramanish")
public class MainController {
	private static final Logger logger = LoggerFactory.getLogger(Application.class);
	private Application app;
	
	@Autowired
	private ImageInfoDao imageInfo;
	
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
	/*
		Set<UserInfoDao> userSet = new HashSet<UserInfoDao>(); 
		for(int i =1;i<4;i++){
			UserInfoDao userInfoDao = new UserInfoDao();
			userInfo.setClue(Integer.toString(i));
			userInfo.setAddress(Integer.toString(i));
			userSet.add(userInfo);
		}
		return userSet;*/
		/*ArrayList<String> a = new ArrayList<String>();
		a.add("hi");
		a.add("hello");
		a.add("bye");
		return a;*/
	}
	
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
}
