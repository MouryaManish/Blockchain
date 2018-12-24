/*
document.getElementById("authenticate").addEventListener("click",authenticate);
document.getElementById("userDatabase").addEventListener("click",userDatabase);
*/
//var image = document.getElementsByName("img")[0];
//image.addEventListener("change", handleFiles, false);
//var fileList = [];
//var ImageData = new FormData();

var form = document.forms.namedItem("fileUpload_id");
var form1 = document.forms.namedItem("itemSearch");
//form.addEventListener('submit',uploadImage);





//document.getElementById("auth").addEventListener("submit",authenticate);


var bcAddr = document.getElementById('bcAddr');
var zipCode = document.getElementById('zipCode');
var auth1 = document.getElementById('auth1');
var auth2 = document.getElementById('auth2');

var pageNo = 1;
var szipCode = document.getElementsByName('zipCode');
var scategory = document.getElementsByName('categorey');



var addCols = function (rData){
	num = rData.length;
	//console.log("length..." )
    for (var i=0;i<num;i++) {
    	var s ="http://ipfs.io/ipfs/" + rData[i].img ;
    	//var description = rData[i].description;
    	var price = rData[i].price;
		var address = rData[i].address;
		var category = rData[i].category;
		var subSection = rData[i].subSection;
		console.log(address+"..."+category+"..."+subSection);
        var myCol = $('<div class="col-lg-4 col-sm-6 portfolio-item"></div>');
        var myPanel = $('<div class="card h-100"> <a><img height="400" width="700" class="card-img-top" src="' + s + '" alt=""></a> <h4 class="card-title"> <a > $' + price + '</a> </h4> <div class="card-body" onclick="shop(this)"> <button type="button" class="btn btn-primary" data-toggle="modal">Buy!!</button><input type="text" class="selectedAddress" style="display:none;" value='+address+'><input type="text" class="selectedCategory" style="display:none;" value='+category+'><input type="text" class="selectedSubcat" style="display:none;" value='+subSection+'></div> </div>');
        myPanel.appendTo(myCol);
        myCol.appendTo('#contentPanel');
    }
    
    
    $('.close').on('click', function(e){
      e.stopPropagation();  
          var $target = $(this).parents('.col-sm-3');
          $target.hide('slow', function(){ $target.remove(); });
    });
  };
  
  
  async function shop(node){
	  try{
		  var  url ="/rudramanish/shop";
		  console.log(url);
		  
			var obj= new Object();
			obj.address = node.querySelector(".selectedAddress").value;
			obj.category = node.querySelector(".selectedCategory").value;
			obj.subSection = node.querySelector(".selectedSubcat").value;
			var data = JSON.stringify(obj);
			console.log("json string******");
			response = await fetch(url,{
				method : "POST",
		        mode: "same-origin", 
		        cache: "no-cache", 
		        credentials: "same-origin",
		        headers: {
		        	"Content-Type": "application/json",
		          "Accept": "application/json",
		        },
		        redirect: "follow",
		        referrer: "no-referrer",
				body:data,
			});
			//////window.location.assign("http://localhost:8086/rudramanish"+data[1]);
			console.log("data response receieved in shop");
			data = await response.json();
			console.log("*****response data*****");
			console.log(data);
			if(data[0]== "success"){
				window.location.assign("http://localhost:8086/rudramanish"+data[1]);
		}
	  }catch(err){
		  console.log("error form shop: sending user selected choice");
		  console.log(err);
	  }
  }
  
  



function show(element,visibility) { 
        element.style.display = visibility; 
   	}

async function showItems1(){
	pageNo++;
	showItems();
}

async function showItems(){
	try{
		var url="/rudramanish/userView";
		var data = new FormData(form1);
		data.append('pageNo', pageNo);
		response = await fetch(url,{
			method : "POST",
	        mode: "same-origin", 
	        cache: "no-cache", 
	        credentials: "same-origin",
	        headers: {
	         // "Accept": "text/plain",
	          "Accept": "application/json",
	        },
	        redirect: "follow",
	        referrer: "no-referrer",
			body:data,
		});
		
		console.log("data response receievd");
		rData = await response.json();
		console.log("*****response data*****");
		console.log(rData);
		addCols(rData);
	}catch(err){
		console.log("err form uploadImage");
		console.log(err);
	}
}



async function uploadImage(){
	try{
		var url="/rudramanish/addImages";
		console.log(url);
		var data = new FormData(form);
		response = await fetch(url,{
			method : "POST",
	        mode: "same-origin", 
	        cache: "no-cache", 
	        credentials: "same-origin",
	        headers: {
	         // "Accept": "text/plain",
	          "Accept": "application/json, text/plain ",
	        },
	        redirect: "follow",
	        referrer: "no-referrer",
			body:data,
		});
		
		console.log("data response receievd");
		rData = await response.json();
		console.log("*****response data*****");
		console.log(rData);
	}catch(err){
		console.log("err form uploadImage");
		console.log(err);
	}
}



/*
 * authenticate the user. if the authentication fails then show some indication.
 * else redirect to new url: addImages. 
 * */
async function authenticate(){
	console.log("*************8");
	try{
	var state = null ;
	var clue = document.getElementById("clue");
	var obj= new Object();
	obj.clue = clue.value;
	obj.address = null;
	obj.pinCode = null;
	if(state == false){
		var address = document.getElementById("address");
		var pinCode = document.getElementById("pinCode");
		obj.address = address;
		obj.pinCode = pinCode;
	}
//	var address = document.getElementById("address");
//	var pinCode = document.getElementById("pinCode");
	
	var data = JSON.stringify(obj);
	var url ="/rudramanish/authenticate";
	// setting request object along with it.
	data = await fetch(url,{
		        method: "POST", 
		        mode: "same-origin", 
		        cache: "no-cache", 
		        credentials: "same-origin", 
		        headers: {
		            "Content-Type": "application/json",
		             "Accept": "application/json",
		        },
		        redirect: "follow",
		        referrer: "no-referrer", 
		        body: data,
	});
	console.log("data response receievd");
	data = await data.json();
	console.log("response data  ");
	console.log(data);
	if(data[0] == "failed"){
		state = false;
		show(bcAddr, 'block');
		show(zipCode, 'block');
	}
	else{
		window.location.assign("http://localhost:8086/rudramanish"+data[1]);
	}
}
	catch(err){
		console.log("******err from send transection*******");
		console.log(err);
	}
}

async function register(){
	show(bcAddr, 'block');
	show(zipCode, 'block');
	show(auth1, 'none');
	show(auth2, 'block');
	//registerInternal();
}

async function registerInternal(){
	var clue = document.getElementById("clue");
	var address = document.getElementById("address");
	var pinCode = document.getElementById("pinCode");

	var obj= new Object();
	obj.clue = clue.value;
	obj.address = address.value;
	obj.pinCode = pinCode.value
	var data = JSON.stringify(obj);
	var url ="/rudramanish/register";
	// setting request object along with it.
	data = await fetch(url,{
		        method: "POST", 
		        mode: "same-origin", 
		        cache: "no-cache", 
		        credentials: "same-origin", 
		        headers: {
		            "Content-Type": "application/json",
		             "Accept": "application/json",
		        },
		        redirect: "follow",
		        referrer: "no-referrer", 
		        body: data,
	});
	console.log("data response receievd");
	data = await data.json();
	console.log("response data  ");
	console.log(data);
	if(data[0] == "failed"){
		alert("Login error");
	}
	else{
		window.location.assign("http://localhost:8086/rudramanish"+data[1]);
	}
}





async function userDatabase(){
	try{
	var clue = document.getElementById("clue");
	var address = document.getElementById("address");
	var pinCode = document.getElementById("pinCode");
	var obj= new Object();
	obj.clue = clue.value;
	obj.address = address.value;
	obj.pinCode = pinCode.value;
	var data = JSON.stringify(obj);
	var url ="/rudramanish/register";
	// setting request object along with it.
	data = await fetch(url,{
		        method: "POST", 
		        mode: "same-origin", 
		        cache: "no-cache", 
		        credentials: "same-origin", 
		        headers: {
		            "Content-Type": "application/json",
		             //"Accept": "application/json",
		            "Accept": "text/plain",
		        },
		        redirect: "follow",
		        referrer: "no-referrer", 
		        body: data,
	});
	console.log("data response receievd");
	data = await data.text();
	console.log("response data  " + data);
	if(data == "failed"){		
	}
	else{
		window.location.assign("http://localhost:8086/rudramanish/"+data);
	}
}
	catch(err){
		console.log("******err from send transection*******");
		console.log(err);
	}
}



// we are using HTml to send this data.
/* 
function submit2(){
	console.log("**** submit2 clicked****");
	var price = document.getElementsByName("price")[0].value;
	var categorey = document.getElementsByName("categorey")[0].value;
	var description = document.getElementsByName("description")[0].value;
	ImageData.set("price",price);
	ImageData.set("categorey",categorey);
	ImageData.set("description",description);
	console.log("length of list: "); 
	console.log(image.files.length);
	if(image.files.length>0){
	for(var i=0;i<(image.files.length);i++){
		console.log("size of each image: "); 
		console.log(image.files[i].size);
		if(image.files[i].size < 1000000){
				fileList.push(image.files[i]);
				console.log("****inside handling file size");
			}
		else
			alert("file size should be not more than 1 MB and jpeg or png");
		}
	for(var i=0;i<fileList.length;i++){
		sendImage(fileList[i]);
	}
	}else
			alert("file not selected");
}

*/
/*
async function sendImage(img){
	try{
	console.log("****image sending iniciated****");
	ImageData.set("img",img,img.name);
	
	var url = "/rudramanish/imageInfo";
	var data = await fetch(url,{
			method : "POST", 
	        body: ImageData,
		});
	console.log("****data receieved before json parsing**");
	data = await data.json();
	console.log("****Data received");
	console.log(data);
	}catch(err){
		console.log("****err in upload");
		console.log(err);
	}
}*/

