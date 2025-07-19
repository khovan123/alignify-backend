package com.api.service;

import com.api.dto.ApiResponse;
import com.api.model.UserPlan;
import com.api.repository.UserPlanRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserPlanService {
    @Autowired
    private UserPlanRepository userPlanRepository;

    public ResponseEntity<?> getUserPlan(HttpServletRequest request){
        if(userPlanRepository.findAll().isEmpty()){
            return ApiResponse.sendError(400,"Empty list", request.getRequestURI());
        }
        return ApiResponse.sendSuccess(200,"Succesffuly", userPlanRepository.findAll(),request.getRequestURI());

    }
    public ResponseEntity<?> deleteUserPlan(String planId, HttpServletRequest request){
        Optional<UserPlan> userPlanOpt = userPlanRepository.findById(planId);
        if (!userPlanOpt.isPresent()) {
            return ApiResponse.sendError(403, "User Plan not found", request.getRequestURI());
        }
        userPlanRepository.deleteById(planId);
        return ApiResponse.sendSuccess(204, "User Plan deleted successfully", null, request.getRequestURI());
    }
}
