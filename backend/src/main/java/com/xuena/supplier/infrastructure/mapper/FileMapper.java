package com.xuena.supplier.infrastructure.mapper;

import com.xuena.supplier.domain.entity.FileDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FileMapper {

    FileDO selectById(String id);

    FileDO selectByStoreKey(String storeKey);

    FileDO selectByFileName(String fileName);

    List<FileDO> selectList(@Param("fileType") String fileType, @Param("fileName") String fileName, @Param("businessType") String businessType);

    List<FileDO> selectTemplatesByBusinessType(@Param("businessType") String businessType);

    void insert(FileDO file);

    void update(FileDO file);

    void delete(String id);
}