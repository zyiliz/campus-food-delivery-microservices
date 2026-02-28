package com.example.cache;


import com.fasterxml.jackson.core.JsonProcessingException;

public interface UserCache {

    void insertToken(Long ID,String token);

}
