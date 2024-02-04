package com.mybatis.type;

import com.mybatis.retention.Delete;

import java.lang.reflect.Method;

public class DelectMethod implements MethodType<Delete> {

    @Override
    public Delete getMethods(Method method) {
        return method.getAnnotation(Delete.class);
    }
}
