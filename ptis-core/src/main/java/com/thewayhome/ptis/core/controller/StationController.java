package com.thewayhome.ptis.core.controller;

import com.thewayhome.ptis.core.service.StationService;
import com.thewayhome.ptis.core.entity.Station;
import com.thewayhome.ptis.core.dto.StationRegisterReqDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StationController {
    @Autowired
    private StationService stationService;

    @PostMapping(name="/station", path="/station", produces = MediaType.APPLICATION_JSON_VALUE)
    public Station createStation(@RequestBody StationRegisterReqDto req) {
        return stationService.saveStation(req);
    }
}
