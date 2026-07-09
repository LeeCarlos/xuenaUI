package com.xuena.supplier.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RolePermissionMapper {

    List<Long> selectPermissionIdsByRoleId(@Param("roleId") Long roleId);

    int insert(@Param("roleId") Long roleId, @Param("permissionId") Long permissionId);

    int deleteByRoleId(@Param("roleId") Long roleId);

    int deleteByRoleIdAndPermissionIds(@Param("roleId") Long roleId, @Param("permissionIds") List<Long> permissionIds);
}