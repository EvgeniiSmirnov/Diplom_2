package stellarBurger.igredients;

import io.qameta.allure.Step;
import org.apache.commons.lang3.RandomUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IngredientsSteps {

    private static final String INGREDIENT_ID_PATH = "data._id";

    IngredientsApi ingredientsApi = new IngredientsApi();

    @Step("Выбираем случайный игредиент")
    public String getIngredientId() {
        List<String> ingredients = ingredientsApi.getIngredients().then()
                .extract().jsonPath().getList(INGREDIENT_ID_PATH);
        return ingredients.get(RandomUtils.nextInt(0, ingredients.size()));
    }

    @Step("Формируем список ингрединтов")
    public List<String> getListOfIngredient() {
        return new ArrayList<>(Arrays.asList(getIngredientId(), getIngredientId()));
    }
}