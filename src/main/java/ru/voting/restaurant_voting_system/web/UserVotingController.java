package ru.voting.restaurant_voting_system.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.voting.restaurant_voting_system.AuthorizedUser;
import ru.voting.restaurant_voting_system.model.Dish;
import ru.voting.restaurant_voting_system.model.Restaurant;
import ru.voting.restaurant_voting_system.model.Vote;
import ru.voting.restaurant_voting_system.repository.CrudUserRepository;
import ru.voting.restaurant_voting_system.repository.DishRepository;
import ru.voting.restaurant_voting_system.repository.RestaurantRepository;
import ru.voting.restaurant_voting_system.repository.VoteRepository;
import ru.voting.restaurant_voting_system.to.BaseTo;

import java.time.LocalDateTime;
import java.util.List;

import static ru.voting.restaurant_voting_system.util.ValidationUtil.checkVoteOnTime;
import static ru.voting.restaurant_voting_system.util.VoteUtil.toLocalTime;

@RestController
@RequestMapping(value = UserVotingController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class UserVotingController {
    private final Logger log = LoggerFactory.getLogger(getClass());
    final static String REST_URL = "/rest/profile/restaurants";

    private final DishRepository dishRepository;
    private final RestaurantRepository restaurantRepository;
    private final VoteRepository voteRepository;
    private final CrudUserRepository userRepository;

    public UserVotingController(DishRepository dishRepository, RestaurantRepository restaurantRepository, VoteRepository voteRepository, CrudUserRepository userRepository) {
        this.dishRepository = dishRepository;
        this.restaurantRepository = restaurantRepository;
        this.voteRepository = voteRepository;
        this.userRepository = userRepository;
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public Vote createOrUpdateVote(@RequestBody BaseTo restaurantId, @AuthenticationPrincipal AuthorizedUser authUser) {
        LocalDateTime dateTime = LocalDateTime.now();
        int userId = authUser.getId();
        Vote vote = voteRepository.findByIdUser(userId);
        Restaurant restaurant = restaurantRepository.getOne(restaurantId.getId());

        if (vote == null) {
            vote = new Vote(restaurant, dateTime);
            vote.setUser(userRepository.getOne(userId));
        } else {
            checkVoteOnTime(toLocalTime(dateTime));
            vote.setDateTime(dateTime);
            vote.setRestaurant(restaurant);
        }
        log.info("save vote {} for restaurant id{} by User id{}", vote, restaurant.getId(), userId);
        return voteRepository.save(vote);
    }

    @Cacheable("dish")
    @GetMapping("{id}")
    public List<Dish> getAllByIdRestaurant(@PathVariable int id) {
        log.info("getAll restaurants");
        return dishRepository.findAllByRestaurantId(id);
    }

    @Cacheable("restaurant")
    @GetMapping
    public List<Restaurant> getAll() {
        log.info("getAll restaurants");
        return restaurantRepository.findAll();
    }
}