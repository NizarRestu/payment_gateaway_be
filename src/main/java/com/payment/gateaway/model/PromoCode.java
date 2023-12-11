package com.payment.gateaway.model;

import javax.persistence.*;

@Entity
@Table(name = "promo_code")
public class PromoCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "code")
    private String code;

    @Column(name = "discount_percentage")
    private Double discount_percentage;

    @Column(name = "min_transaction")
    private Double min_transaction;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Double getDiscount_percentage() {
        return discount_percentage;
    }

    public void setDiscount_percentage(Double discount_percentage) {
        this.discount_percentage = discount_percentage;
    }

    public Double getMin_transaction() {
        return min_transaction;
    }

    public void setMin_transaction(Double min_transaction) {
        this.min_transaction = min_transaction;
    }
}
