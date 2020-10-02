package ru.voting.restaurant_voting_system.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.voting.restaurant_voting_system.AuthorizedUser;
import ru.voting.restaurant_voting_system.model.Vote;
import ru.voting.restaurant_voting_system.util.VoteUtil;
import ru.voting.restaurant_voting_system.web.json.JsonUtil;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.voting.restaurant_voting_system.TestUtil.readFromJson;
import static ru.voting.restaurant_voting_system.TestUtil.userHttpBasic;
import static ru.voting.restaurant_voting_system.UserTestData.USER_2;
import static ru.voting.restaurant_voting_system.VoteTestData.*;

class UserVotingControllerTest extends AbstractTestController {
    private static final String REST_URL = UserVotingController.REST_URL + "/";

    @Autowired
    UserVotingController controller;

    @Test
    void create() throws Exception {
        Vote newVote = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newVote))
                .with(userHttpBasic(USER_2)));
        Vote created = readFromJson(action, Vote.class);
        int newId = created.getId();
        newVote.setId(newId);
        VOTE_MATCHER.assertMatch(newVote, created);
    }

    @Test
    void update() throws Exception {
        VoteUtil.setClock("2020-10-02T10:00:00Z");
        Vote created = getNewWithId();
        AuthorizedUser authUser = new AuthorizedUser(USER_2);
        Vote updated = controller.update(created, created.getId(), authUser);
        int id = updated.getId();
        perform(MockMvcRequestBuilders.put(REST_URL + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated))
                .with(userHttpBasic(USER_2)))
                .andExpect(status().isNoContent());
        VOTE_MATCHER.assertMatch(controller.get(id), updated);
    }

    @Test
    void updateVoteOnTimeException() throws Exception {
        VoteUtil.setClock("2020-06-06T13:00:00Z");
        Vote created = getNewWithId();
        AuthorizedUser authUser = new AuthorizedUser(USER_2);
        Vote updated = controller.update(created, created.getId(), authUser);
        int id = updated.getId();
        perform(MockMvcRequestBuilders.put(REST_URL + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated))
                .with(userHttpBasic(USER_2)))
                .andExpect(status().is5xxServerError());
    }

}