package com.payment.gateaway.service;

import com.payment.gateaway.model.LoginRequest;
import com.payment.gateaway.model.Product;
import com.payment.gateaway.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface UserService {
    Map<Object, Object> login(LoginRequest loginRequest);
    User add(User user);
    User get(Long id);

    List<User> getAll();

    User edit(Long id, User user);

    Map<String, Boolean> delete(Long id);
}
