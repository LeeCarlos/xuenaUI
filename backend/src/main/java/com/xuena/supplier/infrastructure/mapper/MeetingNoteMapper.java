package com.xuena.supplier.infrastructure.mapper;

import com.xuena.supplier.domain.entity.MeetingNoteDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MeetingNoteMapper {

    MeetingNoteDO selectById(@Param("id") String id);

    List<MeetingNoteDO> selectList(@Param("supplierName") String supplierName, @Param("monthFrom") String monthFrom, @Param("monthTo") String monthTo);

    int insert(MeetingNoteDO note);

    int update(MeetingNoteDO note);

    int delete(@Param("id") String id);
}