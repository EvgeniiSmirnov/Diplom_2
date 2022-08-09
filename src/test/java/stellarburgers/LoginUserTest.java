package stellarburgers;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import stellarburgers.client.UserApi;
import stellarburgers.model.UserInfo;
import stellarburgers.steps.UserSteps;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.assertEquals;

public class LoginUserTest {

    public static final String INCORRECT_DATA_MESSAGE = "email or password are incorrect";

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
    @Description("Позитивная проверка. Логиним юзера с корректными емейлом и паролем")
    public void loginUser() {
        userInfo = new UserInfo(userInfo.getEmail(), userInfo.getPassword());
        Response responseLogin = userApi.loginUser(userInfo);

        assertEquals(SC_OK, responseLogin.statusCode());
        assertEquals("true", responseLogin.body().jsonPath().getString("success"));
    }

    @Test
    @Description("Негативная проверка. Логиним юзера с неправильной почтой")
    public void loginUserWithBadEmail() {
        userInfo = new UserInfo(userInfo.getEmail() + "0", userInfo.getPassword());
        Response responseLogin = userApi.loginUser(userInfo);

        assertEquals(SC_UNAUTHORIZED, responseLogin.statusCode());
        assertEquals("false", responseLogin.body().jsonPath().getString("success"));
        assertEquals(INCORRECT_DATA_MESSAGE, responseLogin.body().jsonPath().getString("message"));
    }

    @Test
    @Description("Негативная проверка. Логиним юзера с неправильным паролем")
    public void loginUserWithBadPassword() {
        userInfo = new UserInfo(userInfo.getEmail(), userInfo.getPassword() + "0");
        Response responseLogin = userApi.loginUser(userInfo);

        assertEquals(SC_UNAUTHORIZED, responseLogin.statusCode());
        assertEquals("false", responseLogin.body().jsonPath().getString("success"));
        assertEquals(INCORRECT_DATA_MESSAGE, responseLogin.body().jsonPath().getString("message"));
    }
}