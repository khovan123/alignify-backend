
package com.api.model;

import com.api.enumeration.Currency;
import com.api.enumeration.PaypalPaymentMethod;
import com.api.enumeration.Status;
import java.util.Date;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;


public class SubscriptionTransactions {
    @Id
    private String transactionId;
    private String userSubscriptionId, userId;
    private double amount;
    private Currency currency;
    private Status status;
    private PaypalPaymentMethod paypalPaymentMethod;
    private String transaction_reference, description;
    @CreatedDate
    private Date created_at;
    private Date completed_at;
    private boolean is_refunded;

    public SubscriptionTransactions() {
    }

    
    
    public SubscriptionTransactions(String transactionId, String userSubscriptionId, String userId, double amount, Currency currency, Status status, PaypalPaymentMethod paypalPayment_method, String transaction_reference, String description, Date created_at, Date completed_at, boolean is_refunded) {
        this.transactionId = transactionId;
        this.userSubscriptionId = userSubscriptionId;
        this.userId = userId;
        this.amount = amount;
        this.currency = currency;
        this.status = status;
        this.paypalPaymentMethod = paypalPayment_method;
        this.transaction_reference = transaction_reference;
        this.description = description;
        this.created_at = created_at;
        this.completed_at = completed_at;
        this.is_refunded = is_refunded;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getUserSubscriptionId() {
        return userSubscriptionId;
    }

    public void setUserSubscriptionId(String userSubscriptionId) {
        this.userSubscriptionId = userSubscriptionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public PaypalPaymentMethod getPaypalPayment_method() {
        return paypalPaymentMethod;
    }

    public void setPayment_method(PaypalPaymentMethod paypalPaymentMethod) {
        this.paypalPaymentMethod = paypalPaymentMethod;
    }

    public String getTransaction_reference() {
        return transaction_reference;
    }

    public void setTransaction_reference(String transaction_reference) {
        this.transaction_reference = transaction_reference;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getCompleted_at() {
        return completed_at;
    }

    public void setCompleted_at(Date completed_at) {
        this.completed_at = completed_at;
    }

    public boolean isIs_refunded() {
        return is_refunded;
    }

    public void setIs_refunded(boolean is_refunded) {
        this.is_refunded = is_refunded;
    }
    
    
}
