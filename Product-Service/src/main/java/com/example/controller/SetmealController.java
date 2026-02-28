package com.example.controller;

import com.example.pojo.DTO.SetmealAddDTO;
import com.example.pojo.DTO.SetmealPageDTO;
import com.example.pojo.VO.OverViewVO;
import com.example.pojo.VO.SetmealVO;
import com.example.result.PageResult;
import com.example.result.Result;
import com.example.service.SetmealService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/setmeal")
public class SetmealController {

    private final SetmealService setmealService;

    @PostMapping("/admin")
    public Result<String> addSetmeal(@RequestBody SetmealAddDTO setmealAddDTO){
        return setmealService.addSetmael(setmealAddDTO);
    }




    @GetMapping("/user/page")
    public Result<PageResult> getSetmaleList(SetmealPageDTO setmealPageDTO){
        return setmealService.getSetmealPage(setmealPageDTO);
    }

    @DeleteMapping("/admin")
    public Result<String> deleteSetmeal(@RequestParam List<Long> ids){
        return setmealService.deleteSetmeal(ids);
    }

    @GetMapping("/user/{id}")
    public Result<SetmealVO> getById(@PathVariable Long id){
        return setmealService.getById(id);
    }

    @GetMapping("/admin/overviewSetmeals")
    public Result<OverViewVO> overviewSetmeals(){
        return setmealService.overviewSetmeals();
    }
}
