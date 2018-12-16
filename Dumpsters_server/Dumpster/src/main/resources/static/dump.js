
document.getElementById("submit1").addEventListener("click",submit1);
document.getElementById("submit2").addEventListener("click",submit2);
var image = document.getElementsByName("img")[0];
//image.addEventListener("change", handleFiles, false);
var fileList = [];
var ImageData = new FormData();


async function submit1(){
	try{
	var clue = document.getElementById("clue");
	var address = document.getElementById("address");
	var pinCode = document.getElementById("pinCode");
	var obj= new Object();
	obj.clue = clue.value;
	obj.address = address.value;
	obj.pinCode = pinCode.value;
	var data = JSON.stringify(obj);
	var url ="/rudramanish/userInfo";
	// setting request object along with it.
	data = await fetch(url,{
		        method: "POST", 
		        mode: "cors", 
		        cache: "no-cache", 
		        credentials: "same-origin", 
		        headers: {
		            "Content-Type": "application/json",
		        },
		        redirect: "follow",
		        referrer: "no-referrer", 
		        body: data,
	});
	
	data = await data.json();
	console.log(data);
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
}
*/


