package com.payment.gateaway.service;

import com.payment.gateaway.model.TransactionRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class PaymentService {

    @Value("${midtrans.server.key}")
    private String serverKey;


    private static final String MIDTRANS_API_URL = "https://api.sandbox.midtrans.com/v1/payment-links";

    private String encodeCredentials(String username, String password) {
        String authString = username + ":" + password;
        byte[] authBytes = authString.getBytes(StandardCharsets.UTF_8);
        return java.util.Base64.getEncoder().encodeToString(authBytes);
    }

    public String createPaymentLink(TransactionRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Basic " + encodeCredentials(serverKey, ""));
        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> requestBody = createPaymentLinkRequest(request.getProductName(), request.getPrice(), request.getQuantity());

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> responseEntity = restTemplate.exchange(
                MIDTRANS_API_URL, HttpMethod.POST, requestEntity, Map.class);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            Map<String, Object> responseBody = responseEntity.getBody();
            return (String) responseBody.get("payment_url");
        } else {
            throw new RuntimeException("Failed to create payment link");
        }
    }

    private Map<String, Object> createPaymentLinkRequest(String productName, double price, int quantity) {
        Map<String, Object> request = new HashMap<>();
        Map<String, Object> transactionDetails = new HashMap<>();
        Map<String, Object> itemDetails = new HashMap<>();

        transactionDetails.put("order_id", UUID.randomUUID().toString());
        transactionDetails.put("gross_amount", price * quantity);

        itemDetails.put("id", "item_id");
        itemDetails.put("price", price);
        itemDetails.put("quantity", quantity);
        itemDetails.put("name", productName);

        request.put("transaction_details", transactionDetails);
        request.put("item_details", new Object[]{itemDetails});

        return request;
    }
}

