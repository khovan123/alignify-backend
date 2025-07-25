package com.api.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.api.config.PaypalPaymentIntent;
import com.api.config.PaypalPaymentMethod;
import com.api.dto.request.PaypalRequest;
import com.api.service.PaypalService;
import com.api.util.UtilsPaypal;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1")
public class PaymentController {

    public static final String URL_PAYPAL_SUCCESS = "pay/success";
    public static final String URL_PAYPAL_CANCEL = "pay/cancel";

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private PaypalService paypalService;

    @GetMapping("")
    public String index() {
        return "index";
    }

    @PostMapping("/pay")
    @ResponseBody
    public Map<String, String> pay(HttpServletRequest request, @RequestBody PaypalRequest paypalRequest) {
        double price = paypalRequest.getPrice();
        String cancelUrl = UtilsPaypal.getBaseURL(request) + "/" + URL_PAYPAL_CANCEL;
        String successUrl = UtilsPaypal.getBaseURL(request) + "/" + URL_PAYPAL_SUCCESS;

        try {
            Payment payment = paypalService.createPayment(
                    price,
                    "USD",
                    PaypalPaymentMethod.paypal,
                    PaypalPaymentIntent.sale,
                    "payment description",
                    cancelUrl,
                    successUrl);

            for (Links links : payment.getLinks()) {
                if ("approval_url".equals(links.getRel())) {
                    return Map.of("redirectUrl", links.getHref());
                }
            }
        } catch (PayPalRESTException e) {
            log.error("PayPal Error: {}", e.getMessage());
        }

        // fallback nếu không tìm thấy approval_url hoặc có lỗi
        return Map.of("error", "Không thể tạo liên kết thanh toán.");
    }



    @GetMapping(URL_PAYPAL_CANCEL)
    public String cancelPay() {
        return "cancel";
    }

    @GetMapping(URL_PAYPAL_SUCCESS)
    public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);
            if (payment.getState().equals("approved")) {
                return "success";
            }
        } catch (PayPalRESTException e) {
            log.error(e.getMessage());
        }
        return "redirect:/";
    }
}
