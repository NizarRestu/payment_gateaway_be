package com.payment.gateaway.model;

import java.util.List;

public class TransactionRequest {
    private List<TransactionRequestItem> items;

    public List<TransactionRequestItem> getItems() {
        return items;
    }

    public void setItems(List<TransactionRequestItem> items) {
        this.items = items;
    }
}
