package com.payment.gateaway.controller;

import com.payment.gateaway.model.Product;
import com.payment.gateaway.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping("/add")
    public Product add(@RequestBody Product product){
        return productService.add(product);
    }
    @GetMapping("/{id}")
    public Product get(@PathVariable("id") Integer id){
        return productService.get(id);
    }
    @PutMapping("/{id}")
    public Product edit(@PathVariable("id") Integer id, @RequestBody Product product){
        return productService.edit(id, product);
    }
    @GetMapping
    public List<Product> getAll(){
        return productService.getAll();
    }
    @DeleteMapping("/{id}")
    public Map<String, Boolean> delete(@PathVariable("id")  Integer id) {
        return productService.delete(id);
    }

}
