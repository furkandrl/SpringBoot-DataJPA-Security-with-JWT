package com.example.jwtdeneme.service;

import com.example.jwtdeneme.dto.AddressDto;
import com.example.jwtdeneme.dto.AddressListResponse;
import com.example.jwtdeneme.dto.CustomerDtoDetailed;
import com.example.jwtdeneme.exception.GenericException;
import com.example.jwtdeneme.model.Address;
import com.example.jwtdeneme.repository.AddressRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    void saveAddress_shouldReturnAddressDto() {
        Mockito.when(customerService.findCustomerInContext()).thenReturn(Utilities.customerDto);
        Mockito.when(addressRepository.findAllByCustomerId(customerService.findCustomerInContext().getId()))
                .thenReturn(Optional.of(Collections.emptyList()));

        Mockito.when(addressRepository.findAddressByAddressName(Utilities.saveAddressRequest.getName()))
                .thenReturn(Optional.empty());

        Address address = new Address();
        address.setId(UUID.randomUUID());
        address.setAddressName(Utilities.saveAddressRequest.getName());
        address.setCity(Utilities.saveAddressRequest.getCity());
        address.setProvince(Utilities.saveAddressRequest.getProvince());
        address.setCustomer(customerService.findCustomerInContextReturnCustomer());

        var convertResponse =CustomerDtoDetailed.builder()
                .id(Utilities.customer.getId())
                .username(Utilities.customer.getUsername())
                .email(Utilities.customer.getEmail())
                .password(Utilities.customer.getPassword())
                .addresses(Utilities.customer.getAddresses())
                .birthday(Utilities.customer.getBirthday())
                .age(24)
                .build();

        Mockito.when(customerService.convertResponse(address.getCustomer()))
                .thenReturn(convertResponse);

        AddressDto expected = AddressDto.builder()
                .id(address.getId())
                .addressName(address.getAddressName())
                .city(address.getCity())
                .province(address.getProvince())
                .customer(customerService.convertResponse(address.getCustomer()))
                .build();

        Mockito.when(addressRepository.save(address))
                .thenReturn(address);

        Mockito.when(addressService.convertDto(address))
                .thenReturn(expected);

        var actual = addressService.saveAddress(Utilities.saveAddressRequest);

        assertEquals(expected, actual);
    }

    @Test
    void updateAddress_ShouldReturn_AddressListResponse_WhenAddressUpdated() {
        var list = Optional.of(List.of(Utilities.address));
        Mockito.when(customerService.findCustomerInContext()).thenReturn(Utilities.customerDto);
        Mockito.when(addressRepository.findAllByCustomerId(customerService.findCustomerInContext().getId()))
                .thenReturn(list);

        Mockito.when(addressRepository.findAddressByAddressName(Utilities.address.getAddressName()))
                .thenReturn(Optional.of(Utilities.address));

        Mockito.when(customerService.findCustomerInContextReturnCustomer()).thenReturn(Utilities.customer);

        Address updatedAddress = new Address();
        updatedAddress.setAddressName(Utilities.saveAddressRequest.getName());
        updatedAddress.setCity(Utilities.saveAddressRequest.getCity());
        updatedAddress.setProvince(Utilities.saveAddressRequest.getProvince());
        updatedAddress.setCustomer(customerService.findCustomerInContextReturnCustomer());

        Mockito.when(addressRepository.save(updatedAddress))
                        .thenReturn(updatedAddress);

        var expected = AddressListResponse.builder()
                .name(updatedAddress.getAddressName())
                .city(updatedAddress.getCity())
                .province(updatedAddress.getProvince())
                .build();

        Mockito.when(addressService.convertListResponse(updatedAddress))
               .thenReturn(expected);

        var actual = addressService.updateAddress(Utilities.address.getAddressName(), Utilities.saveAddressRequest);

        assertEquals(expected, actual);

    }

    @Test
    void deleteAddress_shouldReturnAddressDto_WhenAddressExists() {
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

        var actual = addressService.deleteAddress(Utilities.address.getAddressName());

        assertEquals(expected, actual);
    }

    @Test
    void deleteAddress_shouldThrowError_WhenCustomerDoesNotHaveThatAddress() {
        var list = Optional.of(List.of(Utilities.address));
        Mockito.when(customerService.findCustomerInContext()).thenReturn(Utilities.customerDto);
        Mockito.when(addressRepository.findAllByCustomerId(customerService.findCustomerInContext().getId()))
                .thenReturn(list);

        org.assertj.core.api.Assertions.assertThatThrownBy(() -> addressService.deleteAddress("non-existing addressName"))
                .isInstanceOf(GenericException.class)
                .hasMessageContaining("You do not have an address with that name.");
    }

    @Test
    void deleteAddress_shouldThrowError_WhenAddressDoesNotExist() {
        var list = Optional.of(List.of(Utilities.address));
        Mockito.when(customerService.findCustomerInContext()).thenReturn(Utilities.customerDto);
        Mockito.when(addressRepository.findAllByCustomerId(customerService.findCustomerInContext().getId()))
                .thenReturn(list);

        Mockito.when(addressRepository.findAddressByAddressName(Utilities.address.getAddressName()))
                .thenReturn(Optional.empty());

        org.assertj.core.api.Assertions.assertThatThrownBy(() -> addressService.deleteAddress(Utilities.address.getAddressName()))
                .isInstanceOf(GenericException.class)
                .hasMessageContaining("Address not found.");
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