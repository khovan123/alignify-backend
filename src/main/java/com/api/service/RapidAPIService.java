package com.api.service;

import java.io.IOException;
import java.util.Map;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.api.dto.ApiResponse;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class RapidAPIService {

    @Value("${spring.x-rapidapi-key}")
    private String rapidapikey;

    public ResponseEntity<?> getUserStatsFromTiktok(String uniqueId, HttpServletRequest request) {
        AsyncHttpClient client = new DefaultAsyncHttpClient();
        final JSONObject[] statsJson = new JSONObject[1];
        try {
            client.prepare("GET", "https://tiktok-api23.p.rapidapi.com/api/user/info?uniqueId=" + uniqueId)
                    .setHeader("x-rapidapi-key", rapidapikey)
                    .setHeader("x-rapidapi-host", "tiktok-api23.p.rapidapi.com")
                    .execute()
                    .toCompletableFuture()
                    .thenAccept(response -> {
                        String body = response.getResponseBody();
                        JSONObject json = new JSONObject(body);
                        statsJson[0] = json
                                .getJSONObject("userInfo")
                                .getJSONObject("statsV2");
                    }).join();

            client.close();
        } catch (Exception e) {
            return ApiResponse.sendError(500, "API service is not available: " + e.getMessage(),
                    request.getRequestURI());
        }
        return ApiResponse.sendSuccess(200, "Tiktok response successfully", statsJson[0].toMap(),
                request.getRequestURI());
    }

    public ResponseEntity<?> getVideoDetailsFromTiktok(String videoId, HttpServletRequest request) {
        AsyncHttpClient client = new DefaultAsyncHttpClient();
        final JSONObject[] statsJson = new JSONObject[1];
        try {
            client.prepare("GET", "https://tiktok-api23.p.rapidapi.com/api/post/detail?videoId=" + videoId)
                    .setHeader("x-rapidapi-key", rapidapikey)
                    .setHeader("x-rapidapi-host", "tiktok-api23.p.rapidapi.com")
                    .execute()
                    .toCompletableFuture()
                    .thenAccept(response -> {
                        String body = response.getResponseBody();
                        JSONObject json = new JSONObject(body);
                        statsJson[0] = json
                                .getJSONObject("itemInfo")
                                .getJSONObject("itemStruct")
                                .getJSONObject("statsV2");
                    }).join();

            client.close();
        } catch (Exception e) {
            return ApiResponse.sendError(500, "API service is not available: " + e.getMessage(),
                    request.getRequestURI());
        }
        return ApiResponse.sendSuccess(200, "Tiktok response successfully", statsJson[0].toMap(),
                request.getRequestURI());
    }

    private String getChannelId(String channelName) throws IOException {
        final JSONObject[] statsJson = new JSONObject[1];
        AsyncHttpClient client = new DefaultAsyncHttpClient();
        client.prepare("GET", "https://youtube-v2.p.rapidapi.com/channel/id?channel_name=" + channelName)
                .setHeader("x-rapidapi-key", rapidapikey)
                .setHeader("x-rapidapi-host", "youtube-v2.p.rapidapi.com")
                .execute()
                .toCompletableFuture()
                .thenAccept(response -> {
                    String body = response.getResponseBody();
                    JSONObject json = new JSONObject(body);
                    statsJson[0] = json;
                })
                .join();

        client.close();

        return statsJson[0].getString("channel_id");
    }

    public ResponseEntity<?> getStatsChannelFromYoutube(String channelName, HttpServletRequest request) {
        final JSONObject[] statsJson = new JSONObject[1];
        try {
            String channelId = getChannelId(channelName);
            AsyncHttpClient client = new DefaultAsyncHttpClient();
            client.prepare("GET", "https://youtube-v2.p.rapidapi.com/channel/details?channel_id=" + channelId)
                    .setHeader("x-rapidapi-key", rapidapikey)
                    .setHeader("x-rapidapi-host", "youtube-v2.p.rapidapi.com")
                    .execute()
                    .toCompletableFuture()
                    .thenAccept(response -> {
                        String body = response.getResponseBody();
                        JSONObject json = new JSONObject(body);
                        statsJson[0] = json;
                    })
                    .join();

            client.close();
        } catch (Exception e) {
            return ApiResponse.sendError(500, "API service is not available: " + e.getMessage(),
                    request.getRequestURI());
        }
        String subcriber = statsJson[0].getString("subscriber_count").replace("subscribers", "").trim();
        if (subcriber.endsWith("K")) {
            subcriber = subcriber.replace("K", "000");
        } else if (subcriber.endsWith("M")) {
            subcriber = subcriber.replace("K", "0000");
        }
        return ApiResponse.sendSuccess(200, "Youtube response successfully", Map.of("subcriber_count", subcriber),
                request.getRequestURI());
    }

    public ResponseEntity<?> getVideoDetailsFromYoutube(String videoId, HttpServletRequest request) {
        final JSONObject[] statsJson = new JSONObject[1];
        try {
            AsyncHttpClient client = new DefaultAsyncHttpClient();
            client.prepare("GET", "https://youtube-v2.p.rapidapi.com/video/details?video_id=" + videoId)
                    .setHeader("x-rapidapi-key", rapidapikey)
                    .setHeader("x-rapidapi-host", "youtube-v2.p.rapidapi.com")
                    .execute()
                    .toCompletableFuture()
                    .thenAccept(response -> {
                        String body = response.getResponseBody();
                        JSONObject json = new JSONObject(body);
                        statsJson[0] = json;
                    })
                    .join();

            client.close();
        } catch (Exception e) {
            return ApiResponse.sendError(500, "API service is not available: " + e.getMessage(),
                    request.getRequestURI());
        }
        return ApiResponse.sendSuccess(200, "Youtube response successfully", statsJson[0].toMap(),
                request.getRequestURI());
    }

    public ResponseEntity<?> getStatsPageFromFacebook(String pageName, HttpServletRequest request) {
        final JSONObject[] statsJson = new JSONObject[1];
        try {
            AsyncHttpClient client = new DefaultAsyncHttpClient();
            client.prepare("GET",
                    "https://facebook-scraper3.p.rapidapi.com/page/details?url=https%3A%2F%2Fwww.facebook.com%2F"
                            + pageName)
                    .setHeader("x-rapidapi-key", rapidapikey)
                    .setHeader("x-rapidapi-host", "facebook-scraper3.p.rapidapi.com")
                    .execute()
                    .toCompletableFuture()
                    .thenAccept(response -> {
                        String body = response.getResponseBody();
                        JSONObject json = new JSONObject(body);
                        statsJson[0] = json.getJSONObject("results");
                    })
                    .join();

            client.close();
        } catch (Exception e) {
            return ApiResponse.sendError(500, "API service is not available: " + e.getMessage(),
                    request.getRequestURI());
        }
        return ApiResponse.sendSuccess(200, "Youtube response successfully",
                Map.of("followers", statsJson[0].getString("followers")),
                request.getRequestURI());
    }

    public ResponseEntity<?> getPostDetailsFromFacebook(String postId, HttpServletRequest request) {
        final JSONObject[] statsJson = new JSONObject[1];
        try {
            AsyncHttpClient client = new DefaultAsyncHttpClient();
            client.prepare("GET",
                    "https://facebook-scraper3.p.rapidapi.com/post?post_id=" + postId)
                    .setHeader("x-rapidapi-key", rapidapikey)
                    .setHeader("x-rapidapi-host", "facebook-scraper3.p.rapidapi.com")
                    .execute()
                    .toCompletableFuture()
                    .thenAccept(response -> {
                        String body = response.getResponseBody();
                        JSONObject json = new JSONObject(body);
                        statsJson[0] = json.getJSONObject("results");
                    })
                    .join();

            client.close();
            client.close();
        } catch (Exception e) {
            return ApiResponse.sendError(500, "API service is not available: " + e.getMessage(),
                    request.getRequestURI());
        }
        return ApiResponse.sendSuccess(200, "Youtube response successfully",
                statsJson[0].toMap(),
                request.getRequestURI());
    }

    public ResponseEntity<?> getStatsUserFromInstagram(String userName, HttpServletRequest request) {
        final JSONObject[] statsJson = new JSONObject[1];
        try {
            AsyncHttpClient client = new DefaultAsyncHttpClient();
            client.prepare("GET",
                    "https://instagram-scrapper-posts-reels-stories-downloader.p.rapidapi.com/profile_by_username?username=instagram")
                    .setHeader("x-rapidapi-key", rapidapikey)
                    .setHeader("x-rapidapi-host", "instagram-scrapper-posts-reels-stories-downloader.p.rapidapi.com")
                    .execute()
                    .toCompletableFuture()
                    .thenAccept(response -> {
                        String body = response.getResponseBody();
                        JSONObject json = new JSONObject(body);
                        statsJson[0] = json;
                    })
                    .join();

            client.close();
        } catch (Exception e) {
            return ApiResponse.sendError(500, "API service is not available: " + e.getMessage(),
                    request.getRequestURI());
        }
        return ApiResponse.sendSuccess(200, "Youtube response successfully",
                Map.of("followers", statsJson[0].getString("follower_count")),
                request.getRequestURI());
    }

    public ResponseEntity<?> getInforDetailsFromInstagram(String code_or_id_or_url, HttpServletRequest request) {
        final JSONObject[] statsJson = new JSONObject[1];
        try {
            AsyncHttpClient client = new DefaultAsyncHttpClient();
            client.prepare("GET",
                    "https://instagram-social-api.p.rapidapi.com/v1/post_info?code_or_id_or_url=" + code_or_id_or_url)
                    .setHeader("x-rapidapi-key", rapidapikey)
                    .setHeader("x-rapidapi-host", "instagram-social-api.p.rapidapi.com")
                    .execute()
                    .toCompletableFuture()
                    .thenAccept(response -> {
                        String body = response.getResponseBody();
                        JSONObject json = new JSONObject(body);
                        statsJson[0] = json.getJSONObject("data").getJSONObject("metrics");
                    })
                    .join();

            client.close();
        } catch (Exception e) {
            return ApiResponse.sendError(500, "API service is not available: " + e.getMessage(),
                    request.getRequestURI());
        }
        return ApiResponse.sendSuccess(200, "Youtube response successfully",
                statsJson[0].toMap(),
                request.getRequestURI());
    }

}
