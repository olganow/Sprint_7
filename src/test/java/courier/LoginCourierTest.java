package courier;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Test;

import static constants.Constants.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class LoginCourierTest {
    String id = null;
    private final String login = randomData(9);
    private final String password = "Password!";
    private final String incorrectLogin = randomData(11);
    private final String incorrectPassword = randomData(7);


    public static String randomData(int length) {
        return RandomStringUtils.randomAlphanumeric(length);
    }

    public static void createCourier(Courier courier) {
        given()
                .header("Content-Type", "application/json")
                .and()
                .baseUri(HOME_URL)
                .body(courier)
                .when()
                .post(COURIER);
    }

    public static Response loginCourier(Courier courier) {
        return given()
                .header("Content-Type", "application/json")
                .and()
                .baseUri(HOME_URL)
                .body(courier)
                .when()
                .post(COURIER_LOGIN);
    }

    public static void deleteCourier(String courierId) {
        given()
                .baseUri(HOME_URL)
                .delete(COURIER + "{courierId}", courierId);
    }

    @Test
    @DisplayName("Successful login and get a courier id")
    public void loginCourier() {
        Courier courier = new Courier(login, password);
        createCourier(courier);
        id = loginCourier(courier)
                .then()
                .extract()
                .path("id").toString();

        Response response = loginCourier(courier);
        response.then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("Authorization with empty login")
    public void loginCourierWithEmptyLogin() {
        String incorrectLogin = "";
        Courier courier = new Courier(incorrectLogin, password);

        Response response = loginCourier(courier);
        response.then().assertThat().statusCode(400)
                .and()
                .assertThat()
                .body("message", equalTo("Недостаточно данных для входа"));

    }

    @Test
    @DisplayName("Authorization with null login")
    public void loginCourierWithNullLogin() {
        String incorrectLogin = null;
        Courier courier = new Courier(incorrectLogin, password);

        Response response = loginCourier(courier);
        response.then().assertThat().statusCode(400)
                .and()
                .assertThat()
                .body("message", equalTo("Недостаточно данных для входа"));

    }

    @Test
    @DisplayName("Authorization with incorrect login")
    public void loginCourierWitEmptyLogin() {
        Courier courier = new Courier(incorrectLogin, password);

        Response response = loginCourier(courier);
        response.then().assertThat().statusCode(404)
                .and()
                .assertThat()
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Authorization with empty password")
    public void loginCourierWithEmptyPassword() {
        String incorrectPassword = "";
        Courier courier = new Courier(login, incorrectPassword);

        Response response = loginCourier(courier);
        response.then().assertThat().statusCode(400)
                .and()
                .assertThat()
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Authorization with null password")
    public void loginCourierWithNullPassword() {
        String incorrectPassword = "";
        Courier courier = new Courier(login, incorrectPassword);

        Response response = loginCourier(courier);
        response.then().assertThat().statusCode(400)
                .and()
                .assertThat()
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Authorization with incorrect password")
    public void loginCourierWithIncorrectPassword() {
        Courier courier = new Courier(login, incorrectPassword);

        Response response = loginCourier(courier);
        response.then().assertThat().statusCode(404)
                .and()
                .assertThat()
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @After
    public void deleteCourier() {
        if (id != null) {
            deleteCourier(id);
        }
    }
}