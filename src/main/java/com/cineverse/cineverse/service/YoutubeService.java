package com.cineverse.cineverse.service;

import com.cineverse.cineverse.configuration.YoutubeApiConfiguration;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.annotation.PostConstruct;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class YoutubeService {
    private final RestTemplate restTemplate;
    private final YoutubeApiConfiguration youtubeApiConfiguration;

    public YoutubeService(RestTemplate restTemplate, YoutubeApiConfiguration youtubeApiConfiguration) {
        this.restTemplate = restTemplate;
        this.youtubeApiConfiguration = youtubeApiConfiguration;
    }

    @PostConstruct
    public void warmUp() {
        try {
            getTrailerUrl("Warming up Youtube Service");
        } catch (Exception e) {
            System.err.println("YouTube API warm-up failed: " + e.getMessage());
        }
    }

    public String buildTrailerQuery(String title) {
        boolean isArabic = title.matches(".*[\\u0600-\\u06FF]+.*");
        return isArabic ? "الاعلان الرسمي " + title : title + " official trailer";
    }

    public String getTrailerUrl(String title) {
        String query = buildTrailerQuery(title);
        String apiKey = youtubeApiConfiguration.getApiKey();

        String apiUrl = "https://www.googleapis.com/youtube/v3/search"
                + "?part=snippet"
                + "&q=" + query
                + "&key=" + apiKey
                + "&type=video"
                + "&maxResults=1";

        ResponseEntity<JsonNode> response = restTemplate.getForEntity(apiUrl, JsonNode.class);
        JsonNode items = response.getBody().get("items");

        if (items != null && items.isArray() && !items.isEmpty()) {
            String videoId = items.get(0).get("id").get("videoId").asText();
            return "https://www.youtube.com/watch?v=" + videoId;
        }

        return null;
    }

}
