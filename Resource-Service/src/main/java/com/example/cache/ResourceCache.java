package com.example.cache;

import java.util.List;
import java.util.Set;

public interface ResourceCache {

    void addImagePath(String path);

    void deleteImagePath(List<String> del_list);

    Set<String> getImagePathList();

    void deleteAllImagePath();

}
