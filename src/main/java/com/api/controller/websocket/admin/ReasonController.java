package com.api.controller.websocket.admin;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.api.dto.ApiResponse;
import com.api.model.Reason;
import com.api.repository.ReasonRepository;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/api/v1/reasons")
public class ReasonController {
  @Autowired
  private ReasonRepository reasonRepository;

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @GetMapping("")
  public ResponseEntity<?> getAllReasons(HttpServletRequest request) {
    List<Reason> reasons = reasonRepository.findAll();
    if (reasons.isEmpty()) {
      reasons = new ArrayList<>();
    }
    return ApiResponse.sendSuccess(200, "Response successfully", reasons, request.getRequestURI());
  }

}
