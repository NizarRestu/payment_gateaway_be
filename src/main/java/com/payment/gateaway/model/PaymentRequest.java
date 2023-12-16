package com.payment.gateaway.model;

import java.util.List;
import java.util.Map;

public class PaymentRequest {
    private String promo_code;
    private List<ItemRequest> items;

    public PaymentRequest() {
    }

    public PaymentRequest(String promo_code, List<ItemRequest> items) {
        this.promo_code = promo_code;
        this.items = items;
    }

    public String getPromo_code() {
        return promo_code;
    }

    public void setPromo_code(String promo_code) {
        this.promo_code = promo_code;
    }

    public List<ItemRequest> getItems() {
        return items;
    }

    public void setItems(List<ItemRequest> items) {
        this.items = items;
    }
}
