package com.payment.gateaway.controller;

import com.payment.gateaway.exception.CommonResponse;
import com.payment.gateaway.exception.ResponseHelper;
import com.payment.gateaway.model.PromoCode;
import com.payment.gateaway.service.PromoCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/promo_code")
public class PromoCodeController {
    @Autowired
    private PromoCodeService promoCodeService;

    @PostMapping("/add")
    public CommonResponse<PromoCode> add(@RequestBody PromoCode promoCode){
        return ResponseHelper.ok( promoCodeService.add(promoCode));
    }
    @GetMapping("/{id}")
    public CommonResponse <PromoCode> get(@PathVariable("id") Integer id){
        return ResponseHelper.ok( promoCodeService.get(id));
    }
    @GetMapping
    public CommonResponse<List<PromoCode>> getAll(){
      return ResponseHelper.ok( promoCodeService.getAll());
    }
    @PutMapping("/{id}")
    public CommonResponse<PromoCode> put(@PathVariable("id") Integer id , @RequestBody PromoCode promoCode){
        return ResponseHelper.ok( promoCodeService.edit(id, promoCode));
    }
    @DeleteMapping("/{id}")
    public CommonResponse<?> delete(@PathVariable("id")  Integer id) {
        return ResponseHelper.ok( promoCodeService.delete(id));
    }
}
