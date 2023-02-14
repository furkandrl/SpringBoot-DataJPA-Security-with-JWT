package com.example.jwtdeneme.service;

import com.example.jwtdeneme.dto.AddressDto;
import com.example.jwtdeneme.dto.AddressListResponse;
import com.example.jwtdeneme.dto.CustomerDtoDetailed;
import com.example.jwtdeneme.exception.GenericException;
import com.example.jwtdeneme.repository.AddressRepository;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AddressServiceTest {

    private AddressRepository addressRepository;
    private CustomerService customerService;

    private AddressService addressService;

    @BeforeEach
    void setUp(){
        addressRepository = Mockito.mock(AddressRepository.class);
        customerService = Mockito.mock(CustomerService.class);
        addressService = new AddressService(addressRepository, customerService);
    }

    @Test
    void saveAddress() {

    }

    @Test
    void updateAddress() {

    }

    @Test
    void deleteAddress_shouldReturnAddressDto_WhenAddressExisted() {
        var list = Optional.of(List.of(Utilities.address));
        Mockito.when(customerService.findCustomerInContext()).thenReturn(Utilities.customerDto);
        Mockito.when(addressRepository.findAllByCustomerId(customerService.findCustomerInContext().getId()))
                .thenReturn(list);

        Mockito.when(addressRepository.findAddressByAddressName(Utilities.address.getAddressName()))
                .thenReturn(Optional.of(Utilities.address));

        var convertResponse =CustomerDtoDetailed.builder()
                .id(Utilities.customer.getId())
                .username(Utilities.customer.getUsername())
                .email(Utilities.customer.getEmail())
                .password(Utilities.customer.getPassword())
                .addresses(Utilities.customer.getAddresses())
                .birthday(Utilities.customer.getBirthday())
                .age(24)
                .build();

        Mockito.when(customerService.convertResponse(Utilities.customer)).thenReturn(convertResponse);

        AddressDto expected = AddressDto.builder()
                .id(Utilities.address.getId())
                .addressName(Utilities.address.getAddressName())
                .city(Utilities.address.getCity())
                .province(Utilities.address.getProvince())
                .customer(customerService.convertResponse(Utilities.address.getCustomer()))
                .build();

        Mockito.when(addressRepository.findAddressByAddressName(Utilities.address.getAddressName()))
                .thenReturn(Optional.of(Utilities.address));

        Mockito.when(addressService.convertDto(addressRepository.findAddressByAddressName(Utilities.address.getAddressName()).get()))
                .thenReturn(expected);

        var actual = addressService.deleteAddress(Utilities.address.getAddressName());

        assertEquals(expected, actual);
    }

    @Test
    void getAllAddressesOfCustomer_shouldReturnListOfAddressListResponse() {
        Mockito.when(customerService.findCustomerInContext())
                .thenReturn(Utilities.customerDto);

        var addressListResponse = AddressListResponse.builder()
                .name(Utilities.address.getAddressName())
                .city(Utilities.address.getCity())
                .province(Utilities.address.getProvince())
                .build();

        var list = List.of(Utilities.address);

        Mockito.when(addressRepository.findAllByCustomerId(Utilities.customerDto.getId()))
                .thenReturn(Optional.of(list));


        var expected = List.of(addressListResponse);
        var actual = addressService.getAllAddressesOfCustomer();

        assertEquals(expected, actual);
    }

    @Test
    void searchAddress_shouldReturnAddressDto_WhenAddressFound() {
        var list = Optional.of(List.of(Utilities.address));
        Mockito.when(customerService.findCustomerInContext()).thenReturn(Utilities.customerDto);
        Mockito.when(addressRepository.findAllByCustomerId(customerService.findCustomerInContext().getId()))
                .thenReturn(list);

        Mockito.when(addressRepository.findAddressByAddressName(Utilities.address.getAddressName()))
                .thenReturn(Optional.of(Utilities.address));

        var convertResponse =CustomerDtoDetailed.builder()
                .id(Utilities.customer.getId())
                .username(Utilities.customer.getUsername())
                .email(Utilities.customer.getEmail())
                .password(Utilities.customer.getPassword())
                .addresses(Utilities.customer.getAddresses())
                .birthday(Utilities.customer.getBirthday())
                .age(24)
                .build();

        Mockito.when(customerService.convertResponse(Utilities.customer)).thenReturn(convertResponse);

        AddressDto expected = AddressDto.builder()
                .id(Utilities.address.getId())
                .addressName(Utilities.address.getAddressName())
                .city(Utilities.address.getCity())
                .province(Utilities.address.getProvince())
                .customer(customerService.convertResponse(Utilities.address.getCustomer()))
                .build();


        var actual = addressService.searchAddress(Utilities.address.getAddressName());
        assertEquals(expected, actual);
    }

    @Test
    void searchAddress_shouldThrowException_WhenAddressNotFound() {
        String addressname_doesnotexist ="addressName";
        Mockito.when(addressService.isCustomerHaveThatAddress(Utilities.address.getAddressName()))
                .thenReturn(false);

        org.assertj.core.api.Assertions.assertThatThrownBy(() -> addressService.searchAddress(addressname_doesnotexist))
                .isInstanceOf(GenericException.class)
                .hasMessageContaining("You do not have an address with that name.");

    }

    @Test
    void isCustomerHaveThatAddress_shouldReturnTrue_WhenCustomerHaveThatAddress() {
        var list = Optional.of(List.of(Utilities.address));
        Mockito.when(customerService.findCustomerInContext()).thenReturn(Utilities.customerDto);
        Mockito.when(addressRepository.findAllByCustomerId(customerService.findCustomerInContext().getId()))
                .thenReturn(list);

        assertTrue(addressService.isCustomerHaveThatAddress(Utilities.address.getAddressName()));
    }

    @Test
    void isCustomerHaveThatAddress_shouldReturnFalse_WhenCustomerDoesNotHaveThatAddress() {
        var list = Optional.of(List.of(Utilities.address));
        Mockito.when(customerService.findCustomerInContext()).thenReturn(Utilities.customerDto);
        Mockito.when(addressRepository.findAllByCustomerId(customerService.findCustomerInContext().getId()))
                .thenReturn(list);

        assertFalse(addressService.isCustomerHaveThatAddress("non-existing address name"));
    }

    @Test
    void convertListResponse_shouldReturnListOfAddressListResponse() {
        var expected = AddressListResponse.builder()
                .name(Utilities.address.getAddressName())
                .city(Utilities.address.getCity())
                .province(Utilities.address.getProvince())
                .build();
        var actual = addressService.convertListResponse(Utilities.address);

        assertEquals(expected, actual);
    }

    @Test
    void convertDto_shouldConvertAddressToAddressDto() {

        var convertResponse =CustomerDtoDetailed.builder()
                        .id(Utilities.customer.getId())
                        .username(Utilities.customer.getUsername())
                        .email(Utilities.customer.getEmail())
                        .password(Utilities.customer.getPassword())
                        .addresses(Utilities.customer.getAddresses())
                        .birthday(Utilities.customer.getBirthday())
                        .age(24)
                                .build();

        Mockito.when(customerService.convertResponse(Utilities.customer)).thenReturn(convertResponse);

        AddressDto expected = AddressDto.builder()
                .id(Utilities.address.getId())
                .addressName(Utilities.address.getAddressName())
                .city(Utilities.address.getCity())
                .province(Utilities.address.getProvince())
                .customer(customerService.convertResponse(Utilities.address.getCustomer()))
                .build();

        var result = addressService.convertDto(Utilities.address);

        assertEquals(expected, result);
    }
}