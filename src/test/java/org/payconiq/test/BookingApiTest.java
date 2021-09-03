package org.payconiq.test;

import static io.restassured.RestAssured.given;

import java.util.Properties;

import org.payconiq.test.model.Booking;
import org.payconiq.test.model.BookingDates;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class BookingApiTest {

	private static RequestSpecification requestSpec;
	private static Properties prop;
	
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

	@Test
	public void testCreateBookingEndpoint(){
		  
		BookingDates bookingDates = new BookingDates("2021-09-02", "2021-09-10");
		Booking booking = new Booking("John", "Doe", 205, false, bookingDates, "Lunch");
		
		given().
				spec(requestSpec).
		and().
				body(booking).
		when().
				post().
		then().
			assertThat().
			statusCode(SUCCESS_STATUS_CODE);				
	}
	
	@Test
	public void testGetBookingByIdEndpoint() {
		
		given().
				spec(requestSpec).
		and().
			    get("/1").
	    then().
			    assertThat().
			    statusCode(SUCCESS_STATUS_CODE);
	}
	
	@Test
	public void testDeleteEndpoint() {
		
		given().
			  	spec(requestSpec).
			  	auth().
			  	preemptive()
			  	.basic(prop.getProperty("username"),prop.getProperty("password")).
	   and().
	   		  delete("/25").
	   then().
	   		  assertThat().
	   		  statusCode(CREATED_STATUS_CODE);
	   
	}
	
	@Test
	public void testGetBookingIdsAndCheckParmaters() {
		
		given().
				spec(requestSpec).
		and().
	    	 	get("/1").
	    then().
	    		extract();
	}
}
