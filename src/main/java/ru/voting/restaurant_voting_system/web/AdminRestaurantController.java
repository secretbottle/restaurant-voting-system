package ru.voting.restaurant_voting_system.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import ru.voting.restaurant_voting_system.model.Restaurant;
import ru.voting.restaurant_voting_system.repository.RestaurantRepository;

import java.util.List;

import static ru.voting.restaurant_voting_system.util.ValidationUtil.checkNotFoundWithId;

@RestController
@RequestMapping(value = AdminRestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminRestaurantController {
    private final Logger log = LoggerFactory.getLogger(getClass());
    static final String REST_URL = "/rest/admin/restaurants";
    private final RestaurantRepository repository;

    public AdminRestaurantController(RestaurantRepository repository) {
        this.repository = repository;
    }

    @CacheEvict(value = "restaurant", allEntries = true)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public Restaurant save(@RequestBody Restaurant restaurant) {
        Assert.notNull(restaurant, "restaurant must not be null");
        log.info("save restaurant {} ", restaurant);
        return repository.save(restaurant);
    }

    @GetMapping("/{id}")
    public Restaurant get(@PathVariable int id) {
        log.info("get restaurant with id {} ", id);
        return checkNotFoundWithId(repository.findById(id).orElse(null), id);
    }

    @CacheEvict(value = "restaurant", allEntries = true)
    @DeleteMapping("{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete restaurant with id {} ", id);
        checkNotFoundWithId(repository.delete(id) != 0, id);
    }

    @Cacheable("restaurant")
    @GetMapping
    public List<Restaurant> getAll() {
        log.info("getAll restaurants");
        return repository.findAll();
    }

}
