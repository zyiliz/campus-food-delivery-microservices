package com.example.client;

import com.example.result.Result;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.math.BigDecimal;
import java.util.List;

@HttpExchange("http://localhost:8184/api/dish")
public interface DishClient {

    @GetExchange("//getToAI")
    Result<List<Long>> getRecommend(@RequestHeader("auth") String auth
                                    ,@RequestParam(value = "categoryName",required = false)String categoryName,
                                    @RequestParam(value = "price",required = false) BigDecimal price);
}
