package com.api.dto;

import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;

public class ApiResponse {

    public static <T> ResponseEntity<?> sendSuccess(int code, String message, T data, String path) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", code);
        map.put("message", message);
        map.put("data", data);
        map.put("timestamp", ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        map.put("path", path);
        return ResponseEntity.status(code).body(map);
    }

    public static ResponseEntity<?> sendError(int code, String error, String path) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", code);
        map.put("error", error);
        map.put("timestamp", ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        map.put("path", path);
        return ResponseEntity.status(code).body(map);
    }
}
