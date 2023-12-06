package com.payment.gateaway.controller;

import com.payment.gateaway.model.Product;
import com.payment.gateaway.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping( name = "/add" , consumes = "multipart/form-data")
    public Product add( Product product , @RequestPart("file") MultipartFile multipartFile){
        return productService.add(product , multipartFile);
    }
    @GetMapping("/{id}")
    public Product get(@PathVariable("id") Integer id){
        return productService.get(id);
    }
    @PutMapping(name = "/{id}" , consumes = "multipart/form-data")
    public Product edit(@PathVariable("id") Integer id,  Product product ,  @RequestPart("file")MultipartFile multipartFile){
        return productService.edit(id, product, multipartFile);
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
