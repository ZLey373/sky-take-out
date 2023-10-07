package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * @author: ZLey
 * @description 菜品
 * @date: 2023/9/21 15:18
 */

@RestController
@RequestMapping("/admin/dish")
@Slf4j
@Api(tags = "菜品相关接口")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private RedisTemplate redisTemplate;


    @PostMapping
    @ApiOperation("新增菜品")
    public Result save(@RequestBody DishDTO dishDTO){
        log.info("新增菜品：{}",dishDTO);
        dishService.savewithFlavor(dishDTO);
        //清理redis下指定的缓存
        String key = "dish_"+dishDTO.getCategoryId();
        cleanCache(key);

        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation("菜品分页查询")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO){
        log.info("菜品分页查询:{}",dishPageQueryDTO);
        PageResult pageResult=dishService.PageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    @DeleteMapping
    @ApiOperation("批量删除菜品")
    public Result delete(@RequestParam List<Long> ids){
        log.info("批量删除菜品的ids:{}",ids);
        dishService.deleteDish(ids);
        String keys="dish_*";
        cleanCache(keys);
        return Result.success();
    }

    /**
     * 根据id查询菜品和口味
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询菜品和口味")
    public Result<DishVO> getById(@PathVariable Long id){
        log.info("根据id查询菜品和口味:{}",id);
        DishVO dishVO=dishService.getDishAndFlavorById(id);

        return Result.success(dishVO);
    }

    /**
     * 根据id修改菜品数据
     * @param dishDTO
     * @return
     */
    @PutMapping
    @ApiOperation("根据id修改菜品数据")
    public Result update(@RequestBody DishDTO dishDTO){
        log.info("根据id修改菜品数据:{}",dishDTO);
        dishService.update(dishDTO);
        String keys="dish_*";
        cleanCache(keys);
        return Result.success();
    }

    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<Dish>> list(Long categoryId){
        List<Dish> list = dishService.list(categoryId);
        return Result.success(list);
    }

    @PostMapping("/status/{status}")
    @ApiOperation("菜品起售停售")
    public Result<String> startOrstop(@PathVariable Integer status,Long id){
        dishService.startOrstop(status,id);
        return Result.success();
    }



    //清理redis缓存
    private void cleanCache(String pattern){
        Set keys = redisTemplate.keys(pattern);
        redisTemplate.delete(keys);
    }

}
