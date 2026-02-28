package com.example.service.Impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.convert.CategoryConvert;
import com.example.mapper.CategoryMapper;
import com.example.mapper.DishMapper;
import com.example.pojo.DTO.CategoryCreateDTO;
import com.example.pojo.DTO.CategoryPageQueryDTO;
import com.example.pojo.DTO.CategoryUpdateDTO;
import com.example.pojo.VO.CategoryVO;
import com.example.pojo.entity.Category;
import com.example.pojo.entity.Dish;
import com.example.result.PageResult;
import com.example.result.Result;
import com.example.service.CategoryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final CategoryMapper categoryMapper;

    private final CategoryConvert categoryConvert;

    private final DishMapper dishMapper;

    @Override
    public Result<String> insertCategory(CategoryCreateDTO categoryCreateDTO) {
        log.info("【添加种类】，参数信息：{}",categoryCreateDTO);
        if (categoryCreateDTO.getType() != 1 || categoryCreateDTO.getType() != 2){
            return Result.fail("分类类型出错！");
        }
        LambdaQueryWrapper<Category> lqw = new LambdaQueryWrapper<Category>()
                .eq(Category::getName,categoryCreateDTO.getName());
        Category category = categoryMapper.selectOne(lqw);
        if (ObjectUtil.isNotNull(category)){
            return Result.fail("该种类已经存在！");
        }
        Category category1 = categoryConvert.toCategory(categoryCreateDTO);
        int insert = categoryMapper.insert(category1);
        if (insert>0){
            log.info("【添加种类】成功，种类id：{}",category1.getId());
            return Result.success("添加成功！");
        }else {
            log.error("【添加种类】失败");
            return Result.fail("添加失败，请稍后再试！");
        }
    }

    @Override
    public Result<PageResult> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO) {
        Page<Category> page = new Page<>(categoryPageQueryDTO.getPage(),categoryPageQueryDTO.getPageSize());
        LambdaQueryWrapper<Category> lqw = new LambdaQueryWrapper<Category>();
        if (categoryPageQueryDTO.getName()!=null){
            lqw.like(Category::getName,categoryPageQueryDTO.getName());
        }
        if (categoryPageQueryDTO.getType()!=null){
            lqw.eq(Category::getType,categoryPageQueryDTO.getType());
        }
        lqw.orderByAsc(Category::getSort);
        lqw.orderByDesc(Category::getCreateTime);
        Page<Category> categoryPage = categoryMapper.selectPage(page, lqw);
        PageResult pageResult = new PageResult(categoryPage.getTotal(),categoryPage.getRecords());
        return Result.success(pageResult);
    }


    @Transactional
    @Override
    public Result<String> deleteCategory(Long id){
        log.info("【删除种类】，种类id：{}",id);
        LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper<Dish>()
                .eq(Dish::getCategoryId,id);
        List<Dish> dishes = dishMapper.selectList(lqw);
        if (!dishes.isEmpty()){
            return Result.fail("该种类下存在菜品，请先删除菜品！");
        }
        int i = categoryMapper.deleteById(id);
        if (i>0){
            log.info("【删除种类】成功，种类id：{}",id);
            return Result.success("删除成功！");
        }else {
            log.error("【删除种类】失败，种类id：{}",id);
            return Result.fail("删除失败！");
        }

    }

    @Override
    public Result<String> updateCategory(CategoryUpdateDTO categoryUpdateDTO) {
        log.info("【更新种类】，更新参数：{}",categoryUpdateDTO);
        LambdaUpdateWrapper<Category> luw = new LambdaUpdateWrapper<Category>()
                .eq(Category::getId,categoryUpdateDTO.getId())
                .set(categoryUpdateDTO.getName()!=null,Category::getName,categoryUpdateDTO.getName())
                .set(categoryUpdateDTO.getSort()!=null,Category::getSort,categoryUpdateDTO.getSort());
        int update = categoryMapper.update(null,luw);
        if (update>0){
            log.info("【更新种类】成功");
            return Result.success("更新成功！");
        }else {
            log.error("【更新种类】失败");
            return Result.fail("更新失败，请稍后再试！");
        }
    }

    @Override
    public Result<String> updateState(Long id,Boolean status) {
        log.info("【更新种类状态】，种类id：{},请求状态:{}",id,status);
        LambdaUpdateWrapper<Category> luw = new LambdaUpdateWrapper<Category>()
                .eq(Category::getId,id)
                .set(Category::getStatus,status);
        int update = categoryMapper.update(null,luw);
        if (update>0){
            log.info("【更新种类状态】成功，种类id：{},请求状态:{}",id,status);
            return Result.success("修改成功！");
        }else {
            log.info("【更新种类状态】失败，种类id：{},请求状态:{}",id,status);
            return Result.fail("修改失败，请稍后再试！");
        }

    }

    @Override
    public Result<List<String>> getCategoryList() {
        List<String> strings = categoryMapper.listAllCategoryNames();
        return Result.success(strings);
    }

    @Override
    public Result<List<CategoryVO>> getALlCategoryList() {
        List<CategoryVO> allCatrgoryList = categoryMapper.getAllCatrgoryList();
        return Result.success(allCatrgoryList);
    }
}
