package com.xuena.supplier.infrastructure.mapper;

import com.xuena.supplier.domain.entity.RoleDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoleMapper {

    RoleDO selectById(@Param("id") String id);

    RoleDO selectByCode(@Param("code") String code);

    List<RoleDO> selectList();

    int insert(RoleDO role);

    int update(RoleDO role);

    int delete(@Param("id") String id);

    int countByCode(@Param("code") String code);
}