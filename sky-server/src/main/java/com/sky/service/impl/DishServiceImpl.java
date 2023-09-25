package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author: ZLey
 * @description
 * @date: 2023/9/21 15:24
 */

@Service
@Slf4j
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Transactional
    public void savewithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);

        // 插入一条菜品数据
        dishMapper.insert(dish);

        Long dishId = dish.getId();

        // 获得菜品的口味信息
        List<DishFlavor> flavors = dishDTO.getFlavors();


        if(flavors!=null && flavors.size()>0){
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishId);
            });
            // 批量插入口味信息
            dishFlavorMapper.insertBatch(flavors);
        }
    }


    /**
     * 分页查询
     * @param dishPageQueryDTO
     * @return
     */
    public PageResult PageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Page<DishVO> page= dishMapper.pageQuery(dishPageQueryDTO);

        return new PageResult(page.getTotal(), page.getResult());
    }


    /**
     * 根据菜品id删除菜品
     * @param ids
     */
    @Transactional
    public void deleteDish(List<Long> ids) {
        // 判断菜品是否可以被删除----》是否处于起售状态
        for (Long id : ids) {
         Long status = dishMapper.getStatusByID(id);
         if(status==1){
             throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
         }
        }

        // 判断菜品是否可以被删除----》是否被套餐关联
        List<Long> setmealIdsByDishIds = setmealDishMapper.getSetmealIdsByDishIds(ids);
        if(setmealIdsByDishIds!=null && setmealIdsByDishIds.size()>0){
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        // 删除菜品
        for (Long id : ids) {
            dishMapper.deletById(id);
            // 删除口味
            dishFlavorMapper.deleteByDishId(id);

        }
    }
}
