package testCases;

import static io.restassured.RestAssured.given;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.hamcrest.Matcher;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class CreateOneProduct {
	String baseURI;
	SoftAssert softAssert;
	String createPayloadPath;
	HashMap<String,String>createPayload;
	String firstProductId;

	public CreateOneProduct() {
	baseURI = "https://techfios.com/api-prod/api/product";
	softAssert = new SoftAssert();
	createPayloadPath = "src\\main\\java\\data\\CreatePayload.json";
	createPayload = new HashMap<String,String>();
}
	public Map<String,String> createPayloadMap(){
		createPayload.put("name","Amazing Headset 3.0 By Swati p.");
		createPayload.put("description","The best Headset for amazing programmers.");
		createPayload.put("price","399");
		createPayload.put("category_id","2");
		createPayload.put("category_name","Electronics");
		return createPayload;
	}
	
	
		
		/*
	03. Create One Product
		 * http method=POST 
          EndPointUrl= https://techfios.com/api-prod/api/product/create.php
          Autherization:(basic auth)
          username=demo@techfios.com
          password=abc123 
          Header/s:
          Content-Type=application/json; charset=UTF-8
          http status code=201
          Response Time= <=1500ms
          Payload/Body:


	
	         /*given() = all inputdetails=(baseUri,header/s,autherization,query params,payload/body) 
			 * when()=submit request =http method(endPoint) 
			 * then()=response validation(statusCode,header/s,responseTime,response payload/body)
			 */
	
	@Test(priority=1)
	public void createOneProduct() {
		
//		System.out.println("Create Payload Map:" + createPayloadMap());
		
		Response response =
		    
			given()
			   .baseUri(baseURI)
			   .header("Content-Type", "application/json; charset=UTF-8")
			   .auth().preemptive().basic("demo@techfios.com", "abc123")
//			   .body(new File(createPayload)).
			   .body(createPayloadMap()).
			when()
			    .post("/create.php").
			then()
			     .extract().response();	
		
		
		int responseStatusCode = response.getStatusCode();
		softAssert.assertEquals(responseStatusCode, 201,"Response status code are not matching!");
		System.out.println("Response status code:" + responseStatusCode);
		
		String responseHeaderContentType = response.getHeader("Content-Type");
		softAssert.assertEquals(responseHeaderContentType, "application/json; charset=UTF-8", "Response Header Content Type is not matching!");
		System.out.println("Response Header ContentType:" + responseHeaderContentType);
		
		
		String responseBody = response.getBody().asString();
		System.out.println("Response Body:" + responseBody);
		
		JsonPath jp = new JsonPath(responseBody);
		
		String productMessage = jp.getString("message");
		softAssert.assertEquals(productMessage, "Product was created.", "Product message is not matching!");
		System.out.println("Product message:" + productMessage);
		
		JsonPath jp2 = new JsonPath(new File(createPayloadPath));
		String name = jp2.getString("name");
		System.out.println("Expected Product Name:" + name);
		
		softAssert.assertAll();
		
	}
	@Test(priority=2)
	public void readAllProduct() {
		
		Response response =
				
				given()
				    .baseUri(baseURI)
				    .header("Content-Type", "application/json; charset=UTF-8")
				    .auth().preemptive().basic("demo@techfios.com", "abc123").
				when()
				    .get("/read.php").
				then()
//				    .log.all()
				     .extract().response();
		
		String responseBody = response.getBody().asString();
		System.out.println("Response Body:" + responseBody);
		
		JsonPath jp = new JsonPath(responseBody);
	    firstProductId = jp.getString("records[0].id");
		System.out.println("First Product Id:" + firstProductId);
	}
	
	@Test(priority=3)
	public void readOneProduct() {

		Response response =

				given().baseUri(baseURI)
				        .header("Content-Type", "application/json")
				        .auth().preemptive()
					    .basic("demo@techfios.com", "abc123")
					    .queryParam("id", firstProductId).
				 when()
						.get("/read_one.php").
				 then()
				        .extract().response();

		String actualResponseBody = response.getBody().asString();
		System.out.println("Actual Resposne Body:" + actualResponseBody);

		JsonPath jp = new JsonPath(actualResponseBody);

		String actualProductName = jp.getString("name");
		String expectedProductName = createPayloadMap().get("name");
		softAssert.assertEquals(actualProductName, expectedProductName, "Product Name is not matching!");
        System.out.println("Actual Product Name:" + actualProductName);
		
		String actualProductDescription = jp.getString("description");
		String expectedProductDescription = createPayloadMap().get("description");
		softAssert.assertEquals(actualProductDescription, expectedProductDescription, "Product description is not matching!");
		System.out.println(" Actual Product Description:" + actualProductDescription);
		
 
		String actualProductPrice = jp.getString("price");
		String expectedProductPrice= createPayloadMap().get("price");
		softAssert.assertEquals(actualProductPrice, expectedProductPrice, "Product price is not matching!");
		System.out.println("Actual Product Price:" + actualProductPrice);
		
		softAssert.assertAll();
	}
		
		
				  
	
	
}
		
		
			   
			    
