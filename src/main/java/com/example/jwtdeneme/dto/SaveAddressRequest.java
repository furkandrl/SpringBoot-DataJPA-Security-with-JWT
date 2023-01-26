package com.example.jwtdeneme.dto;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaveAddressRequest {
    private String name;
    private String city;
    private String province;
}
