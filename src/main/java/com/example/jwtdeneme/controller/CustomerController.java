package com.example.jwtdeneme.controller;

import com.example.jwtdeneme.dto.CustomerDtoDetailed;
import com.example.jwtdeneme.dto.CustomerLoginRequest;
import com.example.jwtdeneme.dto.CustomerRegisterRequest;
import com.example.jwtdeneme.dto.TokenResponseDto;
import com.example.jwtdeneme.service.AuthenticationService;
import com.example.jwtdeneme.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {
    private final CustomerService customerService;
    private final AuthenticationService authService;

    public CustomerController(CustomerService customerService, AuthenticationService authService) {
        this.customerService = customerService;
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<CustomerDtoDetailed> register(@RequestBody CustomerRegisterRequest request){
        return ResponseEntity
                .ok(customerService.createCustomer(request));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@RequestBody CustomerLoginRequest request){
        return  ResponseEntity.ok(authService.login(request));
    }


}
