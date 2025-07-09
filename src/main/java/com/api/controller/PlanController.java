package com.api.controller;

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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/plans")
public class PlanController {

    @Autowired
    private PlanService planService;

    @PostMapping("")
    public ResponseEntity<?> createPackage(
            @RequestBody Plan plan,
            HttpServletRequest request) {
        return planService.createPlan(plan, request);
    }
    @GetMapping("")
    public ResponseEntity<?> getPlan(
            @PathVariable("roleId") String roleId,
            HttpServletRequest request){
        return planService.getAllPlanByRoleId(roleId, request);
    }
    @PutMapping("")
     public ResponseEntity<?> updatePlan(@PathVariable("planId") String planId,
            @RequestPart("plan") String obj,
            HttpServletRequest request) {
        return planService.updatePlan(planId, planService.convertToPlan(obj),request);
    }
    @DeleteMapping("")
     public ResponseEntity<?> deletePlan(
            @PathVariable("packageId") String packageId,
            HttpServletRequest request) {
        return planService.deletePlan(packageId, request);
    }
}
