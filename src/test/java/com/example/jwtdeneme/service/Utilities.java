package com.example.jwtdeneme.service;

import com.example.jwtdeneme.dto.AddressDto;
import com.example.jwtdeneme.dto.CustomerDto;
import com.example.jwtdeneme.dto.SaveAddressRequest;
import com.example.jwtdeneme.model.Address;
import com.example.jwtdeneme.model.Customer;
import com.example.jwtdeneme.model.Role;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class Utilities {
    static Customer customer =new Customer(UUID.randomUUID(), "username", "email@email.com", "password",
            List.of(), Role.USER,
            LocalDate.of(1999, 1,1) );
    static Address address = new Address(UUID.randomUUID(), "addressName", "city", "province", customer);

    static CustomerDto customerDto = CustomerDto.builder()
            .id(customer.getId())
            .username(customer.getUsername())
            .role(customer.getRole())
            .build();

    static SaveAddressRequest saveAddressRequest = SaveAddressRequest.builder()
            .name("updated-addressName")
            .city("updated-city")
            .province("updated-province")
            .build();
}
