package ru.voting.restaurant_voting_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.voting.restaurant_voting_system.model.Dish;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface DishRepository extends JpaRepository<Dish, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Dish d WHERE d.id=:id")
    int delete(@Param("id") int id);

    @Query("SELECT d FROM Dish d JOIN FETCH d.restaurant WHERE d.restaurant.id=:restaurant_id ORDER BY d.name")
    List<Dish> findAllByRestaurantId(@Param("restaurant_id") int restaurant_id);

    @Query("SELECT d FROM Dish d JOIN FETCH d.restaurant WHERE d.id=:dish_id")
    Dish getDishById(@Param("dish_id") int dish_id);
}
