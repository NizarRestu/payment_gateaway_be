package com.payment.gateaway.model;

import java.util.List;
import java.util.Map;

public class PaymentRequest {
    private String promo_code;

    public PaymentRequest() {
    }

    public PaymentRequest(String promo_code) {
        this.promo_code = promo_code;
    }

    public String getPromo_code() {
        return promo_code;
    }

    public void setPromo_code(String promo_code) {
        this.promo_code = promo_code;
    }
}
