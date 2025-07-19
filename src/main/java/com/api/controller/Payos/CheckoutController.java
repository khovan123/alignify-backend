
package com.api.controller.Payos;

import java.util.Date;
import java.util.Optional;

import com.api.dto.request.CreatePaymentLinkRequest;
import com.api.model.Plan;
import com.api.repository.PlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.payos.PayOS;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.ItemData;
import vn.payos.type.PaymentData;

@Controller
public class CheckoutController {
    private final PayOS payOS;
    @Autowired
    private PlanRepository planRepository;
    public CheckoutController(PayOS payOS) {
        super();
        this.payOS = payOS;
    }

    @RequestMapping(value = "/")
    public String Index() {
        return "index";
    }

    @RequestMapping(value = "/success")
    public String Success() {
        return "success";
    }

    @RequestMapping(value = "/cancel")
    public String Cancel() {
        return "cancel";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/create-payment-link", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public void checkout(HttpServletRequest request, HttpServletResponse httpServletResponse,@RequestBody CreatePaymentLinkRequest RequestBody) {
        try {
            final String baseUrl = getBaseUrl(request);
            final String planId = RequestBody.getPlanId();
            Optional<Plan> plan = planRepository.findByPlanId(planId);
            if(plan.isPresent()) {
                final String planName = plan.get().getPlanName();
                final String description = plan.get().getPlanName();
                Double planPrice = plan.get().getPrice();
                if (planPrice == null || planPrice <= 0) {
                    return ;
                }
                final int price = plan.get().getPrice().intValue();
                final String returnUrl = RequestBody.getReturnUrl();
                final String cancelUrl = RequestBody.getCancelUrl();

                // Gen order code
                String currentTimeString = String.valueOf(new Date().getTime());
                long orderCode = Long.parseLong(currentTimeString.substring(currentTimeString.length() - 6));
                ItemData item = ItemData.builder().name(planName).quantity(1).price(price).build();
                PaymentData paymentData = PaymentData.builder().orderCode(orderCode).amount(price).description(description)
                        .returnUrl(returnUrl).cancelUrl(cancelUrl).item(item).build();
                CheckoutResponseData data = payOS.createPaymentLink(paymentData);

                String checkoutUrl = data.getCheckoutUrl();

                httpServletResponse.setHeader("Location", checkoutUrl);
                httpServletResponse.setStatus(302);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getBaseUrl(HttpServletRequest request) {
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        String contextPath = request.getContextPath();

        String url = scheme + "://" + serverName;
        if ((scheme.equals("http") && serverPort != 80) || (scheme.equals("https") && serverPort != 443)) {
            url += ":" + serverPort;
        }
        url += contextPath;
        return url;
    }
}