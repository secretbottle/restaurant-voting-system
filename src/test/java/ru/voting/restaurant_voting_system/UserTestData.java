package ru.voting.restaurant_voting_system;

import ru.voting.restaurant_voting_system.model.Role;
import ru.voting.restaurant_voting_system.model.User;

import static ru.voting.restaurant_voting_system.model.AbstractBaseEntity.START_SEQ;

public class UserTestData {

    public static final int USER_ID = START_SEQ;

    public static final User USER_1 = new User(USER_ID, "User1", "user1@email", "password", Role.USER);
    public static final User USER_2 = new User(USER_ID + 1, "User1", "user2@email", "password", Role.USER);
    public static final User ADMIN_USER = new User(USER_ID + 2, "Admin", "admin@email", "admin", Role.ADMIN);

    public static User getNew() {
        return new User("New User", "NewUser@mail.com", "password", Role.USER);
    }
}
