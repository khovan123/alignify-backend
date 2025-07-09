package com.api.service;

import com.api.config.EnvConfig;
import com.api.dto.ApiResponse;
import com.api.model.PackageType;
import com.api.repository.PackageTypeRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PackageTypeService {

    @Autowired
    private PackageTypeRepository packageTypeRepository;

    public ResponseEntity<?> createPackage(PackageType packageType, HttpServletRequest request) {
        packageTypeRepository.save(packageType);
        return ApiResponse.sendSuccess(200, "Success", packageType, request.getRequestURI());

    }

    public ResponseEntity<?> getAllPackageRoleId(String roleId, HttpServletRequest request) {
        if (roleId.equalsIgnoreCase(EnvConfig.BRAND_ROLE_ID) || roleId.equalsIgnoreCase(EnvConfig.INFLUENCER_ROLE_ID)) {
            List<PackageType> packageTypes = packageTypeRepository.findByRoleId(roleId);
            return ApiResponse.sendSuccess(200, "Response successfully", packageTypes, request.getRequestURI());
        }
        if (roleId == null) {
            List<PackageType> packageTypes = packageTypeRepository.findAll();
            return ApiResponse.sendSuccess(200, "Response successfully", packageTypes, request.getRequestURI());
        }
        return ApiResponse.sendError(400, "RoleID not exist!", request.getRequestURI());
    }

    public ResponseEntity<?> deletePackage(String packageId, HttpServletRequest request) {
        Optional<PackageType> packageOpt = packageTypeRepository.findById(packageId);
        if (!packageOpt.isPresent()) {
            return ApiResponse.sendError(403, "PackageType not found", request.getRequestURI());
        }
        packageTypeRepository.deleteById(packageId);
        return ApiResponse.sendSuccess(204, "PackageType deleted successfully", null, request.getRequestURI());
    }

    public ResponseEntity<?> updatePackage(String packageId, PackageType updatedPackage, HttpServletRequest request) {
        Optional<PackageType> packageOpt = packageTypeRepository.findById(packageId);
        if (packageOpt.isPresent()) {
            PackageType packageType = packageOpt.get();
            double newPrice = updatedPackage.getPrice();
            if (newPrice < 0) {
                return ApiResponse.sendError(400, "Price must be a non-negative number", request.getRequestURI());
            }
            if (updatedPackage.getPackageName() != null) {
                packageType.setPackageName(updatedPackage.getPackageName());
            }
            if (updatedPackage.getDiscription() != null) {
                packageType.setDiscription(updatedPackage.getDiscription());
            }
            if (updatedPackage.getRoleId() != null) {
                packageType.setRoleId(updatedPackage.getRoleId());
            }
            if (updatedPackage.getSubcribeType() != null) {
                packageType.setSubcribeType(updatedPackage.getSubcribeType());
            }
            if (newPrice != packageType.getPrice()) {
                packageType.setPrice(newPrice);
            }
            if (updatedPackage.getFeature() != null
                    && !updatedPackage.getFeature().isEmpty()) {
                packageType.setFeature(updatedPackage.getFeature());
            }
            packageTypeRepository.save(packageType);
            return ApiResponse.sendSuccess(200, "PackageType updated successfully", packageType, request.getRequestURI());
        } else {
            return ApiResponse.sendError(404, "PackageType not found", request.getRequestURI());
        }
    }
     public PackageType convertToPackage(String obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            return mapper.readValue(obj, PackageType.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Invalid package JSON: " + e.getMessage(), e);
        }
    }
}
