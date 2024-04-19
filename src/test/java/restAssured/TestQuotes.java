package restAssured;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class TestQuotes {
	
	private String USER_TOKEN = "0b5c842f93a56f58ee052b2938a4f557";

    @BeforeClass
    public void setUp() {
        // Set the Base URI that RestAssured will use for all requests
        RestAssured.baseURI = "https://favqs.com/api";
    }

    @Test
    public void testGetRandomQuotes() {
        given()
            .header("Authorization", "Token token=" + USER_TOKEN)
        .when()
            .get("/quotes")
        .then()
            .statusCode(200)
            .log().body()
            .body("quotes.size()", equalTo(25)); }

    @Test
    public void testSearchQuotesByKeyword() {
        given()
            .header("Authorization", "Token token=" + USER_TOKEN)
            .queryParam("filter", "inspiration")
        .when()
            .get("/quotes")
        .then()
            .statusCode(200)
            .log().body()
            .body("quotes.findAll { it.body.toLowerCase().contains('inspiration') }.size()", greaterThan(0));
    }

    @Test
    public void testPaginatedQuoteResults() {
        int pageNumber = 2; // Testing the second page
        given()
            .header("Authorization", "Token token=" + USER_TOKEN)
            .queryParam("page", pageNumber)
        .when()
            .get("/quotes")
        .then()
            .statusCode(200)
            .log().body()
            .body("page", equalTo(pageNumber));
    }
    @Test
    public void testQuotesByTag() {
        final String tag = "dad";  // Define the tag we are looking for
        given()
            .header("Authorization", "Token token=" + USER_TOKEN)
            .queryParam("filter", tag)
            .queryParam("type", "tag")
        .when()
            .get("/quotes")
        .then()
            .statusCode(200)
            .log().body();   }


    @Test
    public void testQuotesByAuthor() {
        String author = "Albert Einstein";
        given()
            .header("Authorization", "Token token=" + USER_TOKEN)
            .queryParam("filter", author)
            .queryParam("type", "author")
        .when()
            .get("/quotes")
        .then()
            .statusCode(200)
            .log().body()
            ;

}
}
