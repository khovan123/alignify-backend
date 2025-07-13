package com.api.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import jakarta.servlet.RequestDispatcher;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for CustomErrorController.
 * Tests the error page functionality for different HTTP status codes.
 */
class CustomErrorControllerTest {

    private CustomErrorController errorController;
    private MockHttpServletRequest request;
    private Model model;

    @BeforeEach
    void setUp() {
        errorController = new CustomErrorController();
        request = new MockHttpServletRequest();
        model = new ConcurrentModel();
    }

    @Test
    void testErrorPageWith404Error() {
        // Set up request attributes for 404 error
        request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, 404);
        request.setAttribute(RequestDispatcher.ERROR_MESSAGE, "Page not found");
        request.setAttribute(RequestDispatcher.ERROR_REQUEST_URI, "/nonexistent");

        // Call the error handler
        String viewName = errorController.handleError(request, model);

        // Verify the response
        assertEquals("error", viewName);
        assertEquals(404, model.getAttribute("status"));
        assertEquals("Not Found", model.getAttribute("error"));
        assertEquals("Page not found", model.getAttribute("message"));
        assertEquals("/nonexistent", model.getAttribute("path"));
        assertNotNull(model.getAttribute("timestamp"));
    }

    @Test
    void testErrorPageWith500Error() {
        // Set up request attributes for 500 error
        request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, 500);
        request.setAttribute(RequestDispatcher.ERROR_MESSAGE, "Internal server error");
        request.setAttribute(RequestDispatcher.ERROR_REQUEST_URI, "/api/test");

        // Call the error handler
        String viewName = errorController.handleError(request, model);

        // Verify the response
        assertEquals("error", viewName);
        assertEquals(500, model.getAttribute("status"));
        assertEquals("Internal Server Error", model.getAttribute("error"));
        assertEquals("Internal server error", model.getAttribute("message"));
        assertEquals("/api/test", model.getAttribute("path"));
        assertNotNull(model.getAttribute("timestamp"));
    }

    @Test
    void testErrorPageWithDefaultValues() {
        // Call error handler with no request attributes
        request.setRequestURI("/current-path");
        String viewName = errorController.handleError(request, model);

        // Verify defaults are used
        assertEquals("error", viewName);
        assertEquals(500, model.getAttribute("status"));
        assertEquals("Internal Server Error", model.getAttribute("error"));
        assertEquals("An unexpected error occurred", model.getAttribute("message"));
        assertEquals("/current-path", model.getAttribute("path"));
        assertNotNull(model.getAttribute("timestamp"));
    }

    @Test
    void testErrorPageWith401Error() {
        // Set up request attributes for 401 error
        request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, 401);
        request.setAttribute(RequestDispatcher.ERROR_MESSAGE, "Authentication required");
        request.setAttribute(RequestDispatcher.ERROR_REQUEST_URI, "/protected");

        // Call the error handler
        String viewName = errorController.handleError(request, model);

        // Verify the response
        assertEquals("error", viewName);
        assertEquals(401, model.getAttribute("status"));
        assertEquals("Unauthorized", model.getAttribute("error"));
        assertEquals("Authentication required", model.getAttribute("message"));
        assertEquals("/protected", model.getAttribute("path"));
        assertNotNull(model.getAttribute("timestamp"));
    }

    @Test
    void testErrorPageWith403Error() {
        // Set up request attributes for 403 error
        request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, 403);
        request.setAttribute(RequestDispatcher.ERROR_MESSAGE, "Access denied");
        request.setAttribute(RequestDispatcher.ERROR_REQUEST_URI, "/admin");

        // Call the error handler
        String viewName = errorController.handleError(request, model);

        // Verify the response
        assertEquals("error", viewName);
        assertEquals(403, model.getAttribute("status"));
        assertEquals("Forbidden", model.getAttribute("error"));
        assertEquals("Access denied", model.getAttribute("message"));
        assertEquals("/admin", model.getAttribute("path"));
        assertNotNull(model.getAttribute("timestamp"));
    }
}