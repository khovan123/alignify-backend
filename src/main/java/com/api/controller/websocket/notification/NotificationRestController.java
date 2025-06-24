package com.api.controller.websocket.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.api.dto.ApiResponse;
import com.api.model.Notification;
import com.api.repository.NotificationRepository;
import com.api.security.CustomUserDetails;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/api/v1/notifications")
public class NotificationRestController {
  @Autowired
  private NotificationRepository notificationRepository;

  @GetMapping("")
  public ResponseEntity<?> getNotifications(
      @RequestParam(defaultValue = "0", name = "pageNumber") int pageNumber,
      @RequestParam(defaultValue = "10", name = "pageSize") int pageSize,
      @AuthenticationPrincipal CustomUserDetails customUserDetails,
      HttpServletRequest request) {
    String userId = customUserDetails.getUserId();
    PageRequest pageable = PageRequest.of(pageNumber, pageSize);
    Page<Notification> notificationPage = notificationRepository.findAllByUserIdOrderByCreatedAtDesc(userId, pageable);
    return ApiResponse.sendSuccess(200, "Response successfully", notificationPage.toList(), request.getRequestURI());
  }
}
