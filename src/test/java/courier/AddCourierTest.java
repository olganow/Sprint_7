package courier;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Test;

import static constants.Constants.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class AddCourierTest {

    String id = null;
    private final String name = "CourierName";
    private final String login = randomData(9);
    private final String password = "Password!";

    public static String randomData(int length) {
        return RandomStringUtils.randomAlphanumeric(length);
    }

    public static Response createCourier(Courier courier) {
        return given()
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
    @DisplayName("Add a courier with correct data")
    public void createCourier() {
        Courier courier = new Courier(login, password, name);
        Response response = createCourier(courier);
        id = loginCourier(courier)
                .then().extract()
                .path("id").toString();

        response.then()
                .assertThat()
                .statusCode(201)
                .and()
                .body("ok", equalTo(true));
    }


    @Test
    @DisplayName("Add a courier with an empty name")
    public void createCourierWithEmptyName() {
        String incorrectName = "";
        Courier courier = new Courier(name, login, incorrectName);

        Response response = createCourier(courier);
        response.then()
                .assertThat()
                .statusCode(400)
                .and()
                .assertThat()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Add a courier with a null name")
    public void createCourierWithNullName() {
        String incorrectName = null;
        Courier courier = new Courier(name, login, incorrectName);

        Response response = createCourier(courier);
        response.then()
                .assertThat()
                .statusCode(400)
                .and()
                .assertThat()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }


    @Test
    @DisplayName("Add a courier with an empty login")
    public void createCourierWithEmptyLogin() {
        String incorrectLogin = "";
        Courier courier = new Courier(name, incorrectLogin, password);

        Response response = createCourier(courier);
        response.then()
                .assertThat()
                .statusCode(400)
                .and()
                .assertThat()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Add a courier with a null login")
    public void createCourierWithNullLogin() {
        String incorrectLogin = null;
        Courier courier = new Courier(name, incorrectLogin, password);

        Response response = createCourier(courier);
        response.then()
                .assertThat()
                .statusCode(400)
                .and()
                .assertThat()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Add a courier with an empty password")
    public void createCourierWithEmptyPassword() {
        String incorrectPassword = "";
        Courier courier = new Courier(name, login, incorrectPassword);

        Response response = createCourier(courier);
        response.then()
                .assertThat()
                .statusCode(400)
                .and()
                .assertThat()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }


    @Test
    @DisplayName("Add a courier with a null password")
    public void createCourierWithNullPassword() {
        String incorrectPassword = null;
        Courier courier = new Courier(name, login, incorrectPassword);

        Response response = createCourier(courier);
        response.then()
                .assertThat()
                .statusCode(400)
                .and()
                .assertThat()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание двух одинаковых курьеров")
    public void createTwoSameCouriers() {
        Courier courier = new Courier(name, login, password);
        createCourier(courier);

        Response response = createCourier(courier);
        id = loginCourier(courier).then().extract().path("id").toString();
        response.then()
                .assertThat()
                .statusCode(409)
                .and()
                .assertThat()
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @After
    public void deleteCourier() {
        if (id != null) {
            deleteCourier(id);
        }
    }
}
