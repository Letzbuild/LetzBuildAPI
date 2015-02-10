LetzBuildAPI
============

This is the API (REST based) that'll provide services for webbased apps as well as mobile etc.

Base URL = http://url:port/

The API list :
-------------

======================================PRODUCTS======================================

**GET**  **http://url:port/products/retrieve?cat=full_word&pcode=product_code&limit=num&page=num**

There are multiple parameters supported. They are all mutually exclusive or can be used to filter if it makes sense. They can also be used independently.

 * cat - This retrieves the products that are categorized under the category.  The full category name is required
 * pcode - Fill this up if the details of a specific product is required. 
 * limit - This parameter is supplied with a number that limits the search results. So for a registered user, this number could be 10/20/25. For a non-registered user, this could be as low as 4. The default is 10. 
 * page - typical pagination numbers. The default if not passed is 1.  


**GET**  **http://url:port/products/categories?limit=num&page=num**  

Lists out all the product categories

 * limit - This parameter is supplied with a number that limits the search results.  
 * page - typical pagination numbers. The default if not passed is 1.  


**GET**  **http://url:port/products/categories/:category**

This lists out sub categories (breadcrum) along with an immediate parent for a specified category.

======================================USERS======================================


**GET**  **http://url:port/users/:email**

Use this API for 2 purposes -
 
 * authentication - **(temporarily)** for a given username i.e. email ID, this gets the email ID and password pair. 
 Authentication involves various other things like checking roles, logging of the user etc.  
 * duplicate email ID check - to check when the user is being registered if the email ID is taken or not. 
  

======================================BUYERS======================================
 
**POST**  **http://url:port/buyers/add**

The parameters are:

 * fname
 * lname
 * company
 * city
 * mobile
 * email
 * address
 * zip
 * password - do an MD5 in the javascript layer itself.  

======================================SUPPLIERS====================================== 

**GET**  **http://url:port/suppliers/:scode**

 Gets the profile details of a supplier based on the supplier's code.   
 

**GET**  **http://url:port/suppliers/retrieve?pcode=product_code&limit=num&page=num**

There are multiple parameters supported. They all form AND clauses if passed together. They can also be used independently.

 * pcode - This retrieves the list of suppliers that are dealing with the product specified as pcode. The pcode is case sensitive.
 * limit - This parameter is supplied with a number that limits the search results. So for a registered user, this number could be 10/20/25. For a non-registered user, this could be as low as 4. The default is 10. 
 * page - typical pagination numbers. The default if not passed is 1.
 
 **GET**  **http://url:port/suppliers/products/retrieve?scode=supplier_code&limit=num&page=num**
 
 There are multiple parameters supported. They all form AND clauses if passed together. They can also be used independently.
 
  * scode - This retrieves the list of products that the supplier deals with. The scode is case sensitive.  
  * limit - This parameter is supplied with a number that limits the search results. So for a registered user, this number could be 10/20/25. For a non-registered user, this could be as low as 4. The default is 10. 
  * page - typical pagination numbers. The default if not passed is 1.
  
 
 **POST**  **http://url:port/suppliers/add**

 The parameters are:
 
  * fname
  * lname
  * company
  * city
  * mobile
  * email
  * address
  * zip
  * password - do an MD5 in the javascript layer itself.  

======================================ENQUIRIES======================================

**POST**  **http://url:port/enquiries/supplier/add**

This enquiry is sent to a specific supplier by selecting a product. 

The parameters are:

* enquirynumber - A supplied value meant for tracking the enquiries. 
* scode - The supplier code for which this enquiry is sent
* suppliername - The name of the supplier
* pcode - The product code
* firstname - First name
* lastname - Last name
* organisation - Organization / Company
* mobilenumber - mobile without the +91
* email
* quantity - Quantity
* subject - units for the quantity
* orderspecification - ordering specification
* specification - the product specification
* dimension - the product dimension
* enquiryheading - Subject line. This could be a standard driven by a template
* datepicker - Date on which the product is needed. The date has to be sent in the yyyy-MM-dd format only
* approximatebudget
* locationlat - the delivery location's latitude
* locationlong - the delivery location's longitude 
* deliverylocation - a locality or location
* frequency - frequency
* reasonforpurchase
* anyspecialinstruction - any specific instruction

**GET**  **http://url:port/enquiries/supplier/retrieve?scode=supplier_code&limit=num&page=num**

There are multiple parameters supported. They all form OR clauses if passed together. Nothing is mandatory

 * scode - This retrieves the list of enquiries that were sent to a specific supplier. The scode if supplied is case sensitive. 
 * limit - This parameter is supplied with a number that limits the search results. So for a registered user, this number could be 10/20/25. For a non-registered user, this could be as low as 4. The default is 10. 
 * page - typical pagination numbers. The default if not passed is 1.
 
 The results are always ordered by the enquiry date descending - latest on top


**POST**  **http://url:port/enquiries/product/add**

This enquiry is sent to LetzBuild by selecting a product. 

The parameters are:

* enquirynumber - A supplied value meant for tracking the enquiries. 
* pcode - The product code for which this enquiry is sent
* firstname - First name
* lastname - Last name
* organisation - Organization / Company
* mobilenumber - mobile without the +91
* email
* quantity - Quantity
* subject - units for the quantity
* orderspecification - ordering specification
* specification - the product specification
* dimension - the product dimension
* enquiryheading - Subject line. This could be a standard driven by a template
* datepicker - Date on which the product is needed. The date has to be sent in the yyyy-MM-dd format only
* approximatebudget
* locationlat - the delivery location's latitude
* locationlong - the delivery location's longitude 
* deliverylocation - a locality or location
* frequency - frequency
* reasonforpurchase
* anyspecialinstruction - any specific instruction

**GET**  **http://url:port/enquiries/product/retrieve?pcode=product_code&limit=num&page=num**

There are multiple parameters supported. They all form OR clauses if passed together. Nothing is mandatory

 * pcode - This retrieves the list of enquiries that were sent for a specific product. The pcode if supplied is case sensitive. 
 * limit - This parameter is supplied with a number that limits the search results. So for a registered user, this number could be 10/20/25. For a non-registered user, this could be as low as 4. The default is 10. 
 * page - typical pagination numbers. The default if not passed is 1.
 
 The results are always ordered by the enquiry date descending - latest on top
 

**POST**  **http://url:port/enquiries/bom/add**

This enquiry is sent to LetzBuild for BOM (Bill Of Material). 

The parameters are:

* enquirynumber - A supplied value meant for tracking the enquiries. 
* firstname - First name
* lastname - Last name
* organisation - Organization / Company
* mobilenumber - mobile without the +91
* email
* enquiryheading - Subject line. This could be a standard driven by a template
* location - a locality or location
* anyadditionalinstruction - any specific instruction

**GET**  **http://url:port/enquiries/bom/retrieve?limit=num&page=num**

There are multiple parameters supported. They all form OR clauses if passed together. Nothing is mandatory

 * limit - This parameter is supplied with a number that limits the search results. So for a registered user, this number could be 10/20/25. For a non-registered user, this could be as low as 4. The default is 10. 
 * page - typical pagination numbers. The default if not passed is 1.
 
 The results are always ordered by the enquiry date descending - latest on top


**POST**  **http://url:port/enquiries/pms/add**

This enquiry is sent to LetzBuild for PMS(Procurement Management Services). 

The parameters are:

* enquirynumber - A supplied value meant for tracking the enquiries. 
* firstname - First name
* lastname - Last name
* organisation - Organization / Company
* mobilenumber - mobile without the +91
* email
* enquiryheading - Subject line. This could be a standard driven by a template
* location - a locality or location
* anyadditionalinstruction - any specific instruction

**GET**  **http://url:port/enquiries/pms/retrieve?limit=num&page=num**

There are multiple parameters supported. They all form OR clauses if passed together. Nothing is mandatory

 * limit - This parameter is supplied with a number that limits the search results. So for a registered user, this number could be 10/20/25. For a non-registered user, this could be as low as 4. The default is 10. 
 * page - typical pagination numbers. The default if not passed is 1.
 
 The results are always ordered by the enquiry date descending - latest on top


**POST**  **http://url:port/enquiries/qs/add**

This enquiry is sent to LetzBuild for QS(Quotation Services). 

The parameters are:

* enquirynumber - A supplied value meant for tracking the enquiries. 
* firstname - First name
* lastname - Last name
* organisation - Organization / Company
* mobilenumber - mobile without the +91
* email
* enquiryheading - Subject line. This could be a standard driven by a template
* location - a locality or location
* anyadditionalinstruction - any specific instruction

**GET**  **http://url:port/enquiries/qs/retrieve?limit=num&page=num**

There are multiple parameters supported. They all form OR clauses if passed together. Nothing is mandatory

 * limit - This parameter is supplied with a number that limits the search results. So for a registered user, this number could be 10/20/25. For a non-registered user, this could be as low as 4. The default is 10. 
 * page - typical pagination numbers. The default if not passed is 1.
 
 The results are always ordered by the enquiry date descending - latest on top
 
 
 ======================================Search======================================
 **GET**  **http://url:port/search/products?word=keyword?limit=num**
  
  This searches for products by picking up textual content from sub category, name, specs, dimensions, manufacturers as well as purpose. 
  The results have the total hits and an aggregation on sub category.
  
   * word - This is the search phrase or keyword or partial word etc.
   * limit - This parameter is supplied with a number that limits the search results.  

   
 
 **GET**  **http://url:port/search/global?word=keyword**
 
 It is called as global because it has the capability to search in product, supplier and categories and services in the future. 
 The results come grouped according to the index names. 
 
  * word - This is the search phrase or keyword or partial word etc.
   
 
**GET**  **http://url:port/search/indexproducts**
 
  Use this API to index all products. 
   
   
**GET**  **http://url:port/search/indexsuppliers**
 
  Use this API to index all suppliers.   
