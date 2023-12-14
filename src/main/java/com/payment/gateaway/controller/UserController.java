package com.payment.gateaway.controller;

import com.payment.gateaway.model.LoginRequest;
import com.payment.gateaway.model.User;
import com.payment.gateaway.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Map<Object, Object> authenticateUser(@RequestBody LoginRequest loginRequest) {
        return userService.login(loginRequest);
    }
    @PostMapping("/user/add")
    public User add(@RequestBody User user){
        return userService.add(user);
    }
    @GetMapping("/user/{id}")
    public User get(@PathVariable("id") Long id){
        return userService.get(id);
    }
    @GetMapping
    public List<User> getAll(){
        return userService.getAll();
    }
    @PutMapping("/user/{id}")
    public User put(@PathVariable("id") Long id , @RequestBody User user){
        return userService.edit(id, user);
    }
    @DeleteMapping("/user/{id}")
    public Map<String, Boolean> delete(@PathVariable("id")  Long id) {
        return userService.delete(id);
    }

}
