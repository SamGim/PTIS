package com.thewayhome.ptis.core.service;

import com.thewayhome.ptis.core.repository.IdSequenceRepository;
import com.thewayhome.ptis.core.repository.RestaurantProcessRepository;
import com.thewayhome.ptis.core.repository.RestaurantRepository;
import com.thewayhome.ptis.core.entity.IdSequence;
import com.thewayhome.ptis.core.entity.Restaurant;
import com.thewayhome.ptis.core.entity.RestaurantProcess;
import com.thewayhome.ptis.core.dto.request.RestaurantRegisterReqDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class RestaurantService {
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private IdSequenceRepository idSequenceRepository;
    @Autowired
    private RestaurantProcessRepository restaurantProcessRepository;

    public Restaurant saveRestaurant(RestaurantRegisterReqDto req) {
        Restaurant restaurant = restaurantRepository.findByRestaurantId(req.getRestaurantId()).orElse(new Restaurant());


        // ID
        if (restaurant.getId() == null) {
            IdSequence idSequence = idSequenceRepository.findById("RESTAURANT")
                    .orElse(new IdSequence("RESTAURANT", 0L));
            Long nextId = idSequence.getNextId();

            idSequence.setNextId(nextId + 1);
            idSequenceRepository.save(idSequence);

            restaurant.setId(String.format("%012d", nextId + 1));

            restaurant.setCreatedAt(LocalDateTime.now());
            restaurant.setCreatedBy(req.getOperatorId());
        }

        // DATA
        restaurant.setRestaurantId(req.getRestaurantId());
        restaurant.setRestaurantName(req.getRestaurantName());
        restaurant.setRestaurantAddress(req.getRestaurantAddress());
        restaurant.setRestaurantPosX(req.getRestaurantPosX());
        restaurant.setRestaurantPosY(req.getRestaurantPosY());
        restaurant.setRestaurantType(req.getRestaurantType());

        // DB
        restaurant.setUpdatedAt(LocalDateTime.now());
        restaurant.setUpdatedBy(req.getOperatorId());


        // processDB
        RestaurantProcess restaurantProcess = restaurantProcessRepository.findById(restaurant.getId())
                .orElse(new RestaurantProcess());

        if (restaurantProcess.getId() == null) {
            restaurantProcess.setId(restaurant.getId());
            restaurantProcess.setCreatedAt(LocalDateTime.now());
            restaurantProcess.setCreatedBy(req.getOperatorId());
            restaurantProcess.setFirstGatheringDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
            restaurantProcess.setGatheringStatusCode("01");
        }
        if (restaurant.getRestaurantPosX() == null || restaurant.getRestaurantPosY() == null) {
            restaurantProcess.setGatheringStatusCode("02");
        }

        restaurantProcess.setUpdatedAt(LocalDateTime.now());
        restaurantProcess.setUpdatedBy(req.getOperatorId());
        restaurantProcess.setLastGatheringDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));

        // ID
        restaurantProcessRepository.save(restaurantProcess);

        return restaurantRepository.save(restaurant);
    }
}
