package com.example.client;

import com.example.pojo.VO.DishPageVO;
import com.example.pojo.VO.SetmealVO;
import com.example.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "product-Service")
public interface ProductClient {

    @GetMapping("/api/dish/user/{id}")
    Result<DishPageVO> getDishById(@PathVariable Long id);

   @GetMapping("//api/setmeal/user/{id}")
    Result<SetmealVO> getSetmealById(@PathVariable Long id);
}
