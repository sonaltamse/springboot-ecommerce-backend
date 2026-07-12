package com.ecommerce.project.service;

import com.ecommerce.project.exceptions.ResourceNotfoundException;
import com.ecommerce.project.model.Address;
import com.ecommerce.project.model.User;
import com.ecommerce.project.payload.AddressDTO;
import com.ecommerce.project.repositories.AddressRepository;
import com.ecommerce.project.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    ModelMapper modelMapper;
    @Autowired
    AddressRepository addressRepository;
    @Autowired
    UserRepository userRepository;

    @Override
    public AddressDTO createAddress(AddressDTO addressDTO, User user) {
        Address address = modelMapper.map(addressDTO, Address.class);
        List<Address> addressList = user.getAddresses();
        addressList.add(address);

        user.setAddresses(addressList);
        address.setUser(user);
        Address savedAddress = addressRepository.save(address);
        return modelMapper.map(savedAddress, AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getAddresses() {
        List<Address> addresses = addressRepository.findAll();
        return addresses.stream().map(address -> modelMapper.map(address, AddressDTO.class)).toList();
    }

    @Override
    public AddressDTO getAddressById(Long id) {
        Address address = addressRepository.findById(id).orElseThrow(()-> new ResourceNotfoundException("Address", "addressId", id));
        return modelMapper.map(address, AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getUserAddresses(User user) {
        List<Address> userAddresses = user.getAddresses();
        return userAddresses.stream().map(address -> modelMapper.map(address, AddressDTO.class)).toList();
    }

    @Override
    public AddressDTO updateAddressById(Long id, AddressDTO addressDTO) {
        Address address = addressRepository.findById(id).orElseThrow(()-> new ResourceNotfoundException("Address", "addressId", id));
        address.setStreet(addressDTO.getStreet());
        address.setBuildingName(addressDTO.getBuildingName());
        address.setCity(addressDTO.getCity());
        address.setState(addressDTO.getState());
        address.setCountry(addressDTO.getCountry());
        address.setPincode(addressDTO.getPincode());
        Address updatedAddress = addressRepository.save(address);

        User user = address.getUser();
        user.getAddresses().removeIf(a -> a.getAddressId().equals(id));
        user.getAddresses().add(updatedAddress);
        userRepository.save(user);
        return modelMapper.map(updatedAddress, AddressDTO.class);
     }

    @Override
    public void deleteAddressById(Long id) {
        Address address = addressRepository.findById(id).orElseThrow(()-> new ResourceNotfoundException("Address", "addressId", id));
        User user = address.getUser();
        user.getAddresses().removeIf(a -> a.getAddressId().equals(id));
        userRepository.save(user);
        addressRepository.delete(address);
    }
}
