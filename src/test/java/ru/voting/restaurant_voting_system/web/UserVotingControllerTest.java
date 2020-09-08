package ru.voting.restaurant_voting_system.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.voting.restaurant_voting_system.AuthorizedUser;
import ru.voting.restaurant_voting_system.VoteTestData;
import ru.voting.restaurant_voting_system.model.Vote;
import ru.voting.restaurant_voting_system.to.BaseTo;
import ru.voting.restaurant_voting_system.util.exception.VoteException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.voting.restaurant_voting_system.VoteTestData.ON_TIME;
import static ru.voting.restaurant_voting_system.VoteTestData.TOO_LATE;

class UserVotingControllerTest extends AbstractTestController {

    @Autowired
    UserVotingController controller;

    @Test
    void create() {
        Vote newVote = VoteTestData.getNew();
        int restaurantId = newVote.getRestaurant().getId();
        Vote created = controller.createOrUpdateVote(new BaseTo(restaurantId), new AuthorizedUser(newVote.getUser()));
        int newId = created.getId();
        newVote.setId(newId);
        assertEquals(newVote, created);
    }

    @Test
    void updateVote() {
        Vote oldVote = VoteTestData.getUpdated(ON_TIME);
        int restaurantId = oldVote.getRestaurant().getId();
        Vote updated = controller.createOrUpdateVote(new BaseTo(restaurantId), new AuthorizedUser(oldVote.getUser()));
        assertEquals(updated, oldVote);
    }

    @Test
    void updateVoteOnTimeException() {
        Vote oldVote = VoteTestData.getUpdated(TOO_LATE);
        int restaurantId = oldVote.getRestaurant().getId();
        assertThrows(VoteException.class, () -> controller.createOrUpdateVote(new BaseTo(restaurantId), new AuthorizedUser(oldVote.getUser())));
    }

}