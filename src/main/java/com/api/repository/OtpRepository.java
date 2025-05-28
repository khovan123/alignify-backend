package com.api.repository;

import com.api.model.Otp;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OtpRepository extends MongoRepository<Otp, String> {

    Optional<Otp> findByEmail(String email);
    Optional<Otp> findByEmailAndOtpCode(String email, String otpCode);
}
