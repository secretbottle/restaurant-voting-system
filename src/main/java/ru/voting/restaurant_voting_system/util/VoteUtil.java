package ru.voting.restaurant_voting_system.util;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class VoteUtil {
    public static final LocalTime MIN_VOTE_TIME = LocalTime.MIDNIGHT;
    public static final LocalTime MAX_VOTE_TIME = LocalTime.of(11, 0);

    public static boolean isVotedOnTime(LocalTime time) {
        return (time.compareTo(MIN_VOTE_TIME) >= 0) && (time.compareTo(MAX_VOTE_TIME) < 0);
    }

    public static LocalTime toLocalTime(LocalDateTime dateTime) {
        return LocalTime.of(dateTime.getHour(), dateTime.getMinute());
    }
}