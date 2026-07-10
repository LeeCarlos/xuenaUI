package com.xuena.supplier.infrastructure.mapper;

import com.xuena.supplier.domain.entity.MenuDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MenuMapper {

    MenuDO selectById(@Param("id") String id);

    List<MenuDO> selectList();

    List<MenuDO> selectByRoleIds(@Param("roleIds") List<String> roleIds);

    int insert(MenuDO menu);

    int update(MenuDO menu);

    int delete(@Param("id") String id);

    int countByPath(@Param("path") String path);

    int countByParentId(@Param("parentId") String parentId);
}