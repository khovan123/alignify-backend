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
    public ResponseEntity<?> createTransaction(@RequestBody Transaction transaction) {
        try {
            Transaction createdTransaction = transactionService.createTransaction(transaction);
            return new ResponseEntity<>(createdTransaction, HttpStatus.CREATED);
        } catch (Exception e) {
            // Log lỗi nếu cần
            return new ResponseEntity<>("Failed to create transaction: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/create/payment")
    public ResponseEntity<?> createPayment(@RequestBody Map<String, Object> request) {
        try {
            // Giả sử nhận userId và amount từ frontend
            String userId = (String) request.get("userId");
            double amount = Double.parseDouble(request.get("amount").toString());

            // Tạo transactionId ngẫu nhiên hoặc theo logic của bạn
            String transactionId = UUID.randomUUID().toString();

            // Tạo Transaction (có thể thêm nhiều trường khác)
            Transaction transaction = new Transaction();
            transaction.setTransactionId(transactionId);
            transaction.setUserId(userId);
            transaction.setAmount(amount);
            transaction.setStatus("pending");
            transaction.setCreatedDate(new Date());
            // Lưu transaction vào DB
            transactionService.createTransaction(transaction);

            // Tạo params VNPAY
            Map<String, String> vnpParams = vnpayConfig.buildVNPayParams(amount, transactionId);

            // Build URL thanh toán (vnp_PayUrl + query params)
            StringBuilder paymentUrl = new StringBuilder(vnpayConfig.getVnp_PayUrl());
            paymentUrl.append("?");
            for (Map.Entry<String, String> entry : vnpParams.entrySet()) {
                paymentUrl.append(entry.getKey())
                          .append("=")
                          .append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
                          .append("&");
            }
            // Xóa dấu & cuối cùng
            paymentUrl.deleteCharAt(paymentUrl.length() - 1);

            // Trả về URL thanh toán cho frontend
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
