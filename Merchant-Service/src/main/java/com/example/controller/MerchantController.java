package com.example.controller;

import com.example.entity.request.MerchantRequest;
import com.example.entity.request.MerchantUpdateRequest;
import com.example.result.DataRespond;
import com.example.result.MsgRespond;
import com.example.service.MerchantService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/merchant-service")
public class MerchantController {

    private final MerchantService merchantService;

    @PostMapping("/creatMerchant")
    public MsgRespond creatMerchant(@RequestBody MerchantRequest merchantRequest){
        return merchantService.insertMerchant(merchantRequest);
    }

    @PutMapping("/updateMerchant")
    public MsgRespond updateMerchant(@RequestBody MerchantUpdateRequest merchantUpdateRequest){
        return merchantService.updateMerchant(merchantUpdateRequest);
    }

    @GetMapping("/judgeExist/{id}")
    public DataRespond judgeExist(@PathVariable String id){
        return merchantService.MerchantExist(id);
    }

}
