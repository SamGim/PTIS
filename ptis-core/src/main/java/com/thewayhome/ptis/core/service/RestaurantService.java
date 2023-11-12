package com.thewayhome.ptis.core.service;

import com.thewayhome.ptis.core.vo.RestaurantVo;
import com.thewayhome.ptis.core.vo.RestaurantProcessVo;
import com.thewayhome.ptis.core.dto.request.RestaurantRegisterRequestDto;
import com.thewayhome.ptis.core.entity.*;
import com.thewayhome.ptis.core.repository.IdSequenceRepository;
import com.thewayhome.ptis.core.repository.RestaurantProcessRepository;
import com.thewayhome.ptis.core.repository.RestaurantRepository;
import com.thewayhome.ptis.core.util.RestaurantEntityVoConverter;
import com.thewayhome.ptis.core.util.RestaurantProcessEntityVoConverter;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final IdSequenceRepository idSequenceRepository;
    private final RestaurantProcessRepository restaurantProcessRepository;
    private final RestaurantEntityVoConverter restaurantEntityDtoConverter;
    private final RestaurantProcessEntityVoConverter restaurantProcessEntityDtoConverter;

    public Optional<Restaurant> findById(String id) {
        return restaurantRepository.findById(id);
    }
    public Optional<RestaurantProcess> findProcessById(String id) {
        return restaurantProcessRepository.findById(id);
    }

    public Restaurant saveRestaurant(RestaurantVo req) {
        Restaurant entity = restaurantEntityDtoConverter.toEntity(req, req.getOperatorId());
        return restaurantRepository.save(entity);
    }

    public RestaurantProcess saveRestaurantProcess(RestaurantProcessVo req) {
        RestaurantProcess entity = restaurantProcessEntityDtoConverter.toEntity(req, req.getOperatorId());
        return restaurantProcessRepository.save(entity);
    }

    @Transactional
    public Restaurant registerRestaurant(RestaurantRegisterRequestDto req) {
        // ID
        IdSequence idSequence = idSequenceRepository.findById("RESTAURANT")
                .orElse(new IdSequence("RESTAURANT", 0L));
        Long id = idSequence.getNextId() + 1;

        idSequence.setNextId(id);
        idSequenceRepository.save(idSequence);

        req.setId(String.format("%012d", id));

        // Restaurant
        RestaurantVo restaurantVo = RestaurantVo.builder()
                .id(req.getId())
                .restaurantId(req.getRestaurantId())
                .restaurantName(req.getRestaurantName())
                .restaurantType(req.getRestaurantType())
                .restaurantAddress(req.getRestaurantAddress())
                .restaurantPosX(req.getRestaurantPosX())
                .restaurantPosY(req.getRestaurantPosY())
                .operatorId(req.getOperatorId())
                .build();

        Restaurant restaurant = this.saveRestaurant(restaurantVo);

        // RestaurantProcess
        RestaurantProcessVo restaurantProcessVo = RestaurantProcessVo.builder()
                .id(req.getId())
                .restaurant(restaurantVo)
                .firstGatheringDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                .lastGatheringDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                .gatheringStatusCode("01")
                .operatorId(req.getOperatorId())
                .build();

        this.saveRestaurantProcess(restaurantProcessVo);

        return restaurant;
    }
}
