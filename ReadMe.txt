Blockchain/Part_1 consists of solidity contract code for betting.
Blockchain/Part_2 consists of simple frontend for the betting contract.


Easy-Recycler : Manish Mourya And Rudresh Trivedi


Requirements
=============
Spring Boot, Maven, MySQL, JavaScript, IPFS

How to run the Project:
Travers to the directory containing pom and run mvn spring-boot:run. This will get the server up running on your localhost:8086
You will need JDK > 8 and latest maven. Rest all dependency is included in the pom so it will get automatically configured.

You also need to setup MYSQL for database. We have provided the driver settings in Blockchain\Dumpsters_server\Dumpster\src\main\resources\application.properties.
From here we retrieve our database name and user name and passcode for the database. The queries for the tables; that are to be made in MySQL is present in Blockchain\Dumpsters_server\sql.txt
All the constrins are well setup.

You also Need IPFS node running. We had used Go Implementation of IPFS. As this works well with our Java IPFS API.


Flow of the Project
=====================
The whole project can be well understood when perceived in terms of sellers perspective and buyers perspective.

Sellers Perspective:
--------------------
There is a sell button at the top to register/authenticate users. We have maintained user record table to authenticate and accept new users.
User needs a small phrase to be authenticated. we map this clue to their address. 

You will we directed to sellers page. At this point we ask for a price, description of the product,category and subsection(only 4 per category) and
image is uploaded.We use multipart/form-data in HTML for image uploading which is a bit unreliable.
We user cookies to keep track of users information.

The uploaded image is then saved to a different location with specific mapping to user_address/category/subsection/image.jpeg(4 max in one subsection).
we then use java ipfs api to upload the images to ipfs node and get their hash values. This image could be checked by
http://ipfs.io/ipfs/<hash values>. This link is down currently hence we used http://127.0.0.1:8080/ipfs/<hash value>.
we then store this hash in the database, used later to retrieve images.


Users Perspective
==================
User will choose the category and zip code of interest. We then query the database and get the product listed in that category with lowest price.
we send the arraylist of the object as json to access it in front end.

you will see different product tiles and their prices. you can click the product. You will be directed to buy page. You will get more pictures
of that product if available( this is where 4 images/subsection helps us). We only send the hash values and use ipfs gateway to display the images.

Once the product is bought we mark it as sold in the database and hence won't get retrieved. 

Contract
----------------------------
We only use contract for money transaction purpose. As Blockchain is new, we didn't want buyers to change the way they did buying online.
So we only had one main contract. A buyer can pay in Dollars. The money will be received in our account and corresponding ether is transferred to the seller's contract.  

 
 issues still present:
=======================
Multipart/form-data: for sending images not very reliable. mostly dependent on the browser's default implementation, so we couldn't get much out here. But it works!

Contract: it's not working, functions are getting called by default(will be fixed soon).
 
