package com.api.repository;

import com.api.model.PaymentTracking;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PaymentTrackingRepository  extends MongoRepository<PaymentTracking, String> {
    PaymentTracking findByOrderCode(long orderCode);
}
