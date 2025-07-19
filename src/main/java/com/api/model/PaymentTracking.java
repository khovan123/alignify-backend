package com.api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "payment_tracking")
public class PaymentTracking {
    @Id
    private String id;
    private long orderCode;
    private String userId;
    private String planId;
    private String status;
}
