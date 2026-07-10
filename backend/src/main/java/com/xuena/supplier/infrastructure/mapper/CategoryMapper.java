package com.xuena.supplier.infrastructure.mapper;

import com.xuena.supplier.domain.entity.CategoryDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CategoryMapper {

    CategoryDO selectById(@Param("id") String id);

    CategoryDO selectByName(@Param("name") String name);

    List<CategoryDO> selectList();

    int insert(CategoryDO category);

    int update(CategoryDO category);

    int delete(@Param("id") String id);

    int countByName(@Param("name") String name);

    int countByNameAndNotId(@Param("name") String name, @Param("id") String id);
}