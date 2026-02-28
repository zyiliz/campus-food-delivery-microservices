package com.example.service.Impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.cache.DishCache;
import com.example.convert.DishConvert;
import com.example.mapper.CategoryMapper;
import com.example.mapper.DishMapper;
import com.example.pojo.DTO.DishInsertDTO;
import com.example.pojo.DTO.DishPageQueryDTO;
import com.example.pojo.DTO.DishStateUpdateDTO;
import com.example.pojo.DTO.DishUpdateDTO;
import com.example.pojo.VO.DishPageVO;
import com.example.pojo.VO.OverViewVO;
import com.example.pojo.entity.Category;
import com.example.pojo.entity.Dish;
import com.example.result.PageResult;
import com.example.result.Result;
import com.example.service.DishService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class DishServiceImpl implements DishService {

    private final DishMapper dishMapper;

    private final DishConvert dishConvert;

    private final CategoryMapper categoryMapper;

    private final DishCache dishCache;

    private static final long DELAY_TIME = 5;




    @Override
    public Result<String> insertDish(DishInsertDTO dishInsertDTO) {
        log.info("【添加菜品】,请求参数：{}",dishInsertDTO);
        LambdaQueryWrapper<Dish> exitsLqw = new LambdaQueryWrapper<Dish>()
                .eq(Dish::getName,dishInsertDTO.getName());
        boolean exists = dishMapper.exists(exitsLqw);
        if (exists){
            return Result.fail("该商品已经存在！");
        }
        LambdaQueryWrapper<Category> categoryLambdaQueryWrapper = new LambdaQueryWrapper<Category>()
                .eq(Category::getId,dishInsertDTO.getCategoryId());
        boolean exists1 = categoryMapper.exists(categoryLambdaQueryWrapper);
        if (!exists1){
            return Result.fail("该品种不存在！");
        }
        Dish dish = dishConvert.toDish(dishInsertDTO);
        int insert = dishMapper.insert(dish);
        if (insert>0){
            log.info("【添加菜品】成功,菜品id：{}",dish.getId());
            return Result.success("菜品添加成功！");
        }else {
            log.error("【添加菜品】失败");
            return Result.fail("菜品添加失败，请稍后再试！");
        }

    }

    @Override
    public Result<PageResult> getDishPage(DishPageQueryDTO dishPageQueryDTO) {
        Page<Dish> page =new Page<>(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());
        LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper<Dish>()
                .like(dishPageQueryDTO.getName()!= null,Dish::getName,dishPageQueryDTO.getName())
                .eq(dishPageQueryDTO.getCategoryId() != null,Dish::getCategoryId,dishPageQueryDTO.getCategoryId())
                .eq(dishPageQueryDTO.getStatus()!=null,Dish::getStatus,dishPageQueryDTO.getStatus())
                .orderByDesc(Dish::getUpdateTime);
        Page<Dish> page1 = dishMapper.selectPage(page, lqw);
        Set<Long> categoryIds = page1.getRecords().stream()
                .map(Dish::getCategoryId)
                .filter(id -> id != null)
                .collect(Collectors.toSet());
        Map<Long,String> categoryMap = new HashMap<>();
        if (!categoryIds.isEmpty()){
            List<Category> categories = categoryMapper.selectByIds(categoryIds);
            Map<Long, String> tempMap= categories.stream().collect(Collectors.toMap(Category::getId,Category::getName));
            categoryMap.putAll(tempMap);
        }
        List<DishPageVO> resultList = page1.getRecords().stream().map((item)->{
            DishPageVO dishPageVO = dishConvert.toDishPageVO(item);
            Long key = dishPageVO.getCategoryId();
            if (key != null && categoryMap.containsKey(key)){
                dishPageVO.setCategoryName(categoryMap.get(key));
            }
            return dishPageVO;
        }).collect(Collectors.toList());
        return Result.success(new PageResult(page1.getTotal(),resultList));
    }

    @Override
    public Result<DishPageVO> getDishById(Long id) {
        DishPageVO dish1 = dishCache.getDish(id);
        if (ObjectUtil.isNotNull(dish1)){
            return Result.success(dish1);
        }
        LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper<Dish>()
                .eq(Dish::getId,id);
        Dish dish = dishMapper.selectOne(lqw);
        if (ObjectUtil.isNull(dish)){
            return Result.fail("该商品不存在！");
        }
        Category category = categoryMapper.selectById(dish.getCategoryId());
        DishPageVO dishPageVO = dishConvert.toDishPageVO(dish);
        if (ObjectUtil.isNotNull(category)){
            dishPageVO.setCategoryName(category.getName());
        }
        dishCache.addDish(dishPageVO);
        return Result.success(dishPageVO);
    }

    @Override
    public Result<String> updateDish(DishUpdateDTO dishUpdateDTO) {
        log.info("【更新菜品】,请求参数：{}",dishUpdateDTO);
        Long dishId = dishUpdateDTO.getId();

        dishCache.deleteDish(dishId);
        LambdaUpdateWrapper<Dish> luw = new LambdaUpdateWrapper<Dish>()
                .eq(Dish::getId,dishUpdateDTO.getId())
                .set(dishUpdateDTO.getName() != null,Dish::getName,dishUpdateDTO.getName())
                .set(dishUpdateDTO.getPrice() != null,Dish::getPrice,dishUpdateDTO.getPrice())
                .set(dishUpdateDTO.getImage() != null,Dish::getImage,dishUpdateDTO.getImage())
                .set(dishUpdateDTO.getDescription() != null,Dish::getDescription,dishUpdateDTO.getDescription());
        if (dishUpdateDTO.getCategoryId() != null){
            LambdaQueryWrapper<Category> lqw = new LambdaQueryWrapper<Category>()
                    .eq(Category::getId,dishUpdateDTO.getId());
            boolean exists = categoryMapper.exists(lqw);
            if (exists){
                luw.set(Dish::getCategoryId,dishUpdateDTO.getCategoryId());
            }else {
                return Result.fail("该品种不存在！");
            }
        }
        int update = dishMapper.update(null,luw);
        CompletableFuture.runAsync(()->{
            try{
                TimeUnit.SECONDS.sleep(DELAY_TIME);
                dishCache.deleteDish(dishId);
            }catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        });
        return update>0?Result.success("更新成功！")
                :Result.fail("更新失败，请稍后再试！");
    }


    @Transactional
    @Override
    public Result<String> updateState(DishStateUpdateDTO dishStateUpdateDTO) {
        List<Long> ids = dishStateUpdateDTO.getIds();
        dishCache.deleteDishList(ids);
        LambdaUpdateWrapper<Dish> luw = new LambdaUpdateWrapper<Dish>()
                .set(Dish::getStatus,dishStateUpdateDTO.getState())
                .in(Dish::getId,dishStateUpdateDTO.getIds());
        int update = dishMapper.update(null,luw);
        CompletableFuture.runAsync(()->{
            try {
                TimeUnit.SECONDS.sleep(DELAY_TIME);
                dishCache.deleteDishList(ids);
            }catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        });
        return update>0?Result.success("更改成功！")
                :Result.fail("更改失败！");
    }

    @Transactional
    @Override
    public Result<String> deleteDish(List<Long> ids) {
        LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper<Dish>()
                .eq(Dish::getStatus,true)
                .in(Dish::getId,ids);
        Long aLong = dishMapper.selectCount(lqw);
        if (aLong>0){
            return Result.fail("存在在售商品，无法删除！");
        }
        int i = dishMapper.deleteByIds(ids);
        dishCache.deleteDishList(ids);
        return i>0?Result.success("删除成功！")
                :Result.fail("删除失败！");
    }

    @Override
    public Result<OverViewVO> overviewDishes() {
        LambdaQueryWrapper<Dish> soldLqw = new LambdaQueryWrapper<Dish>()
                .eq(Dish::getStatus,Dish.sold);
        Long sold = dishMapper.selectCount(soldLqw);
        LambdaQueryWrapper<Dish> discontinuedLqw = new LambdaQueryWrapper<Dish>()
                .eq(Dish::getStatus,Dish.discontinued);
        Long discontinued = dishMapper.selectCount(discontinuedLqw);
        OverViewVO overViewVO = OverViewVO.builder()
                .sold(sold)
                .discontinued(discontinued)
                .build();
        return Result.success(overViewVO);
    }

    @Override
    public Result<List<Long>> getRecommendToAI(String category, BigDecimal price) {
        List<Long> results = dishMapper.selectDishIdsByCategoryAndPriceGt(category,price);
        if (results == null || results.isEmpty()){
            return Result.fail(null);
        }

        return Result.success(results);
    }

    @Override
    public List<String> getImagePathList() {
        List<String> recent24HoursDishImages = dishMapper.getRecent24HoursDishImages();
        return recent24HoursDishImages;
    }

    @Override
    public Result<List<DishPageVO>> getByCategoryName(String name) {
        LambdaQueryWrapper<Category> exist = new LambdaQueryWrapper<Category>()
                .eq(Category::getName,name);
        boolean exists = categoryMapper.exists(exist);
        if (!exists){
            return Result.fail("该种类不存在!");
        }
        List<Dish> byCategoryName = dishMapper.getByCategoryName(name);
        List<DishPageVO> dishPageVOList = dishConvert.toDishPageVOList(byCategoryName);
        return Result.success(dishPageVOList);
    }


}
