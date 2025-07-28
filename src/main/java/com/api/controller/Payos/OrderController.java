
package com.api.controller.Payos;

import com.api.dto.request.CreatePaymentLinkRequest;

import java.net.URI;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import com.api.model.Plan;
import com.api.model.User;
import com.api.model.UserPlan;
import com.api.repository.PlanRepository;
import com.api.repository.UserPlanRepository;
import com.api.repository.UserRepository;
import com.api.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import vn.payos.PayOS;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.ItemData;
import vn.payos.type.PaymentData;
import vn.payos.type.PaymentLinkData;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {
    private final PayOS payOS;
    @Autowired
    private PlanRepository planRepository;
    @Autowired
    private UserPlanRepository userPlanRepository;
    @Autowired
    private UserRepository userRepository;

    private static final String FRONTEND_BASE_URL = "https://alignify.vercel.app";
    private static final String BACKEND_BASE_URL = "https://alignify-backend.onrender.com";

    private static final String SUCCESS_REACT_URL = FRONTEND_BASE_URL + "/upgrade-plan";
    private static final String CANCEL_REACT_URL = FRONTEND_BASE_URL + "/upgrade-plan";
    private static final String FAILURE_REACT_URL = FRONTEND_BASE_URL + "/payment/failure";


    public OrderController(PayOS payOS, PlanRepository planRepository) {
        super();
        this.payOS = payOS;
        this.planRepository = planRepository;
    }

    @PostMapping(path = "/create")
    public ObjectNode createPaymentLink(@RequestBody CreatePaymentLinkRequest requestBody, @AuthenticationPrincipal CustomUserDetails userDetails) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();

        try {
            Optional<Plan> plan = planRepository.findByPlanId(requestBody.getPlanId());
            if (!plan.isPresent()) {
                response.put("error", -1);
                response.put("message", "Plan not found");
                return response;
            }
            String planName = plan.get().getPlanName();

            Double planPrice = plan.get().getPrice();
            if (planPrice == null || planPrice <= 0) {
                response.put("error", -1);
                response.put("message", "Invalid plan price: must be > 0");
                return response;
            }
            int price = plan.get().getPrice().intValue();

            String timestamp = String.valueOf(new Date().getTime());
            long orderCode = Long.parseLong(timestamp.substring(timestamp.length() - 6));

            UserPlan userPlan = new UserPlan();
            userPlan.setUserId(userDetails.getUserId());
            userPlan.setPlanId(requestBody.getPlanId());
            userPlan.setCreatedAt(ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
            userPlan.setStatus("PENDING");
            userPlan.setUserPlanId(String.valueOf(orderCode));
            userPlanRepository.save(userPlan);

            ItemData item = ItemData.builder()
                    .name(planName)
                    .price(price)
                    .quantity(1)
                    .build();

            PaymentData paymentData = PaymentData.builder()
                    .orderCode(orderCode)
                    .description(plan.get().getPlanName())
                    .amount(price)
                    .item(item)
                    .returnUrl(SUCCESS_REACT_URL)
                    .cancelUrl(CANCEL_REACT_URL)
                    .build();

            CheckoutResponseData data = payOS.createPaymentLink(paymentData);

            response.put("error", 0);
            response.put("message", "success");
            response.set("data", objectMapper.valueToTree(data));
            return response;

        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", -1);
            response.put("message", "fail");
            response.set("data", null);
            return response;
        }
    }
//    @GetMapping("/handle-payment")
//    public ResponseEntity<Void> handlePaymentRedirect(@RequestParam Map<String, String> params) {
//        try {
//            long orderCode = Long.parseLong(params.get("orderCode")); // PayOS gửi về
//
//            // Lấy thông tin đơn thanh toán từ PayOS
//            PaymentLinkData payment = payOS.getPaymentLinkInformation(orderCode);
//
//            if ("PAID".equals(payment.getStatus())) {
//                // Parse description để lấy userId và planId
//                String description = payment.get();
//                String[] parts = description.split("\\|");
//                String userId = null;
//                String planId = null;
//
//                for (String part : parts) {
//                    if (part.startsWith("userId:")) {
//                        userId = part.replace("userId:", "").trim();
//                    } else if (part.startsWith("planId:")) {
//                        planId = part.replace("planId:", "").trim();
//                    }
//                }
//
//                if (userId != null && planId != null) {
//                    Optional<User> userOpt = userRepository.findById(userId);
//                    Optional<Plan> planOpt = planRepository.findById(planId);
//
//                    if (userOpt.isPresent() && planOpt.isPresent()) {
//                        UserPlan userPlan = new UserPlan();
//                        userPlan.setUserId(userId);
//                        userPlan.setPlanId(planId);
//                        userPlan.setOrderCode(orderCode);
//                        userPlan.setCreatedAt(ZonedDateTime.now());
//
//                        userPlanRepository.save(userPlan);
//                    } else {
//                        System.out.println("User or Plan not found.");
//                    }
//                } else {
//                    System.out.println("Missing userId or planId in description.");
//                }
//            }
//
//            // Redirect về frontend
//            URI redirectUri = URI.create(SUCCESS_REACT_URL);
//            return ResponseEntity.status(HttpStatus.FOUND).location(redirectUri).build();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            URI failRedirect = URI.create(FAILURE_REACT_URL);
//            return ResponseEntity.status(HttpStatus.FOUND).location(failRedirect).build();
//        }
//    }


    @GetMapping(path = "/{orderId}")
    public ObjectNode getOrderById(@PathVariable("orderId") long orderId) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();

        try {
            PaymentLinkData order = payOS.getPaymentLinkInformation(orderId);

            response.set("data", objectMapper.valueToTree(order));
            response.put("error", 0);
            response.put("message", "ok");
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", -1);
            response.put("message", e.getMessage());
            response.set("data", null);
            return response;
        }

    }

    @PutMapping(path = "/{orderId}")
    public ObjectNode cancelOrder(@PathVariable("orderId") int orderId) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        try {
            PaymentLinkData order = payOS.cancelPaymentLink(orderId, null);
            response.set("data", objectMapper.valueToTree(order));
            response.put("error", 0);
            response.put("message", "ok");
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", -1);
            response.put("message", e.getMessage());
            response.set("data", null);
            return response;
        }
    }

    @PostMapping(path = "/confirm-webhook")
    public ObjectNode confirmWebhook(@RequestBody Map<String, String> requestBody) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        try {
            String str = payOS.confirmWebhook(requestBody.get("webhookUrl"));
            response.set("data", objectMapper.valueToTree(str));
            response.put("error", 0);
            response.put("message", "ok");
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", -1);
            response.put("message", e.getMessage());
            response.set("data", null);
            return response;
        }
    }
}

