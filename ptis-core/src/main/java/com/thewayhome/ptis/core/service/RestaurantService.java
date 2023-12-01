package com.thewayhome.ptis.core.service;

import com.thewayhome.ptis.core.dto.request.RestaurantRegisterProcessRequestDto;
import com.thewayhome.ptis.core.dto.request.RestaurantRegisterRequestDto;
import com.thewayhome.ptis.core.entity.IdSequence;
import com.thewayhome.ptis.core.entity.Restaurant;
import com.thewayhome.ptis.core.entity.RestaurantProcess;
import com.thewayhome.ptis.core.repository.IdSequenceRepository;
import com.thewayhome.ptis.core.repository.RestaurantProcessRepository;
import com.thewayhome.ptis.core.repository.RestaurantRepository;
import com.thewayhome.ptis.core.util.RestaurantEntityVoConverter;
import com.thewayhome.ptis.core.util.RestaurantProcessEntityVoConverter;
import com.thewayhome.ptis.core.vo.RestaurantProcessVo;
import com.thewayhome.ptis.core.vo.RestaurantVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public Restaurant saveRestaurant(RestaurantVo req, String operatorId) {
        Restaurant entity = restaurantEntityDtoConverter.toEntity(req, operatorId);
        return restaurantRepository.save(entity);
    }

    public RestaurantProcess saveRestaurantProcess(RestaurantProcessVo req, String operatorId) {
        RestaurantProcess entity = restaurantProcessEntityDtoConverter.toEntity(req, operatorId);
        return restaurantProcessRepository.save(entity);
    }

    public RestaurantVo registerRestaurant(RestaurantRegisterRequestDto req) {
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
                .build();

        Restaurant restaurant = this.saveRestaurant(restaurantVo, req.getOperatorId());

        return restaurantEntityDtoConverter.toVo(restaurant);
    }

    public RestaurantProcessVo registerRestaurantProcess(RestaurantRegisterProcessRequestDto req) {
        // Restaurant
        RestaurantVo restaurantVo = RestaurantVo.builder()
                .id(req.getId())
                .build();

        // RestaurantProcess
        RestaurantProcessVo restaurantProcessVo = RestaurantProcessVo.builder()
                .id(req.getId())
                .restaurant(restaurantVo)
                .restaurantLastGatheringDate(req.getRestaurantLastGatheringDate())
                .restaurantGatheringStatusCode(req.getRestaurantGatheringStatusCode())
                .build();

        RestaurantProcess restaurantProcess = this.saveRestaurantProcess(restaurantProcessVo, req.getOperatorId());

        return restaurantProcessEntityDtoConverter.toVo(restaurantProcess);
    }
}
