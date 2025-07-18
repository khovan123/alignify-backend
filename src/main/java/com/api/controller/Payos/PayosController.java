
package com.api.controller.Payos;


import com.api.model.PaymentTracking;
import com.api.model.Plan;
import com.api.model.User;
import com.api.repository.PaymentTrackingRepository;
import com.api.repository.PlanRepository;
import com.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import vn.payos.PayOS;
import vn.payos.type.Webhook;
import vn.payos.type.WebhookData;

@RestController
@RequestMapping("api/v1/payment")
public class PayosController {
    private final PayOS payOS;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PlanRepository planRepository;
    @Autowired
    private PaymentTrackingRepository paymentTrackingRepository;

    public PayosController(PayOS payOS) {
        super();
        this.payOS = payOS;

    }

    @PostMapping(path = "/payos_transfer_handler")
    public ObjectNode payosTransferHandler(@RequestBody ObjectNode body)
            throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();

        try {
            Webhook webhookBody = objectMapper.treeToValue(body, Webhook.class);
            WebhookData data = payOS.verifyPaymentWebhookData(webhookBody);

            // Kiểm tra trạng thái thanh toán thành công
            if (
                    data.getDesc() != null && data.getDesc().toLowerCase().contains("thành công")
                            || "00".equals(data.getCode())
            ) {
                long orderCode = data.getOrderCode();

                // 1. Tìm PaymentTracking theo orderCode
                PaymentTracking tracking = paymentTrackingRepository.findByOrderCode(orderCode);
                if (tracking == null || "COMPLETED".equals(tracking.getStatus())) {
                    throw new RuntimeException("Đơn hàng không tồn tại hoặc đã xử lý");
                }

                // 2. Tìm user và plan
                User user = userRepository.findById(tracking.getUserId()).orElseThrow();
                Plan plan = planRepository.findById(tracking.getPlanId()).orElseThrow();

                // 3. Cập nhật quyền
                user.setPermissionIds(plan.getPermissionIds());
                user.setUserPlanId(plan.getPlanId());
                userRepository.save(user);

                // 4. Đánh dấu đơn đã xử lý
                tracking.setStatus("COMPLETED");
                paymentTrackingRepository.save(tracking);
            }

            // Trả response cho PayOS
            response.put("error", 0);
            response.put("message", "Webhook delivered");
            response.set("data", null);
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
