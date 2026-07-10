package com.xuena.supplier.infrastructure.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoleMenuMapper {

    List<String> selectMenuIdsByRoleId(@Param("roleId") String roleId);

    int insert(@Param("roleId") String roleId, @Param("menuId") String menuId);

    int deleteByRoleId(@Param("roleId") String roleId);

    int deleteByRoleIdAndMenuIds(@Param("roleId") String roleId, @Param("menuIds") List<String> menuIds);
}