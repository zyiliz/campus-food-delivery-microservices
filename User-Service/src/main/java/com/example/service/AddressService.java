package com.example.service;

import com.example.pojo.DTO.AddressDTO;
import com.example.pojo.DTO.AddressUpdateDTO;
import com.example.pojo.VO.AddressVO;
import com.example.result.Result;

import java.util.List;

public interface AddressService {
    Result<String> insertAddress(AddressDTO addressDTO);

    Result<List<AddressVO>> getAddressList();

    Result<String> updateAddress(AddressUpdateDTO updateDTO);

    Result<String> deleteAddress(Long id);

    Result<AddressVO> getAddressById(Long id);

    Result<AddressVO> getDefaultAddress();

}
