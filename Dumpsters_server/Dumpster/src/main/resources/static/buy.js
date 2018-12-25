var price;
var address; 
var category; 
var subSection; 
var flag = document.querySelector(".flag");





async function getSelectedImages(){
	try{
		console.log("inside getSelectedImages function from buy.js");
		var url="/rudramanish/selectedItems";
		response = await fetch(url,{
			method : "GET",
	        mode: "same-origin", 
	        cache: "no-cache", 
	        credentials: "same-origin",
	        headers: {
	          "Accept": "application/json",
	        },
	        redirect: "follow",
	        referrer: "no-referrer",
		});
		console.log("data received for selected items");
		var data = await response.json();
		console.log(data);
		console.log("tiles function called");
		
		createTiles(data);
	}catch(err){
		console.log("error from getSelectedImages");
		console.log(err);
	}
}


var createTiles = function (data){
	num= data.length;
	console.log("lenght==== " +num);
    for (var i=0;i<num;i++) {
    	console.log(data[i]['img']);
    	var s = "http://ipfs.io/ipfs/" + data[i]['img'] ;
    	console.log("*******jquery called for tiles******");
        var myCol = $('<div class="col-lg-4 col-sm-6 portfolio-item"></div>');
        var myPanel = $('<div class="card h-100"> <a><img height="400" width="700" class="card-img-top" src="' + s + '" alt=""></a></div>');
        myPanel.appendTo(myCol);
        myCol.appendTo('#contentPanel');
    }
    																																				
   var details=$('<div><br><br><p>Description</p><p>'+ data[0].description +'</p><p>price</p><p>'+ data[0].price +' ether</p><a style="color:green;font-weight:bold;" onclick="order(this)" > Buy </a><input type=text class="address" style="display:none;" value = '+data[0].address+'><br><input type="text" class="category" style="display:none;" value='+ data[0].category +'><br><input type="text" class = "subcat" style="display:none;" value='+ data[0].subSection +'></div>');
   details.appendTo('#container');
   
};



async function order(element){
/*
	console.log("inside order function of buy.js");
	var transactForm = document.querySelector(".transact_div"); //document.forms.namedItem("fileUpload_id");
	transactForm.style.display = "block";
	console.log("form enabled");

	*/

	console.log("**************order is called********8");
	 var address = document.querySelector(".address").value;
	 var category = document.querySelector(".category").value;
	 var subcat = document.querySelector(".subcat").value;
	 console.log(address + "...." +category+ "......"+ subcat);
	 var obj= new Object();
	 obj.address = address;
	 obj.subSection = subcat;
	 obj.category = category;
	 
	 var data = JSON.stringify(obj);
	 url="/rudramanish/orderConfirmation"
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
		console.log("data response receieved in order");
		data = await response.json();
		console.log(data);
		if(data[0]=="success"){
			alert("order placed");
			window.location.assign("http://localhost:8086/rudramanish"+data[1]);
		}
	 
}




