package com.example.controller;

import com.example.pojo.DTO.DishInsertDTO;
import com.example.pojo.DTO.DishPageQueryDTO;
import com.example.pojo.DTO.DishStateUpdateDTO;
import com.example.pojo.DTO.DishUpdateDTO;
import com.example.pojo.VO.DishPageVO;
import com.example.pojo.VO.OverViewVO;
import com.example.result.PageResult;
import com.example.result.Result;
import com.example.service.DishService;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/dish")
public class DishController {

    private final DishService dishService;

    @PostMapping("/admin")
    public Result<String> insertDish(@Validated @RequestBody DishInsertDTO dishInsertDTO){
        return dishService.insertDish(dishInsertDTO);
    }

    @GetMapping("/user/page")
    public Result<PageResult> getPageList(@RequestBody DishPageQueryDTO dishPageQueryDTO){
        return dishService.getDishPage(dishPageQueryDTO);
    }

    @GetMapping("/user/{id}")
    public Result<DishPageVO> getDishById(@PathVariable Long id){
        return dishService.getDishById(id);
    }



    @PostMapping("/user/status")
    public Result<String> updateStatus(@RequestBody DishStateUpdateDTO dishStateUpdateDTO){
        return dishService.updateState(dishStateUpdateDTO);
    }

    @PutMapping("/admin")
    public Result<String> updateDish(@RequestBody DishUpdateDTO dishUpdateDTO){
        return dishService.updateDish(dishUpdateDTO);
    }

    @DeleteMapping("/admin")
    public Result<String> deleteDish(@RequestParam List<Long> ids){
        return dishService.deleteDish(ids);
    }

    @GetMapping("/admin/overviewDishes")
    public Result<OverViewVO> overviewDishes(){
        return dishService.overviewDishes();
    }

    @GetMapping("/admin/getImagePathList")
    public List<String> getImagePathList(){
        return dishService.getImagePathList();
    }

    @GetMapping("/getToAI")
    public Result<List<Long>> getToAI(@RequestParam(value = "categoryName",required = false)String categoryName,
                                      @RequestParam(value = "price",required = false)BigDecimal price){
        return dishService.getRecommendToAI(categoryName,price);
    }

    @GetMapping("/getByCategoryName/{name}")
    public Result<List<DishPageVO>> getByCategoryName(@PathVariable String name){
        return dishService.getByCategoryName(name);
    }


}
