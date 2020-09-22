package ru.voting.restaurant_voting_system.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.voting.restaurant_voting_system.AuthorizedUser;
import ru.voting.restaurant_voting_system.model.Dish;
import ru.voting.restaurant_voting_system.model.Restaurant;
import ru.voting.restaurant_voting_system.model.Vote;
import ru.voting.restaurant_voting_system.repository.DishRepository;
import ru.voting.restaurant_voting_system.repository.RestaurantRepository;
import ru.voting.restaurant_voting_system.repository.UserRepository;
import ru.voting.restaurant_voting_system.repository.VoteRepository;
import ru.voting.restaurant_voting_system.to.BaseTo;
import ru.voting.restaurant_voting_system.util.VoteUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.voting.restaurant_voting_system.util.ValidationUtil.checkVoteOnTime;

@RestController
@RequestMapping(value = UserVotingController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class UserVotingController {
    private final Logger log = LoggerFactory.getLogger(getClass());
    static final String REST_URL = "/rest/profile/votes";


    private final DishRepository dishRepository;
    private final RestaurantRepository restaurantRepository;
    private final VoteRepository voteRepository;
    private final UserRepository userRepository;

    public UserVotingController(DishRepository dishRepository, RestaurantRepository restaurantRepository, VoteRepository voteRepository, UserRepository userRepository) {
        this.dishRepository = dishRepository;
        this.restaurantRepository = restaurantRepository;
        this.voteRepository = voteRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public Vote createOrUpdate(@RequestBody BaseTo restaurantId, @AuthenticationPrincipal AuthorizedUser authUser) {
        LocalDate date = LocalDate.now();
        int userId = authUser.getId();
        Vote vote = voteRepository.findByUserIdAndDate(userId, date);
        Restaurant restaurant = restaurantRepository.getOne(restaurantId.getId());

        if (vote == null) {
            vote = new Vote(restaurant, date);
            vote.setUser(userRepository.getOne(userId));
        } else {
            checkVoteOnTime(LocalTime.now(VoteUtil.getClock()));
            vote.setDate(date);
            vote.setRestaurant(restaurant);
        }
        log.info("save vote {} for restaurant id{} by User id{}", vote, restaurant.getId(), userId);
        return voteRepository.save(vote);
    }

    @Cacheable("dish")
    @GetMapping("/restaurants/{id}/dishes")
    public List<Dish> getAllByIdRestaurant(@PathVariable int id) {
        log.info("getAllByIdRestaurant");
        return dishRepository.findAllByRestaurantId(id);
    }

    @Cacheable("restaurant")
    @GetMapping("/restaurants")
    public List<Restaurant> getAll() {
        log.info("getAll restaurants");
        return restaurantRepository.findAll();
    }

}