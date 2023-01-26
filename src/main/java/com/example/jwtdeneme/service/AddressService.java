package com.example.jwtdeneme.service;

import com.example.jwtdeneme.dto.AddressDto;
import com.example.jwtdeneme.dto.AddressListResponse;
import com.example.jwtdeneme.dto.SaveAddressRequest;
import com.example.jwtdeneme.exception.GenericException;
import com.example.jwtdeneme.model.Address;
import com.example.jwtdeneme.repository.AddressRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressService {
    private final AddressRepository addressRepository;
    private final CustomerService customerService;

    public AddressService(AddressRepository addressRepository, CustomerService customerService) {
        this.addressRepository = addressRepository;
        this.customerService = customerService;

    }

    public AddressDto saveAddress (SaveAddressRequest addressRequest){
       if(isCustomerHaveThatAddress(addressRequest.getName())
       ){
            throw  GenericException.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .message("You already have an address with the same name.")
                    .build();
       }
        Address address = new Address();
        address.setAddressName(addressRequest.getName());
        address.setCity(addressRequest.getCity());
        address.setProvince(addressRequest.getProvince());
        address.setCustomer(customerService.findCustomerInContextReturnCustomer());

        return  convertDto(addressRepository.save(address));
    }

    public AddressListResponse updateAddress(String addressName, SaveAddressRequest addressRequest) {
        if(!isCustomerHaveThatAddress(addressName)){
            throw GenericException.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .message("You do not have an address with that name.")
                    .build();
        }
            var addr = addressRepository.findAddressByAddressName(addressName)
                    .orElseThrow(() ->  GenericException.builder()
                            .httpStatus(HttpStatus.NOT_FOUND)
                            .message("Address not found.")
                            .build());
            addr.setAddressName(addressRequest.getName());
            addr.setCity(addressRequest.getCity());
            addr.setProvince(addressRequest.getProvince());
            addr.setCustomer(customerService.findCustomerInContextReturnCustomer());
            Address updatedAddress = addressRepository.save(addr);
            return convertListResponse(updatedAddress);

    }

    public AddressDto deleteAddress(String addressName){
        if(!isCustomerHaveThatAddress(addressName)){
            throw GenericException.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .message("You do not have an address with that name.")
                    .build();
        }

        var addressDto = convertDto(addressRepository.findAddressByAddressName(addressName)
                .orElseThrow(() -> GenericException.builder()
                        .httpStatus(HttpStatus.NOT_FOUND)
                        .message("Address not found.")
                        .build()));

        addressRepository.delete(addressRepository.findAddressByAddressName(addressName).get());

        return addressDto;
    }

    public List<AddressListResponse> getAllAddressesOfCustomer(){
        return addressRepository.findAllByCustomerId(customerService.findCustomerInContext().getId()).get()
                    .stream()
                    .map(model -> convertListResponse(model))
                    .collect(Collectors.toList());
    }

    public AddressDto searchAddress(String addressName){
        if(isCustomerHaveThatAddress(addressName)){
            return(convertDto(addressRepository.findAddressByAddressName(addressName).get()));
        }
        throw GenericException.builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .message("You do not have an address with that name.")
                .build();
    }
    public  boolean isCustomerHaveThatAddress(String addressName){
        return addressRepository.findAllByCustomerId(customerService.findCustomerInContext().getId()).get()
                .stream()
                .map(model -> model.getAddressName().equals(addressName))
                .collect(Collectors.toList()).contains(true);
    }

    private static AddressListResponse convertListResponse(Address model){
        return AddressListResponse.builder()
                .name(model.getAddressName())
                .city(model.getCity())
                .province(model.getProvince())
                .build();
    }

    public AddressDto convertDto(Address model){
        return  AddressDto.builder()
                .id(model.getId())
                .addressName(model.getAddressName())
                .city(model.getCity())
                .province(model.getProvince())
                .customer(customerService.convertResponse(model.getCustomer()))
                .build();
    }



}
