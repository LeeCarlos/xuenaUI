package com.xuena.supplier.mapper;

import com.xuena.supplier.entity.MenuDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MenuMapper {

    MenuDO selectById(@Param("id") Long id);

    List<MenuDO> selectList();

    List<MenuDO> selectByRoleIds(@Param("roleIds") List<Long> roleIds);

    int insert(MenuDO menu);

    int update(MenuDO menu);

    int delete(@Param("id") Long id);

    int countByPath(@Param("path") String path);

    int countByParentId(@Param("parentId") Long parentId);
}