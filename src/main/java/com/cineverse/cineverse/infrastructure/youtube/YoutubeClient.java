package com.cineverse.cineverse.infrastructure.youtube;

import com.cineverse.cineverse.configuration.YoutubeApiConfiguration;
import com.cineverse.cineverse.domain.enums.ContentType;
import com.cineverse.cineverse.infrastructure.youtube.trailerquery.*;
import com.cineverse.cineverse.util.YouTubeVideoScorerUtil;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.annotation.PostConstruct;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;


@Service
public class YoutubeClient {
    private final RestTemplate restTemplate;
    private final YoutubeApiConfiguration youtubeApiConfiguration;

    public YoutubeClient(RestTemplate restTemplate, YoutubeApiConfiguration youtubeApiConfiguration) {
        this.restTemplate = restTemplate;
        this.youtubeApiConfiguration = youtubeApiConfiguration;
    }

    @PostConstruct
    private void warmUp() {
        try {
            getTrailerUrl("Warming up Youtube Service", 2023, ContentType.MOVIE, "en", List.of());
        } catch (Exception e) {
            System.err.println("YouTube API warm-up failed: " + e.getMessage());
        }
    }

    private String buildTrailerQuery(String title, int year, ContentType type, String language, List<String> genres) {
        boolean isArabic = isArabic(language);
        boolean isAnime = isAnimeBasedOnMetadata(language, genres);
        TrailerQueryBuilder queryBuilder;
        if (isArabic) {
            queryBuilder = (type == ContentType.MOVIE) ?
                    new ArabicMovieQueryBuilder() :
                    new ArabicSeriesQueryBuilder();
        } else if (isAnime) {
            queryBuilder = new AnimeQueryBuilder();
        } else
            queryBuilder = new EnglishContentQueryBuilder();
        return queryBuilder.build(title, year);
    }

    private boolean isArabic(String language) {
        return language != null && language.equalsIgnoreCase("ar");
    }

    private boolean isAnimeBasedOnMetadata(String language, List<String> genres) {
        boolean isJapanese = language != null && language.equalsIgnoreCase("ja");
        boolean isAnimation = isAnimationGenre(genres);
        return isAnimation && isJapanese;
    }

    private boolean isAnimationGenre(List<String> genres) {
        if (genres != null) {
            for (String genre : genres) {
                if (genre.equalsIgnoreCase("Animation")) {
                    return true;
                }
            }
        }
        return false;
    }

    private static String normalizeArabic(String text) {
        if (text == null) return "";
        return text
                .replace("أ", "ا")
                .replace("إ", "ا")
                .replace("آ", "ا")
                .replace("ى", "ي")
                .replace("ئ", "ي")
                .replace("ؤ", "و")
                .replace("ة", "ه")
                .replaceAll("[\\p{Punct}ـ]", "")
                .replaceAll("\s+", " ")
                .trim();
    }


    public String getTrailerUrl(String title, int year, ContentType type, String language, List<String> genres) {
        String query = buildTrailerQuery(title, year, type, language, genres);
        String apiKey = youtubeApiConfiguration.getApiKey();
        String normTitle = normalizeArabic(title).toLowerCase();
        YouTubeVideoScorerUtil scorer = new YouTubeVideoScorerUtil();

        String apiUrl = "https://www.googleapis.com/youtube/v3/search"
                + "?part=snippet"
                + "&q=" + query
                + "&key=" + apiKey
                + "&type=video"
                + "&maxResults=10"
                + "&order=relevance";

        try {
            ResponseEntity<JsonNode> response = restTemplate.getForEntity(apiUrl, JsonNode.class);
            JsonNode items = response.getBody().get("items");

            if (items != null && items.isArray() && !items.isEmpty()) {
                List<Video> videos = new ArrayList<>();

                for (JsonNode item : items) {
                    JsonNode snippet = item.get("snippet");
                    String videoId = item.get("id").get("videoId").asText();
                    String vTitle = normalizeArabic(snippet.get("title").asText()).replaceAll("#", "");
                    String vDescription = snippet.get("description").asText();
                    String vChannel = snippet.get("channelTitle").asText();

                    videos.add(new Video(videoId, vTitle, vDescription, vChannel));
                }

                final int MIN_SCORE = 8;

                Optional<Video> bestVideo = videos.stream()
                        .map(video -> {
                            int score = scorer.score(video, normTitle, year);
                            return new AbstractMap.SimpleEntry<>(video, score);
                        })
                        .filter(entry -> {
                            return entry.getValue() >= MIN_SCORE;
                        })
                        .max(Comparator.comparingInt(Map.Entry::getValue))
                        .map(Map.Entry::getKey);

                return bestVideo.map(video -> video.videoId).orElse(null);
            }
            return null;

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch YouTube trailer: " + e.getMessage(), e);
        }
    }

}
