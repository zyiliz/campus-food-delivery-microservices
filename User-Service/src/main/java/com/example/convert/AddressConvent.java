package com.example.convert;

import com.example.pojo.DTO.AddressDTO;
import com.example.pojo.VO.AddressVO;
import com.example.pojo.entity.Address;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AddressConvent {
    Address ToAddress(AddressDTO addressDTO);

    AddressVO toAddressVo(Address address);

    List<AddressVO> toAddressVoList(List<Address> list);
}
