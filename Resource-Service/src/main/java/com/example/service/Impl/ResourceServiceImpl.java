package com.example.service.Impl;

import com.example.cache.ResourceCache;
import com.example.result.DataFailRespond;
import com.example.result.DataRespond;
import com.example.result.DataSuccessRespond;
import com.example.result.Result;
import com.example.service.ResourceService;
import com.example.utils.MinioUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResourceServiceImpl implements ResourceService {

    private final MinioUtils minioUtils;



    //上传文件
    @Override
    public DataRespond upload(MultipartFile file) {

        String s = minioUtils.minioUpload(file);
        if (s.isBlank()){
            log.error("【文件上传】失败");
            return new DataFailRespond("文件上传失败！");
        }
        log.error("【文件上传】成功,文件路径：{}",s);
        return new DataSuccessRespond("文件上传成功！",s);
    }

    @Override
    public DataRespond preview(String fileName) {
        String preview = minioUtils.getPreview(fileName);
        if (preview.isBlank()){
            return new DataFailRespond("文件获取失败！");
        }
        return new DataSuccessRespond("文件获取成功！",preview);
    }

    @Override
    public Result<String> getUrl(MultipartFile file) {
        String s = minioUtils.minioUpload(file);
        if (s.isBlank()){
            return Result.fail("文件上传失败！");
        }
        return Result.success(s);
    }
}
