package com.xuena.supplier.mapper;

import com.xuena.supplier.entity.RoleDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoleMapper {

    RoleDO selectById(@Param("id") Long id);

    RoleDO selectByCode(@Param("code") String code);

    List<RoleDO> selectList();

    int insert(RoleDO role);

    int update(RoleDO role);

    int delete(@Param("id") Long id);

    int countByCode(@Param("code") String code);
}