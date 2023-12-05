package com.payment.gateaway.controller;

import com.payment.gateaway.model.TransactionRequest;
import com.payment.gateaway.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
   PaymentService paymentService;

    @PostMapping("/create-link")
    public String createPaymentLink(@RequestBody TransactionRequest paymentRequest) {
        return paymentService.createPaymentLink(paymentRequest);
    }
}
