package com.aqi;

import com.aqi.service.AirQualityService;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;

@Component
public class ScheduledTasks {

    @Autowired
    private AirQualityService airQualityService;
    // Tác vụ chạy mỗi 10 phút
    @Scheduled(fixedRate = 600000)
    public void taskWithFixedRate() throws JSONException, IOException {
        airQualityService.save();
    }
}
