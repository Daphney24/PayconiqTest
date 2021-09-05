package org.payconiq.test;

import static io.restassured.RestAssured.given;

import java.util.LinkedHashMap;
import java.util.Properties;

import org.json.simple.JSONObject;
import org.payconiq.test.model.Booking;
import org.payconiq.test.model.BookingDates;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class BookingApiTest {

	private static RequestSpecification requestSpec;
	private static Properties prop;
	private Booking createdBooking;
	private Integer createdBookingid = 28;
	
	private static final int SUCCESS_STATUS_CODE = 200;
	private static final int CREATED_STATUS_CODE = 201;
	
	@BeforeClass
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
	}

	@Test(priority = 1)
	public void testCreateBookingEndpoint(){
		  
		BookingDates bookingDates = new BookingDates("2021-09-02", "2021-09-10");
		createdBooking = new Booking("John", "Doe", 205, false, bookingDates, "Lunch");
		
		createdBookingid = given().
				spec(requestSpec).
		and().
				body(createdBooking).
		when().
				post().
		then().
			assertThat().
			statusCode(SUCCESS_STATUS_CODE).
			extract().
			path("bookingid");
		
		Assert.assertNotNull(createdBookingid, "Booking id not found");
		System.out.println(createdBookingid);
	}
	
	@Test(priority = 2)
	public void testGetBookingByIdEndpoint() {
		
		given().
				spec(requestSpec).
		and().
			    get("/{bookingId}", createdBookingid).
	    then().
			    assertThat().
			    statusCode(SUCCESS_STATUS_CODE);
	}

	@Test(priority = 3)
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
	
	@Test(priority =4)
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
	}
	
	@Test(priority = 5)
	public void testDeleteEndpoint() {
		
	   given().
			  	spec(requestSpec).
			  	auth().
			  	preemptive().
			  	basic(prop.getProperty("username"),prop.getProperty("password")).
	   and().
	   			delete("/{bookingId}", createdBookingid).
	   then().
	   		  	assertThat().
	   		  	statusCode(CREATED_STATUS_CODE);
	}

}
