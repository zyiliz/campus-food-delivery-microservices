package com.example.client;

import com.example.config.FeignConfiguration;
import com.example.result.DataRespond;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(value = "resource-Service",configuration = FeignConfiguration.class)
public interface ResourceClient {

    @PostMapping(value = "/api/resource-service/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    DataRespond upload(@RequestPart("file")MultipartFile file);


    @GetMapping("/api/resource-service/getPreview")
    DataRespond getPreview(@RequestParam String fileName);

}
