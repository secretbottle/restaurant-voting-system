package ru.voting.restaurant_voting_system.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.voting.restaurant_voting_system.AuthorizedUser;
import ru.voting.restaurant_voting_system.model.Vote;
import ru.voting.restaurant_voting_system.to.BaseTo;
import ru.voting.restaurant_voting_system.util.VoteUtil;
import ru.voting.restaurant_voting_system.web.json.JsonUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.voting.restaurant_voting_system.RestaurantTestData.RESTAURANT_1;
import static ru.voting.restaurant_voting_system.RestaurantTestData.RESTAURANT_2;
import static ru.voting.restaurant_voting_system.TestUtil.readFromJson;
import static ru.voting.restaurant_voting_system.TestUtil.userHttpBasic;
import static ru.voting.restaurant_voting_system.UserTestData.USER_1;
import static ru.voting.restaurant_voting_system.VoteTestData.getNew;

class UserVotingControllerTest extends AbstractTestController {
    private static final String REST_URL = UserVotingController.REST_URL + "/";

    @Autowired
    UserVotingController controller;

    @Test
    void create() throws Exception {
        Vote newVote = getNew();
        int restaurantId = newVote.getRestaurant().getId();
        BaseTo newBaseTo = new BaseTo(restaurantId);
        ResultActions action = perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newBaseTo))
                .with(userHttpBasic(USER_1)));
        Vote created = readFromJson(action, Vote.class);
        int newId = created.getId();
        newVote.setId(newId);
        assertEquals(newVote, created);
    }

    @Test
    void updateVote() throws Exception {
        VoteUtil.setClock("2020-06-06T10:00:00Z");
        BaseTo votedRestaurant = new BaseTo(RESTAURANT_1.getId());
        AuthorizedUser authUser = new AuthorizedUser(USER_1);
        Vote created = controller.createOrUpdate(votedRestaurant, authUser);
        BaseTo updatedRestaurant = new BaseTo(RESTAURANT_2.getId());
        ResultActions action = perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedRestaurant))
                .with(userHttpBasic(USER_1)));
        Vote updated = readFromJson(action, Vote.class);
        assertEquals(updated, created);
    }

    @Test
    void updateVoteOnTimeException() throws Exception {
        VoteUtil.setClock("2020-06-06T13:00:00Z");
        BaseTo votedRestaurant = new BaseTo(RESTAURANT_1.getId());
        AuthorizedUser authUser = new AuthorizedUser(USER_1);
        controller.createOrUpdate(votedRestaurant, authUser);
        BaseTo updatedRestaurant = new BaseTo(RESTAURANT_2.getId());
        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedRestaurant))
                .with(userHttpBasic(USER_1)))
                .andExpect(status().is5xxServerError());
    }

}