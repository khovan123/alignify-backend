package com.api.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfirmWebhookRequest {

    private String webhookUrl;

    public ConfirmWebhookRequest(String webhookUrl) {
        this.webhookUrl = webhookUrl;
    }
}
