LetzBuildAPI
============

This is the API (REST based) that'll provide services for webbased apps as well as mobile etc.

Base URL = http://url:port/

The API list :
-------------
**GET**  **http://url:port/products/search?cat=full_word&word=full_word&limit=num**

There are multiple parameters supported. They all form AND clauses if passed together. They can also be used independently.

 * cat - This retrieves the products that are categorized under the category.  The full category name is required
 * word - This searches for the full_word on the name and category fields. The word is case insensitive. Note: As of now it is implemented as $regex. Once we have fulltext implemented, we'll alter the implementation to do mongodb's text search
 * limit - This parameter is supplied with a number that limits the search results. So for a registered user, this number could be 10/20/25. For a non-registered user, this could be as low as 4. 


**GET**  **http://url:port/products/categories**  

Lists out all the product categories


**GET**  **http://url:port/products/categories/:category**

This lists out sub categories (breadcrum) along with an immediate parent for a specified category.  