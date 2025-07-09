package com.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreatePaymentLinkRequest {

    private String productName;
    private String description;
    private String returnUrl;
    private int price;
    private String cancelUrl;

}
