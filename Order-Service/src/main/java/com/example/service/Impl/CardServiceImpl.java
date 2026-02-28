package com.example.service.Impl;

import com.example.cache.CardCache;
import com.example.client.ProductClient;
import com.example.pojo.DTO.AddCardDTO;
import com.example.pojo.VO.DishPageVO;
import com.example.pojo.VO.SetmealVO;
import com.example.result.Result;
import com.example.service.CardService;
import com.example.utils.UserContext;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class CardServiceImpl implements CardService {

    private final CardCache cardCache;

    private final ProductClient productClient;

    @Override
    public Result<String> addCart(AddCardDTO addCardDTO) {
        Long userId = UserContext.getId();
        log.info("【用户添加购物车】,用户id：{},用户请求：{}",userId,addCardDTO);
        if (addCardDTO.getDishId() != null && cardCache.judgeDishExist(userId,addCardDTO.getDishId())){
                Result<DishPageVO> dishById = productClient.getDishById(addCardDTO.getDishId());
                DishPageVO data = dishById.getData();
                addCardDTO.setName(data.getName());
                addCardDTO.setImage(data.getImage());
                addCardDTO.setPrice(data.getPrice());
        } else if (addCardDTO.getSetmealId() != null && cardCache.judgeSetmealExist(userId,addCardDTO.getSetmealId())) {
            Result<SetmealVO> setmealById = productClient.getSetmealById(addCardDTO.getSetmealId());
            SetmealVO data = setmealById.getData();
            addCardDTO.setName(data.getName());
            addCardDTO.setImage(data.getImage());
            addCardDTO.setPrice(data.getPrice());
        };
        cardCache.addCard(addCardDTO,userId);
        log.info("【用户添加购物车】成功,用户id：{}",userId);
        return Result.success("加入购物车成功！");
    }

    @Override
    public Result<List<AddCardDTO>> getCartList() {
        Long userId = UserContext.getId();
        List<AddCardDTO> card = cardCache.getCard(userId);
        return Result.success(card);
    }

    @Override
    public Result<String> subCart(AddCardDTO addCardDTO) {
        Long userId = UserContext.getId();
        cardCache.subCart(userId,addCardDTO);
        return Result.success("扣除成功！");
    }

    @Override
    public Result<String> clearCart() {
        Long userId = UserContext.getId();
        cardCache.clearCart(userId);
        return Result.success("清空成功！");
    }


}
