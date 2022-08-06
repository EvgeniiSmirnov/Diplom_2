package stellarBurger.user;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;

public class UserSteps {

    @Step("Генерация полных данных юзера")
    public UserInfo getRandomUserInfo() {
        String email = RandomStringUtils.randomAlphanumeric(5) + "@gmail.com";
        String password = RandomStringUtils.randomAlphanumeric(7);
        String name = RandomStringUtils.randomAlphabetic(7);
        return new UserInfo(email, password, name);
    }

    @Step("Генерация данных юзера без емейла")
    public UserInfo getRandomUserInfoWithoutEmail() {
        String email = null;
        String password = RandomStringUtils.randomAlphanumeric(7);
        String name = RandomStringUtils.randomAlphabetic(7);
        return new UserInfo(email, password, name);
    }

    @Step("Генерация данных юзера без пароля")
    public UserInfo getRandomUserInfoWithoutPassword() {
        String email = RandomStringUtils.randomAlphanumeric(5) + "@gmail.com";
        String password = null;
        String name = RandomStringUtils.randomAlphabetic(7);
        return new UserInfo(email, password, name);
    }

    @Step("Генерация данных юзера без имени")
    public UserInfo getRandomUserInfoWithoutName() {
        String email = RandomStringUtils.randomAlphanumeric(5) + "@gmail.com";
        String password = RandomStringUtils.randomAlphanumeric(7);
        String name = null;
        return new UserInfo(email, password, name);
    }

    @Step("Получение токена юзера")
    public String getAccessToken(Response response) {
        return response.body().jsonPath().getString("accessToken");
    }
}