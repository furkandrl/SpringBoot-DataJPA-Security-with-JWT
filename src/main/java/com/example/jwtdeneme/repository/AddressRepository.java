package com.example.jwtdeneme.repository;

import com.example.jwtdeneme.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AddressRepository extends JpaRepository<Address, UUID> {
    Optional<Address> findAddressByAddressName(String name);
    Optional<List<Address>> findAllByCustomerId(UUID id);


}
