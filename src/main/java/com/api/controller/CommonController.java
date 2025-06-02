package com.api.controller;

import com.api.dto.ApiResponse;
import com.api.repository.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class CommonController {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/role")
    public ResponseEntity<?> getAllRole() {
        return ResponseEntity.status(200).body(Map.of(
                "data", Map.of("role", roleRepository.findAll())));
    }

    @GetMapping("/category")
    public ResponseEntity<?> getAllCategory() {
        return ResponseEntity.status(200).body(Map.of(
                "data", Map.of("category", categoryRepository.findAll())));
    }

}
