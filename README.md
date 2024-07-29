Telus Assessment Documentation 

For Testing : 

Mock Webserver - Used to mock http client 

JUnit - used for asserting the tests code to improve code quality. 

MVVM architecture - Used for separation of business logic from UI, it is easy to do unit test and retain the data during screen orientation. 

Data Binding - to bind data with UI	 

Paging library – to fetch page wise list from webservice   

 Retrofit – It is typing safe REST API client, used to simplify API calls  

Dagger Hilt – to create singleton objects, inject them and use in the project 

Navigation component – USed to navigate from fragment to fragment 

Glide – used to cache images 
 

Issue faced: 

Ideal way to initialize API_KEY is to keep in local.properties, and define in buildConfig, then use the parameter in required code. I tried to implement it but it did not generate Buildconfig class. Currently I have created constant variable to use in the project. 

 
