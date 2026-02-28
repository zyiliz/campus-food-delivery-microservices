package com.example.cache;

import com.example.pojo.DTO.AddCardDTO;


import java.util.List;

public interface CardCache {
    void addCard(AddCardDTO addCardDTO, Long userId);

    List<AddCardDTO> getCard(Long userId);

    Boolean judgeDishExist(Long userId,Long id);

    Boolean judgeSetmealExist(Long userId,Long id);

    void clearCart(Long userId);

    void subCart(Long userId,AddCardDTO addCardDTO);




}
