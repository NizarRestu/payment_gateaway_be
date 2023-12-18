package com.payment.gateaway.service;

import com.payment.gateaway.exception.NotFoundException;
import com.payment.gateaway.model.Cart;
import com.payment.gateaway.model.User;
import com.payment.gateaway.repository.CartRepository;
import com.payment.gateaway.repository.UserRepository;
import com.payment.gateaway.security.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CartImpl implements CartService{
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;
    @Override
    public Cart add(Cart cart, String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String email = claims.getSubject();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Email Not Found"));
        cart.setUser_id(user.getId());
        return cartRepository.save(cart);
    }

    @Override
    public Cart edit(Cart cart, Integer id) {
        Cart update = cartRepository.findById(id).orElseThrow(() -> new NotFoundException("Id not found") );
        update.setQuantity(cart.getQuantity());
        return cartRepository.save(update);
    }

    @Override
    public Map<String, Boolean> delete(Integer id) {
        try {
            cartRepository.deleteById(id);
            Map<String, Boolean> res = new HashMap<>();
            res.put("Deleted", Boolean.TRUE);
            return res;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Cart> get(String jwtToken) {
        Claims claims = jwtUtils.decodeJwt(jwtToken);
        String email = claims.getSubject();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Email Not Found"));
        return cartRepository.findByUserId(String.valueOf(user.getId()));
    }
}
