package com.api.controller;

import com.api.dto.ApiResponse;
import com.api.repository.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class CommonController {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/role")
    public ResponseEntity<?> getAllRole(HttpServletRequest request) {
        return ApiResponse.sendSuccess(200, "Roles", roleRepository.findAll(), request.getRequestURI());
    }

    @GetMapping("/category")
    public ResponseEntity<?> getAllCategory(HttpServletRequest request) {
        return ApiResponse.sendSuccess(200, "Categories", categoryRepository.findAll(), request.getRequestURI());
    }

}
