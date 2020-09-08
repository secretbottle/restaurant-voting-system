package ru.voting.restaurant_voting_system.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.voting.restaurant_voting_system.model.Restaurant;
import ru.voting.restaurant_voting_system.util.exception.NotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.voting.restaurant_voting_system.RestaurantTestData.*;


class AdminRestaurantControllerTest extends AbstractTestController {

    @Autowired
    private AdminRestaurantController controller;

    @Test
    void save() throws Exception {
        Restaurant newRestaurant = getNew();
        Restaurant created = controller.save(newRestaurant);
        int newId = created.getId();
        newRestaurant.setId(newId);
        assertEquals(newRestaurant, created);
    }

    @Test
    void get() throws Exception {
        Restaurant actual = controller.get(RESTAURANT_1.getId());
        RESTAURANT_MATCHER.assertMatch(actual, RESTAURANT_1);
    }

    @Test
    void getNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> controller.get(NOT_FOUND));
    }

    @Test
    void delete() throws Exception {
        controller.delete(RESTAURANT_2.getId());
        assertThrows(NotFoundException.class, () -> controller.get(RESTAURANT_2.getId()));
    }

    @Test
    void deleteNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> controller.delete(NOT_FOUND));
    }

    @Test
    void getAll() throws Exception {
        RESTAURANT_MATCHER.assertMatch(controller.getAll(), LIST_ALL_RESTAURANT);
    }
}