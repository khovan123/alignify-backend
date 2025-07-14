package com.api.controller;

import com.api.dto.request.PlanRequest;
import com.api.model.Plan;
import com.api.service.PlanService;
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

    @GetMapping("")
    public ResponseEntity<?> getPlan(
            @RequestParam("roleId") String roleId,
            HttpServletRequest request) {
        return planService.getAllPlanByRoleId(roleId, request);
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
}
