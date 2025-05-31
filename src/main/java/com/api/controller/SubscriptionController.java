package com.api.controller;

import com.api.service.SubscriptionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/subscription")
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    @PostMapping("/select")
    public ResponseEntity<?> selectSubscription(@RequestParam("userId") String userId,
                                                @RequestParam("subscriptionId") String subscriptionId,
                                                HttpServletRequest request) {
        return subscriptionService.selectSubscription(userId, subscriptionId, request);
    }

    @GetMapping("/success")
    public ResponseEntity<?> paymentSuccess(@RequestParam("paymentId") String paymentId,
                                            @RequestParam("PayerID") String payerId,
                                            @RequestParam("userId") String userId,
                                            @RequestParam("subscriptionId") String subscriptionId) {
        return subscriptionService.handleSuccessPayment(paymentId, payerId, userId, subscriptionId);
    }

    @GetMapping("/cancel")
    public ResponseEntity<?> paymentCancel() {
        return ResponseEntity.ok("Payment was cancelled.");
    }
}
