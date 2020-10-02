package ru.voting.restaurant_voting_system.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.voting.restaurant_voting_system.model.Dish;
import ru.voting.restaurant_voting_system.util.exception.NotFoundException;
import ru.voting.restaurant_voting_system.web.json.JsonUtil;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.voting.restaurant_voting_system.DishTestData.*;
import static ru.voting.restaurant_voting_system.RestaurantTestData.RESTAURANT_1;
import static ru.voting.restaurant_voting_system.TestUtil.readFromJson;
import static ru.voting.restaurant_voting_system.TestUtil.userHttpBasic;
import static ru.voting.restaurant_voting_system.UserTestData.ADMIN_USER;

class AdminDishControllerTest extends AbstractTestController {
    private static final String REST_URL = AdminDishController.REST_URL + "/";

    @Autowired
    AdminDishController controller;

    @Test
    void create() throws Exception {
        Dish newDish = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newDish))
                .with(userHttpBasic(ADMIN_USER)));
        Dish created = readFromJson(action, Dish.class);
        int newId = created.getId();
        newDish.setId(newId);
        DISH_MATCHER.assertMatch(newDish, created);
    }

    @Test
    void update() throws Exception {
        Dish updated = getUpdated();
        int id = DISH_1.getId();
        perform(MockMvcRequestBuilders.put(REST_URL + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated))
                .with(userHttpBasic(ADMIN_USER)))
                .andExpect(status().isNoContent());
        DISH_MATCHER.assertMatch(controller.get(id), updated);
    }

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + DISH_1.getId())
                .with(userHttpBasic(ADMIN_USER)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(DISH_1));
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + NOT_FOUND)
                .with(userHttpBasic(ADMIN_USER)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + DISH_1.getId())
                .with(userHttpBasic(ADMIN_USER)))
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> controller.get(DISH_1.getId()));
    }

    @Test
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + NOT_FOUND)
                .with(userHttpBasic(ADMIN_USER)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void getAllByIdRestaurant() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "?id=" + RESTAURANT_1.getId())
                .with(userHttpBasic(ADMIN_USER)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(LIST_ALL_RESTAURANT_1_DISHES));
    }

}