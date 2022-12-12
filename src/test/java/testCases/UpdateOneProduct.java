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

public class UpdateOneProduct {
	String baseURI;
	SoftAssert softAssert;
	String firstProductId;
	HashMap<String,String>updatePayload;

	public UpdateOneProduct() {
	baseURI = "https://techfios.com/api-prod/api/product";
	softAssert = new SoftAssert();
	updatePayload = new HashMap<String,String>();
}
	
	public Map<String,String> upatePayloadMap(){
		updatePayload.put("id", "6277");
		updatePayload.put("name","Super Cool Headset 3.0 By Swati");
		updatePayload.put("description","The best Headset for amazing programmers.");
		updatePayload.put("price","399");
		updatePayload.put("category_id","2");
		updatePayload.put("category_name","Electronics");
		return updatePayload;
	}
		
	@Test(priority=1)
	public void updateOneProduct() {
		
//		System.out.println("Create Payload Map:" + createPayloadMap());
		
		Response response =
		    
			given()
			   .baseUri(baseURI)
			   .header("Content-Type", "application/json; charset=UTF-8")
			   .auth().preemptive().basic("demo@techfios.com", "abc123")
//			   .body(new File(createPayload)).
			   .body(upatePayloadMap()).
			when()
			    .put("/update.php").
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
		softAssert.assertEquals(productMessage, "Product was updated.", "Product message is not matching!");
		System.out.println("Product message:" + productMessage);
		
		softAssert.assertAll();
		
	}	
	@Test(priority=2)
	public void readOneUpdatedProduct() {

		Response response =

				given().baseUri(baseURI)
				        .header("Content-Type", "application/json")
				        .auth().preemptive()
					    .basic("demo@techfios.com", "abc123")
					    .queryParam("id", upatePayloadMap().get("id")).
				 when()
						.get("/read_one.php").
				 then()
				        .extract().response();

		String actualResponseBody = response.getBody().asString();
		System.out.println("Actual Resposne Body:" + actualResponseBody);

		JsonPath jp = new JsonPath(actualResponseBody);

		String actualProductName = jp.getString("name");
		String expectedProductName = upatePayloadMap().get("name");
		softAssert.assertEquals(actualProductName, expectedProductName, "Product Name is not matching!");
        System.out.println("Actual Product Name:" + actualProductName);
		
		String actualProductDescription = jp.getString("description");
		String expectedProductDescription = upatePayloadMap().get("description");
		softAssert.assertEquals(actualProductDescription, expectedProductDescription, "Product description is not matching!");
		System.out.println(" Actual Product Description:" + actualProductDescription);
		
 
		String actualProductPrice = jp.getString("price");
		String expectedProductPrice= upatePayloadMap().get("price");
		softAssert.assertEquals(actualProductPrice, expectedProductPrice, "Product price is not matching!");
		System.out.println("Actual Product Price:" + actualProductPrice);
		
		softAssert.assertAll();
	}
		
				  
	
	
}
		
		
			   
			    
