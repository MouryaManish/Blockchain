pragma solidity ^0.4.25;

contract Betting{

	struct member {
		address user;
		uint total_money;
		uint betting_count;
		uint8[] choosed_number;
		mapping(uint8 => uint256) money_paid;
		uint amount_win;
		string message;
	}
	/* we maintain*/
	string general_message;
	mapping(address => member) members;
	address owner;
	uint8 betting_range;
	uint8 random_number;
	uint256 seed;
	uint8 [] number_key_list;						//maintaining numbers chossed as record for mapping
	mapping(uint8 => address []) classified_record;
	address [] address_record;					//maintaining address records for mapping.
	enum game_state {invitation_mode,game_mode,result_mode,view_mode,owner_mode}
	game_state state;
	address[] winners;
	//address[] biggest_looser;
	uint total_betting_money;
	address[] failed_list ;
	//int loss;


	
	/*mapping(address => uint256[][]) user_betting_record;*/
	
	

	constructor() public {
		owner = msg.sender;
		state = game_state.invitation_mode;
	}

/*============================= reading messages and general function ==================================*/

	function read_message() public view returns(string){
		require(members[msg.sender].user == msg.sender,"you are not a member");
		return members[msg.sender].message;
	}
	
	function read_general_message() public view returns(string){
		return general_message;
	}
	
	function get_range() public view returns(uint8){
		require(state == game_state.game_mode,"game not initiated");
		return betting_range;
	}
	
	function get_winning_number() public view returns(uint8){
	//	require(state == game_state.result_mode,"results not out yet");
		return random_number;
	}
	
	function get_balance(address _of) public view returns(uint){
		return _of.balance;
	}
	
/*	function owner_address() public view returns(address){
		return owner;
	}
	
	function check_sum() public view returns(uint){
		return sum;
	}

	
	function check_filed_list() public view returns(address[]){
		return failed_list;
	}
	
	function check_winners() public view returns(address[]){
		return winners;
	}
	
	function check_total_bettingmoney() public view returns(uint){
		return total_betting_money;
	}*/
	

/*============================ invitation ===========================================*/	
	modifier _sent_invitation(){
		require(state == game_state.invitation_mode,"Invitation has been closed");
		_;
	}
	
	function send_invitation(address _to) public _sent_invitation{
			if(!find(_to, address_record)){
			address_record.push(_to);
			members[_to].user = _to;
			members[_to].message = "you are invited";
			}
			else{
			    members[_to].message = "you are already a member";
			}
			
	}
	
	
/*=================================== starting betting and initializing N====================================*/
	modifier _start_betting(){
		require(msg.sender == owner,"only owner can initialize the betting");
		require(state == game_state.invitation_mode," game has moved from invitation state");
		_;
	}
	
	function start_betting(uint8 N) public _start_betting {
		betting_range = N;		// restricting it to 8 bits so max 255.
		state = game_state.game_mode;
		general_message = "betting initiated";
	}

/*============================================= bet ==============================================================*/
	modifier _bet(){
		require(state == game_state.game_mode,"you can't play this game at this stage");
		require(members[msg.sender].user == msg.sender,"you are not a member to play this game");
					
					/*checking id f the sender has vaild money*/
						
		require(msg.value > 0 && msg.sender.balance >= 0,"negative transection not allowed");//====================check if money is transfered first or condition checked first
		require((msg.sender.balance + msg.value) >= msg.value," no overflow");
		_;
	}
	
	function bet(uint8 number) public _bet payable {
				/*checking for number duplication if not found then update number_key_list and classified_record */
				require(number <= betting_range && number >=0 ,"you are above the range");
				if(!find(number,number_key_list)){
					number_key_list.push(number);
					classified_record[number].push(msg.sender);
				}
				else{
					if(!find(msg.sender,classified_record[number])){
						classified_record[number].push(msg.sender);
					}
				}
			/* we also update each members personal list with no duplicatcy*/		
				if(!find(number,members[msg.sender].choosed_number)){
					members[msg.sender].choosed_number.push(number);
				}
				
				members[msg.sender].money_paid[number] += msg.value;
				members[msg.sender].total_money += msg.value;
				members[msg.sender].betting_count += 1;
				seed += uint(msg.value + number);
				total_betting_money += msg.value;
				members[msg.sender].message = "bet successful";
				
	}
	
	function find(uint8 number,uint8[] list) internal pure returns(bool){
			bool found = false;
			for(uint8 i=0;i< list.length;i++){
				if(list[i] == number)  found = true;
			}
			return found;
	}
	
	function find(address _from,address[] list) internal pure returns(bool){
		bool found=false;
		for(uint8 i=0;i< list.length;i++){
			if(list[i] == _from)  found = true;
		}
		return found;
	}
	
	
	/*=====================================end betting, winner declaration, giving the money to winners =======================================*/
	modifier _results(){
		require(msg.sender == owner," you don't have access to this function");
		_;
	}
	
	function results() public _results returns(string){
		stop_betting();
		random_number = uint8(uint8(uint256(sha256(seed + block.number + now))) % betting_range) + 1;
		general_message = "results declared";
		state = game_state.view_mode;
		if(classified_record[random_number].length>0){ // people betted on this number
			winners = classified_record[random_number];
			uint sum = update_members_reward();
			calculate_share(sum);
			bool transfer_state = distribute_money();
			if(transfer_state){
			   // master_reset();
				return "all the transfer successful";
			}
			else{
				return "some transfer failed";
			  }
		}
		else{
			general_message = "No one won";
			return owner_transfer();
		}
		
	}
	
	function stop_betting() internal {
		state = game_state.result_mode;
	}
	
	function update_members_reward() internal returns(uint){
		uint winning_sum = 0;
		//loss = 0;
		for(uint i=0;i<address_record.length;i++){
			for(uint j=0;j<winners.length;j++){
				if(winners[j] == address_record[i]){
					winning_sum += members[winners[j]].money_paid[random_number];
					
				}
				else{
					members[address_record[i]].amount_win = 0;
					/*if(int(members[address_record[i]].amount_win - members[address_record[i]].total_money) < loss){
							biggest_looser.push(address_record[i]);
							loss = int(members[address_record[i]].amount_win - members[address_record[i]].total_money);
						}*/
				}
			}
		}
		return winning_sum;
	} 
	
	function calculate_share(uint sum1) internal{
		uint share;
		for(uint i=0;i<winners.length;i++){
			if(find(random_number,members[winners[i]].choosed_number)){ // crosschecking that the number was selected by the user.
				//share = (members[winners[i]].money_paid[random_number] * 1000 /*three decimal places*/ * total_betting_money ) / sum1;
				share = (members[winners[i]].money_paid[random_number]* total_betting_money)  / sum1;
				//share = share *(10^18/uint(1000)); // converting to ether;
				members[winners[i]].amount_win = share;
				/*if(int(members[winners[i]].amount_win - members[winners[i]].total_money) < loss){
						loss = int(members[winners[i]].amount_win - members[winners[i]].total_money);
						biggest_looser.push(winners[i]);
					}*/
			}
		}
	}
	
	
	function distribute_money() internal returns(bool){
		for(uint i =0;i< winners.length;i++){
			if(winners[i].send(members[winners[i]].amount_win)){
				members[winners[i]].message = "winning amount transferd";
			}
			else{
				failed_list.push(winners[i]);
			}
		}
		if(failed_list.length==0)
			return true;
		else
			return false;
	}
	
	function owner_transfer() internal returns(string) {
		require(owner == msg.sender," you don't have access");
		require(state == game_state.view_mode," can't transfer right now");
	    if(owner.send(address(this).balance)){
           // master_reset();
	 }
	    else{
	        return "money not transfered to owner";
	    }   
	}
	/*================================================== check winning or lost =====================================================================*/
	
	function address_state(address _check) public view returns(int){
		require(state == game_state.view_mode,"game is still not ended");
		require(members[_check].user == _check);
		return int(members[_check].amount_win - members[_check].total_money);
	}

	/*================================================address's of the winners and loosers ===============================================================*/
	
	function winners_address() public view returns(address[]){
		require(state == game_state.view_mode,"game is still not ended");
		return winners;
	}
	
	function lossers_address() public view returns(address){
		require(state == game_state.view_mode,"game is still not ended");
		int temp;
		address looser;
		for(uint i=0;i<address_record.length;i++){
		    if(int(members[address_record[i]].amount_win - members[address_record[i]].total_money) < temp){
				looser = members[address_record[i]].user;
				temp = int(members[address_record[i]].amount_win - members[address_record[i]].total_money);
			}
		}
		return looser;
	
	}
	
		/*======================================================resetting=========================================================================================*/
	function reset() public returns(bytes){
		require(msg.sender == owner,"only owner can reset it");
		require(state == game_state.game_mode,"game is not in game mode");
		state = game_state.owner_mode;
	    betting_range = 0;
	    random_number=0;
	    total_betting_money = 0;
	    seed=0;
	    for(uint i=0;i<number_key_list.length;i++){
	         delete classified_record[number_key_list[i]]; 
	    }
	    delete number_key_list;
	    delete winners;
	   // delete biggest_looser;
	    delete failed_list;
	   // delete loss;
	    general_message = "";
		for(i=0;i< address_record.length;i++){
			if(members[address_record[i]].betting_count > 0 ){
				if(address_record[i].send(members[address_record[i]].total_money)){
					members[address_record[i]].total_money = 0;
					members[address_record[i]].betting_count = 0;
					members[address_record[i]].amount_win = 0;
					members[address_record[i]].message = "";
					for(uint j=0;j<members[address_record[i]].choosed_number.length;j++){
						members[address_record[i]].money_paid[members[address_record[i]].choosed_number[j]] = 0;
					}
					members[address_record[i]].choosed_number.length = 0;
				}
				else{
					return "something went wrong in resetting";
				}
			}
			continue;
		}
        state = game_state.invitation_mode;
		return "successfully resetted";
		
	}
	
	
	
	function master_reset() public {
		require(owner == msg.sender,"you don't have access");
	    state = game_state.invitation_mode;
	    betting_range = 0;
	    random_number=0;
	    total_betting_money = 0;
	    seed=0;
	    for(uint i=0;i<number_key_list.length;i++){
	         delete classified_record[number_key_list[i]]; 
	    }
	    delete number_key_list;
	    delete winners;
	   // delete biggest_looser;
	    delete failed_list;
	   // delete loss;
	    
	    general_message = "";
	    
		for( i=0;i< address_record.length;i++){
			if(members[address_record[i]].betting_count > 0 ){
					members[address_record[i]].total_money = 0;
					members[address_record[i]].betting_count = 0;
					members[address_record[i]].amount_win = 0;
					members[address_record[i]].message = "";
					for(uint j=0;j<members[address_record[i]].choosed_number.length;j++){
						members[address_record[i]].money_paid[members[address_record[i]].choosed_number[j]] = 0;
					}
					members[address_record[i]].choosed_number.length = 0;

			}
			continue;
		}
	    
	}
	
}