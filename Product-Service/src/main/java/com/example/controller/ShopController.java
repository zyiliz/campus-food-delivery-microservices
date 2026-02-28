package com.example.controller;

import com.example.result.Result;
import com.example.service.StatusService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shop/admin")
@AllArgsConstructor
public class ShopController {

    private final StatusService service;

    @PutMapping("/{status}")
    public Result<String> updateStatus(@PathVariable Integer status){
        return service.updateStatus(status);
    }

    @GetMapping("/getStatus")
    public Result<Integer> getStatus(){
        return service.getStatus();
    }
}
