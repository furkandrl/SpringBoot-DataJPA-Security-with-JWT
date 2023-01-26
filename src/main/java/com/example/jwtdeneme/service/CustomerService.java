package com.example.jwtdeneme.service;

import com.example.jwtdeneme.dto.CustomerDtoDetailed;
import com.example.jwtdeneme.dto.CustomerRegisterRequest;
import com.example.jwtdeneme.dto.CustomerDto;
import com.example.jwtdeneme.exception.GenericException;
import com.example.jwtdeneme.model.Customer;
import com.example.jwtdeneme.model.Role;
import com.example.jwtdeneme.repository.CustomerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;


    public CustomerService(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public CustomerDtoDetailed createCustomer (CustomerRegisterRequest customerRegisterRequest){
        if(customerRepository.findByUsername(customerRegisterRequest.getUsername()).isPresent()){
            throw GenericException.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .message("A user with the same username already exists.")
                    .build();
        }
        Customer customer = new Customer();
        customer.setUsername(customerRegisterRequest.getUsername());
        customer.setEmail(customerRegisterRequest.getEmail());
        customer.setPassword(passwordEncoder.encode(customerRegisterRequest.getPassword()));
        customer.setRole(Role.USER);
        customer.setBirthday(customerRegisterRequest.getBirthday());
        customer.setAge(customer.getAge());

        return convertResponse(customerRepository.save(customer));

    }

    public Customer findCustomerByUsername(String username){
        return customerRepository.findByUsername(username).orElseThrow(() -> GenericException.builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .message("Customer not found.")
                .build());
    }

    public CustomerDto findCustomer(String username) {
        var user = findCustomerByUsername(username);
        return CustomerDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }

    public CustomerDto findCustomerInContext() {
        final Authentication authentication = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .orElseThrow(() ->  GenericException.builder()
                .httpStatus(HttpStatus.UNAUTHORIZED)
                .message("Customer not found.")
                .build());
        final UserDetails details = Optional.ofNullable((UserDetails) authentication.getPrincipal())
                .orElseThrow(() -> GenericException.builder()
                .httpStatus(HttpStatus.UNAUTHORIZED)
                .message("Customer not found.")
                .build());
        return findCustomer(details.getUsername());
    }

    public Customer findCustomerInContextReturnCustomer() {
        final Authentication authentication = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .orElseThrow(() ->  GenericException.builder()
                .httpStatus(HttpStatus.UNAUTHORIZED)
                .message("Customer not found.")
                .build());
        final UserDetails details = Optional.ofNullable((UserDetails) authentication.getPrincipal())
                .orElseThrow(() ->  GenericException.builder()
                .httpStatus(HttpStatus.UNAUTHORIZED)
                .message("Customer not found.")
                .build());
        return findCustomerByUsername(details.getUsername());
    }

    public CustomerDtoDetailed convertResponse(Customer model){
        return CustomerDtoDetailed.builder()
                .id(model.getId())
                .username(model.getUsername())
                .email(model.getEmail())
                .password(model.getPassword())
                .birthday(model.getBirthday())
                .age(model.getAge())
                .build();
    }


}
