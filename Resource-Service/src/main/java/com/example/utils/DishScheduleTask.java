package com.example.utils;

import com.example.cache.ResourceCache;
import com.example.client.DishClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
@Slf4j
public class DishScheduleTask {

    private final DishClient dishClient;

    private final ResourceCache resourceCache;

    private final MinioUtils minioUtils;


    /**
     * 每天凌晨1点执行的定时任务
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void resourceTask(){
        log.info("【定时任务删除冗余图片】");
        List<String> imagePathList = dishClient.getImagePathList();
        Set<String> imagePathList1 = resourceCache.getImagePathList();
        Set<String> dbPathSet = new HashSet<>(imagePathList);
        List<String> all = imagePathList1.stream()
                .filter(item -> !dbPathSet.contains(item)).toList();
        if (!all.isEmpty()){
            minioUtils.deletePathList(all);
            log.info("【批量删除minio中冗余的图片】");
        }
        resourceCache.deleteAllImagePath();
        log.info("【批量删除redis中图片路径】");

    }
}
