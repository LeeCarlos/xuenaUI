package com.xuena.supplier.mapper;

import com.xuena.supplier.entity.MeetingNoteDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MeetingNoteMapper {

    MeetingNoteDO selectById(@Param("id") Long id);

    List<MeetingNoteDO> selectList(@Param("supplierName") String supplierName);

    int insert(MeetingNoteDO note);

    int update(MeetingNoteDO note);

    int delete(@Param("id") Long id);
}