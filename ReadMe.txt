Part_1 consists of solidity contract code.
Part_2 consists of simple frontend for the contract.

Betting contract
==============
This contract is about betting. There is a main owner who owns the contract and its flow.
The owner initiates the betting game. Any user can invite himself for the betting game.
Then the owner moves game further by initiation the betting range and user can start betting with their choice of number within the betting range with valid ether amount. 
The owner can declare the results at this point a random number is selected and the corresponding winner are paid their shares.
The owner himself doesn’t have any control on the process except moving the process to the next stage.
The owner himself can participate in the game. 
The owner has an especial power to reset the game in case of uncertain situation, but this reset of the game can be done only if the game has not been called to declare the results. if the game were to be reset then all the players are returned with respective ether.
Players can do multiple betting on the same number or on different number.

Betting Front end
==============
1.	I am not using metamask, hence we have to inject web3.js via Javascript file.
2.	The source Javascript file is present in src/index.js
3.	I had to use webpack to inject web3.js

Object used
============
1.	Wallet: It keeps the account records and used for signing the transaction.
2.	Web3.js 
** The code has been successfully tested on Ganache. 
@Code Author 
Manish Mourya


 
 
