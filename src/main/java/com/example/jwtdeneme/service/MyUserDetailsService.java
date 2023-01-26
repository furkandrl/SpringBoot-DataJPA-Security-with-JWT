package com.example.jwtdeneme.service;

import com.example.jwtdeneme.model.Customer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final CustomerService service;

    public MyUserDetailsService(CustomerService service) {
        this.service = service;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = service.findCustomerByUsername(username);
        var roles = Stream.of(customer.getRole())
                .map(role-> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());

        return new User(customer.getUsername(),
                customer.getPassword(),
                roles);
    }
}
