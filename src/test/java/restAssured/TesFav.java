package restAssured;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.*;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class TesFav {
	
	private String USER_TOKEN = "0b5c842f93a56f58ee052b2938a4f557";
	private String sessionToken; // To store the session token
   
	 @BeforeClass
	    public void setUp() {
	        // Set the Base URI that RestAssured will use for all requests
	        RestAssured.baseURI = "https://favqs.com/api";
	    }
	 @Test(priority = 0)
	    public void createSession() {
	        Response response = given()
	            .contentType(ContentType.JSON)
	            .header("Authorization","Token token=" + USER_TOKEN)
	            .body("{ \"user\": { \"login\": \"suprajab95@gmail.com\", \"password\": \"e6c24217a381f\" } }")
	        .when()
	            .post("/session")
	        .then()
	            .statusCode(200)
	            .log().body()
	            .extract().response(); // Extract the response to get the session token
	        
	        sessionToken = response.jsonPath().getString("User-Token");
	        Assert.assertNotNull(sessionToken, "Session token is null, session creation failed.");
	    }
	 @Test
	    public void whenFavoriteAQuoteThenDetailsAreCorrect() {
	       given()
	        .contentType(ContentType.JSON)
	            .header("Authorization", "Token token=" + USER_TOKEN)
	            .header("User-Token",sessionToken)
	            .pathParam("quote_id", 4)
	        .when()
	            .put("/quotes/{quote_id}/fav")
	        .then()
	            .statusCode(200).log().body()
	          .body("id", equalTo(4))
	            .body("author", equalTo("Anonymous"))
	           .body("body", equalTo("Make everything as simple as possible, but not simpler."))
	          .body("user_details.favorite", equalTo(true));
	    }

	    @Test
	    public void whenFavoriteANonExistentQuoteThenErrorResponse() {
	        RestAssured.given()
	                .header("Authorization", "Token token=" + USER_TOKEN)
	                .pathParam("quote_id", 99)
	            .when()
	                .put("/quotes/{quote_id}/fav").then().statusCode(404)
	                .body("status", equalTo(404))
	                .body("error", equalTo("Not Found"))
	                .log().body();


}

}