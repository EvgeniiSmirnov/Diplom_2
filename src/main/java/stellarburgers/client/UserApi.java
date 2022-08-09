package stellarburgers.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import stellarburgers.client.BaseSpecification;
import stellarburgers.model.UserInfo;

import static io.restassured.RestAssured.given;

public class UserApi extends BaseSpecification {

    private static final String REGISTER_USER_PATH = "/api/auth/register";
    private static final String LOGIN_USER_PATH = "/api/auth/login";
    private static final String DEFAULT_USER_PATH = "/api/auth/user";

    @Step("Создание юзера")
    public Response createUser(UserInfo userInfo) {
        return given()
                .spec(getBaseURI())
                .body(userInfo)
                .when()
                .post(REGISTER_USER_PATH);
    }

    @Step("Авторизация юзера")
    public Response loginUser(UserInfo userInfo) {
        return given()
                .spec(getBaseURI())
                .body(userInfo)
                .post(LOGIN_USER_PATH);
    }

    @Step("Изменение данных юзера")
    public Response changeUser(UserInfo userInfo, String authToken) {
        return given()
                .spec(getBaseURI())
                .header("Authorization", authToken)
                .body(userInfo)
                .patch(DEFAULT_USER_PATH);
    }

    @Step("Удаление юзера")
    public Response deleteUser(String authToken) {
        return given()
                .spec(getBaseURI())
                .header("Authorization", authToken)
                .delete(DEFAULT_USER_PATH);
    }
}
