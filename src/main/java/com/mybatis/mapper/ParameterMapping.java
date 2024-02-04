package com.mybatis.mapper;

public class ParameterMapping {
    // java对象的属性名
    private String property;

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public ParameterMapping(String property) {
        this.property = property;
    }
}