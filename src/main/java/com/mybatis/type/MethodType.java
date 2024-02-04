package com.mybatis.type;

import java.lang.reflect.Method;

public interface MethodType<T> {
    T getMethods(Method method);
}
