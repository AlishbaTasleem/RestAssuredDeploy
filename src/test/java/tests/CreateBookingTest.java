package tests;

import base.BaseTest;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static utils.Payloads.CreateBookingPayload.*;

public class CreateBookingTest extends BaseTest {

    @Test
    public void createBooking(){
        String payload = createBookingWithAllFields();

        Response response = given()
                .header("Content-Type","application/json")
                .body(payload)
                .post("/booking");

        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode,200, "Expected Response code was 200");

        response.then().body("booking.firstname", equalTo("Jim"));
        response.then().body("booking.lastname", equalTo("Brown"));

        System.out.println(response.prettyPrint());
    }

    @Test
    public void createBookingWithoutFirstName(){
        String payload = bookingWithoutFirstName();

        Response response = given()
                .header("Content-Type", "application/json")
                .body(payload)
                .post("/booking");

        Assert.assertEquals(response.statusCode(),500,"Expected a 500");
    }

    @Test
    public void createBookingWithInvalidDateFormat() {
        String payload = bookingWithInvalidDateFormat();

        Response response = given()
                .header("Content-Type", "application/json")
                .body(payload)
                .post("/booking");

        Assert.assertEquals(response.getStatusCode(), 400, "Expected a 400 Bad Request due to invalid date format.");
    }

    @Test
    public void createBookingWithCheckoutBeforeCheckIn() {
        String payload = bookingWithCheckoutBeforeCheckin();

        Response response = given()
                .header("Content-Type", "application/json")
                .body(payload)
                .post("/booking");

        Assert.assertEquals(response.getStatusCode(), 400, "Expected a 400 Bad Request due to checkout date before CheckIn date.");
    }

    @Test
    public void createBookingWithoutOptionalField() {
        // Payload without the "additionalneeds" field
        String payload = bookingWithoutOptionalFields();

        Response response = given()
                .header("Content-Type", "application/json")
                .body(payload)
                .post("/booking");

        Assert.assertEquals(response.getStatusCode(), 200, "Expected a 200 OK.");
        String additionalNeeds = response.jsonPath().getString("additionalneeds");
        Assert.assertNull(additionalNeeds, "Expected 'additionalneeds' to be null or not present.");
    }

    @Test
    public void createBookingWithEmptyPayload() {
        String emptyPayload = "{}";

        Response response = given()
                .header("Content-Type", "application/json")
                .body(emptyPayload)
                .post("/booking");

        Assert.assertEquals(response.getStatusCode(), 500, "Expected a 500   due to empty payload.");
    }

    @Test
    public void createBookingWithSpecialCharsInFirstName() {
        // Payload with special characters in the "firstname" field
        String payload = bookingWithSpecialCharactersInFirstName();

        Response response = given()
                .header("Content-Type", "application/json")
                .body(payload)
                .post("/booking");

        Assert.assertEquals(response.getStatusCode(), 200, "Expected a 200 OK.");
    }

    @Test
    public void createBookingWithIntegersInFirstName() {
        // Payload with an integer in the "firstname" field
        String payload = bookingWithIntegersInFirstName();
        Response response = given()
                .header("Content-Type", "application/json")
                .body(payload)
                .post("/booking");

        Assert.assertEquals(response.getStatusCode(), 500, "Expected a 500 Bad Request due to integer in firstname field.");
        Assert.assertEquals(response.body().asString(),"Internal Server Error");
    }
}
