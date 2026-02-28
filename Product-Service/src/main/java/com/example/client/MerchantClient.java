package com.example.client;

import com.example.result.DataRespond;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "Merchant-Service")
public interface MerchantClient {
    @GetMapping("/api/merchant-service/judgeExist/{id}")
    DataRespond judgeExist(@PathVariable String id);
}
