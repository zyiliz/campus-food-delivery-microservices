package com.example.utils;

import com.alibaba.nacos.common.utils.UuidUtils;
import com.example.cache.ResourceCache;
import com.example.config.MinioConfig;
import io.micrometer.common.util.StringUtils;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class MinioUtils {

    private final MinioClient minioClient;

    private final ResourceCache resourceCache;

    @Value("${minio.endpoint}")
    private String endpoint;

    @Value("${minio.bucket}")
    private String bucket;


    /**
     * 文件上传
     * @param file 文件
     */
    @SneakyThrows
    public String minioUpload(MultipartFile file){
        String originalFilename = file.getOriginalFilename();
        if (StringUtils.isBlank(originalFilename)){
            throw new IllegalArgumentException("文件不能为空！");
        }

        // 1. 先找到点号的位置
        int dotIndex = originalFilename.lastIndexOf(".");
        String extension;

        // 2. 安全处理后缀名：如果找不到点号（比如文件名是 "blob"），默认给 .jpg
        if (dotIndex != -1) {
            extension = originalFilename.substring(dotIndex);
        } else {
            extension = ".jpg";
        }

        // 3. 使用 UUID 拼接，这样即便文件名没点号也不会报错了
        String fileName = UuidUtils.generateUuid() + extension;

        String objectName = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM/dd")) + "/" + fileName;

        minioClient.putObject(PutObjectArgs.builder()
                .bucket(bucket).object(objectName)
                .stream(file.getInputStream(), file.getSize(), -1)
                .contentType(file.getContentType()).build());

        resourceCache.addImagePath(objectName);
        return objectName;
    }

    @SneakyThrows
    public String getPreview(String fileName){
        GetPresignedObjectUrlArgs build = new GetPresignedObjectUrlArgs()
                                            .builder()
                                            .bucket(bucket)
                                            .object(fileName)
                                            .method(Method.GET)
                                            .build();
            String presignedObjectUrl = minioClient.getPresignedObjectUrl(build);
            return presignedObjectUrl;
    }


    @SneakyThrows
    public void removeFile(String fileName){
            minioClient.removeObject( RemoveObjectArgs.builder().bucket(bucket).object(fileName).build());
    }


    /**
     * 查看文件对象
     * @return 存储bucket内文件对象信息
     */
    @SneakyThrows
    public List<Item> listObjects() {
        Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder().bucket(bucket).build());
        List<Item> items = new ArrayList<>();

        for (Result<Item> result : results) {
            items.add(result.get());
        }
        return items;
    }


    public void deletePathList(List<String> pathList){
        List<DeleteObject> objects = pathList.stream().map(DeleteObject::new).toList();
        minioClient.removeObjects(RemoveObjectsArgs.builder().bucket(bucket).objects(objects).build());
    }


}
