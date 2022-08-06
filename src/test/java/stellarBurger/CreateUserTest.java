package stellarBurger;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import stellarBurger.user.UserApi;
import stellarBurger.user.UserInfo;
import stellarBurger.user.UserSteps;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CreateUserTest {

    public static final String CONFLICT_MESSAGE = "User already exists";
    public static final String REQUIRED_FIELDS_MESSAGE = "Email, password and name are required fields";

    UserApi userApi = new UserApi();
    UserSteps userSteps = new UserSteps();
    UserInfo userInfo;
    UserInfo userWithoutName;
    UserInfo userWithoutPassword;
    UserInfo userWithoutEmail;
    String authToken;

    @After
    public void clearData() {
        if (authToken != null) {
            userApi.deleteUser(authToken);
        }
    }

    @Test
    @Description("Позитивная проверка. Создаём нового юзера. Все требуемые поля заполнены")
    public void createNewUser() {
        userInfo = userSteps.getRandomUserInfo();
        Response response = userApi.createUser(userInfo);
        authToken = userSteps.getAccessToken(response);
        assertEquals(SC_OK, response.statusCode());

        assertNotNull(response.body().jsonPath().getString("accessToken"));
        assertNotNull(response.body().jsonPath().getString("refreshToken"));
    }

    @Test
    @Description("Негативная проверка. Нельзя создать двух одинаковых юзеров")
    public void createNewUserTwice() {
        userInfo = userSteps.getRandomUserInfo();
        Response response = userApi.createUser(userInfo);
        authToken = userSteps.getAccessToken(response);
        assertEquals(SC_OK, response.statusCode());

        Response response2 = userApi.createUser(userInfo);
        assertEquals(SC_FORBIDDEN, response2.statusCode());
        assertEquals(CONFLICT_MESSAGE, response2.body().jsonPath().getString("message"));
    }

    @Test
    @Description("Негативная проверка. Нельзя создать юзера без емейла")
    public void createUserWithoutEmail() {
        userWithoutEmail = userSteps.getRandomUserInfoWithoutEmail();
        Response response = userApi.createUser(userWithoutEmail);

        assertEquals(SC_FORBIDDEN, response.statusCode());
        assertEquals(REQUIRED_FIELDS_MESSAGE, response.body().jsonPath().getString("message"));
    }

    @Test
    @Description("Негативная проверка. Нельзя создать юзера без пароля")
    public void createUserWithoutPassword() {
        userWithoutPassword = userSteps.getRandomUserInfoWithoutPassword();
        Response response = userApi.createUser(userWithoutPassword);

        assertEquals(SC_FORBIDDEN, response.statusCode());
        assertEquals(REQUIRED_FIELDS_MESSAGE, response.body().jsonPath().getString("message"));
    }

    @Test
    @Description("Негативная проверка. Нельзя создать юзера без логина")
    public void createUserWithoutLogin() {
        userWithoutName = userSteps.getRandomUserInfoWithoutName();
        Response response = userApi.createUser(userWithoutName);

        assertEquals(SC_FORBIDDEN, response.statusCode());
        assertEquals(REQUIRED_FIELDS_MESSAGE, response.body().jsonPath().getString("message"));
    }
}