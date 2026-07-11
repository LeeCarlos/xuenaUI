package com.xuena.supplier.infrastructure.mapper;

import com.xuena.supplier.domain.entity.SupplierPoolDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SupplierPoolMapper {

    SupplierPoolDO selectById(@Param("id") String id);

    SupplierPoolDO selectByName(@Param("name") String name);

    List<SupplierPoolDO> selectList(@Param("name") String name,
            @Param("category") String category,
            @Param("isDisabled") Integer isDisabled);

    int insert(SupplierPoolDO supplier);

    int update(SupplierPoolDO supplier);

    int delete(@Param("id") String id);

    int countByName(@Param("name") String name);

    int countByNameAndNotId(@Param("name") String name, @Param("id") String id);

    int updateStatus(@Param("id") String id, @Param("isDisabled") Integer isDisabled);

    int batchDelete(@Param("ids") List<String> ids);

    List<String> selectDistinctCategory();
}