package com.example.service;

import com.example.pojo.DTO.AddCardDTO;
import com.example.result.Result;

import java.util.List;

public interface CardService {

    Result<String> addCart(AddCardDTO addCardDTO);

    Result<List<AddCardDTO>> getCartList();

    Result<String> subCart(AddCardDTO addCardDTO);

    Result<String> clearCart();

}
