package order;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;


import java.util.HashMap;
import java.util.List;

import static constants.Constants.*;
import static io.restassured.RestAssured.given;
import static org.junit.Assert.*;

public class GetOrdersTest {

    public ValidatableResponse getOrdersList() {
        return given()
                .contentType(ContentType.JSON)
                .baseUri(HOME_URL)
                .when()
                .get(ORDER)
                .then();
    }

    @Test
    @DisplayName("Get Orders List")
    public void getOrderList() {
        ValidatableResponse response = getOrdersList();

        int actualStatusCode = response.extract().statusCode();
        assertEquals(200, actualStatusCode);

        List<HashMap> orderList = response.extract().path("orders");
        assertNotNull(orderList);

        if (!orderList.isEmpty()) {
            assertTrue(orderList.get(0).get("id") instanceof Integer);
        }
    }

}