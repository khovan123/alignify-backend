package com.api.service;

import com.api.config.PaypalPaymentIntent;
import com.api.config.PaypalPaymentMethod;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.paypal.api.payments.Amount;
import com.paypal.api.payments.CreateProfileResponse;
import com.paypal.api.payments.InputFields;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.Presentation;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.api.payments.WebProfile;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class PaypalService {

    @Autowired
    private APIContext apiContext;

    public String createNoShippingWebProfile() throws PayPalRESTException {
        WebProfile profile = new WebProfile();
        profile.setName("Alignify-NoShipping-" + System.currentTimeMillis());

        InputFields inputFields = new InputFields();
        inputFields.setNoShipping(1);
        profile.setInputFields(inputFields);

        Presentation presentation = new Presentation();
        presentation.setBrandName("Alignify");
        profile.setPresentation(presentation);

        CreateProfileResponse createdProfile = profile.create(apiContext);
        return createdProfile.getId();
    }

    public Payment createPayment(
            Double total,
            String currency,
            PaypalPaymentMethod method,
            PaypalPaymentIntent intent,
            String description,
            String cancelUrl,
            String successUrl) throws PayPalRESTException {

        Amount amount = new Amount();
        amount.setCurrency(currency);
        amount.setTotal(String.format("%.2f", total));

        Transaction transaction = new Transaction();
        transaction.setDescription(description);
        transaction.setAmount(amount);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod(method.toString());

        Payment payment = new Payment();
        payment.setIntent(intent.toString());
        payment.setPayer(payer);
        payment.setTransactions(transactions);
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);
        payment.setRedirectUrls(redirectUrls);
        String profileId = createNoShippingWebProfile();
        payment.setExperienceProfileId(profileId);

        apiContext.setMaskRequestId(true);
        return payment.create(apiContext);

    }

    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecute = new PaymentExecution();
        paymentExecute.setPayerId(payerId);
        return payment.execute(apiContext, paymentExecute);
    }
}
