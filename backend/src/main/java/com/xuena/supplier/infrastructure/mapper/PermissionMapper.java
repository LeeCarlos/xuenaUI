package com.xuena.supplier.infrastructure.mapper;

import com.xuena.supplier.domain.entity.PermissionDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PermissionMapper {

    PermissionDO selectById(@Param("id") String id);

    PermissionDO selectByCode(@Param("code") String code);

    List<PermissionDO> selectList();

    List<PermissionDO> selectByRoleIds(@Param("roleIds") List<String> roleIds);

    int insert(PermissionDO permission);

    int update(PermissionDO permission);

    int delete(@Param("id") String id);

    int countByCode(@Param("code") String code);
}