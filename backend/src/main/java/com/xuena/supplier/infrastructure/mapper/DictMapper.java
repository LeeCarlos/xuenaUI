package com.xuena.supplier.infrastructure.mapper;

import com.xuena.supplier.domain.entity.DictDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DictMapper {

    DictDO selectById(@Param("id") String id);

    List<DictDO> selectByItem(@Param("item") String item);

    DictDO selectByItemAndKey(@Param("item") String item, @Param("key") String key);

    List<DictDO> selectAll();

    int insert(DictDO dict);

    int update(DictDO dict);

    int delete(@Param("id") String id);

    int countByItemAndKey(@Param("item") String item, @Param("key") String key);

    int countByItemAndKeyAndNotId(@Param("item") String item, @Param("key") String key, @Param("id") String id);
}