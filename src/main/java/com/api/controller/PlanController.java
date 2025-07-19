package com.api.controller;

import com.api.dto.request.PlanRequest;
import com.api.model.Plan;
import com.api.service.PlanService;
import com.api.service.UserPlanService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/plans")
public class PlanController {

    @Autowired
    private PlanService planService;
    @Autowired
    private UserPlanService userPlanService;
    @PostMapping("")
    public ResponseEntity<?> createPlan(
            @RequestBody  PlanRequest plan,
            HttpServletRequest request) {
        return planService.createPlan(plan, request);
    }

    @GetMapping("/permission")
    public ResponseEntity<?> getPermission(
            HttpServletRequest request) {
        return planService.getPermission(request);
    }

    @GetMapping("/brand")
    public ResponseEntity<?> getPlanByBrandRole(
            HttpServletRequest request) {
        return planService.getAllPlanByBrandRole(request);
    }

    @GetMapping("/influencer")
    public ResponseEntity<?> getPlanByInfluencerRole(
            HttpServletRequest request) {
        return planService.getAllPlanByInfluencerRole(request);
    }

    @PutMapping("/{planId}")
    public ResponseEntity<?> updatePlan(@PathVariable("planId") String planId,
            @RequestBody Plan plan,
            HttpServletRequest request) {
        return planService.updatePlan(planId, plan, request);
    }

    @DeleteMapping("/{planId}")
    public ResponseEntity<?> deletePlan(
            @PathVariable("planId") String planId,
            HttpServletRequest request) {
        return planService.deletePlan(planId, request);
    }

    @GetMapping("/userPlan")
    public ResponseEntity<?> getUserPlan(HttpServletRequest request){
        return userPlanService.getUserPlan(request);
    }
    @DeleteMapping("/userPlan/{userPlanId}")
    public  ResponseEntity<?> deleteUserPlan(
            @PathVariable("userPlanId") String userPlanId,
            HttpServletRequest request){
        return userPlanService.deleteUserPlan(userPlanId,request);
    }
}
