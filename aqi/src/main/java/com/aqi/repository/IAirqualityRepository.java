package com.aqi.repository;

import com.aqi.entity.AirQuality;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IAirqualityRepository extends JpaRepository<AirQuality, Integer> {
    @Query("SELECT a from AirQuality a where a.location.id = :lid order by a.created_at desc")
    List<AirQuality> findLast4Entities(@Param("lid") int id, Pageable pageable);
}
