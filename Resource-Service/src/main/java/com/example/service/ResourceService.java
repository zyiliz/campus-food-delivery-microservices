package com.example.service;

import com.example.result.DataRespond;
import com.example.result.Result;
import org.springframework.web.multipart.MultipartFile;

public interface ResourceService {

    DataRespond upload(MultipartFile file);

    DataRespond preview(String fileName);

    Result<String> getUrl(MultipartFile file);

}
