# Tacx ActivityEntity Microservice

** Steps to run **
1. Import this into any IDE workspace
2. Change the variable "save.file.path" in application.properties to desired folder where the source file has to be saved
2. Run the command : mvn clean install
3. Start the app from location of pom.xml : mvn spring-boot:run

** 4 Rest API's **
1. createActivityEntity [Post] : To create ActivityEntity for the incoming file and save it to MongoDb and file system
2. retrieveActivityEntity [Get] : To retrieve the Summary of already saved ActivityEntity
3. updateActivityEntity [Put] : To update the record to existing ActivityEntity and retrieve the Summary. Also save back to MongoDB
4. deleteRecordActivityEntity [Put] : To delete the record in existing ActivityEntity and retrieve the Summary. Also save back to MongoDB

** Details of API's**
Once the app is run, check the swagger UI : http://localhost:8083/swagger-ui.html#/

** Postman scripts **
1. Import the postman collection file : Tacx CRUD.postman_collection.json into Postman for testing
2. For GET and PUT calls, <<DOCUMENT_ID>> has to be replaced by the ID returned in createActivityEntity API
3. For createActivity API, in Body tab, csv file has to be uploaded which goes as input 

** Assumptions with request csv file **
1. Row 1 is the header of Activity definition
2. Row 3 is the header of Records
3. Row 4 and above contains all the records



