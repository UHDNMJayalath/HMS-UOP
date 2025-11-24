package com.hostelManagementSystem.HostelManagementSystem.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class DistanceService {


    private final String API_KEY = "AIzaSyANyfgGOtQ-tBGu8AIWDmmshuzDc228bKM";

    public String calculateDistance(String studentAddress) {
        String universityAddress = "University of Peradeniya, Sri Lanka";

        try {

            String url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins="
                    + studentAddress + "&destinations=" + universityAddress
                    + "&mode=driving&key=" + API_KEY;


            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(url, String.class);


            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);


            String distanceText = root.path("rows").get(0)
                    .path("elements").get(0)
                    .path("distance").path("text").asText();


            return distanceText.replace(" km", "").trim();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}