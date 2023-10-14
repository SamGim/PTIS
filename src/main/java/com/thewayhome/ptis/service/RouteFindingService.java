package com.thewayhome.ptis.service;

import com.thewayhome.ptis.vo.RouteFindingAPIReqVo;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class RouteFindingService {
    public Mono<String> findRoute(@Valid RouteFindingAPIReqVo req) {
        return Mono.just(req.toString());
    }
}
