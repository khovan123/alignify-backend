package com.api.controller;

import com.api.config.PaypalPaymentIntent;
import com.api.config.PaypalPaymentMethod;
import com.api.service.PaypalService;
import com.api.util.UtilsPaypal;
import com.api.util.VietQRGenerator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class PaymentController {

    public static final String URL_PAYPAL_SUCCESS = "pay/success";
    public static final String URL_PAYPAL_CANCEL = "pay/cancel";
    private static final Map<String, String> BIN_TO_SWIFT = Map.ofEntries(
            Map.entry("970403", "BFTVVNVX"), // VietinBank
            Map.entry("970418", "VTCBVNVX"), // Techcombank
            Map.entry("970415", "SCBLVNVX"), // Standard Chartered
            Map.entry("970405", "BIDVVNVX"), // BIDV
            Map.entry("970432", "BFTVVNVX"), // SHB
            Map.entry("970436", "VCBVVNVX"), // Vietcombank
            Map.entry("970407", "SACLVNVX"), // Sacombank
            Map.entry("970422", "EIBVVNVX"), // Eximbank
            Map.entry("970423", "TPBVVNVX"), // TPBank
            Map.entry("970427", "OCBVVNVX"), // OCB
            Map.entry("970430", "VPBKVNVX"), // VPBank
            Map.entry("970406", "HVBKVNVX"), // HSBC
            Map.entry("970441", "ABBKVNVX"), // ABBank
            Map.entry("970428", "MBBKVNVX"), // MBBank
            Map.entry("970454", "NVBAVNVX"), // Nam A Bank
            Map.entry("970429", "VIBVVNVX"), // VIB
            Map.entry("970431", "SEAVVNVX"), // SeABank
            Map.entry("970452", "PVBKVNVX"), // PVcomBank
            Map.entry("970437", "LPBKVNVX"), // LienVietPostBank
            Map.entry("970440", "BVBVVNVX") // Bao Viet Bank
    );

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private PaypalService paypalService;

    @GetMapping("")
    public String index() {
        return "index";
    }

    @GetMapping("/pay-bank")
    public ResponseEntity<byte[]> generateBankQr(
            @RequestParam("amount") long amount,
            @RequestParam("bankCode") String bankCode,
            @RequestParam("account") String bankAccount) {
        try {
            String swiftCode = BIN_TO_SWIFT.getOrDefault(bankCode, bankCode); // Ánh xạ nếu cần
            String payload = VietQRGenerator.generateVietQrPayload(swiftCode, bankAccount, amount);
            byte[] qrImage = VietQRGenerator.generateQRImage(payload);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);

            return ResponseEntity.ok().headers(headers).body(qrImage);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/pay")
    public String pay(HttpServletRequest request, @RequestParam("price") double price) {
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
                if (links.getRel().equals("approval_url")) {
                    return "redirect:" + links.getHref();
                }
            }

        } catch (PayPalRESTException e) {
            log.error(e.getMessage());
        }
        return "redirect:/";
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
