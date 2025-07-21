package com.api.service;

import java.io.IOException;
import java.util.Map;

import com.api.dto.response.PostDetailStats;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.json.JSONArray;
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
        double value = 0;
        if (subcriber.endsWith("K")) {
            value = Double.parseDouble(subcriber.replace("K", ""));
            value *= 1_000;
        } else if (subcriber.endsWith("M")) {
            value = Double.parseDouble(subcriber.replace("M", ""));
            value *= 1_000_000;
        } else {
            value = Double.parseDouble(subcriber.replace(".", ""));
        }
        subcriber = String.valueOf((int) value);
        return ApiResponse.sendSuccess(200, "Youtube response successfully", Map.of("subcriber_count", subcriber),
                request.getRequestURI());
    }

    public ResponseEntity<?> getVideoDetailsFromYoutube(String videoId, HttpServletRequest request) {
        final JSONObject[] statsJson = new JSONObject[1];
        PostDetailStats postDetailStats = new PostDetailStats();
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
            System.out.println(statsJson[0]);
            if (statsJson[0].has("number_of_views") && !statsJson[0].isNull("number_of_views")) {
                postDetailStats.setView_count(statsJson[0].getInt("number_of_views"));
            }
            if (statsJson[0].has("published_time") && !statsJson[0].isNull("published_time")) {
                postDetailStats.setCreated_at_utc(statsJson[0].getString("published_time"));
            }
            if (statsJson[0].has("thumbnails") && !statsJson[0].isNull("thumbnails")) {
                JSONArray thumbnails = statsJson[0].getJSONArray("thumbnails");
                if (thumbnails.isEmpty()) {
                    if (thumbnails.getJSONObject(0).has("url") && !thumbnails.getJSONObject(0).isNull("url")) {
                        postDetailStats.setThumbnail_url(thumbnails.getJSONObject(0).getString("url"));
                    }
                }
            }
        } catch (Exception e) {
            return ApiResponse.sendError(500, "API service is not available: " + e.getMessage(),
                    request.getRequestURI());
        }
        return ApiResponse.sendSuccess(200, "Youtube response successfully", postDetailStats,
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
                Map.of("followers", statsJson[0].getInt("followers")),
                request.getRequestURI());
    }

    public ResponseEntity<?> getPostDetailsFromFacebook(String postId, HttpServletRequest request) {
        final JSONObject[] statsJson = new JSONObject[1];
        PostDetailStats postDetailStats = new PostDetailStats();
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
            System.out.println(statsJson[0]);
            if (statsJson[0].has("image") && !statsJson[0].isNull("image")) {
                JSONObject image = statsJson[0].getJSONObject("image");
                if (statsJson[0].has("uri") && !statsJson[0].isNull("uri")) {
                    postDetailStats.setThumbnail_url(image.getString("uri"));
                }
            } else {
                if (statsJson[0].has("thumbnail_uri") && !statsJson[0].isNull("thumbnail_uri")) {
                    postDetailStats.setThumbnail_url(statsJson[0].getString("thumbnail_uri"));
                }
            }
            if (statsJson[0].has("reshare_count") && !statsJson[0].isNull("reshare_count")) {
                postDetailStats.setShare_count(statsJson[0].getInt("reshare_count"));
            }
            if (statsJson[0].has("comments_count") && !statsJson[0].isNull("comments_count")) {
                postDetailStats.setComment_count(statsJson[0].getInt("comments_count"));
            }
            if (statsJson[0].has("reactions_count") && !statsJson[0].isNull("reactions_count")) {
                postDetailStats.setLike_count(statsJson[0].getInt("reactions_count"));
            }
            if (statsJson[0].has("play_count") && !statsJson[0].isNull("play_count")) {
                postDetailStats.setPlay_count(statsJson[0].getInt("play_count"));
            }
        } catch (Exception e) {
            return ApiResponse.sendError(500, "API service is not available: " + e.getMessage(),
                    request.getRequestURI());
        }
        return ApiResponse.sendSuccess(200, "Facebook response successfully",
                postDetailStats,
                request.getRequestURI());
    }

    public ResponseEntity<?> getStatsUserFromInstagram(String userName, HttpServletRequest request) {
        final JSONObject[] statsJson = new JSONObject[1];
        try {
            AsyncHttpClient client = new DefaultAsyncHttpClient();
            client.prepare("GET",
                            "https://instagram-scrapper-posts-reels-stories-downloader.p.rapidapi.com/profile_by_username?username="
                                    + userName)
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
                Map.of("followers", statsJson[0].getInt("follower_count")),
                request.getRequestURI());
    }

    public ResponseEntity<?> getInforDetailsFromInstagram(String code_or_id_or_url, HttpServletRequest request) {
        final JSONObject[] statsJson = new JSONObject[1];
        PostDetailStats postDetailStats = new PostDetailStats();
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
                        statsJson[0] = json.getJSONObject("data");
                    })
                    .join();
            client.close();
            System.out.println(statsJson[0]);
            JSONObject metrics = statsJson[0].getJSONObject("metrics");
            if (statsJson[0].has("thumbnail_url") && !statsJson[0].isNull("thumbnail_url")) {
                postDetailStats.setThumbnail_url(statsJson[0].getString("thumbnail_url"));
            }
            if (statsJson[0].has("video_url") && !statsJson[0].isNull("video_url")) {
                postDetailStats.setVideo_url(statsJson[0].getString("video_url"));
            }
            if (metrics.has("comment_count") && !metrics.isNull("comment_count")) {
                postDetailStats.setComment_count(metrics.getInt("comment_count"));
            }
            if (metrics.has("play_count") && !metrics.isNull("play_count")) {
                postDetailStats.setPlay_count(metrics.getInt("play_count"));
            }
            if (metrics.has("share_count") && !metrics.isNull("share_count")) {
                postDetailStats.setShare_count(metrics.getInt("share_count"));
            }
            if (metrics.has("like_count") && !metrics.isNull("like_count")) {
                postDetailStats.setLike_count(metrics.getInt("like_count"));
            }
        } catch (Exception e) {
            return ApiResponse.sendError(500, "API service is not available: " + e.getMessage(),
                    request.getRequestURI());
        }
        return ApiResponse.sendSuccess(200, "Youtube response successfully",
                postDetailStats,
                request.getRequestURI());
    }

}
