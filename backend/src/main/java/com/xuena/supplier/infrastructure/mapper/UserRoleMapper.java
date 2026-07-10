package com.xuena.supplier.infrastructure.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserRoleMapper {

    List<String> selectRoleIdsByUserId(@Param("userId") String userId);

    int insert(@Param("userId") String userId, @Param("roleId") String roleId);

    int deleteByUserId(@Param("userId") String userId);

    int deleteByUserIdAndRoleIds(@Param("userId") String userId, @Param("roleIds") List<String> roleIds);
}