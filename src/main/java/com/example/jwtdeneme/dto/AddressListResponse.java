package com.example.jwtdeneme.dto;

import com.example.jwtdeneme.model.Customer;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AddressListResponse {
    private String name;
    private String city;
    private String province;
}


