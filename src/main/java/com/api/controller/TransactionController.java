package com.api.controller;

import com.api.config.VNPayConfig;
import com.api.model.Transaction;
import com.api.service.TransactionService;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private VNPayConfig vnpayConfig;

    @PostMapping("/create")
    public ResponseEntity<?> createPayment(@RequestBody Map<String, Object> request) {
        try {
            String userId = (String) request.get("userId");
            double amount = Double.parseDouble(request.get("amount").toString());

            String transactionId = UUID.randomUUID().toString();

            // Lưu transaction
            Transaction transaction = new Transaction();
            transaction.setTransactionId(transactionId);
            transaction.setUserId(userId);
            transaction.setAmount(amount);
            transaction.setStatus("pending");
            transaction.setCreatedDate(new Date());
            transactionService.createTransaction(transaction);

            // Tạo tham số cho VNPay
            Map<String, String> vnpParams = vnpayConfig.buildVNPayParams(amount, transactionId);

            // ✅ Tạo SecureHash
            String secureHash = vnpayConfig.generateSecureHash(vnpParams);
            vnpParams.put("vnp_SecureHash", secureHash);

            // ✅ Tạo URL thanh toán hoàn chỉnh
            StringBuilder paymentUrl = new StringBuilder(vnpayConfig.getVnp_PayUrl());
            paymentUrl.append("?");
            for (Map.Entry<String, String> entry : vnpParams.entrySet()) {
                paymentUrl.append(entry.getKey())
                        .append("=")
                        .append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
                        .append("&");
            }
            paymentUrl.deleteCharAt(paymentUrl.length() - 1); // Xóa dấu & cuối

            return ResponseEntity.ok(Map.of("paymentUrl", paymentUrl.toString()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTransactionsByUserId(@PathVariable("id") String userId) {
        try {
            List<Transaction> transactions = transactionService.viewTransactionsByUserId(userId);
            return ResponseEntity.ok(Map.of("data", transactions));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Unable to fetch transactions"));
        }
    }
}
