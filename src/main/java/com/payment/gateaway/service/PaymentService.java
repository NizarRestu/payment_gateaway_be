package com.payment.gateaway.service;

import com.payment.gateaway.model.Product;
import com.payment.gateaway.model.PromoCode;
import com.payment.gateaway.model.TransactionRequestItem;
import com.payment.gateaway.repository.ProductRepository;
import com.payment.gateaway.repository.PromoCodeRepository;
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

    @Autowired
    private PromoCodeRepository promoCodeRepository;

    @Value("${midtrans.promo.code}")
    private String defaultPromoCode;

    private static final String MIDTRANS_API_URL = "https://api.sandbox.midtrans.com/v1/payment-links";

    private String encodeCredentials(String username, String password) {
        String authString = username + ":" + password;
        byte[] authBytes = authString.getBytes(StandardCharsets.UTF_8);
        return java.util.Base64.getEncoder().encodeToString(authBytes);
    }

    public Map<String, Object> createPaymentLink(List<TransactionRequestItem> items, String promoCode) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Basic " + encodeCredentials(serverKey, ""));

        List<Map<String, Object>> itemDetailsList = new ArrayList<>();

        for (TransactionRequestItem item : items) {
            Product product = productRepository.findById(item.getProduct_id()).orElse(null);

            if (product != null) {
                Map<String, Object> itemDetails = createPaymentLinkRequest(product, item, promoCode);
                itemDetailsList.add(itemDetails);
            }
        }

        Map<String, Object> transactionDetails = createTransactionDetails(items, itemDetailsList, promoCode);
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
            response.put("order_id", responseBody.get("order_id"));
            return response;
        } else {
            throw new RuntimeException("Failed to create payment link");
        }
    }

    private Map<String, Object> createPaymentLinkRequest(Product product, TransactionRequestItem item, String promoCode) {
        Map<String, Object> itemDetails = new HashMap<>();
        itemDetails.put("id", String.valueOf(product.getId()));
        itemDetails.put("price", product.getPrice());
        itemDetails.put("quantity", item.getQuantity());
        itemDetails.put("name", product.getName());

        // Add promo code to the item details
        itemDetails.put("promo_code", (promoCode != null && !promoCode.isEmpty()) ? promoCode : defaultPromoCode);

        return itemDetails;
    }

    private Map<String, Object> createTransactionDetails(List<TransactionRequestItem> items, List<Map<String, Object>> itemDetailsList, String promoCode) {
        Map<String, Object> transactionDetails = new HashMap<>();
        transactionDetails.put("order_id", UUID.randomUUID().toString());

        double totalGrossAmount = 0.0;
        double totalDiscountAmount = 0.0;

        for (TransactionRequestItem item : items) {
            Product product = productRepository.findById(item.getProduct_id()).orElse(null);

            if (product != null) {
                double itemGrossAmount = product.getPrice() * item.getQuantity();
                totalGrossAmount += itemGrossAmount;

                if (promoCode != null && !promoCode.isEmpty()) {
                    double discountPercentage = getDiscountPercentageFromMidtrans(promoCode);

                    double itemDiscountAmount = itemGrossAmount * discountPercentage;
                    totalDiscountAmount += itemDiscountAmount;
                }
            }
        }

        double grossAmountAfterDiscount = totalGrossAmount - totalDiscountAmount;

        transactionDetails.put("gross_amount", grossAmountAfterDiscount);
        transactionDetails.put("item_details", itemDetailsList);
        transactionDetails.put("discount_amount", totalDiscountAmount);

        return transactionDetails;
    }



    private double getDiscountPercentageFromMidtrans(String promoCode) {
        PromoCode code = promoCodeRepository.findByCode(promoCode);

        if (code != null) {
            return code.getDiscount_percentage();
        } else {
            return 0.0;
        }
    }
}