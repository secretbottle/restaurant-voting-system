package ru.voting.restaurant_voting_system;

import ru.voting.restaurant_voting_system.model.Dish;

import java.math.BigDecimal;
import java.time.Month;
import java.util.List;

import static java.time.LocalDate.of;
import static ru.voting.restaurant_voting_system.RestaurantTestData.RESTAURANT_2;
import static ru.voting.restaurant_voting_system.model.AbstractBaseEntity.START_SEQ;

public class DishTestData {
    public static TestMatcher<Dish> DISH_MATCHER = TestMatcher.usingFieldsWithIgnoringAssertions(Dish.class, "restaurantId");

    public static final int NOT_FOUND = 10;
    public static final int DISH_ID = START_SEQ + 5;
    public static final int RESTAURANT_2_ID = RESTAURANT_2.getId();

    public static final Dish DISH_1 = new Dish(DISH_ID + 1, "Chefburger", getBigDecimal(19900), of(2020, Month.JANUARY, 30));
    public static final Dish DISH_2 = new Dish(DISH_ID + 2, "Longer", getBigDecimal(9900), of(2020, Month.JANUARY, 30));
    public static final Dish DISH_3 = new Dish(DISH_ID + 3, "Twister", getBigDecimal(6600), of(2020, Month.JANUARY, 30));
    public static final Dish DISH_4 = new Dish(DISH_ID + 4, "Ice coffee", getBigDecimal(9900), of(2020, Month.JANUARY, 30));
    public static final Dish DISH_5 = new Dish(DISH_ID + 5, "Donat", getBigDecimal(9900), of(2020, Month.JANUARY, 30));
    public static final Dish DISH_6 = new Dish(DISH_ID + 6, "Big Mak", getBigDecimal(14900), of(2020, Month.JANUARY, 30));
    public static final Dish DISH_7 = new Dish(DISH_ID + 7, "Whopper", getBigDecimal(13600), of(2020, Month.JANUARY, 30));
    public static final Dish DISH_8 = new Dish(DISH_ID + 8, "Big King", getBigDecimal(15900), of(2020, Month.JANUARY, 30));
    public static final Dish DISH_9 = new Dish(DISH_ID + 9, "Pepsi", getBigDecimal(8900), of(2020, Month.JANUARY, 30));

    public static final List<Dish> LIST_ALL_DISHES = List.of(DISH_1, DISH_2, DISH_3, DISH_4, DISH_5, DISH_6, DISH_7, DISH_8, DISH_9);

    public static final List<Dish> LIST_ALL_RESTAURANT_1_DISHES = List.of(DISH_1, DISH_2, DISH_3);

    public static Dish getNew() {
        return new Dish(null, "New Dish", getBigDecimal(999), of(2020, Month.AUGUST, 1));
    }

    public static Dish getUpdated() {
        Dish updated =  new Dish(DISH_ID + 1, "New Dish", getBigDecimal(999), of(2020, Month.AUGUST, 1));
        updated.setRestaurantId(RESTAURANT_2_ID);
        return updated;
    }

    private static BigDecimal getBigDecimal(int num) {
        return BigDecimal.valueOf(num, 2);
    }

}
