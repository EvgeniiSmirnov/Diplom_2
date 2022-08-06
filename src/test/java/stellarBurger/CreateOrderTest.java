package stellarBurger;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import stellarBurger.igredients.Ingredients;
import stellarBurger.igredients.IngredientsSteps;
import stellarBurger.user.UserApi;
import stellarBurger.user.UserInfo;
import stellarBurger.user.UserSteps;

import java.util.ArrayList;
import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CreateOrderTest {

    public static final String CONFLICT_MESSAGE = "Ingredient ids must be provided";

    UserApi userApi = new UserApi();
    UserSteps userSteps = new UserSteps();
    UserInfo userInfo;

    OrdersApi ordersApi = new OrdersApi();
    IngredientsSteps ingredientsSteps = new IngredientsSteps();
    Ingredients ingredients;

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
    @Description("Позитивная проверка. Юзер авторизован. Создаём заказ с ингредиентами")
    public void createOrderAuthUser() {
        ingredients = new Ingredients(ingredientsSteps.getListOfIngredient());
        Response responseOrder = ordersApi.createOrder(authToken, ingredients);

        assertEquals(SC_OK, responseOrder.statusCode());
        assertEquals("true", responseOrder.body().jsonPath().getString("success"));
        assertNotNull(responseOrder.then().extract().jsonPath().getString("order.number"));
    }

    @Test
    @Description("Негативная проверка. Юзер авторизован. Создаём заказ без ингредиентов")
    public void createEmptyOrderAuthUser() {
        ingredients = new Ingredients(new ArrayList<>());
        Response responseOrder = ordersApi.createOrder(authToken, ingredients);

        assertEquals(SC_BAD_REQUEST, responseOrder.statusCode());
        assertEquals("false", responseOrder.body().jsonPath().getString("success"));
        assertEquals(CONFLICT_MESSAGE, responseOrder.body().jsonPath().getString("message"));
    }

    @Test
    @Description("Позитивная проверка. Юзер не авторизован. Создаём заказ с ингредиентами")
    public void createOrderNotAuthUser() {
        ingredients = new Ingredients(ingredientsSteps.getListOfIngredient());
        Response responseOrder = ordersApi.createOrder("", ingredients);

        assertEquals(SC_OK, responseOrder.statusCode());
        assertEquals("true", responseOrder.body().jsonPath().getString("success"));
        assertNotNull(responseOrder.then().extract().jsonPath().getString("order.number"));
    }

    @Test
    @Description("Негативная проверка. Юзер не авторизован. Создаём заказ без ингредиентов")
    public void createEmptyOrderNotAuthUser() {
        ingredients = new Ingredients(new ArrayList<>());
        Response responseOrder = ordersApi.createOrder("", ingredients);

        assertEquals(SC_BAD_REQUEST, responseOrder.statusCode());
        assertEquals("false", responseOrder.body().jsonPath().getString("success"));
        assertEquals(CONFLICT_MESSAGE, responseOrder.body().jsonPath().getString("message"));
    }

    @Test
    @Description("Негативная проверка. Юзер авторизован. Создаём заказ неверным хешем ингредиентов")
    public void createEmptyOrderAuthUser1() {
        ingredients = new Ingredients(List.of("123", "321"));
        Response responseOrder = ordersApi.createOrder(authToken, ingredients);

        assertEquals(SC_INTERNAL_SERVER_ERROR, responseOrder.statusCode());
    }
}