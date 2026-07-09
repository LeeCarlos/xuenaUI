package com.xuena.supplier.mapper;

import com.xuena.supplier.entity.SupplierPoolDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SupplierPoolMapper {

    SupplierPoolDO selectById(@Param("id") Long id);

    SupplierPoolDO selectByName(@Param("name") String name);

    List<SupplierPoolDO> selectList(@Param("name") String name, @Param("category") String category, @Param("isDisabled") Integer isDisabled);

    int insert(SupplierPoolDO supplier);

    int update(SupplierPoolDO supplier);

    int delete(@Param("id") Long id);

    int countByName(@Param("name") String name);

    int countByNameAndNotId(@Param("name") String name, @Param("id") Long id);

    int updateStatus(@Param("id") Long id, @Param("isDisabled") Integer isDisabled);
}