package com.api.controller.websocket.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.api.dto.ApiResponse;
import com.api.model.Permission;
import com.api.model.User;
import com.api.repository.PermissionRepository;
import com.api.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/api/v1/admins")
public class AdminRestController {

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private PermissionRepository permissionRepository;

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @GetMapping("/permissions")
  public ResponseEntity<?> getAllPermissionByRoleId(HttpServletRequest request) {
    List<Permission> permissions = permissionRepository.findAll();
    if (permissions.isEmpty()) {
      permissions = new ArrayList<>();
    }
    return ApiResponse.sendSuccess(200, "Response successfully", permissions, request.getRequestURI());
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @PostMapping("/users/{userId}/{permissionId}")
  public ResponseEntity<?> blockAction(
      @PathVariable("userId") String userId,
      @PathVariable("permissionId") String permissonId,
      @RequestParam("block") Boolean block,
      HttpServletRequest request) {
    Optional<User> userOpt = userRepository.findById(userId);
    if (!userOpt.isPresent()) {
      return ApiResponse.sendError(404, "Not found user: " + userId, request.getRequestURI());
    }
    Optional<Permission> perrmissionOpt = permissionRepository.findById(permissonId);
    if (!perrmissionOpt.isPresent()) {
      return ApiResponse.sendError(404, "Not found permission: " + userId, request.getRequestURI());
    }
    User user = userOpt.get();
    List<String> permissionIds = user.getPermissionIds();
    if (block) {
      if (permissionIds.contains(permissonId)) {
        permissionIds.remove(permissonId);
      }
    } else {
      if (!permissionIds.contains(permissonId)) {
        permissionIds.add(permissonId);
      }
    }
    user.setPermissionIds(permissionIds);
    userRepository.save(user);
    return ApiResponse.sendSuccess(200, "Block permission successfully", null, request.getRequestURI());
  }
}
