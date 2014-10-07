LetzBuildAPI
============

This is the API (REST based) that'll provide services for webbased apps as well as mobile etc.

Base URL = http://url:port/

The API list are:
-----------------
*GET* products/search?word=full_word&purp=full_word

There are multiple parameters supported. They all form AND clauses. They can also be used independently. For e.g., purpose may be used without the word param

 * **word** - This searches for the full_word on the name and category fields. The word is case insensitive. Note: As of now it is implemented as $regex. Once we have fulltext implemented, we'll alter the implementation to do mongodb's text search 
 * **purp** - This is the purpose. This searches for a full word in the purpose fields. The word is case sensitive. We need to make sure that the purpose is used like tags and not verbose. 


