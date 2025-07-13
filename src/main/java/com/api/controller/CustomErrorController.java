package com.api.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Custom Error Controller for handling HTML error pages.
 * This works alongside the existing JWTExceptionHandler for API error responses.
 * When a browser requests HTML content and an error occurs, this controller will
 * return the error.html template instead of JSON.
 */
@Controller
public class CustomErrorController implements ErrorController {

    private static final String ERROR_PATH = "/error";

    @RequestMapping(value = ERROR_PATH)
    public String handleError(HttpServletRequest request, Model model) {
        // Get error attributes from the request
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object exception = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        Object requestUri = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);

        // Set default values and format them for the template
        int statusCode = (status != null) ? Integer.parseInt(status.toString()) : 500;
        String errorMessage = extractErrorMessage(exception, message);
        String path = (requestUri != null) ? requestUri.toString() : request.getRequestURI();
        
        // Format timestamp
        String timestamp = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"))
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // Determine error type based on status code
        String error = getErrorType(statusCode);
        
        // Add attributes to the model for the Thymeleaf template
        model.addAttribute("status", statusCode);
        model.addAttribute("error", error);
        model.addAttribute("message", errorMessage);
        model.addAttribute("timestamp", timestamp);
        model.addAttribute("path", path);

        // Return the error template
        return "error";
    }

    /**
     * Extract specific error message from exception or message attribute
     */
    private String extractErrorMessage(Object exception, Object message) {
        // First, try to get message from the exception object
        if (exception instanceof Exception ex) {
            String exceptionMessage = ex.getMessage();
            if (exceptionMessage != null && !exceptionMessage.trim().isEmpty()) {
                return exceptionMessage;
            }
            // If no message, return exception class name for specificity
            return ex.getClass().getSimpleName() + " occurred";
        }
        
        // Second, try to get message from request attribute
        if (message != null) {
            String messageStr = message.toString();
            if (!messageStr.trim().isEmpty()) {
                return messageStr;
            }
        }
        
        // Last resort - provide a generic message
        return "An unexpected error occurred";
    }

    /**
     * Get human-readable error type based on HTTP status code
     */
    private String getErrorType(int statusCode) {
        return switch (statusCode) {
            case 400 -> "Bad Request";
            case 401 -> "Unauthorized";
            case 403 -> "Forbidden";
            case 404 -> "Not Found";
            case 405 -> "Method Not Allowed";
            case 408 -> "Request Timeout";
            case 429 -> "Too Many Requests";
            case 500 -> "Internal Server Error";
            case 502 -> "Bad Gateway";
            case 503 -> "Service Unavailable";
            case 504 -> "Gateway Timeout";
            default -> HttpStatus.valueOf(statusCode).getReasonPhrase();
        };
    }
}