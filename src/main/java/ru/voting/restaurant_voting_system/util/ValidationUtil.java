package ru.voting.restaurant_voting_system.util;

import ru.voting.restaurant_voting_system.util.exception.NotFoundException;
import ru.voting.restaurant_voting_system.util.exception.VoteException;

import java.time.LocalTime;

public class ValidationUtil {
    private ValidationUtil() {
    }

    public static void checkVoteOnTime(LocalTime time) {
        if (!VoteUtil.isVotedOnTime(time)) {
            throw new VoteException("It's too late, vote can't be changed. You can change your vote until 11:00.");
        }
    }

    public static <T> T checkNotFoundWithId(T object, int id) {
        checkNotFoundWithId(object != null, id);
        return object;
    }

    public static void checkNotFoundWithId(boolean found, int id) {
        checkNotFound(found, "id=" + id);
    }

    public static <T> T checkNotFound(T object, String msg) {
        checkNotFound(object != null, msg);
        return object;
    }

    public static void checkNotFound(boolean found, String arg) {
        if (!found) {
            throw new NotFoundException(arg);
        }
    }

}