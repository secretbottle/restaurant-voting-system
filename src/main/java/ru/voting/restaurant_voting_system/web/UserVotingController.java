package ru.voting.restaurant_voting_system.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.voting.restaurant_voting_system.AuthorizedUser;
import ru.voting.restaurant_voting_system.model.Dish;
import ru.voting.restaurant_voting_system.model.User;
import ru.voting.restaurant_voting_system.model.Vote;
import ru.voting.restaurant_voting_system.repository.DishRepository;
import ru.voting.restaurant_voting_system.repository.UserRepository;
import ru.voting.restaurant_voting_system.repository.VoteRepository;
import ru.voting.restaurant_voting_system.util.VoteUtil;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.voting.restaurant_voting_system.util.ValidationUtil.*;

@RestController
@RequestMapping(value = UserVotingController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class UserVotingController {
    private final Logger log = LoggerFactory.getLogger(getClass());
    static final String REST_URL = "/rest/profile/votes";

    private final DishRepository dishRepository;
    private final VoteRepository voteRepository;
    private final UserRepository userRepository;

    public UserVotingController(DishRepository dishRepository, VoteRepository voteRepository, UserRepository userRepository) {
        this.dishRepository = dishRepository;
        this.voteRepository = voteRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public ResponseEntity<Vote> create(@RequestBody Vote vote, @AuthenticationPrincipal AuthorizedUser authUser) {
        Assert.notNull(vote, "vote must not be null");
        vote.setUser(getUser(authUser));
        checkNew(vote);
        log.info("save vote {} for restaurant id{} by User id{}", vote, vote.getRestaurantId(), vote.getUser().getId());
        Vote created = voteRepository.save(vote);

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @Transactional
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public Vote update(@RequestBody Vote vote, @PathVariable int id, @AuthenticationPrincipal AuthorizedUser authUser) {
        LocalDate date = LocalDate.now();
        int userId = authUser.getId();
        assureIdConsistent(vote, id);
        log.info("update {} for user {}", vote, userId);
        Assert.notNull(vote, "vote must not be null");
        int restaurantId = vote.getRestaurantId();
        Vote voted = voteRepository.findByUserIdAndDate(userId, date);

        if (voted == null) {
            voted = new Vote(restaurantId, date);
            voted.setUser(getUser(authUser));
        } else {
            checkVoteOnTime(LocalTime.now(VoteUtil.getClock()));
            voted.setRestaurantId(vote.getRestaurantId());
        }

        return checkNotFoundWithId(voteRepository.save(voted), id);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Vote get(@PathVariable int id) {
        log.info("get vote with id {} ", id);
        return checkNotFoundWithId(voteRepository.findById(id).orElse(null), id);
    }

    @Cacheable("dish")
    @GetMapping("/restaurants/{id}/dishes")
    public List<Dish> getAllByRestaurantIdToday(@PathVariable int id) {
        log.info("getAllByIdRestaurant");
        return dishRepository.findAllByRestaurantIdToday(id, LocalDate.now());
    }

    private User getUser(AuthorizedUser authUser) {
        return userRepository.findById(authUser.getId()).orElse(null);
    }

}