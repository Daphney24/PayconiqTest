package org.payconiq.test;

import static io.restassured.RestAssured.given;

import org.payconiq.test.model.Booking;
import org.payconiq.test.model.BookingDates;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class BookingApiTest {

	private static RequestSpecification requestSpec;

	@BeforeClass
	public void setup()  {
		BaseTest baseTest = new BaseTest();
		String url = baseTest.getProp().getProperty("api.baseUri");
		requestSpec = new RequestSpecBuilder()
				.setBaseUri(url)
				.setBasePath("/booking")
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
			statusCode(200);				
	}
	
	@Test
	public void testGetBookingByIdEndpoint() {
		
		given().
				spec(requestSpec).
		and().
			    get("/1").
	    then().
			    assertThat().
			    statusCode(200);
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
