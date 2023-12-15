package com.payment.gateaway.controller;

import com.payment.gateaway.exception.CommonResponse;
import com.payment.gateaway.exception.ResponseHelper;
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
    public CommonResponse<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        return ResponseHelper.ok( userService.login(loginRequest));
    }
    @PostMapping("/user/add")
    public CommonResponse<User> add(@RequestBody User user){
        return ResponseHelper.ok( userService.add(user));
    }
    @GetMapping("/user/{id}")
    public CommonResponse <User> get(@PathVariable("id") Long id){
        return ResponseHelper.ok( userService.get(id));
    }
    @GetMapping
    public CommonResponse<List<User>> getAll(){
        return ResponseHelper.ok( userService.getAll());
    }
    @PutMapping("/user/{id}")
    public CommonResponse<User> put(@PathVariable("id") Long id , @RequestBody User user){
        return ResponseHelper.ok( userService.edit(id, user));
    }
    @DeleteMapping("/user/{id}")
    public CommonResponse<?> delete(@PathVariable("id")  Long id) {
        return ResponseHelper.ok( userService.delete(id));
    }

}
