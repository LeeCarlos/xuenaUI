package com.xuena.supplier.mapper;

import com.xuena.supplier.entity.CategoryDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CategoryMapper {

    CategoryDO selectById(@Param("id") Long id);

    CategoryDO selectByName(@Param("name") String name);

    List<CategoryDO> selectList();

    int insert(CategoryDO category);

    int update(CategoryDO category);

    int delete(@Param("id") Long id);

    int countByName(@Param("name") String name);

    int countByNameAndNotId(@Param("name") String name, @Param("id") Long id);
}