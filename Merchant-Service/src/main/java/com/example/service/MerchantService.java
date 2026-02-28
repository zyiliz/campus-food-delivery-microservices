package com.example.service;

import com.example.entity.request.MerchantRequest;
import com.example.entity.request.MerchantUpdateRequest;
import com.example.result.DataRespond;
import com.example.result.MsgRespond;

public interface MerchantService {
    MsgRespond insertMerchant(MerchantRequest merchantRequest);

    MsgRespond updateMerchant(MerchantUpdateRequest merchantUpdateRequest);

    DataRespond MerchantExist(String id);
}
