package com.api.util;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class Helper {
    @Autowired
    private static Cloudinary cloudinary;
    @Value("${cloudinary.upload-preset}")
    private static String uploadPreset;

    public static boolean isOwner(String id, HttpServletRequest request) {
        DecodedJWT decodeJWT = JwtUtil.decodeToken(request);
        String ownerId = decodeJWT.getSubject();
        return ownerId.equalsIgnoreCase(id);
    }

    public static String saveImage(MultipartFile file) throws Exception {
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap("upload_preset", uploadPreset));
            return (String) uploadResult.get("secure_url");
        } catch (IOException e) {
            throw new Exception("Internal server error");
        }
    }
}
