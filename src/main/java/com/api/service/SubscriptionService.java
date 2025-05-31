package com.api.service;

import com.api.enumeration.PaypalPaymentIntent;
import com.api.enumeration.PaypalPaymentMethod;
import com.api.enumeration.Currency;
import com.api.enumeration.Payment_method;
import com.api.enumeration.Status;
import com.api.model.*;
import com.api.repository.*;
import com.api.util.Utils;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class SubscriptionService {

    @Autowired
    private ISubscriptionRepository subscriptionRepo;

    @Autowired
    private IUserRepository userRepo;

    @Autowired
    private IUserSubscriptionRepository userSubscriptionRepo;

    @Autowired
    private ISubscriptionTransactionRepository transactionRepo;

    @Autowired
    private PaypalService paypalService;

    public ResponseEntity<?> selectSubscription(String userId, String subscriptionId, HttpServletRequest request) {
        Optional<Subscription> optSub = subscriptionRepo.findById(subscriptionId);
        if (optSub.isEmpty()) {
            return ResponseEntity.badRequest().body("Subscription not found");
        }

        Subscription subscription = optSub.get();
        String cancelUrl = Utils.getBaseURL(request) + "/api/v1/subscription/cancel";
        String successUrl = Utils.getBaseURL(request) + "/api/v1/subscription/success?userId=" + userId + "&subscriptionId=" + subscriptionId;

        try {
            Payment payment = paypalService.createPayment(
                    subscription.getPrice(),
                    subscription.getCurrency(),
                    PaypalPaymentMethod.paypal,
                    PaypalPaymentIntent.sale,
                    "Subscription to " + subscription.getName(),
                    cancelUrl,
                    successUrl
            );

            for (Links link : payment.getLinks()) {
                if (link.getRel().equals("approval_url")) {
                    return ResponseEntity.ok(link.getHref());
                }
            }
        } catch (PayPalRESTException e) {
            return ResponseEntity.internalServerError().body("Error during payment: " + e.getMessage());
        }

        return ResponseEntity.badRequest().body("Unable to create PayPal payment");
    }

    public ResponseEntity<?> handleSuccessPayment(String paymentId, String payerId, String userId, String subscriptionId) {
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);
            if (!"approved".equals(payment.getState())) {
                return ResponseEntity.badRequest().body("Payment was not approved");
            }

            Subscription subscription = subscriptionRepo.findById(subscriptionId).orElse(null);
            if (subscription == null) {
                return ResponseEntity.badRequest().body("Subscription not found");
            }

            Date now = new Date();

            // Create UserSubscription
            UserSubscriptions userSub = new UserSubscriptions();
            userSub.setUserSubscriptonId(UUID.randomUUID().toString());
            userSub.setUserId(userId);
            userSub.setSubscriptionId(subscriptionId);
            userSub.setStatus("active");
            userSub.setStart_date(now);
            userSub.setEnd_date(new Date(now.getTime() + 30L * 24 * 60 * 60 * 1000)); // 30 days
            userSub.setCreate_at(now);
            userSub.setUpdate_at(now);
            userSubscriptionRepo.save(userSub);

            // Create transaction record
            SubscriptionTransactions tx = new SubscriptionTransactions();
            tx.setTransactionId(UUID.randomUUID().toString());
            tx.setUserSubscriptionId(userSub.getUserSubscriptonId());
            tx.setUserId(userId);
            tx.setAmount(subscription.getPrice());
            tx.setCurrency(Currency.valueOf(subscription.getCurrency()));
            tx.setStatus(Status.Completed);
            tx.setPayment_method(PaypalPaymentMethod.paypal);
            tx.setTransaction_reference(paymentId);
            tx.setDescription("Subscription payment");
            tx.setCreated_at(now);
            tx.setCompleted_at(now);
            tx.setIs_refunded(false);
            transactionRepo.save(tx);

            // Update user's role
            userRepo.findById(userId).ifPresent(user -> {
                user.setRoleId(subscription.getRoleId());
                userRepo.save(user);
            });

            return ResponseEntity.ok("Payment success. Subscription activated.");
        } catch (PayPalRESTException e) {
            return ResponseEntity.internalServerError().body("Payment execution failed: " + e.getMessage());
        }
    }
}
