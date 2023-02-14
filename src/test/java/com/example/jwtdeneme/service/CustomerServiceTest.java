package com.example.jwtdeneme.service;

import com.example.jwtdeneme.dto.CustomerDto;
import com.example.jwtdeneme.dto.CustomerDtoDetailed;
import com.example.jwtdeneme.dto.CustomerRegisterRequest;
import com.example.jwtdeneme.exception.GenericException;
import com.example.jwtdeneme.model.Customer;
import com.example.jwtdeneme.model.Role;
import com.example.jwtdeneme.repository.CustomerRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import org.springframework.security.crypto.password.PasswordEncoder;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;



class CustomerServiceTest {

    private CustomerService customerService;
    private CustomerRepository customerRepository;
    private PasswordEncoder passwordEncoder;



    @BeforeEach
    void setUp() {
        customerRepository = Mockito.mock(CustomerRepository.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);

        customerService = new CustomerService(customerRepository, passwordEncoder);
    }

    @Test
    void createCustomer() {

    }

    @Test
    void findCustomerByUsername_shouldReturnCustomer_WhenCustomerExists() {
        String username = "username";

        Customer expected = Utilities.customer;

        Mockito.when(customerRepository.findByUsername(username)).thenReturn(Optional.of(expected));

        var result = customerService.findCustomerByUsername(username);
        assertEquals(expected, result);
    }

    @Test
    void findCustomerByUsername_shouldThrowCustomerNotFoundError_WhenCustomerDoesNotExist() {
        String username = "username";

        Mockito.when(customerRepository.findByUsername(username)).thenReturn(Optional.empty());

        org.assertj.core.api.Assertions.assertThatThrownBy(() -> customerService.findCustomerByUsername(username))
                .isInstanceOf(GenericException.class)
                .hasMessageContaining("Customer not found.");
    }

    @Test
    void findCustomer_shouldReturnCustomerDto_WhenCustomerExists() {
        String username = "username";

        Customer customerObject = Utilities.customer;

        Mockito.when(customerRepository.findByUsername(username)).thenReturn(Optional.of(customerObject));
        Mockito.when(customerService.findCustomerByUsername(username)).thenReturn(customerObject);

        var expected = CustomerDto.builder()
                .id(customerObject.getId())
                .username(customerObject.getUsername())
                .role(customerObject.getRole())
                .build();

        var result = customerService.findCustomer(username);

        assertEquals(expected, result);
    }

    @Test
    void findCustomerInContext() {
    }

    @Test
    void findCustomerInContextReturnCustomer() {
    }

    @Test
    void convertResponse() {
    }
}