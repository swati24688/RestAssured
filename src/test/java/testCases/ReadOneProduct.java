package testCases;

import static io.restassured.RestAssured.given;

import java.util.concurrent.TimeUnit;

import org.hamcrest.Matcher;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class ReadOneProduct {
	String baseURI;
	SoftAssert softAssert;
	/*
	 * 02.ReadOneProduct
	 * 
	 * http method=GET 
	 * EndPointUrl=
	 * https://techfios.com/api-prod/api/product/read_one.php 
	 * Autherization:(basicAuth)
	 * username=demo@techfios.com password=abc123 
	 * Query Parameters=6034
	 * Header/s:
	 * Content-Type = application/json http status
	 * code=200
	 *  Response Time=<=1500ms
	 * 
	 * 
	 * given() = all inputdetails=(baseUri,header/s,autherization,query
	 * params,payload/body) when()=submit request =http method(endPoint) then()=
	 * response validation(statusCode,header/s,responseTime,response payload/body)
	 */

	public ReadOneProduct() {
		baseURI = "https://techfios.com/api-prod/api/product";
		softAssert = new SoftAssert();
	}

	@Test
	public void readOneProduct() {

		Response response =

				given().baseUri(baseURI).header("Content-Type", "application/json")
				        .auth().preemptive()
					    .basic("demo@techfios.com", "abc123")
					    .queryParam("id", "6369").
				 when()
//		         .log().all()
						.get("/read_one.php").
				 then().extract().response();

        long responseTime = response.getTimeIn(TimeUnit.MILLISECONDS);
//		System.out.println("Response Time:" + responseTime);

		if (responseTime <= 2500) {
//			System.out.println("Response Time is within range");
		} else {
//			System.out.println("Response Time is out of range");
		}

		
        int responseStatusCode = response.getStatusCode();
	    softAssert.assertEquals(responseStatusCode, 200, "Response Status Code are not matching!");
		System.out.println("Response status code:" + responseStatusCode);
		
		String responseHeaderContentType = response.getHeader("Content-Type");
		softAssert.assertEquals(responseHeaderContentType, "application/json2","Response Header Content Types are not matching");
		System.out.println("Response Header ContentType:" + responseHeaderContentType);
		

		String responseBody = response.getBody().asString();
		System.out.println("Resposne Body:" + responseBody);

		JsonPath jp = new JsonPath(responseBody);

		String ProductName = jp.getString("name");
		softAssert.assertEquals(ProductName, "Amazing Headset 2.0 By Swati P", "Product Name is not matching!");
        System.out.println("Product Name:" + ProductName);
		
		String ProductDescription = jp.getString("description");
		softAssert.assertEquals(ProductDescription, "The best Headset for amazing programmers", "Product description is not matching!");
		System.out.println("Product Description:" + ProductDescription);
		

		String ProductPrice = jp.getString("price");
		softAssert.assertEquals(ProductPrice, "299", "Product price is not matching!");
		System.out.println("Product Price:" + ProductPrice);
		
        softAssert.assertAll();
	}

}
