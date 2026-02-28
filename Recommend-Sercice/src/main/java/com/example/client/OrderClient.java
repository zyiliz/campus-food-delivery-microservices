package com.example.client;

import cn.hutool.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;

@HttpExchange("http://localhost:8183/api/admin/report")
public interface OrderClient {

    @GetExchange("/recommendList/{id}")
    List<Long> getRecommendList(@RequestHeader("auth") String auth, @PathVariable Long id);
}
