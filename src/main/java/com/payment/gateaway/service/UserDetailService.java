package com.payment.gateaway.service;

import com.payment.gateaway.model.User;
import com.payment.gateaway.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserDetailService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) {
        if (userRepository.existsByEmail(username)){
            User user = userRepository.findByEmail(username).orElseThrow(() -> new RuntimeException("Username not found"));;
            return UserDetail.buildUser(user);
        }
         throw new RuntimeException("User Not Found with username: " + username);
    }

}
