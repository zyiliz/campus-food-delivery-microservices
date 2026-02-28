package com.example.service.Impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.cache.SetmealCache;
import com.example.convert.SetmealConvert;
import com.example.mapper.CategoryMapper;
import com.example.mapper.SetmealDishMapper;
import com.example.mapper.SetmealMapper;
import com.example.pojo.DTO.SetmealAddDTO;
import com.example.pojo.DTO.SetmealPageDTO;
import com.example.pojo.VO.OverViewVO;
import com.example.pojo.VO.SetmealVO;
import com.example.pojo.entity.Category;
import com.example.pojo.entity.Setmeal;
import com.example.pojo.entity.SetmealDish;
import com.example.result.PageResult;
import com.example.result.Result;
import com.example.service.SetmealService;
import com.example.utils.UserContext;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SetmealServiceImpl implements SetmealService {

    private final SetmealMapper setmealMapper;

    private final SetmealDishMapper setmealDishMapper;

    private final CategoryMapper categoryMapper;

    private final SetmealConvert setmealConvert;

    private final SetmealCache setmealCache;


    @Transactional
    @Override
    public Result<String> addSetmael(SetmealAddDTO setmealAddDTO) {
        LambdaQueryWrapper<Category> existWrapper = new LambdaQueryWrapper<Category>()
                .eq(Category::getId,setmealAddDTO.getCategoryId());
        boolean exists = categoryMapper.exists(existWrapper);
        if (!exists){
            return Result.fail("商品种类不存在！");
        }
        Long userId = UserContext.getId();
        Setmeal setmeal = setmealConvert.toSetmeal(setmealAddDTO);
        setmeal.setCreateUser(userId);
        setmeal.setUpdateUser(userId);
        int insert = setmealMapper.insert(setmeal);
        Long id = setmeal.getId();
        List<SetmealDish> list = setmealAddDTO.getSetmealDishes();
        if (!list.isEmpty()){
            list.forEach(setmealDish -> {
                setmealDish.setSetmaelId(id);
            });
            setmealDishMapper.batchInsertSetmealDish(setmealAddDTO.getSetmealDishes());
        }

        return insert>0?Result.success("套餐创建成功！")
                :Result.fail("套餐创建失败！");
    }

    @Override
    public Result<PageResult> getSetmealPage(SetmealPageDTO setmealPageDTO) {
        Page<Setmeal> page = new Page<>(setmealPageDTO.getPage(),setmealPageDTO.getPageSize());
        LambdaQueryWrapper<Setmeal> lqw = new LambdaQueryWrapper<Setmeal>()
                .eq(setmealPageDTO.getCategoryId() != null,Setmeal::getCategoryId,setmealPageDTO.getCategoryId());
        Page<Setmeal> page1 = setmealMapper.selectPage(page, lqw);
        Set<Long> categoryIds = page1.getRecords().stream()
                .map(Setmeal::getCategoryId)
                .filter(id -> id != null)
                .collect(Collectors.toSet());

        Map<Long,String> categoryList = new HashMap<>();
        if (!categoryIds.isEmpty()){
            List<Category> categories = categoryMapper.selectByIds(categoryIds);
            Map<Long, String> tempMap= categories.stream().collect(Collectors.toMap(Category::getId,Category::getName));
            categoryList.putAll(tempMap);
        }
        List<SetmealVO> setmealVOList = page1.getRecords().stream()
                .map(item -> {
                    SetmealVO setmealVO = setmealConvert.toSetmealVO(item);
                    Long key = item.getCategoryId();
                    if (key != null && categoryList.containsKey(key)) {
                        setmealVO.setCategoryName(categoryList.get(key));
                    }
                    LambdaQueryWrapper<SetmealDish> dishLqw = new LambdaQueryWrapper<SetmealDish>()
                            .eq(SetmealDish::getSetmaelId,item.getId());
                    List<SetmealDish> setmealDishes = setmealDishMapper.selectList(dishLqw);
                    setmealVO.setSetmealDishes(setmealDishes);
                    return setmealVO;
                }).toList();
        PageResult pageResult = new PageResult(page1.getTotal(),setmealVOList);
        return Result.success(pageResult);
    }

    @Transactional
    @Override
    public Result<String> deleteSetmeal(List<Long> ids) {
        setmealCache.deleteSetmealList(ids);
        LambdaQueryWrapper<Setmeal> setmealLqw = new LambdaQueryWrapper<Setmeal>()
                .in(!ids.isEmpty(),Setmeal::getId,ids)
                .eq(Setmeal::getStatus,1);
        List<Setmeal> setmeals = setmealMapper.selectList(setmealLqw);
        if (!setmeals.isEmpty()){
            return Result.fail("存在在售套餐，无法删除！");
        }
        LambdaQueryWrapper<SetmealDish> setmealdishLqw = new LambdaQueryWrapper<SetmealDish>()
                .in(!ids.isEmpty(),SetmealDish::getSetmaelId,ids);
        int delete = setmealDishMapper.delete(setmealdishLqw);
        int i = setmealMapper.deleteByIds(ids);
        return delete>0 && i>0 ?Result.success("删除成功！")
                :Result.fail("删除失败!");
    }

    @Override
    public Result<SetmealVO> getById(Long id) {
        SetmealVO setmeal1 = setmealCache.getSetmeal(id);
        if (ObjectUtil.isNotNull(setmeal1)){
            return Result.success(setmeal1);
        }
        LambdaQueryWrapper<Setmeal> lqw = new LambdaQueryWrapper<Setmeal>()
                .eq(Setmeal::getId,id);
        Setmeal setmeal = setmealMapper.selectOne(lqw);
        if (setmeal.getStatus() != 1){
            return Result.fail("该产品不在售，无法添加至购物车！");
        }
        LambdaQueryWrapper<SetmealDish> dishLambdaQueryWrapper = new LambdaQueryWrapper<SetmealDish>()
                .eq(setmeal.getId()!= null,SetmealDish::getSetmaelId,setmeal.getId());
        List<SetmealDish> list = setmealDishMapper.selectList(dishLambdaQueryWrapper);
        if (ObjectUtil.isNull(setmeal) || list.isEmpty()){
            return Result.fail("获取失败！");
        }
        SetmealVO setmealVO = setmealConvert.toSetmealVO(setmeal);
        setmealVO.setSetmealDishes(list);
        setmealCache.addSetmeal(setmealVO);
        return Result.success(setmealVO);
    }

    @Override
    public Result<OverViewVO> overviewSetmeals() {
        LambdaQueryWrapper<Setmeal> soldLqw = new LambdaQueryWrapper<Setmeal>()
                .eq(Setmeal::getStatus,Setmeal.sold);
        Long sold = setmealMapper.selectCount(soldLqw);
        LambdaQueryWrapper<Setmeal> discontinuedLqw = new LambdaQueryWrapper<Setmeal>()
                .eq(Setmeal::getStatus,Setmeal.discontinued);
        Long discontinued = setmealMapper.selectCount(discontinuedLqw);
        OverViewVO overViewVO = OverViewVO.builder()
                .sold(sold)
                .discontinued(discontinued)
                .build();
        return Result.success(overViewVO);
    }
}
