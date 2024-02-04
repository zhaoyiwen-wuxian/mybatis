package com.mybatis.util;

import com.mybatis.mapper.ParameterMapping;
import com.mybatis.retention.Delete;
import com.mybatis.retention.Param;
import com.mybatis.retention.Selcet;
import com.mybatis.retention.Update;
import com.mybatis.token.GenericTokenParser;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.sql.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MapperUtil {

    public static Object getResult(Method method, List<Object> list, Map<Integer, Object> handerMap) {
        Object result;
        if (method.getReturnType().equals(List.class)){
            result= list;
        }else if (method.getReturnType().equals(void.class)) {
            result = null;
        }else if (method.getReturnType().equals(Map.class)){
            result= handerMap;
        }else {
            result= list.get(0);
        }
        return result;
    }



    public static Map<String, Method> getStringMethodMap(Class resultType) {
        Map<String, Method> methodMap=new ConcurrentHashMap<>();
        Arrays.stream(resultType.getDeclaredMethods()).forEach(method1 -> {
            if (method1.getName().startsWith("set")){
                String propertyName = method1.getName().substring(3);
                propertyName=propertyName.substring(0,1).toLowerCase(Locale.ROOT)+propertyName.substring(1);
                methodMap.put(propertyName,method1);
            }
        });
        return methodMap;
    }

    public static List<String> getColumList(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        List<String> columList=new ArrayList<>();
        for (int i=0;i<metaData.getColumnCount();i++){
            columList.add(metaData.getColumnName(i));
        }
        return columList;
    }

    public static Class getResultType(Method method, Class resultType) {
        Type genericReturnType = method.getGenericReturnType();
        if (genericReturnType instanceof Class){
            resultType =(Class) genericReturnType;
        }else if (genericReturnType instanceof ParameterizedType){
            resultType =(Class) ((ParameterizedType)genericReturnType) .getActualTypeArguments()[0];
        }
        return resultType;
    }



    public static Map<String, Object> getStringObjectMap(Method method, Object[] args) {
        Map<String,Object> map=new ConcurrentHashMap<>();
        Parameter[] parameters = method.getParameters();
        for (int i =0;i<parameters.length;i++) {
            Parameter parameter = parameters[i];
            String value = parameter.getAnnotation(Param.class).value();
            map.put(value, args[i]);
        }
        return map;
    }

    public static String sql(Method method, GenericTokenParser parser){
        String sql="";
        Annotation[] annotations = method.getAnnotations();
        Class<? extends Annotation> aClass1 = annotations[0].annotationType();
        if (aClass1.equals(Update.class)){
            sql = parser.parse( method.getAnnotation(Update.class).value());
        }else if (aClass1.equals(Selcet.class)){
            sql = parser.parse( method.getAnnotation(Selcet.class).value());
        }else if (aClass1.equals(Delete.class)){
            sql = parser.parse( method.getAnnotation(Delete.class).value());
        }
        return sql;
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=UTF-8&useSSL=false", "root", "root");
    }
}
