package com.payment.gateaway.service;

import com.payment.gateaway.model.Product;
import com.payment.gateaway.repository.ProductRepository;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService{
    @Autowired
    private ProductRepository productRepository;

    private String convertToBase64Url(MultipartFile file) {
        String url = "";
        try {
            byte[] byteData = Base64.encodeBase64(file.getBytes());
            String result = new String(byteData);
            url = "data:" + file.getContentType() + ";base64," + result;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return url;
        }

    }

    @Override
    public Product add(Product product, MultipartFile multipartFile) {
        product.setImage(convertToBase64Url(multipartFile));
        return productRepository.save(product);
    }

    @Override
    public Product get(Integer id) {
        return productRepository.findById(id).get();
    }

    @Override
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    @Override
    public Product edit(Integer id, Product product, MultipartFile multipartFile) {
        Product update = productRepository.findById(id).get();
        update.setName(product.getName());
        update.setPrice(product.getPrice());
        update.setDescription(product.getDescription());
        update.setImage(convertToBase64Url(multipartFile));
        return productRepository.save(update);
    }

    @Override
    public Map<String, Boolean> delete(Integer id) {
        try {
            productRepository.deleteById(id);
            Map<String, Boolean> res = new HashMap<>();
            res.put("Deleted", Boolean.TRUE);
            return res;
        } catch (Exception e) {
            return null;
        }

    }
    public Optional<Product> getProductById(Integer productId) {
        return productRepository.findById(productId);
    }
    public Optional<Product> getProductByName(String name) {
        return productRepository.findByName(name);
    }
}
