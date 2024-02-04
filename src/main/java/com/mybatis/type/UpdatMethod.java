package com.mybatis.type;

import com.mybatis.retention.Update;

import java.lang.reflect.Method;

public class UpdatMethod implements MethodType<Update> {

    @Override
    public Update getMethods(Method method) {
        return method.getAnnotation(Update.class);
    }
}
