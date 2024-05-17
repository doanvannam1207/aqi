package com.aqi.service;

import com.aqi.dto.AirQualityDTO;
import com.aqi.entity.*;
import com.aqi.repository.IAirqualityRepository;
import com.aqi.repository.ILocationRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Service
public class AirQualityService {


    @Autowired
    private ILocationRepository iLocationRepository;
    @Autowired
    private IAirqualityRepository iAirqualityRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${waqi.api.key}")
    private String apiKey;


    public List<AirQualityDTO> getLast4Entities(){
        List<AirQualityDTO> airQualityDTOS = new ArrayList<>();
        for(Location l: iLocationRepository.findAll()){
            Pageable pageable = PageRequest.of(0, 4);
            AirQualityDTO airQualityDTO = new AirQualityDTO();
            List<AirQuality> airQualities = iAirqualityRepository.findLast4Entities(l.getId(), pageable);
            airQualityDTO.setAirQuality_1(airQualities.get(0));
            airQualityDTO.setAirQuality_2(airQualities.get(1));
            airQualityDTO.setAirQuality_3(airQualities.get(2));
            airQualityDTO.setAirQuality_4(airQualities.get(3));
            airQualityDTOS.add(airQualityDTO);
        }
        return airQualityDTOS;
    }

    //private final String apiUrl = "https://api.airvisual.com/v2/nearest_city";
    private final String apiUrl = "https://api.waqi.info/feed/geo:";


    public String getAirQualityIndex(int stationId) {
        String url = "https://api.waqi.info/feed/@" + stationId + "/?token=" + apiKey;
        return restTemplate.getForObject(url, String.class);
    }

    public String getAirQualityByCoordinates(double lat, double lon) {
        StringBuilder result = new StringBuilder();
            String url = "https://api.waqi.info/feed/geo:" + lat + ";" + lon + "/?token=" + apiKey;
            result.append(restTemplate.getForObject(url, String.class));

        return result.toString();
    }

    public String getAirQualityByCoordinate() {
        String url = "https://api.waqi.info/map/bounds/?latlng=20.8136,105.3388,20.8056,105.3355&token=" + apiKey;
        return restTemplate.getForObject(url, String.class);
    }
    public List<AirQuality> getAirQualityData() throws JSONException, JsonProcessingException {
        if(iLocationRepository.findAll() == null){
            return null;
        } else {
            List<AirQuality> airQualityDataList = new ArrayList<>();
            for(Location l: iLocationRepository.findAll()){
                String url = apiUrl + l.getLatitude() + ";" + l.getLongitude() + "/?token=" + apiKey;
                ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
                JSONObject jsonResponse = new JSONObject(response.getBody());


                AirQuality airQuality = new AirQuality();
                JSONObject data = jsonResponse.getJSONObject("data");
                JSONObject iaqi = data.getJSONObject("iaqi");
                //System.out.println(iaqi.toString());
                //AirQuality airQuality = JSONToAirQuality(iaqi);
                airQuality.setLocation(l);
                String aqi = data.getString("aqi");
                if(aqi.equals("-")){
                    aqi = "0";
                }
                airQuality.setAqi(Integer.valueOf(aqi));
                airQuality.setMain_pollutant(data.getString("dominentpol"));
                if(iaqi.has("co")){
                    airQuality.setCo(iaqi.getJSONObject("co").getDouble("v"));
                }
                if(iaqi.has("dew")){
                    airQuality.setDew(iaqi.getJSONObject("dew").getDouble("v"));
                }
                if(iaqi.has("h")){
                    airQuality.setH(iaqi.getJSONObject("h").getDouble("v"));
                }
                if(iaqi.has("no2")){
                    airQuality.setNo2(iaqi.getJSONObject("no2").getDouble("v"));
                }
                if(iaqi.has("o3")){
                    airQuality.setO3(iaqi.getJSONObject("o3").getDouble("v"));
                }
                if(iaqi.has("p")){
                    airQuality.setP(iaqi.getJSONObject("p").getDouble("v"));
                }
                if(iaqi.has("pm10")){
                    airQuality.setPm10(iaqi.getJSONObject("pm10").getDouble("v"));
                }
                if(iaqi.has("pm25")){
                    airQuality.setPm25(iaqi.getJSONObject("pm25").getDouble("v"));
                }
                if(iaqi.has("so2")){
                    airQuality.setSo2(iaqi.getJSONObject("so2").getDouble("v"));
                }
                if(iaqi.has("t")){
                    airQuality.setT(iaqi.getJSONObject("t").getDouble("v"));
                }
                if(iaqi.has("w")){
                    airQuality.setW(iaqi.getJSONObject("w").getDouble("v"));
                }
                if(iaqi.has("wg")){
                    airQuality.setWg(iaqi.getJSONObject("wg").getDouble("v"));
                }
//                airQuality.setDew(iaqi.getJSONObject("dew").getDouble("v"));
//                airQuality.setH(iaqi.getJSONObject("h").getInt("v"));
//                //airQuality.setNo2(iaqi.getJSONObject("no2").getDouble("v"));
//                //airQuality.setO3(iaqi.getJSONObject("o3").getDouble("v"));
//                airQuality.setP(iaqi.getJSONObject("p").getInt("v"));
//                //airQuality.setPm10(iaqi.getJSONObject("pm10").getDouble("v"));
//                airQuality.setPm25(iaqi.getJSONObject("pm25").getDouble("v"));
//                //airQuality.setSo2(iaqi.getJSONObject("so2").getDouble("v"));
//                airQuality.setT(iaqi.getJSONObject("t").getDouble("v"));
//                airQuality.setW(iaqi.getJSONObject("w").getDouble("v"));
                //airQuality.setWg(iaqi.getJSONObject("wg").getDouble("v"));
//                airQuality.setCity(l.getCity());
//                JSONObject location = data.getJSONObject("location");
//                JSONArray coordinates = (JSONArray) location.get("coordinates");
//                airQuality.setLongitude((double) coordinates.get(0));
//                airQuality.setLatitude((double) coordinates.get(1));

//                JSONObject current_js = data.getJSONObject("current");
//                JSONObject pollution_js = current_js.getJSONObject("pollution");
//                JSONObject weather_js = current_js.getJSONObject("weather");
//
//                airQuality.setAqi(pollution_js.getInt("aqius"));
//                airQuality.setMain_pollutant(pollution_js.getString("mainus"));
//
//                airQuality.setTemperature(weather_js.getInt("tp"));
//                airQuality.setAtmospheric_pressure(weather_js.getInt("pr"));
//                airQuality.setHumidity(weather_js.getInt("hu"));
//                airQuality.setWind_speed(weather_js.getDouble("ws"));
//                airQuality.setWeather(weather_js.getString("ic"));

                airQualityDataList.add(airQuality);
            }
            return airQualityDataList;
        }
    }

    public List<AirQuality> save() throws JSONException, URISyntaxException, IOException, InterruptedException {
        List<AirQuality> airQualityList = getAirQualityData();
        for(AirQuality airQuality: airQualityList){
            if(airQuality.getAqi() >= 0 && airQuality.getAqi() <= 50){
                airQuality.setColor("#17fc03");
            } else if(airQuality.getAqi() > 50 && airQuality.getAqi() <= 100){
                airQuality.setColor("#fcba03");
            } else if(airQuality.getAqi() > 100 && airQuality.getAqi() <= 150){
                airQuality.setColor("#fc8403");
            } else if(airQuality.getAqi() > 150 && airQuality.getAqi() <= 200){
                airQuality.setColor("#fc2c03");
            } else if(airQuality.getAqi() > 201 && airQuality.getAqi() <= 300){
                airQuality.setColor("#660066");
            } else {
                airQuality.setColor("#800000");
            }
        }
        return iAirqualityRepository.saveAll(airQualityList);
    }
}