package com.aqi.api;

import com.aqi.dto.AirQualityDTO;
import com.aqi.service.AirQualityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AirQualityAPI {

    @Autowired
    private AirQualityService airQualityService;

    @GetMapping("/airquality-data")
    public List<AirQualityDTO> test(){
        return airQualityService.getLast4Entities();
    }
}
