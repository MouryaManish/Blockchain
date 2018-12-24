package com.rudramanish.dumpster.controller;



import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.CookieStore;
import java.util.ArrayList;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
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
	public ModelAndView dumsterRouter(HttpServletResponse httpResponse){
		
		ModelAndView mav = new ModelAndView("dumpsters");
		
		Cookie sAddress = new Cookie("sAddress","");
		sAddress.setMaxAge(-1);
		httpResponse.addCookie(sAddress);
		
		Cookie sCategory = new Cookie("sCategory","");
		sCategory.setMaxAge(-1);
		httpResponse.addCookie(sCategory);
		
		
		Cookie sSubcat = new Cookie("sSubcat","");
		sSubcat.setMaxAge(-1);
		httpResponse.addCookie(sSubcat);
		
		
	   // ((DatabaseDaoQuery)this.app.ctx.getBean(DatabaseDaoQuery.class)).test1();
		return mav;
	}
	
//	@RequestParam("category") String category,@RequestParam("address") String address,@RequestParam("subSection")Integer subSection
	@RequestMapping(value ="/shop",method = RequestMethod.POST)
	@ResponseBody
	public ArrayList<String> shop(@RequestBody ImageInfoDao imageInfo,
			HttpServletRequest httpRequest,HttpServletResponse response){
		 String state = null;
		 System.out.println("*************inside shop*******");
		ArrayList<String> data = new ArrayList<String>();
		Cookie[] cookie = httpRequest.getCookies();
	/*	imageInfo.setAddress(address);
		imageInfo.setCategory(category);
		imageInfo.setSubSection(subSection);*/
		for(Cookie item: cookie){ 
			state = "failed";
			if(item.getName().equals("sAddress")){
				
				item.setValue(imageInfo.getAddress());
				response.addCookie(item);
				System.out.println("inside address");
				state="success";
			}
		if(item.getName().equals("sCategory")){
			item.setValue(imageInfo.getCategory());
			response.addCookie(item);
			System.out.println("inside category");
			state="success";
		}
		if(item.getName().equals("sSubcat")){
			item.setValue(imageInfo.getSubSection().toString());
			response.addCookie(item);
			System.out.println("inside subcat");
			state="success";
		}
		System.out.println(state);
	} 
		data.add(state);
		data.add("/shopping");
		
		return data;
		//imageDatabase.getSubImages(infoDao);
	}
	
	
	@RequestMapping(value = "/shopping",method = RequestMethod.GET)
	public ModelAndView shoopingRouter(){
		System.out.println("********8inside shopping********");
		ModelAndView mav = new ModelAndView("shop");
	   // ((DatabaseDaoQuery)this.app.ctx.getBean(DatabaseDaoQuery.class)).test1();
		return mav;
	}
	

	
	@RequestMapping(value="/userView",method = RequestMethod.POST)
	@ResponseBody
	public ArrayList<ImageInfoDao> userView(@RequestParam("zipCode") Integer zipCode,
			@RequestParam("category") String category,@RequestParam("pageNo") Integer pageNo){
		int minPointer=0;
		int maxPointer=0;
		int distance=0;
		String range = "short";
		ArrayList<ImageInfoDao> list;
		ArrayList<ImageInfoDao> tempList = new ArrayList<ImageInfoDao>();
		minPointer = pageNo -1;
		maxPointer = 3*pageNo -1;
		System.out.println("inside**********88");
		imageInfo.setCategory(category);
		imageInfo.setZipCode(zipCode);
		imageInfo.setState("active");
		if(range == "short"){
			distance = 2;
		}else if(range =="medium"){
			distance = 5;
		}else{
			distance = 7;
		}
	
		list = imageDatabase.getSingleImage(imageInfo,distance);
		System.out.println("****single image list received");
		for(ImageInfoDao imageInfo: list){
			System.out.println(imageInfo.getImg());
		}
		if(list.size()!= 0 && maxPointer < list.size()){
			for(int i=minPointer;i<=maxPointer;i++){
				tempList.add(list.get(i));
			}
		}else if((list.size()!= 0) && (minPointer < list.size())&& (maxPointer > list.size())){
			for(int i=minPointer;i<list.size();i++){
				tempList.add(list.get(i));
			}
		}else{
			
		}
		System.out.println("single list sent....");
		return tempList;
	}

	@RequestMapping(value="/selectedItems",method = RequestMethod.GET)
	@ResponseBody
	public ArrayList<ImageInfoDao> itemDetails(HttpServletRequest request,
			HttpServletResponse response){
			
			ArrayList<ImageInfoDao> list;
			Cookie[] cookies = request.getCookies();
			for(Cookie item:cookies){
				if(item.getName().equals("sAddress")){
					imageInfo.setAddress(item.getValue());
					item.setValue("");
					response.addCookie(item);
				}
			if(item.getName().equals("sCategory")){
				imageInfo.setCategory(item.getValue());
				item.setValue("");
				response.addCookie(item);
			}
			if(item.getName().equals("sSubcat")){
				imageInfo.setSubSection(Integer.parseInt(item.getValue()));
				item.setValue("");
				response.addCookie(item);
			}
	}	
		
		list = imageDatabase.getSubImages(imageInfo);
		return list;
	}
		
	/*
	@RequestMapping(value="/itemPage",method=RequestMethod.GET)
	public ModelAndView itemPage(){
		ModelAndView mav = new ModelAndView("");//******************
		return mav;
	}*/
	
	@RequestMapping(value="/orderConfirmation",method = RequestMethod.POST)
	@ResponseBody
	public ArrayList<String> order(@RequestBody ImageInfoDao imageInfo){
		ArrayList<String> data =  new ArrayList<String>();
		String state = imageDatabase.orderRecord(imageInfo);
		data.add(state);
		return data;
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

	
	@RequestMapping(value="/addImages",method = RequestMethod.POST)
	@ResponseBody                           
	public ArrayList<String> imageUpload(@RequestParam("price") BigDecimal price,
			@RequestParam("category") String category,
			@RequestParam("description") String description,
			@RequestParam("section") Integer section,
			@RequestParam("img") MultipartFile file,
			HttpServletRequest request){
			
		String state = "active";
	//	Integer section;
	//	Integer section_count;
		Integer count;
		ArrayList<String>hash = null;
		ArrayList<String>result = new ArrayList<String>();
		Cookie[] c = request.getCookies();
		System.out.println(c[0].getValue());
		imageInfo.setAddress(c[0].getValue());
		imageInfo.setCategory(category);
		imageInfo.setState(state);
		imageInfo.setDescription(description);
		imageInfo.setPrice(price);
		/*
		// checking for sebsection
		
	section_count = imageDatabase.getSubSectionCount();
		section = imageDatabase.getSubSection(imageInfo);
		System.out.println("section " + section);
		if(section == null || section < section_count){
			if(section == null){
				section = 1;
			}else{
				section +=1; 
			}*/
		
			imageInfo.setSubSection(section);
			if(!file.isEmpty()){
				try{ 
				String path = "E:/CodeBase/Pro.Duc_Tran/Blockchain/imageFiles/"+ 
				imageInfo.getAddress()+"/"+imageInfo.getCategory()+"/"+ imageInfo.getSubSection();
				File dir = new File(path);
				if(!dir.exists()){
					dir.mkdirs();	
					}
				File image = new File(path+"/"+file.getOriginalFilename());
				file.transferTo(image);
				logger.info("Image created in the directory");
				//*******************************************
				hash = imageDatabase.ipfsUpload(imageInfo.getCategory(),section,imageInfo.getAddress());
				logger.info("ipfs image has created");
				for(String img:hash){
					imageInfo.setImg(img);
					System.out.println("image hash check");
					System.out.println(img);
					count = imageDatabase.imageCheck(imageInfo);
					System.out.println("count check for add image "+count);
					if(count == 0){
						System.out.println("image added");
						System.out.println(imageDatabase.addImages(imageInfo));
						//imageDatabase.addImages(imageInfo);
						count = imageDatabase.priceCheck(imageInfo);
						if(count == 0){
							System.out.println("price added");
							System.out.println(imageDatabase.addPrice(imageInfo));
							
							//imageDatabase.addPrice(imageInfo);
						}
					}
				}
				}catch(IOException ex){
					logger.info("IO exception from imageUpload controller");
				}
				}else{
					result.add("No Image uploaded");
					return result;
				}
			
	/*	}else{
			return "We allow only limit section"+section_count;
		}*/
			result.add("image uploaded!");
			return result;
	}
	
	
	

}
