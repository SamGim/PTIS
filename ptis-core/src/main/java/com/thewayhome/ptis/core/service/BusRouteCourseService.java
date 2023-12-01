package com.thewayhome.ptis.core.service;

import com.thewayhome.ptis.core.dto.request.BusRouteAddCourseItemRequestDto;
import com.thewayhome.ptis.core.entity.BusRouteCourse;
import com.thewayhome.ptis.core.entity.IdSequence;
import com.thewayhome.ptis.core.repository.BusRouteCourseRepository;
import com.thewayhome.ptis.core.repository.IdSequenceRepository;
import com.thewayhome.ptis.core.util.BusRouteCourseEntityVoConverter;
import com.thewayhome.ptis.core.vo.BusRouteCourseVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BusRouteCourseService {
    private final IdSequenceRepository idSequenceRepository;
    private final BusRouteCourseRepository busRouteCourseRepository;
    private final BusRouteCourseEntityVoConverter busRouteCourseEntityDtoConverter;
    public Optional<BusRouteCourse> findById(String id) {
        return busRouteCourseRepository.findById(id);
    }

    private BusRouteCourse saveBusRouteCourse(BusRouteCourseVo req, String operatorId) {
        BusRouteCourse entity = busRouteCourseEntityDtoConverter.toEntity(req, operatorId);
        return busRouteCourseRepository.save(entity);
    }

    public BusRouteCourseVo registerBusRouteCourse(BusRouteAddCourseItemRequestDto req) {
        // ID
        IdSequence idSequence = idSequenceRepository.findById("BUS_ROUTE_COURSE")
                .orElse(new IdSequence("BUS_ROUTE_COURSE", 0L));
        Long id = idSequence.getNextId() + 1;

        idSequence.setNextId(id);
        idSequenceRepository.save(idSequence);

        req.setId(String.format("%012d", id));

        BusRouteCourseVo busRouteCourseVo = BusRouteCourseVo.builder()
                .id(req.getId())
                .busRoute(req.getBusRoute())
                .busStation(req.getBusStation())
                .firstBusTime(req.getFirstBusTime())
                .lastBusTime(req.getLastBusTime())
                .build();

        BusRouteCourse busRouteCourse = this.saveBusRouteCourse(busRouteCourseVo, req.getOperatorId());

        return busRouteCourseEntityDtoConverter.toVo(busRouteCourse);
    }

    public List<BusRouteCourse> getBusRouteCourseByBusStationId(String stationId) {
        return busRouteCourseRepository.findByBusStationIdAndFirstBusTimeIsNotNull(stationId);
    }

    // 해당 BusRouteCourse의 fisrtBusTime 이후이면서 BusRoute가 동일한 BusRouteCourse를 가져온다.
    public List<BusRouteCourse> getBusRouteCourseByBusRouteIdAndTimeAfter(String busRouteCoruseId) {
        BusRouteCourse busRouteCourse = busRouteCourseRepository.findById(busRouteCoruseId).orElseThrow(() -> new RuntimeException("BusRouteCourse not found"));
        return busRouteCourseRepository.findByFirstBusTimeIsNotNullAndBusRouteIdAndFirstBusTimeAfter(busRouteCourse.getBusRoute().getBusRouteId(), busRouteCourse.getFirstBusTime());
    }

}
