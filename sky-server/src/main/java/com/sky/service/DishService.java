package com.sky.service;


import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import org.springframework.stereotype.Service;

import java.util.List;


public interface DishService {

    public void savewithFlavor(DishDTO dishDTO);


    PageResult PageQuery(DishPageQueryDTO dishPageQueryDTO);

    void deleteDish(List<Long> ids);
}
