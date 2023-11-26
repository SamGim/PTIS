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

    private BusRouteCourse saveBusRouteCourse(BusRouteCourseVo req) {
        BusRouteCourse entity = busRouteCourseEntityDtoConverter.toEntity(req, req.getOperatorId());
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
                .operatorId(req.getOperatorId())
                .build();

        BusRouteCourse busRouteCourse = this.saveBusRouteCourse(busRouteCourseVo);

        return busRouteCourseEntityDtoConverter.toVo(busRouteCourse, req.getOperatorId());
    }

}
