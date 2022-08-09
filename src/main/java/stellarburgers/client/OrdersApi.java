package stellarburgers.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import stellarburgers.client.BaseSpecification;
import stellarburgers.model.Ingredients;

import static io.restassured.RestAssured.given;

public class OrdersApi extends BaseSpecification {

    private static final String ORDERS_PATH = "/api/orders";

    @Step("Создаём заказ")
    public Response createOrder(String authToken, Ingredients ingredients) {
        return given()
                .spec(getBaseURI())
                .header("Authorization", authToken)
                .body(ingredients)
                .post(ORDERS_PATH);
    }

    @Step("Получаем список заказов юзера")
    public Response getOrders(String authToken) {
        return given()
                .spec(getBaseURI())
                .header("Authorization", authToken)
                .get(ORDERS_PATH);
    }
}