package stellarBurger.igredients;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import stellarBurger.BaseSpecification;

import static io.restassured.RestAssured.given;

public class IngredientsApi extends BaseSpecification {

    private static final String INGREDIENTS_PATH = "/api/ingredients";

    @Step("Получаем список доступных ингредиентов")
    public Response getIngredients() {
        return given()
                .spec(getBaseURI())
                .get(INGREDIENTS_PATH);
    }
}