package com.payment.gateaway.controller;

import com.payment.gateaway.exception.CommonResponse;
import com.payment.gateaway.exception.ResponseHelper;
import com.payment.gateaway.model.PaymentRequest;
import com.payment.gateaway.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class  PaymentController {

    @Autowired
   private PaymentService paymentService;

    private static final String JWT_PREFIX = "jwt ";

    @PostMapping("/create-payment-link")
    public CommonResponse<?> createPaymentLink(@RequestBody PaymentRequest paymentRequest, HttpServletRequest requests) {
        String jwtToken = requests.getHeader("auth-tgh").substring(JWT_PREFIX.length());
        return ResponseHelper.ok(paymentService.createPaymentLink(paymentRequest, jwtToken));
    }
}
