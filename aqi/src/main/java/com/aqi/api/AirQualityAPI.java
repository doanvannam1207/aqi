package com.aqi.api;

import com.aqi.entity.AirQuality;
import com.aqi.service.AirQualityService;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@RestController
public class AirQualityAPI {

    @Autowired
    private AirQualityService airQualityService;

    @GetMapping("/airquality")
    public List<AirQuality> getAirQuality() throws JSONException, URISyntaxException, IOException, InterruptedException {
        return airQualityService.save();
    }
//    @GetMapping("/airquality")
//    public String getAirQuality() throws JSONException, URISyntaxException, IOException, InterruptedException {
//        return airQualityService.getAirQualityData();
//    }
}
