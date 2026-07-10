package com.xuena.supplier.infrastructure.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RolePermissionMapper {

    List<String> selectPermissionIdsByRoleId(@Param("roleId") String roleId);

    int insert(@Param("roleId") String roleId, @Param("permissionId") String permissionId);

    int deleteByRoleId(@Param("roleId") String roleId);

    int deleteByRoleIdAndPermissionIds(@Param("roleId") String roleId, @Param("permissionIds") List<String> permissionIds);
}