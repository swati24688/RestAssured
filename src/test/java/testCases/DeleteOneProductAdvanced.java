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

public class DeleteOneProductAdvanced {
	String baseURI;
	SoftAssert softAssert;
	String createPayloadPath;
	HashMap<String,String>createPayload;
	String firstProductId;
	HashMap<String,String>deletePayload;
	String DeleteProductId; 

	public DeleteOneProductAdvanced() {
	baseURI = "https://techfios.com/api-prod/api/product";
	softAssert = new SoftAssert();
	createPayloadPath = "src\\main\\java\\data\\CreatePayload.json";
	createPayload = new HashMap<String,String>();
	deletePayload = new HashMap<String,String>();
}
	public Map<String,String> createPayloadMap(){
		createPayload.put("name","Amazing Headset 5.0 By Swati p.");
		createPayload.put("description","The best Headset for amazing programmers.");
		createPayload.put("price","499");
		createPayload.put("category_id","2");
		createPayload.put("category_name","Electronics");
		return createPayload;
	}
	
	public Map<String,String> deletePayloadMap(){
		deletePayload.put("id", DeleteProductId);
		return deletePayload;
	}
	
	@Test(priority=1)
	public void createOneProduct() {
		
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
		DeleteProductId = firstProductId;
	}
		
	@Test(priority=3)
	public void deleteOneProduct() {
		
		Response response =
		    
			given()
			   .baseUri(baseURI)
			   .header("Content-Type", "application/json; charset=UTF-8")
			   .auth().preemptive().basic("demo@techfios.com", "abc123")
//			   .body(new File(createPayload)).
			   .body(deletePayloadMap()).
			when()
			    .delete("/delete.php").
			then()
			     .extract().response();	
		
		
		int responseStatusCode = response.getStatusCode();
		softAssert.assertEquals(responseStatusCode, 200,"Response status code are not matching!");
		System.out.println("Response status code:" + responseStatusCode);
		
		String responseHeaderContentType = response.getHeader("Content-Type");
		softAssert.assertEquals(responseHeaderContentType, "application/json; charset=UTF-8", "Response Header Content Type is not matching!");
		System.out.println("Response Header ContentType:" + responseHeaderContentType);
		
		
		String responseBody = response.getBody().asString();
		System.out.println("Response Body:" + responseBody);
		
		JsonPath jp = new JsonPath(responseBody);
		
		String productMessage = jp.getString("message");
		softAssert.assertEquals(productMessage, "Product was deleted.", "Product message is not matching!");
		System.out.println("Product message:" + productMessage);
		
		softAssert.assertAll();
		
	}	
	@Test(priority=4)
	public void readOneDeletedProduct() {

		Response response =

				given().baseUri(baseURI)
				        .header("Content-Type", "application/json")
				        .auth().preemptive()
					    .basic("demo@techfios.com", "abc123")
					    .queryParam("id", deletePayloadMap().get("id")).
				 when()
						.get("/read_one.php").
				 then()
				        .extract().response();
		

		int responseStatusCode = response.getStatusCode();
		softAssert.assertEquals(responseStatusCode, 404,"Response status code are not matching!");
		System.out.println("Response status code:" + responseStatusCode);

		String actualResponseBody = response.getBody().asString();
		System.out.println("Actual Resposne Body:" + actualResponseBody);

		JsonPath jp = new JsonPath(actualResponseBody);

		String actualDeleteMessage = jp.getString("message");
		String expectedDeleteMessage ="Product does not exist.";
		softAssert.assertEquals(actualDeleteMessage, expectedDeleteMessage, "Product messages are not matching!");
        System.out.println("Actual Delete Message:" + actualDeleteMessage);
		
		
		softAssert.assertAll();
	}
		
}
		
		
			   
			    
