package com.aqi.service;

import com.aqi.entity.*;
import com.aqi.repository.IAirqualityRepository;
import com.aqi.repository.ILocationRepository;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
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

    @Value("${airvisual.api.key}")
    private String apiKey;

    private final String apiUrl = "https://api.airvisual.com/v2/nearest_city";

//    public String getAirQualityData()
//            throws IOException, InterruptedException, URISyntaxException {
//        String url = apiUrl + "?key=" + apiKey;
//        HttpClient client = HttpClient.newHttpClient();
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(new URI(url))
//                .build();
//
//        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//        return response.body();
//    }

//    public List<AirQuality> getAirQualityData() throws URISyntaxException, IOException, InterruptedException, JSONException {
//        List<AirQuality> airQualityDataList = new ArrayList<>();
//        for(Location l: iLocationRepository.findAll()){
//            String api_url = apiUrl + "?lat=" + l.getLatitude() + "&lon=" + l.getLongitude() + "&key=" + apiKey;
//
//            // Tạo HTTP client
//            CloseableHttpClient httpClient = HttpClients.createDefault();
//            HttpGet httpGet = new HttpGet(api_url);
//            try {
//                // Gửi yêu cầu và nhận phản hồi
//                CloseableHttpResponse response = httpClient.execute(httpGet);
//
//                // Xử lý phản hồi
//                if (response.getStatusLine().getStatusCode() == 200) {
//                    String responseBody = EntityUtils.toString(response.getEntity());
//                    JSONObject jsonResponse = new JSONObject(responseBody);
//
//
//                    AirQuality airQuality = new AirQuality();
//
//                    JSONObject data = jsonResponse.getJSONObject("data");
//                    airQuality.setCity(l.getCity());
//                    JSONObject location = data.getJSONObject("location");
//                    JSONArray coordinates = (JSONArray) location.get("coordinates");
//                    airQuality.setLongitude((double) coordinates.get(0));
//                    airQuality.setLatitude((double) coordinates.get(1));
//
//                    JSONObject current_js = data.getJSONObject("current");
//                    JSONObject pollution_js = current_js.getJSONObject("pollution");
//                    JSONObject weather_js = current_js.getJSONObject("weather");
//
//                    airQuality.setAqi(pollution_js.getInt("aqius"));
//                    airQuality.setMain_pollutant(pollution_js.getString("mainus"));
//
//                    airQuality.setTemperature(weather_js.getInt("tp"));
//                    airQuality.setAtmospheric_pressure(weather_js.getInt("pr"));
//                    airQuality.setHumidity(weather_js.getInt("hu"));
//                    airQuality.setWind_speed(weather_js.getDouble("ws"));
//                    airQuality.setWeather(weather_js.getString("ic"));
//
//                    airQualityDataList.add(airQuality);
//                } else {
//                    System.out.println("Failed to get data. Status code: " + response.getStatusLine().getStatusCode());
//                }
//            } catch (IOException e) {
//                System.err.println("Error executing HTTP request: " + e.getMessage());
//            } finally {
//                try {
//                    // Đóng HTTP client sau khi sử dụng
//                    httpClient.close();
//                } catch (IOException e) {
//                    System.err.println("Error closing HTTP client: " + e.getMessage());
//                }
//            }
//
//        }
//        return airQualityDataList;
//    }

    public List<AirQuality> getAirQualityData() throws JSONException {
        if(iLocationRepository.findAll() == null){
            return null;
        } else {
            List<AirQuality> airQualityDataList = new ArrayList<>();
            for(Location l: iLocationRepository.findAll()){

                String url = apiUrl + "?lat=" + l.getLatitude() + "&lon=" + l.getLongitude() + "&key=" + apiKey;
                //String url = apiUrl + apiKey;
                ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
                JSONObject jsonResponse = new JSONObject(response.getBody());


                AirQuality airQuality = new AirQuality();

                JSONObject data = jsonResponse.getJSONObject("data");
                airQuality.setCity(l.getCity());
                JSONObject location = data.getJSONObject("location");
                JSONArray coordinates = (JSONArray) location.get("coordinates");
                airQuality.setLongitude((double) coordinates.get(0));
                airQuality.setLatitude((double) coordinates.get(1));

                JSONObject current_js = data.getJSONObject("current");
                JSONObject pollution_js = current_js.getJSONObject("pollution");
                JSONObject weather_js = current_js.getJSONObject("weather");

                airQuality.setAqi(pollution_js.getInt("aqius"));
                airQuality.setMain_pollutant(pollution_js.getString("mainus"));

                airQuality.setTemperature(weather_js.getInt("tp"));
                airQuality.setAtmospheric_pressure(weather_js.getInt("pr"));
                airQuality.setHumidity(weather_js.getInt("hu"));
                airQuality.setWind_speed(weather_js.getDouble("ws"));
                airQuality.setWeather(weather_js.getString("ic"));

                airQualityDataList.add(airQuality);
            }
            return airQualityDataList;
        }
    }
    public List<AirQuality> save() throws JSONException, URISyntaxException, IOException, InterruptedException {
        List<AirQuality> airQualityList = getAirQualityData();
        for(AirQuality airQuality: airQualityList){
            if(airQuality.getWeather().equals("01d")) {
                airQuality.setWeather("Trời quang đãng");
            } else if(airQuality.getWeather().equals("01n")){
                airQuality.setWeather("Trời quang đãng");
            } else if(airQuality.getWeather().equals("02d")){
                airQuality.setWeather("Trời ít mây");
            } else if(airQuality.getWeather().equals("02n")){
                airQuality.setWeather("Trời ít mây");
            } else if(airQuality.getWeather().equals("03d")){
                airQuality.setWeather("Trời có mây");
            } else if(airQuality.getWeather().equals("03n")){
                airQuality.setWeather("Trời có mây");
            } else if(airQuality.getWeather().equals("04d")){
                airQuality.setWeather("Trời nhiều mây");
            } else if(airQuality.getWeather().equals("04n")){
                airQuality.setWeather("Trời nhiều mây");
            } else if(airQuality.getWeather().equals("09d")){
                airQuality.setWeather("Mưa rải rác");
            } else if(airQuality.getWeather().equals("09n")){
                airQuality.setWeather("Mưa rải rác");
            } else if(airQuality.getWeather().equals("10d")){
                airQuality.setWeather("Mưa nhẹ");
            } else if(airQuality.getWeather().equals("10n")){
                airQuality.setWeather("Mưa nhẹ");
            } else if(airQuality.getWeather().equals("11d")){
                airQuality.setWeather("Mưa");
            } else if(airQuality.getWeather().equals("11n")){
                airQuality.setWeather("Mưa");
            } else if(airQuality.getWeather().equals("13d")){
                airQuality.setWeather("Tuyết");
            } else if(airQuality.getWeather().equals("13n")){
                airQuality.setWeather("Tuyết");
            } else if(airQuality.getWeather().equals("50d")){
                airQuality.setWeather("Sương mù");
            } else if(airQuality.getWeather().equals("50n")){
                airQuality.setWeather("Sương mù");
            }

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