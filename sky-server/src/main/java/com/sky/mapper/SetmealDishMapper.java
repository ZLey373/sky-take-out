package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {
    /**
     * 根据菜品Id查询关联套餐的id
     * @param dishIds
     * @return
     */
    List<Long> getSetmealIdsByDishIds(List<Long> dishIds);

    /**
     * 新增套餐和菜品的关系记录
     * @param setmealDish
     */
    @Insert("insert into sky_take_out.setmeal_dish(id, setmeal_id, dish_id, name, price, copies) VALUES (#{id}, #{setmealId},#{dishId},#{name},#{price},#{copies})")
    void insert(SetmealDish setmealDish);

    /**
     * 删除套餐对应的菜品的信息
     * @param id
     */
    @Delete("delete from sky_take_out.setmeal_dish where setmeal_id=#{id} ;")
    void delete(Long id);

    /**
     * 根据id查询套餐所对应的菜品
     * @param id
     * @return
     */
    @Select("select * from sky_take_out.setmeal_dish where setmeal_id=#{id};")
    List<SetmealDish> getById(Long id);

    /**
     * 批量插入菜品信息
     * @param setmealDishes
     */
    void insertBatch(List<SetmealDish> setmealDishes);
}
