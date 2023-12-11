package com.payment.gateaway.controller;

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
    public PromoCode add(@RequestBody PromoCode promoCode){
        return promoCodeService.add(promoCode);
    }
    @GetMapping("/{id}")
    public PromoCode get(@PathVariable("id") Integer id){
        return promoCodeService.get(id);
    }
    @GetMapping
    public List<PromoCode> getAll(){
      return promoCodeService.getAll();
    }
    @PutMapping("/{id}")
    public PromoCode put(@PathVariable("id") Integer id , @RequestBody PromoCode promoCode){
        return promoCodeService.edit(id, promoCode);
    }
    @DeleteMapping("/{id}")
    public Map<String, Boolean> delete(@PathVariable("id")  Integer id) {
        return promoCodeService.delete(id);
    }
}
