package com.xuena.supplier.mapper;

import com.xuena.supplier.entity.PermissionDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PermissionMapper {

    PermissionDO selectById(@Param("id") Long id);

    PermissionDO selectByCode(@Param("code") String code);

    List<PermissionDO> selectList();

    List<PermissionDO> selectByRoleIds(@Param("roleIds") List<Long> roleIds);

    int insert(PermissionDO permission);

    int update(PermissionDO permission);

    int delete(@Param("id") Long id);

    int countByCode(@Param("code") String code);
}