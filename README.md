# PayconiqAssessment

Tech Stack: Java , RestAssured , TestNG, Maven

In this assignment, you are responsible for writing automated API tests for Hotel Booking Endpoints.

Assignment Workflow:

API Documentation:

http://restful-booker.herokuapp.com/apidoc/index.html#api-Booking-GetBookings

Project Requirements:

• You have a working “createBooking” and “getBookingById” endpoints, make sure that “partialUpdateBooking , “deleteBooking” and “getBookingIds”(test get all bookingIds
without filter, and add test for 1-2 parameters) are working. (Write automated tests that can be used for regression) 

Framework Architecture
--------------
    PayconiqAssessment
	   |
       |_src/main/java
       |  |_org.payconiq.test
       |  |  |_BaseTest.java   
       |  |_utils
       |  |  |_ExtentReportListener.java
	   |_src/main/resources
       |  |_config.properties
	   |_src/test/java
       |  |_org.api.test
	   |  |  |_BookingApiTest.java
       |  |_org.model.test
       |  |  |_Booking.java
       |  |  |_BookingDates.java

   
Building Up the Framework
--------------
	
* Step 1 : Create a maven project
* Step 2 : Add dependencies in pom.xml
* Step 3 : Add testing plugin
* Step 4 : Create tests
* Step 5 : Run and verify

### Installation (pre-requisites)
1. JDK 1.8+ (make sure Java class path is set)
2. Maven (make sure .m2 class path is set)
3. IDE (Eclipse, IntelliJ, etc)
4. TestNG
5. Rest Assured Dependencies
6. Git

Setup Instructions
--------------
Clone the repo from below copmmand or download zip and set it up in your local workspace.
```
git clone https://github.com/Daphney24/PayconiqAssessment.git
```

Verify Installation and Running test
--------------
Use Maven:
	
Go to your project directory from terminal and hit following commands
```
mvn test
```
	
	
### Reporters
	
Once you run the tests the reports will be generated in Extent Reports format to communicate pass/failure.It also includes the default HTML reports as well.

Note: To look at the results in IDE, open the folder test-output/Report/test/ExtentReport.html 
