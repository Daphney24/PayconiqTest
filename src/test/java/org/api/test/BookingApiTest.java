package org.api.test;

import static io.restassured.RestAssured.given;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import org.base.test.BaseTest;
import org.json.simple.JSONObject;
import org.model.test.Booking;
import org.model.test.BookingDates;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.LogStatus;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utils.ExtentReportListener;

@Listeners(ExtentReportListener.class)
public class BookingApiTest extends ExtentReportListener{

	private static RequestSpecification requestSpec;
	private static Properties prop;
	private Booking createdBooking;
	private Integer createdBookingid;
	
	private static final int SUCCESS_STATUS_CODE = 200;
	private static final int CREATED_STATUS_CODE = 201;
	
	@BeforeTest
	public void setup()  {
		BaseTest baseTest = new BaseTest();
		prop = baseTest.getProp();
		String url = prop.getProperty("api.baseUri");
		String basePath = prop.getProperty("api.basePath");
		requestSpec = new RequestSpecBuilder()
				.setBaseUri(url)
				.setBasePath(basePath)
				.setContentType(ContentType.JSON)
				.build();	
		test.log(LogStatus.PASS, "Setup is Succcessfully");
	}

	@Test(priority = 1)
	public void testCreateBookingEndpoint(){
		  
		BookingDates bookingDates = new BookingDates("2021-09-02", "2021-09-10");
		createdBooking = new Booking("John", "Doe", 205, false, bookingDates, "Lunch");
		
		Response response = given().
				spec(requestSpec).
		and().
				body(createdBooking).
		when().
				post().
		then().
				extract().
				response();
				
		try {
			Assert.assertEquals(SUCCESS_STATUS_CODE, response.getStatusCode());
			//test.log(LogStatus.PASS, "Succcessfully validated status code:: " + response.getStatusCode());
			createdBookingid = response.path("bookingid");
		}catch (AssertionError e) {
			//test.log(LogStatus.FAIL, "Incorrect status code:: "+ response.getStatusCode()+" is returned with response :: "+response.prettyPrint());
			Assert.fail();
		}catch(Exception e) {
			//test.log(LogStatus.FAIL,"Error thrown is: "+e.fillInStackTrace());
			Assert.fail();
		}
	}
	
	//@Test(priority = 2)
	public void testGetBookingByIdEndpoint() {
		
		Response response = given().
				spec(requestSpec).
		and().
			    get("/{bookingId}", createdBookingid).
	    then().
	    		extract().
	    		response();
		
		try {
			Assert.assertEquals(SUCCESS_STATUS_CODE, response.getStatusCode());
			test.log(LogStatus.PASS, "Succcessfully validated status code:: " + response.getStatusCode());
		}catch (AssertionError e) {
			test.log(LogStatus.FAIL, "Expected status code is:: "+SUCCESS_STATUS_CODE+", instead got:: "+ response.getStatusCode());
			test.log(LogStatus.FAIL, "Failure logs are:: "+ e.fillInStackTrace());
			Assert.fail();
		}catch(Exception e) {
			test.log(LogStatus.FAIL,"Error thrown is: "+e.fillInStackTrace());
			Assert.fail();
		}
	}

	//@Test(priority = 3)
	public void testGetBookingByIdResponse() {
		
		Booking booking =given().
				spec(requestSpec).
		and().
			    get("/{bookingId}", createdBookingid).
	    then().
	    		contentType(ContentType.JSON).
	    		extract().
	    		as(Booking.class);
		
		Assert.assertEquals(booking.getFirstname(), createdBooking.getFirstname());		
		
	}
	
	//@Test(priority =4)
	public void testPartialUpdateBooking() {
		
		String newLastName = "dave";
		JSONObject jsonRequestforUpdate = new JSONObject();
		jsonRequestforUpdate.put("lastname", newLastName); 
		
		Booking updatedBooking =given().
				spec(requestSpec).
			  	auth().
			  	preemptive().
			  	basic(prop.getProperty("username"),prop.getProperty("password")).
		and().
				body(jsonRequestforUpdate).
		when().	
	    	 	patch("/{bookingId}", createdBookingid).
	    then().
	    		assertThat().
	    		statusCode(SUCCESS_STATUS_CODE).
	    		extract().
	    		as(Booking.class);
		
		Assert.assertEquals(updatedBooking.getLastname(), newLastName);
		createdBooking = updatedBooking;
	}
	
	//@Test(priority = 5)
	public void testGetBookingIdswithoutParameters() {
		
		
		Response response = given().
				spec(requestSpec).
		and().
	    		get().
	    then().
	    		extract().
	    		response();
		
		try {
			Assert.assertEquals(SUCCESS_STATUS_CODE, response.getStatusCode());
			test.log(LogStatus.PASS, "Succcessfully validated status code:: " + response.getStatusCode());
		}catch (AssertionError e) {
			test.log(LogStatus.FAIL, "Expected status code is:: "+SUCCESS_STATUS_CODE+", instead got:: "+ response.getStatusCode());
			test.log(LogStatus.FAIL, "Failure logs are:: "+ e.fillInStackTrace());
			Assert.fail();
		}catch(Exception e) {
			test.log(LogStatus.FAIL,"Error thrown is: "+e.fillInStackTrace());
			Assert.fail();
		}
		
		List<HashMap<String, Integer>> bookingids = response.jsonPath().getList("$");
		Assert.assertNotNull(bookingids, "Booking id not returned");		
		
	}

	//@Test(priority = 6)
	public void testGetBookingIdswithParameters() {
		
		Response response = given().
				spec(requestSpec).
				queryParam("firstname", createdBooking.getFirstname()).
				queryParam("lastname", createdBooking.getLastname()).
		and().
	    		get().
	    then().
	    		extract().
	    		response();
		
		try {
			Assert.assertEquals(SUCCESS_STATUS_CODE, response.getStatusCode());
			test.log(LogStatus.PASS, "Succcessfully validated status code:: " + response.getStatusCode());
		}catch (AssertionError e) {
			test.log(LogStatus.FAIL, "Expected status code is:: "+SUCCESS_STATUS_CODE+", instead got:: "+ response.getStatusCode());
			test.log(LogStatus.FAIL, "Failure logs are:: "+ e.fillInStackTrace());
			Assert.fail();
		}catch(Exception e) {
			test.log(LogStatus.FAIL,"Error thrown is: "+e.fillInStackTrace());
			Assert.fail();
		}
		
		List<HashMap<String, Integer>> bookingids = response.jsonPath().getList("$");
		Integer actualBookingId = bookingids.get(0).get("bookingid");
		Assert.assertEquals(actualBookingId, createdBookingid);		
	}
	
	//@Test(priority = 7)
	public void testDeleteEndpoint() {
		
		Response response = given().
			  	spec(requestSpec).
			  	auth().
			  	preemptive().
			  	basic(prop.getProperty("username"),prop.getProperty("password")).
	   and().
	   			delete("/{bookingId}", createdBookingid).
	   then().
	   			extract().
	   			response();
		try {
			Assert.assertEquals(CREATED_STATUS_CODE, response.getStatusCode());
			test.log(LogStatus.PASS, "Succcessfully deleted for bookingId:: "+createdBookingid+"by validating status code:: " + response.getStatusCode());
		}catch (AssertionError e) {
			test.log(LogStatus.FAIL, "Expected status code is:: "+CREATED_STATUS_CODE+", instead got:: "+ response.getStatusCode());
			test.log(LogStatus.FAIL, "Failure logs are:: "+ e.fillInStackTrace());
			Assert.fail();
		}catch(Exception e) {
			test.log(LogStatus.FAIL,"Error thrown is: "+e.fillInStackTrace());
			Assert.fail();
		}
	}
	
}
