package com.example.service.Impl;

import com.example.cache.ShopStatusCache;
import com.example.result.Result;
import com.example.service.StatusService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class StatusServiceImpl implements StatusService {

    private final ShopStatusCache shopStatusCache;

    @Override
    public Result<String> updateStatus(Integer status) {
        log.info("【更新商店状态】,请求参数：{}",status);
        shopStatusCache.updateShopStatus(status);
        return Result.success("更新成功！");
    }

    @Override
    public Result<Integer> getStatus(){
        Integer shopStatus = shopStatusCache.getShopStatus();
        return Result.success(shopStatus);
    }
}
