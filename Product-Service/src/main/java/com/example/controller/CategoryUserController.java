package com.example.controller;

import com.example.pojo.VO.CategoryVO;
import com.example.result.Result;
import com.example.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/user/category")
public class CategoryUserController {

    private final CategoryService categoryService;

    @GetMapping("/All")
    public Result<List<CategoryVO>> getALl(){
        return categoryService.getALlCategoryList();
    }
}
