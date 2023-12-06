package com.payment.gateaway.service;

import com.payment.gateaway.model.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface ProductService {
    Product add(Product product , MultipartFile multipartFile);
    Product get(Integer id);

    List<Product> getAll();

    Product edit(Integer id, Product product , MultipartFile multipartFile);

    Map<String, Boolean> delete(Integer id);

}
