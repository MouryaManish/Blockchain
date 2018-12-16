pragma solidity ^0.4.25;



contract dumpsters{
    
   
    
    //pincode to list of address mapping. zipcode is input and list of addresses present in that zipcode.  
    mapping(uint256 =>address[]) zipSort;
    address owner;
    
    struct imageStock{
        uint8 total;
        string _1;
        string _2;
        string _3;
        string _4;
        string _5;
        string _6;
    }
    
    //record of each address.
    struct usersRecord{
    
        // list of category's each user has subscribed to. Indexes can map from categoryType enum above.
        //so for each category we put true or false.
        // not needed but just incase
        string []category;
    
        mapping(string => uint8) numberOfSubItems;
       // mapping(string => mapping(uint8 => bytes[6])) imageList;
        mapping(string => mapping(uint8 => imageStock)) imageList;
    }
     
    
    // mapping from address obtained from the zipcode to each users's struct.
    mapping(address => usersRecord) record;
    
    //==================================constructor========================================================
    constructor() public{
        owner = msg.sender;
    }
    
    //===================================uploading Images by main account======================================
   function uploadImages(uint256 zipcode,address userAddress, string memory categoryType,uint8 section,string image) public{
        
        if(!checkForDuplicityOfAddress(zipSort[zipcode],userAddress)){
            zipSort[zipcode].push(userAddress);
        }
        
        usersRecord storage userData = record[userAddress];
        if(!checkForDuplicityOfString(userData.category,categoryType)){
            userData.category.push(categoryType);
        }
        userData.numberOfSubItems[categoryType] = section; //userData.numberOfSubItems[categoryType] + 1;
        
        
        imageStock storage stock = userData.imageList[categoryType][section];
        if(!(stock.total == 6)){
            stock.total = stock.total + 1;
            if(stock.total == 1){
                stock._1 = image;
            }else if(stock.total == 2){
                stock._2 = image;
            }else if(stock.total == 3){
                stock._3 = image;
            }else if(stock.total == 4){
                stock._4 = image;
            }else if(stock.total == 5){
                stock._5 = image;
            }else{
                stock._6 = image;
            }
            
        }
    }
    
    
//    clear the storage
    function soldOut(address add, string cat,uint8 section) public {
        usersRecord storage r = record[add];
        delete r.imageList[cat][section]; 
    }
    
modifier _sendMoney{
    require(msg.value > 0 && msg.sender.balance >= 0,"negative transection not allowed");//====================check if money is transfered first or condition checked first
	require((msg.sender.balance + msg.value) >= msg.value," no overflow");
	_;
}


function sendMoney(uint256 itemCost,address _to) payable _sendMoney public {
    require(msg.value >= itemCost," Money paid is less then the itemcost");
    require(_to.send(itemCost),"error in forwarding the money to the product owner");    
}


function checkBalance() public view returns(uint256){
    return address(this).balance;
}


function checkForDuplicityOfAddress(address[] data,address _tocheck ) internal view returns(bool){
        for(uint8 i=0;i<data.length;i++){
            if(data[i] == _tocheck )
                return true;
            else
                return false;

        }
    }
    
    function checkForDuplicityOfString(string[] data,string memory _tocheck) internal view returns(bool){
        for(uint8 i=0;i<data.length;i++){
            
            if(keccak256(abi.encodePacked(data[i])) == keccak256(abi.encodePacked(_tocheck)))
                return true;
            else
                return false;

        }
    }
}