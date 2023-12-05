package com.payment.gateaway.service;

import com.payment.gateaway.model.Product;

import java.util.List;
import java.util.Map;

public interface ProductService {
    Product add(Product product);
    Product get(Integer id);

    List<Product> getAll();

    Product edit(Integer id, Product product);

    Map<String, Boolean> delete(Integer id);

}
