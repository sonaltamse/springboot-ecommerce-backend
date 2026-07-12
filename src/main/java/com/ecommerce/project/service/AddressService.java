package com.ecommerce.project.service;

import com.ecommerce.project.model.User;
import com.ecommerce.project.payload.AddressDTO;
import jakarta.validation.Valid;

import java.util.List;

public interface AddressService {
    AddressDTO createAddress(AddressDTO addressDTO, User user);

    List<AddressDTO> getAddresses();

    AddressDTO getAddressById(Long id);

    List<AddressDTO> getUserAddresses(User user);

    AddressDTO updateAddressById(Long id, @Valid AddressDTO addressDTO);

    void deleteAddressById(Long id);
}
