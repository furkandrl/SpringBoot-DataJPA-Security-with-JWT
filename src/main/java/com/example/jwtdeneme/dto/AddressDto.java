package com.example.jwtdeneme.dto;

import com.example.jwtdeneme.model.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AddressDto {
    private UUID id;
    private String addressName;
    private String city;
    private String province;
    private CustomerDtoDetailed customer;


}
