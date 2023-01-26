package com.example.jwtdeneme.dto;

import com.example.jwtdeneme.model.Address;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDtoDetailed {
    private UUID id;
    private String username;
    private String email;
    private String password;
    private List<Address> addresses = new ArrayList<>();
    private LocalDate birthday;
    private int age;
}
