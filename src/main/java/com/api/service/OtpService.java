package com.api.service;

import com.api.model.Otp;
import com.api.repository.OtpRepository;
import java.time.LocalDateTime;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OtpService {

    @Autowired
    private OtpRepository otpRepository;

    public String generateOtp(String email) {
        Optional<Otp> existingOtp = otpRepository.findByEmail(email);
        if (existingOtp.isPresent()) {
            Otp otp = existingOtp.get();
            otpRepository.delete(otp);
            if (otp.getRequestCount() >= 5 && otp.getCreatedAt().isAfter(LocalDateTime.now().minusMinutes(30))) {
                throw new RuntimeException("Too many OTP requests. Please try again after 1 hour.");
            }
            if (otp.getCreatedAt().isAfter(LocalDateTime.now().minusMinutes(30))) {
                otp.setRequestCount(otp.getRequestCount() + 1);
            } else {
                otp.setRequestCount(1);
                otp.setCreatedAt(LocalDateTime.now());
            }
        }

        String otpCode;
        boolean isUnique;
        int maxAttempts = 10;
        do {
            otpCode = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 6).toUpperCase();
            isUnique = !otpRepository.findByEmailAndOtpCode(email, otpCode).isPresent();
        } while (!isUnique && maxAttempts-- > 0);

        if (!isUnique) {
            throw new RuntimeException("Unable to generate unique OTP after " + maxAttempts + " attempts");
        }

        Otp newOtp;

        if (!existingOtp.isPresent()) {
            newOtp = new Otp(email, otpCode);
        } else {
            newOtp = new Otp(email, otpCode, existingOtp.get().getRequestCount(), existingOtp.get().getCreatedAt());
        }

        otpRepository.save(newOtp);

        return otpCode;
    }

    public boolean verifyOtp(String email, String otpCode) {
        Optional<Otp> otpOptional = otpRepository.findByEmail(email);
        if (otpOptional.isPresent()) {
            Otp otp = otpOptional.get();
            if (otp.getOtpCode().equalsIgnoreCase(otpCode.toUpperCase())) {
                otpRepository.delete(otp);
                return true;
            } else {
                if (otp.getAttemptCount() >= 5) {
                    otpRepository.delete(otp);
                    throw new RuntimeException("Too many OTP requests. Please try again after 1 hour.");
                }
                otp.setAttemptCount(otp.getAttemptCount() + 1);
                otpRepository.save(otp);
            }
        } else {
            throw new RuntimeException("Your OTP is expired.");
        }
        return false;
    }
}
