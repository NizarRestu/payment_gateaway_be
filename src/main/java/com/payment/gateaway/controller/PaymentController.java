package com.payment.gateaway.controller;

import com.payment.gateaway.model.TransactionRequest;
import com.payment.gateaway.model.TransactionRequestItem;
import com.payment.gateaway.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class PaymentController {

    @Autowired
   PaymentService paymentService;

    @PostMapping("/payment")
    public ResponseEntity<Map<String, Object>> createPaymentLinks(@RequestBody List<TransactionRequestItem> items) {
        try {
            Map<String, Object> response = paymentService.createPaymentLink(items);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace(); // Handle the exception appropriately
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
