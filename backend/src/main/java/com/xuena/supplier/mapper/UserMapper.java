package com.xuena.supplier.mapper;

import com.xuena.supplier.entity.UserDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {

    UserDO selectByUsername(@Param("username") String username);

    UserDO selectById(@Param("id") Long id);

    List<UserDO> selectList(@Param("username") String username, @Param("department") String department);

    int insert(UserDO user);

    int update(UserDO user);

    int delete(@Param("id") Long id);

    int countByUsername(@Param("username") String username);
}