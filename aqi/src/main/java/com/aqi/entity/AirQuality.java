package com.aqi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "airquality")
@EntityListeners(AuditingEntityListener.class)
public class AirQuality {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;
    @Column
    private double temperature; //nhiệt độ
    @Column
    private double humidity; //độ ẩm
    @Column
    private double atmospheric_pressure; // áp suất khí quyển
    @Column
    private double wind_speed; //tốc độ gió
    @Column
    private String weather; //thời tiết
    @Column
    private int aqi; // chỉ số chất lượng không khí
    @Column
    private String main_pollutant; //chất gây ô nhiễm chính
    @Column
    private String color;
    @Column(updatable = false)
    @CreatedDate
    private Date created_at;
}
