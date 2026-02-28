package com.example.client;

import com.example.pojo.VO.AddressVO;
import com.example.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "User-Service")
public interface AddressClient {

    @GetMapping("/{id}")
    Result<AddressVO> getAddressById(@PathVariable Long id);
}
