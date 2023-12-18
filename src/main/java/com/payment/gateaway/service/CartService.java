package com.payment.gateaway.service;

import com.payment.gateaway.model.Cart;

import java.util.List;
import java.util.Map;

public interface CartService {
    Cart add(Cart cart , String jwtToken);

    Cart edit(Cart cart , Integer id);

    Map<String, Boolean> delete(Integer id);

    List<Cart> get(String jwtToken);



}
