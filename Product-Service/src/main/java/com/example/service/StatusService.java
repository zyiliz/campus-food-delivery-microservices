package com.example.service;

import com.example.result.Result;

public interface StatusService {

    Result<String> updateStatus(Integer status);

    Result<Integer> getStatus();

}
