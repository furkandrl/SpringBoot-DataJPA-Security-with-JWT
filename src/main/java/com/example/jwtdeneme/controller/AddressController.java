package com.example.jwtdeneme.controller;

import com.example.jwtdeneme.dto.AddressDto;
import com.example.jwtdeneme.dto.AddressListResponse;
import com.example.jwtdeneme.dto.SaveAddressRequest;
import com.example.jwtdeneme.service.AddressService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/address")
public class AddressController {
    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping("/add")
    public ResponseEntity<AddressDto> addAddress(@RequestBody SaveAddressRequest request) {
        return ResponseEntity
                .ok(addressService.saveAddress(request));
    }

    @PutMapping("/update/{addressName}")
    public ResponseEntity<AddressListResponse> updateAddress(@PathVariable String addressName,
                                                             @RequestBody SaveAddressRequest request) {
        return ResponseEntity
                .ok(addressService.updateAddress(addressName, request));
    }

    @GetMapping("/show-all")
    public ResponseEntity<List<AddressListResponse>> getAllAddresses() {
        return ResponseEntity
                .ok(addressService.getAllAddressesOfCustomer());
    }

    @DeleteMapping("/delete/{addressName}")
    public ResponseEntity<AddressDto> deleteAddress(@PathVariable String addressName) {
        return ResponseEntity
                .ok(addressService.deleteAddress(addressName));
    }

    @GetMapping("/search/{name}")
    public ResponseEntity<AddressDto> searchAddress(@PathVariable String name){
        return ResponseEntity
                .ok(addressService.searchAddress(name));
    }

}
