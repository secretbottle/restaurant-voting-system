package ru.voting.restaurant_voting_system;

import ru.voting.restaurant_voting_system.model.Vote;

import java.time.LocalDateTime;
import java.time.Month;

import static java.time.LocalDateTime.of;
import static ru.voting.restaurant_voting_system.RestaurantTestData.RESTAURANT_1;
import static ru.voting.restaurant_voting_system.RestaurantTestData.RESTAURANT_3;
import static ru.voting.restaurant_voting_system.UserTestData.USER_1;
import static ru.voting.restaurant_voting_system.UserTestData.USER_2;
import static ru.voting.restaurant_voting_system.model.AbstractBaseEntity.START_SEQ;

public class VoteTestData {

    public static final TestMatcher<Vote> VOTE_MATCHER = TestMatcher.usingFieldsWithIgnoringAssertions(Vote.class, "");

    public static final int NOT_FOUND = 10;
    public static final int VOTE_ID = START_SEQ + 14;
    public static final LocalDateTime ON_TIME = of(2020, Month.JANUARY, 30, 9, 0);
    public static final LocalDateTime TOO_LATE = of(2020, Month.JANUARY, 30, 13, 0);


    public static final Vote VOTE_1 = new Vote(VOTE_ID + 1, USER_1, RESTAURANT_1, of(2020, Month.JANUARY, 30, 10, 0));
    public static final Vote VOTE_2 = new Vote(VOTE_ID + 2, USER_2, RESTAURANT_3, of(2020, Month.JANUARY, 30, 10, 0));

    public static Vote getNew() {
        return new Vote(USER_1, RESTAURANT_1, LocalDateTime.now());
    }

    public static Vote getUpdated(LocalDateTime voteTime) {
        Vote updated = new Vote(VOTE_1.getId(), VOTE_1.getUser(), VOTE_1.getRestaurant(), voteTime);
        updated.setRestaurant(RESTAURANT_3);
        return updated;
    }
}
