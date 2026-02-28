package com.example.controller;

import com.example.pojo.DTO.AddCardDTO;
import com.example.result.Result;
import com.example.service.CardService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/user/shoppingCard")
public class UserController {

    private final CardService cardService;

    //添加商品到购物车
    @PostMapping("/add")
    public Result<String> addCart(@RequestBody AddCardDTO addCardDTO){
        return cardService.addCart(addCardDTO);
    }

    //查询购物车列表
    @GetMapping("")
    public Result<List<AddCardDTO>> getCart(){
        return cardService.getCartList();
    }

    //减少购物车商品
    @PostMapping("/sub")
    public Result<String> subCart(@RequestBody AddCardDTO addCardDTO){
        return cardService.subCart(addCardDTO);
    }


    //清空购物车商品
    @GetMapping("/clear")
    public Result<String> clearCart(){
        return cardService.clearCart();
    }



}
