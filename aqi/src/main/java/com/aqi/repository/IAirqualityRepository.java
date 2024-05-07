package com.aqi.repository;

import com.aqi.entity.AirQuality;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAirqualityRepository extends JpaRepository<AirQuality, Integer> {
}
