package com.xuena.supplier.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoleMenuMapper {

    List<Long> selectMenuIdsByRoleId(@Param("roleId") Long roleId);

    int insert(@Param("roleId") Long roleId, @Param("menuId") Long menuId);

    int deleteByRoleId(@Param("roleId") Long roleId);

    int deleteByRoleIdAndMenuIds(@Param("roleId") Long roleId, @Param("menuIds") List<Long> menuIds);
}