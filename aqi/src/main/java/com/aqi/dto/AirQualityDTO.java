package com.aqi.dto;

import com.aqi.entity.AirQuality;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AirQualityDTO {
    private AirQuality airQuality_1;
    private AirQuality airQuality_2;
    private AirQuality airQuality_3;
    private AirQuality airQuality_4;
}
