package com.sky.service;


import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;
import org.springframework.stereotype.Service;

import java.util.List;


public interface DishService {

    public void savewithFlavor(DishDTO dishDTO);


    PageResult PageQuery(DishPageQueryDTO dishPageQueryDTO);

    void deleteDish(List<Long> ids);

    /**
     * 根据id查询菜品和口味
     * @param id
     * @return
     */
    DishVO getDishAndFlavorById(Long id);

    void update(DishDTO dishDTO);

    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    List<Dish> list(Long categoryId);
}
