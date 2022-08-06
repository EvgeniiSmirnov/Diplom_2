package stellarBurger;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import stellarBurger.user.UserApi;
import stellarBurger.user.UserInfo;
import stellarBurger.user.UserSteps;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.assertEquals;

public class ChangeUserDataTest {

    public static final String CONFLICT_MESSAGE = "You should be authorised";

    UserApi userApi = new UserApi();
    UserSteps userSteps = new UserSteps();
    UserInfo userInfo;
    String authToken;

    @Before
    public void createData() {
        userInfo = userSteps.getRandomUserInfo();
        Response response = userApi.createUser(userInfo);
        assertEquals(SC_OK, response.statusCode());
        authToken = userSteps.getAccessToken(response);
    }

    @After
    public void clearData() {
        if (authToken != null) {
            userApi.deleteUser(authToken);
        }
    }

    @Test
    @Description("Позитивная проверка. Юзер авторизован. Меняем емейл юзера")
    public void successChangeUserEmail() {
        Response responseChange = userApi.changeUser(new UserInfo(userInfo.getEmail() + "0", userInfo.getPassword(), userInfo.getName()), authToken);
        assertEquals(SC_OK, responseChange.statusCode());
        assertEquals("true", responseChange.body().jsonPath().getString("success"));
    }

    @Test
    @Description("Негативная проверка. Юзер не авторизован. Меняем емейл юзера")
    public void failedChangeUserEmail() {
        Response responseChange = userApi.changeUser(new UserInfo(userInfo.getEmail() + "0", userInfo.getPassword(), userInfo.getName()), "");
        assertEquals(SC_UNAUTHORIZED, responseChange.statusCode());
        assertEquals("false", responseChange.body().jsonPath().getString("success"));
        assertEquals(CONFLICT_MESSAGE, responseChange.body().jsonPath().getString("message"));
    }

    @Test
    @Description("Позитивная проверка. Юзер авторизован. Меняем пароль юзера")
    public void successChangeUserPassword() {
        Response responseChange = userApi.changeUser(new UserInfo(userInfo.getEmail(), userInfo.getPassword() + "0", userInfo.getName()), authToken);
        assertEquals(SC_OK, responseChange.statusCode());
        assertEquals("true", responseChange.body().jsonPath().getString("success"));
    }

    @Test
    @Description("Негативная проверка. Юзер не авторизован. Меняем пароль юзера")
    public void failedChangeUserPassword() {
        Response responseChange = userApi.changeUser(new UserInfo(userInfo.getEmail(), userInfo.getPassword() + "0", userInfo.getName()), "");
        assertEquals(SC_UNAUTHORIZED, responseChange.statusCode());
        assertEquals("false", responseChange.body().jsonPath().getString("success"));
        assertEquals(CONFLICT_MESSAGE, responseChange.body().jsonPath().getString("message"));
    }

    @Test
    @Description("Позитивная проверка. Юзер авторизован. Меняем имя юзера")
    public void successChangeUserName() {
        Response responseChange = userApi.changeUser(new UserInfo(userInfo.getEmail(), userInfo.getPassword(), userInfo.getName() + "0"), authToken);
        assertEquals(SC_OK, responseChange.statusCode());
        assertEquals("true", responseChange.body().jsonPath().getString("success"));
    }

    @Test
    @Description("Негативная проверка. Юзер не авторизован. Меняем имя юзера")
    public void failedChangeUserName() {
        Response responseChange = userApi.changeUser(new UserInfo(userInfo.getEmail(), userInfo.getPassword(), userInfo.getName() + "0"), "");
        assertEquals(SC_UNAUTHORIZED, responseChange.statusCode());
        assertEquals("false", responseChange.body().jsonPath().getString("success"));
        assertEquals(CONFLICT_MESSAGE, responseChange.body().jsonPath().getString("message"));
    }

}