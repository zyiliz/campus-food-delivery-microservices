package com.example.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(value = "product-Service")
public interface DishClient {

    @GetMapping("/api/dish/admin/getImagePathList")
    List<String> getImagePathList();
}
