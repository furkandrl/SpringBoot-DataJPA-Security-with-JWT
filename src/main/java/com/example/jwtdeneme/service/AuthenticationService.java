package com.example.jwtdeneme.service;

import com.example.jwtdeneme.dto.CustomerLoginRequest;
import com.example.jwtdeneme.dto.TokenResponseDto;
import com.example.jwtdeneme.security.TokenGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthenticationService {
    private final TokenGenerator tokenGenerator;
    private final AuthenticationManager authenticationManager;
    private final CustomerService customerService;

    public AuthenticationService(TokenGenerator tokenGenerator, AuthenticationManager authenticationManager, CustomerService customerService) {
        this.tokenGenerator = tokenGenerator;
        this.authenticationManager = authenticationManager;
        this.customerService = customerService;
    }

    public TokenResponseDto login(CustomerLoginRequest request){
        try{
            Authentication auth =authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    ));
        return TokenResponseDto.builder()
                .accessToken(tokenGenerator.generateToken(auth))
                .user(customerService.findCustomer(request.getUsername()))
                .build();

        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found", e);
        }
    }
}
