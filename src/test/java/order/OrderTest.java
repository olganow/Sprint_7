package order;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

import static constants.Constants.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
public class OrderTest {

    private static final String FIRST_NAME = "Маша";
    private static final String LAST_NAME = "Миронова";
    private static final String ADDRESS = "г. Москва, Вятский пер, д.4 ";
    private static final String METRO_STATION = "Савёловская";
    private static final String PHONE_NUMBER = "12345678901";
    private static final int RENT_TIME = 1;
    private static final String DELIVERY_DATE = "2024-05-29";
    private static final String COMMENT_FOR_COURIER = "Вход со двора";
    private List<String> color;

    String orderNumber;

    public OrderTest(List<String> color) {
        this.color = color;
    }

    public static Response addOrder(Order order) {
        return given()
                .contentType(ContentType.JSON)
                .baseUri(HOME_URL)
                .header("Content-Type", "application/json")
                .and()
                .body(order)
                .when()
                .post(ORDER);
    }

    @Parameterized.Parameters
    public static Object[][] getColor() {
        return new Object[][]{
                {List.of("BLACK", "GREY")},
                {List.of("BLACK")},
                {List.of("GREY")},
                {List.of()}
        };
    }

    Order order = new Order(FIRST_NAME, LAST_NAME, ADDRESS, METRO_STATION, PHONE_NUMBER, RENT_TIME, DELIVERY_DATE,
            COMMENT_FOR_COURIER, color);

    @Test
    @DisplayName("Add order")
    public void createOrder() {
        Response response = addOrder(order);
        orderNumber = response
                .then()
                .extract()
                .path("track").toString();
        response.then()
                .assertThat()
                .statusCode(201)
                .and()
                .assertThat()
                .body("track", notNullValue());
    }

    @After
    public void cancelOrder() {
        RestAssured.baseURI = HOME_URL;
        given()
                .header("Content-type", "application/json")
                .body(orderNumber)
                .when()
                .put(CANCEL_ORDER);
    }
}