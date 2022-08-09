package stellarburgers;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import stellarburgers.client.OrdersApi;
import stellarburgers.client.UserApi;
import stellarburgers.model.UserInfo;
import stellarburgers.steps.UserSteps;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.assertEquals;

public class GetOrdersTest {

    public static final String CONFLICT_MESSAGE = "You should be authorised";

    UserApi userApi = new UserApi();
    UserSteps userSteps = new UserSteps();
    OrdersApi ordersApi = new OrdersApi();

    UserInfo userInfo;
    String authToken;

    @After
    public void clearData() {
        if (authToken != null) {
            userApi.deleteUser(authToken);
        }
    }

    @Test
    @Description("Позитивная проверка. Юзер авторизован. Получаем список заказов")
    public void getOrdersAuthUser() {
        userInfo = userSteps.getRandomUserInfo();
        Response response = userApi.createUser(userInfo);
        assertEquals(SC_OK, response.statusCode());
        authToken = userSteps.getAccessToken(response);

        Response responseOrder = ordersApi.getOrders(authToken);
        assertEquals(SC_OK, responseOrder.statusCode());
        assertEquals("true", responseOrder.body().jsonPath().getString("success"));
    }

    @Test
    @Description("Негативная проверка. Юзер не авторизован. Получаем список заказов")
    public void getOrdersNotAuthUserTest() {
        Response responseOrder = ordersApi.getOrders("123");
        assertEquals(SC_UNAUTHORIZED, responseOrder.statusCode());
        assertEquals("false", responseOrder.body().jsonPath().getString("success"));
        assertEquals(CONFLICT_MESSAGE, responseOrder.body().jsonPath().getString("message"));
    }
}