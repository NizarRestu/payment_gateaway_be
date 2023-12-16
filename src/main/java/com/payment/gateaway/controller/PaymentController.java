package com.payment.gateaway.controller;

import com.payment.gateaway.exception.CommonResponse;
import com.payment.gateaway.exception.ResponseHelper;
import com.payment.gateaway.model.ItemRequest;
import com.payment.gateaway.model.PaymentRequest;
import com.payment.gateaway.model.TransactionRequestItem;
import com.payment.gateaway.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public CommonResponse<?> createPaymentLink(@RequestBody PaymentRequest paymentRequest, HttpServletRequest requests) {
        String promoCode = paymentRequest.getPromo_code();
        List<ItemRequest> items = paymentRequest.getItems();

        String jwtToken = requests.getHeader("auth-tgh").substring(JWT_PREFIX.length());

        // Transform DTO items into a List of TransactionRequestItem
        List<TransactionRequestItem> transactionRequestItems = new ArrayList<>();
        for (ItemRequest item : items) {
            int productId = item.getProduct_id();
            int quantity = item.getQuantity();
            transactionRequestItems.add(new TransactionRequestItem(productId, quantity));
        }

        return ResponseHelper.ok(paymentService.createPaymentLink(transactionRequestItems, promoCode, jwtToken));
    }
}
