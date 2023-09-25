package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper {
    void insertBatch(List<DishFlavor> flavors);


    /**
     * 根据菜品id删除口味
     * @param id
     */
    @Delete("delete from sky_take_out.dish_flavor where dish_id=#{id};")
    void deleteByDishId(Long id);

    /**
     * // 根据菜品id集合批量删除关联的口味
     * @param dishIds
     */
    void deleteByDishIds(List<Long> dishIds);

    @Select("select * from sky_take_out.dish_flavor where dish_id=#{DishId}")
    List<DishFlavor> getFlavors(Long DishId);
}
