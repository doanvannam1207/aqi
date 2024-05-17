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
    private int aqi; // chỉ số chất lượng không khí
    @Column
    private String main_pollutant; //chất gây ô nhiễm chính
    @Column
    private double co; //CO (Carbon Monoxide) µg/m³
    @Column
    private double dew; //Dew (Hạt sương): Độ ẩm hạt sương g/m³
    @Column
    private double h; //Humidity - Độ ẩm %
    @Column
    private double no2; //NO2 (Nitrogen Dioxide) µg/m³
    @Column
    private double o3; //O3 (Ozone) µg/m³
    @Column
    private double p; //Pressure - Áp suất hPa
    @Column
    private double pm10; //PM10 (Particulate Matter < 10 µm) µg/m³
    @Column
    private double pm25; //PM2.5 (Particulate Matter < 2.5 µm) µg/m³
    @Column
    private double so2; //SO2 (Sulfur Dioxide) µg/m³
    @Column
    private double t; //Temperature - Nhiệt độ °C
    @Column
    private double w; //wind-gió m/s
    @Column
    private double wg; //Wind Gust - Cơn gió): Cơn gió có tốc độ là m/s
    @Column
    private String color;
    @Column(updatable = false)
    @CreatedDate
    private Date created_at;
}
