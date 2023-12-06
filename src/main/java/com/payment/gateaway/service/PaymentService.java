package com.payment.gateaway.service;

import com.payment.gateaway.model.Product;
import com.payment.gateaway.model.TransactionRequestItem;
import com.payment.gateaway.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class PaymentService {

    @Value("${midtrans.server.key}")
    private String serverKey;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private RestTemplate restTemplate;


    private static final String MIDTRANS_API_URL = "https://api.sandbox.midtrans.com/v1/payment-links";

    private String encodeCredentials(String username, String password) {
        String authString = username + ":" + password;
        byte[] authBytes = authString.getBytes(StandardCharsets.UTF_8);
        return java.util.Base64.getEncoder().encodeToString(authBytes);
    }

    public Map<String, Object> createPaymentLink(List<TransactionRequestItem> items) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Basic " + encodeCredentials(serverKey, ""));

        RestTemplate restTemplate = new RestTemplate();

        List<Map<String, Object>> itemDetailsList = new ArrayList<>();

        for (TransactionRequestItem item : items) {
            Product product = productRepository.findById(item.getProduct_id()).orElse(null);

            if (product != null) {
                Map<String, Object> itemDetails = createPaymentLinkRequest(product, item);
                itemDetailsList.add(itemDetails);
            }
        }

        Map<String, Object> transactionDetails = createTransactionDetails(items, itemDetailsList);
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("transaction_details", transactionDetails);
        requestBody.put("item_details", itemDetailsList);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> responseEntity = restTemplate.exchange(
                MIDTRANS_API_URL, HttpMethod.POST, requestEntity, Map.class);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            Map<String, Object> responseBody = responseEntity.getBody();
            Map<String, Object> response = new HashMap<>();
            response.put("payment_links", responseBody.get("payment_url"));
            return response;
        } else {
            throw new RuntimeException("Failed to create payment link");
        }
    }

    private Map<String, Object> createPaymentLinkRequest(Product product, TransactionRequestItem item) {
        Map<String, Object> itemDetails = new HashMap<>();
        itemDetails.put("id", String.valueOf(product.getId()));
        itemDetails.put("price", product.getPrice());
        itemDetails.put("quantity", item.getQuantity());
        itemDetails.put("name", product.getName());
        return itemDetails;
    }

    private Map<String, Object> createTransactionDetails(List<TransactionRequestItem> items, List<Map<String, Object>> itemDetailsList) {
        Map<String, Object> transactionDetails = new HashMap<>();
        transactionDetails.put("order_id", UUID.randomUUID().toString());

        double totalGrossAmount = items.stream().mapToDouble(item -> {
            Product product = productRepository.findById(item.getProduct_id()).orElse(null);
            if (product != null) {
                return product.getPrice() * item.getQuantity();
            } else {
                return 0.0;
            }
        }).sum();

        transactionDetails.put("gross_amount", totalGrossAmount);
        return transactionDetails;
    }
}

