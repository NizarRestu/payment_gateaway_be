package com.payment.gateaway.controller;

import com.payment.gateaway.exception.CommonResponse;
import com.payment.gateaway.exception.ResponseHelper;
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
    public CommonResponse<Product> add(Product product , @RequestPart("file") MultipartFile multipartFile){
        return ResponseHelper.ok( productService.add(product , multipartFile));
    }
    @GetMapping("/{id}")
    public CommonResponse<Product> get(@PathVariable("id") Integer id){
        return ResponseHelper.ok( productService.get(id));
    }
    @PutMapping(name = "/{id}" , consumes = "multipart/form-data")
    public CommonResponse<Product> edit(@PathVariable("id") Integer id,  Product product ,  @RequestPart("file")MultipartFile multipartFile){
        return ResponseHelper.ok( productService.edit(id, product, multipartFile));
    }
    @GetMapping
    public CommonResponse<List<Product>> getAll(){
        return ResponseHelper.ok( productService.getAll());
    }
    @DeleteMapping("/{id}")
    public CommonResponse<?> delete(@PathVariable("id")  Integer id) {
        return ResponseHelper.ok( productService.delete(id));
    }

}
