package com.trench.mapper;

import com.mybatis.retention.Param;
import com.mybatis.retention.Selcet;
import com.trench.User;

import java.util.List;

public interface UserMapper {

    @Selcet("select * from user where name =#{name}")
    List<User> getUer(@Param(value = "name") String name);

    User getUserId(Integer id);
}
