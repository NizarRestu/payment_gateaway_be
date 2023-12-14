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

    @Column(name = "name_sponsor")
    private String name_sponsor;

    @Column(name = "discount_percentage")
    private Double discount_percentage;

    @Column(name = "min_transaction")
    private Double min_transaction;

    @Column(name = "max_transaction")
    private Double max_transaction;

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

    public String getName_sponsor() {
        return name_sponsor;
    }

    public void setName_sponsor(String name_sponsor) {
        this.name_sponsor = name_sponsor;
    }

    public Double getMax_transaction() {
        return max_transaction;
    }

    public void setMax_transaction(Double max_transaction) {
        this.max_transaction = max_transaction;
    }
}
