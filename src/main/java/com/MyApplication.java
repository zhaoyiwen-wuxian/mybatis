package com;

import com.mybatis.factory.MapperProxyFactory;
import com.trench.User;
import com.trench.mapper.UserMapper;
import java.util.List;

public class MyApplication {
    public static void main(String[] args) {
        UserMapper mapper = MapperProxyFactory.getMapper(UserMapper.class);
        List<User> trench = mapper.getUer("trench");

    }
}
