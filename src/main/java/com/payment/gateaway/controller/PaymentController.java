package com.payment.gateaway.controller;

import com.payment.gateaway.model.TransactionRequest;
import com.payment.gateaway.model.TransactionRequestItem;
import com.payment.gateaway.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class PaymentController {

    @Autowired
   private PaymentService paymentService;

    private static final String JWT_PREFIX = "jwt ";

    @PostMapping("/create-payment-link")
    public Map<String, Object> createPaymentLink(@RequestBody Map<String, Object> request , HttpServletRequest requests) {
        List<Map<String, Object>> items = (List<Map<String, Object>>) request.get("items");
        String promoCode = (String) request.get("promo_code");

        String jwtToken = requests.getHeader("auth-tgh").substring(JWT_PREFIX.length());

        // Transform Map items into a List of TransactionRequestItem
        List<TransactionRequestItem> transactionRequestItems = new ArrayList<>();
        for (Map<String, Object> item : items) {
            Long product_id = Long.parseLong(item.get("product_id").toString());
            int quantity = Integer.parseInt(item.get("quantity").toString());
            transactionRequestItems.add(new TransactionRequestItem(Math.toIntExact(product_id), quantity));
        }

        return paymentService.createPaymentLink(transactionRequestItems, promoCode , jwtToken);
    }
}
