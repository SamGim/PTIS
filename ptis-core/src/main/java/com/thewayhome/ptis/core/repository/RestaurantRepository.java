package com.thewayhome.ptis.core.repository;

import com.thewayhome.ptis.core.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, String> {
    Optional<Restaurant> findByRestaurantId(String restaurantId);
}