package com.api.controller;

import com.api.service.PackageTypeService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/packageTypes")
public class PackageTypeController {

    @Autowired
    private PackageTypeService packageTypeService;

    @PostMapping("")
    public ResponseEntity<?> createPackage(
            @RequestPart("packageType") String obj,
            HttpServletRequest request) {
        return packageTypeService.createPackage(packageTypeService.convertToPackage(obj), request);
    }
    @GetMapping("")
    public ResponseEntity<?> getPackage(
            @PathVariable("roleId") String roleId,
            HttpServletRequest request){
        return packageTypeService.getAllPackageRoleId(roleId, request);
    }
    @PutMapping("")
     public ResponseEntity<?> updatePost(@PathVariable("contentId") String contentId,
            @RequestPart("packageType") String obj,
            HttpServletRequest request) {
        return packageTypeService.updatePackage(contentId, packageTypeService.convertToPackage(obj),request);
    }
    @DeleteMapping("")
     public ResponseEntity<?> deleteCampaign(
            @PathVariable("packageId") String packageId,
            HttpServletRequest request) {
        return packageTypeService.deletePackage(packageId, request);
    }
}
