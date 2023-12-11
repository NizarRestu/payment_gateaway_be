package com.payment.gateaway.service;

import com.payment.gateaway.model.Product;
import com.payment.gateaway.model.PromoCode;

import java.util.List;
import java.util.Map;

public interface PromoCodeService {
    PromoCode add(PromoCode promoCode);

    PromoCode get(Integer id);
    PromoCode edit(Integer id ,PromoCode promoCode);

    List<PromoCode> getAll();

    Map<String, Boolean> delete(Integer id);
}
