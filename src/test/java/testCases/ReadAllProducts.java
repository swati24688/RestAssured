package testCases;

import org.hamcrest.Matcher;
import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;
import static org.testng.Assert.assertEquals;

import java.util.concurrent.TimeUnit;

public class ReadAllProducts {
	/*
	 * 01. Read All Products
	 * 
	 * http method=GET 
	 * EndPointUrl=
	 * https://techfios.com/api-prod/api/product/read.php
	 * Autherization:(basic auth)
	 * username=demo@techfios.com 
	 * password=abc123 Header/s:
	 * Content-Type=application/json; charset=UTF-8 
	 * http status code=200 Response
	 * Time= <=1500ms
	 * 
	 * given() = all inputdetails=(baseUri,header/s,autherization,query params,payload/body) 
	 * when()=submit request =http method(endPoint) 
	 * then()= response validation(statusCode,header/s,responseTime,response payload/body)
	 */

	@Test
	public void readAllProducts() {
     
	Response response=	
		
		
		given()        
				.baseUri("https://techfios.com/api-prod/api/product")
				.header("Content-Type","application/json; charset=UTF-8")
				.auth().preemptive().basic("demo@techfios.com", "abc123").
		when()
//		         .log().all()
		        .get("/read.php").
		then()
		         .extract().response();
      
	  long responseTime = response.getTimeIn(TimeUnit.MILLISECONDS);
      System.out.println("Response Time:" + responseTime);
      
      if(responseTime<=2500) {
    	  System.out.println("Response Time is within range");
      }else {
    	  System.out.println("Response Time is out of range");
      }
	
      int responseStatusCode = response.getStatusCode();
      System.out.println("Response status code:" + responseStatusCode);
      Assert.assertEquals(responseStatusCode, 200);
      
      String getresponseHeaderContentType = response.getHeader("Content-Type");
      System.out.println("Response Header ContentType:" + getresponseHeaderContentType);
	  Assert.assertEquals(getresponseHeaderContentType, "application/json; charset=UTF-8");
	
	  String responseBody = response.getBody().asString();
	  System.out.println("Resposne Body:" + responseBody);
	 
      JsonPath jp = new JsonPath(responseBody);
      String firstProductid = jp.get("records[0].id");
      System.out.println("First Product ID:" + firstProductid);
      
      if(firstProductid !=null) {
    	  System.out.println("Product list is not empty.");
      }else {
    	  System.out.println("Product list is empty!");
      }
	}

	
}
