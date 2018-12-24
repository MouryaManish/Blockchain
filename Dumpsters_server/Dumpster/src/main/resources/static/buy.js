var price;
var address; 
var category; 
var subSection; 



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
		createTiles(data);
	}catch(err){
		console.log("error from getSelectedImages");
		console.log(err);
	}
}

function createTiles(data){
	console.log("creating tiles in the container");
	num = data.lenght;
	for(var i = 0;i< num;i++){
		var s ="http://ipfs.io/ipfs/" + data[i].img ;
		console.log(address+"..."+category+"..."+subSection);
        var myCol = $('<div class="col-lg-4 col-sm-6 portfolio-item"></div>');
        var myPanel = $('<div class="card h-100"> <a><img height="400" width="700" class="card-img-top" src="' + s + '" alt=""></a></div>');
        myPanel.appendTo(myCol);
        myCol.appendTo('#contentPanel');
	}
	price = data[0].price;
	address = data[0].address;
	category = data[0].category;
	subSection = data[0].subSection;
	Console.log("form create tiles");
	Console.log("price: " +price + "  category: " +category+"  subcat:" + subSection);
}

async function orderItem(){
	var url="/rudramanish/orderConfirmation";
	console.log("inside orderItem");
	 var buyData = new FormData();
	 buyData.append("address",address);
	 buyData.append("price",price);
	 buyData.append("category",category);
	 buyData.append("subSection",subSection);
	 response = await fetch(url,{
			method : "POST",
	        mode: "same-origin", 
	        cache: "no-cache", 
	        credentials: "same-origin",
	        headers: {
	          "Accept": "application/json",
	        },
	        redirect: "follow",
	        referrer: "no-referrer",
			body:buyData,
		});
		//////window.location.assign("http://localhost:8086/rudramanish"+data[1]);
		console.log("data response receieved in orderItem");
		data = await response.json();
		console.log(data);
}