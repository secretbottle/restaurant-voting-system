package ru.voting.restaurant_voting_system.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.voting.restaurant_voting_system.model.Dish;
import ru.voting.restaurant_voting_system.util.exception.NotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.voting.restaurant_voting_system.DishTestData.*;
import static ru.voting.restaurant_voting_system.RestaurantTestData.RESTAURANT_1;

class AdminDishControllerTest extends AbstractTestController{

    @Autowired
    AdminDishController controller;

    @Test
    void save() {
        Dish newDish = getNew();
        Dish created = controller.save(newDish);
        int newId = created.getId();
        newDish.setId(newId);
        assertEquals(newDish, created);
    }

    @Test
    void get() {
        Dish actual = controller.get(DISH_1.getId());
        DISH_MATCHER.assertMatch(actual, DISH_1);
    }

    @Test
    void getNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> controller.get(NOT_FOUND));
    }

    @Test
    void delete() {
        controller.delete(DISH_2.getId());
        assertThrows(NotFoundException.class, () -> controller.get(DISH_2.getId()));
    }

    @Test
    void deleteNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> controller.delete(NOT_FOUND));
    }

    @Test
    void getAllByIdRestaurant() {
        DISH_MATCHER.assertMatch(controller.getAllByIdRestaurant(RESTAURANT_1.getId()), LIST_ALL_RESTAURANT_1_DISHES);
    }
}