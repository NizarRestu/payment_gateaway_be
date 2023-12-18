package com.payment.gateaway.controller;

import com.payment.gateaway.exception.CommonResponse;
import com.payment.gateaway.exception.ResponseHelper;
import com.payment.gateaway.model.Cart;
import com.payment.gateaway.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    private CartService cartService;
    private static final String JWT_PREFIX = "jwt ";

    @PostMapping("/add")
    public CommonResponse<Cart> add(@RequestBody Cart cart , HttpServletRequest requests){
        String jwtToken = requests.getHeader("auth-tgh").substring(JWT_PREFIX.length());
        return ResponseHelper.ok(cartService.add(cart , jwtToken));
    }
    @PutMapping("/{id}")
    public CommonResponse<Cart> put(@RequestBody Cart cart , @PathVariable("id") Integer id) {
        return ResponseHelper.ok(cartService.edit(cart,id));
    }
    @GetMapping()
    public CommonResponse<List<Cart>> get( HttpServletRequest requests){
        String jwtToken = requests.getHeader("auth-tgh").substring(JWT_PREFIX.length());
        return ResponseHelper.ok(cartService.get(jwtToken));
    }
    @DeleteMapping("/{id}")
    public CommonResponse<?> delete(@PathVariable("id")  Integer id) {
        return ResponseHelper.ok( cartService.delete(id));
    }
}
