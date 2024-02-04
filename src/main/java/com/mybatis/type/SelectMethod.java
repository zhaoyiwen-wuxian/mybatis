package com.mybatis.type;

import com.mybatis.retention.Selcet;

import java.lang.reflect.Method;

public class SelectMethod implements MethodType<Selcet> {

    @Override
    public Selcet getMethods(Method method) {
        return method.getAnnotation(Selcet.class);
    }
}
