package com.payment.gateaway.model;

public class TransactionRequestItem {
    private Integer product_id;
    private int quantity;

    public TransactionRequestItem(Integer product_id, int quantity) {
        this.product_id = product_id;
        this.quantity = quantity;
    }

    public Integer getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Integer product_id) {
        this.product_id = product_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


}
