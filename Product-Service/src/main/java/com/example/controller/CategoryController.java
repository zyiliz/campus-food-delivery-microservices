package com.example.controller;

import com.example.pojo.DTO.CategoryCreateDTO;
import com.example.pojo.DTO.CategoryPageQueryDTO;
import com.example.pojo.DTO.CategoryUpdateDTO;
import com.example.pojo.VO.CategoryVO;
import com.example.result.PageResult;
import com.example.result.Result;
import com.example.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/admin/category")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("")
    public Result<String> insertCategory(@Validated @RequestBody CategoryCreateDTO categoryCreateDTO){
        return categoryService.insertCategory(categoryCreateDTO);
    }

    @GetMapping("/page")
    public Result<PageResult> pageQuery(@RequestBody CategoryPageQueryDTO categoryPageQueryDTO){
        return categoryService.pageQuery(categoryPageQueryDTO);
    }

    @DeleteMapping("/{id}")
    public Result<String> deleteCategory(@PathVariable Long id){
        return categoryService.deleteCategory(id);
    }

    @PutMapping("")
    public Result<String> updateCategory(@RequestBody CategoryUpdateDTO categoryUpdateDTO){
        return categoryService.updateCategory(categoryUpdateDTO);
    }

    @PutMapping("/{id}/{status}")
    public Result<String> updateState(@PathVariable Long id,
                                      @PathVariable Boolean status){
        return categoryService.updateState(id,status);
    }

    @GetMapping("/list")
    public Result<List<String>> list(){
        return categoryService.getCategoryList();
    }

    @GetMapping("/getCategoryIdAndName")
    public Result<List<CategoryVO>> getCategoryIdAndName(){
        return categoryService.getALlCategoryList();
    }
}
