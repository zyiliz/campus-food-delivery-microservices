package com.example.controller;

import com.example.pojo.DTO.AddressDTO;
import com.example.pojo.DTO.AddressUpdateDTO;
import com.example.pojo.VO.AddressVO;
import com.example.result.Result;
import com.example.service.AddressService;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/address")
public class AddressController {

    private final AddressService addressService;

    @PostMapping("")
    public Result<String> insertAddress(@Validated @RequestBody AddressDTO addressDTO){
        return addressService.insertAddress(addressDTO);
    }


    @GetMapping("/list")
    public Result<List<AddressVO>> getAddressList(){
        return addressService.getAddressList();
    }


    @PutMapping("")
    public Result<String> updateAddress(@Validated @RequestBody AddressUpdateDTO updateDTO){
        return addressService.updateAddress(updateDTO);
    }

    @DeleteMapping("/{id}")
    public Result<String> deleteAddress(@PathVariable Long id){
        return addressService.deleteAddress(id);
    }

    @GetMapping("/{id}")
    public Result<AddressVO> getById(@PathVariable Long id){
        return addressService.getAddressById(id);
    }

    @GetMapping("/getDefault")
    public Result<AddressVO> getDefault(){
        return addressService.getDefaultAddress();
    }
}
