package com.payment.gateaway.service;

import com.payment.gateaway.exception.BadRequestException;
import com.payment.gateaway.exception.InternalErrorException;
import com.payment.gateaway.exception.NotFoundException;
import com.payment.gateaway.model.Product;
import com.payment.gateaway.model.PromoCode;
import com.payment.gateaway.model.TransactionRequestItem;
import com.payment.gateaway.model.User;
import com.payment.gateaway.repository.ProductRepository;
import com.payment.gateaway.repository.PromoCodeRepository;
import com.payment.gateaway.repository.UserRepository;
import com.payment.gateaway.security.JwtUtils;
import io.jsonwebtoken.Claims;
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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Value("${midtrans.promo.code}")
    private String defaultPromoCode;

    private static final String MIDTRANS_API_URL = "https://api.sandbox.midtrans.com/v1/payment-links";

    private String encodeCredentials(String username, String password) {
        String authString = username + ":" + password;
        byte[] authBytes = authString.getBytes(StandardCharsets.UTF_8);
        return java.util.Base64.getEncoder().encodeToString(authBytes);
    }


    public Map<String, Object> createPaymentLink(List<TransactionRequestItem> items, String promoCode , String jwtToken) {
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
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String email = claims.getSubject();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Email Not Found"));
        Map<String, Object> customerDetails = new HashMap<>();
        customerDetails.put("first_name" , user.getName());
        customerDetails.put("email" , user.getEmail());
        customerDetails.put("phone" , user.getNo_hp());
        customerDetails.put("notes" , user.getAddress());
        Map<String, Object> transactionDetails = createTransactionDetails(items, itemDetailsList, promoCode);
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("transaction_details", transactionDetails);
        requestBody.put("item_details", itemDetailsList);
        requestBody.put("customer_details" , customerDetails);

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
            throw new InternalErrorException("Failed to create payment link");
        }
    }

    private Map<String, Object> createPaymentLinkRequest(Product product, TransactionRequestItem item, String promoCode) {
        Map<String, Object> itemDetails = new HashMap<>();
        itemDetails.put("id", String.valueOf(product.getId()));

        if (promoCode == null || promoCode.isEmpty()) {
            itemDetails.put("price", product.getPrice());
        } else {
            double discountedPrice = product.getPrice() * (1 - getDiscountPercentageFromMidtrans(promoCode));
            itemDetails.put("price", discountedPrice);
        }

        itemDetails.put("quantity", item.getQuantity());
        itemDetails.put("name", product.getName());

        itemDetails.put("promo_code", (promoCode != null && !promoCode.isEmpty()) ? promoCode : defaultPromoCode);
        return itemDetails;
    }


    private Map<String, Object> createTransactionDetails(List<TransactionRequestItem> items, List<Map<String, Object>> itemDetailsList, String promoCode) {
        Map<String, Object> transactionDetails = new HashMap<>();
        transactionDetails.put("order_id", UUID.randomUUID().toString());

        double totalGrossAmount = 0.0;

        for (TransactionRequestItem item : items) {
            Product product = productRepository.findById(item.getProduct_id()).orElseThrow(() -> new NotFoundException("Id Not Found"));;

            if (product != null) {
                if (promoCode == null || promoCode.isEmpty()) {
                    double itemGrossAmount = product.getPrice() * item.getQuantity();
                    totalGrossAmount += itemGrossAmount;
                } else {
                    double discountedPrice = product.getPrice() * (1 - getDiscountPercentageFromMidtrans(promoCode));
                    double itemGrossAmount = discountedPrice * item.getQuantity();
                    totalGrossAmount += itemGrossAmount;
                }
            }
        }
        if (!promoCode.isEmpty()) {
            PromoCode code = promoCodeRepository.findByCode(promoCode);
            if (code.getMin_transaction() > totalGrossAmount) {
                throw new BadRequestException("Total transaction min " + code.getMin_transaction());
            } else if (code.getMax_transaction() < totalGrossAmount) {
                throw new BadRequestException("Total transaction max " + code.getMax_transaction());
            }
            transactionDetails.put("gross_amount", totalGrossAmount);
            transactionDetails.put("item_details", itemDetailsList);
        }
        transactionDetails.put("gross_amount", totalGrossAmount);
        transactionDetails.put("item_details", itemDetailsList);
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