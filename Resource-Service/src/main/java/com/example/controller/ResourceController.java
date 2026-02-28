package com.example.controller;

import com.example.result.DataRespond;
import com.example.result.Result;
import com.example.service.ResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/resource-service")
public class ResourceController {
    public final ResourceService resourceService;


    @PostMapping("/admin/upload")
    public Result<String> uploadImage(@RequestParam("file")MultipartFile file){
        return resourceService.getUrl(file);
    }

    @PostMapping("/uploadUsrImage")
    public Result<String> uploadUsrImage(@RequestParam("file")MultipartFile file){
        return resourceService.getUrl(file);
    }



    @GetMapping("/getPreview")
    public DataRespond preview(@RequestParam String fileName){
        System.out.println("获取到了preview接口！");
        return resourceService.preview(fileName);
    }



}
