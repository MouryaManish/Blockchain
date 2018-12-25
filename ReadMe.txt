Blockchain/Part_1 consists of solidity contract code for betting.
Blockchain/Part_2 consists of simple frontend for the betting contract.


Easy-Recycler
By Manish Mourya And Rudresh Trivedi

Requirements
=============
The Pom is located at Blockchain/Dumpsters_server/Dumpster/

We have used Maven for building our project.

How to run the Project:
Travers to the directory containing pom and run mvn spring-boot:run. This will get the server up running on your localhost:8086
You will need JDK > 8 and latest maven. Rest all dependency are included in the  pom so it will get automatecally configured.

You also need to setup MYSQL for database. We have provided the driver settings in Blockchain\Dumpsters_server\Dumpster\src\main\resources\application.properties.
From here we retrive our database name and user name and passcode for the database. The queries for the tables that is to be made in mysql is present in Blockchain\Dumpsters_server\sql.txt
All the constrins are well setup.

You also Need IPFS node running. We had used Go Implementation of IPFS.AS this works good with our Java IPFS Api.

Flow of the Project
=====================
For Selling goods:
------------------
There is sell button at the top to register/authenticate as user. We have maintained userrecord table to authenticate and accept new users.
User needs a small phrase to be authenticated. we map this clue to their address. 

You will we directed to sellers page. At this point we ask for price, description of the product,category and subsection(only 4 per category) and
image is uploaded.We use multipart/form-data in HTML for image uploading which is bit unreliable but definately works with works.
We user cookies to remember the user address,henceforth.

The uploaded image is than saved to a different location with specific mappping to user_address/category/subsection/image.jpeg(4 max in one subsection).
we then use java ipfs api to upload the images to ipfs node and get their hash values. this image could be checked by
http://ipfs.io/ipfs/<hash values>. This link is down currently hence we used http://127.0.0.1:8080/ipfs/<hash value>.
we then store this hash in the database as retreving array of bytes is not possible in solidity without using the encoded patch which is not recommended.


Users perspective
------------------
user will choose category and zipcode of interest. we then querry the database and get the product listed in that category with lowest price.
we send the arraylist of the object as json to access it in front end.

you will see diffrent product tiles and their price. you can click the product and then you will directed to buy page and you will get more pictures
of that product if available(this is where 4 images/subsection helps us). We only send the hash values and use ipfs gateway to display the images.

there is buy botton to buy the product. once the product is bought we mark it as sold in the database and hence won't get retrived henceforth.

Contract
----------------------------
We only use contract for money transaction purpose. buyer sends the money to us and we send the money to seller. we don't keep any record of user's private key.

 
 issues still present:
=======================
Mutlypart/form data: for sending images not very reliable. mostly dependent on browser's default implementation, so we couldn't get much out here. But it works!

Contract: it's not working, functions are getting called by default even if we don't want them to......still not figured it out (but could because of jquery)
 







 
