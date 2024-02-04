package com.mybatis.factory;

import com.mybatis.mapper.ParameterMapping;
import com.mybatis.retention.Selcet;
import com.mybatis.token.GenericTokenParser;
import com.mybatis.token.ParameterMappingTokenHandler;
import com.mybatis.token.TypeHander;
import com.mybatis.type.*;
import com.mybatis.util.MapperUtil;
import java.lang.reflect.*;
import java.sql.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MapperProxyFactory {
    private static Map<Class, TypeHander> typeHanderMap=new ConcurrentHashMap<>();

    //第一种请求方式
    static {
        typeHanderMap.put(String.class,new StringTypeHander());
        typeHanderMap.put(Integer.class,new IntegerTypeHander());
        try {
            Class.forName("come.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException classNotFoundException) {
            classNotFoundException.printStackTrace();
        }
    }

    public static <T> T getMapper(Class<T> tClass){

     return(T) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[]{tClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                //处理sql
                //第一种sql链接方式
                Connection connection = MapperUtil.getConnection();
                //第二种
              //  Connection connection1 = DataSourceFactory.connection();
                Map<String, Object> map = MapperUtil.getStringObjectMap(method, args);
                ParameterMappingTokenHandler parameterMappingTokenHandler=new ParameterMappingTokenHandler();
                GenericTokenParser parser=new GenericTokenParser("#{","}",parameterMappingTokenHandler);

                List<ParameterMapping> parameterMappings = parameterMappingTokenHandler.getParameterMappings();
                PreparedStatement statement = connection.prepareStatement(MapperUtil.sql(method,parser));

                updateStatement(map, parameterMappings, statement);
                statement.execute();

                if (!method.getAnnotations()[0].equals(Selcet.class)){
                    connection.close();
                    return null;
                }

                Class resultType=null;
                resultType = MapperUtil.getResultType(method, resultType);

                ResultSet resultSet = statement.getResultSet();
                List<String> columList =MapperUtil. getColumList(resultSet);

                Map<String, Method> methodMap = MapperUtil.getStringMethodMap(resultType);
                Object result=null;
                List<Object> list=new ArrayList<>();
                Map<Integer,Object> handerMap=new ConcurrentHashMap<>();
                while (resultSet.next()){
                    if (resultType!=null) {
                        Object instance = resultType.newInstance();
                        for (int i=0;i<columList.size();i++){
                            selecteInstance(resultSet, columList, methodMap, instance, i);
                            list.add(instance);
                            handerMap.put(i,instance);
                        }
                    }
                }

                result = MapperUtil.getResult(method, list, handerMap);
                connection.close();
                return result;
            }
        });
    }
    private static void selecteInstance(ResultSet resultSet, List<String> columList, Map<String, Method> methodMap, Object instance, int i) throws IllegalAccessException, InvocationTargetException, SQLException {
        String column = columList.get(i);
        Method set = methodMap.get(column);
        Class<?> parameterType = set.getParameterTypes()[0];
        TypeHander typeHander = typeHanderMap.get(parameterType);
        set.invoke(instance,typeHander.getResult(resultSet,column));
    }

    private static void updateStatement(Map<String, Object> map, List<ParameterMapping> parameterMappings, PreparedStatement statement) throws SQLException {
        for (int i = 0; i< parameterMappings.size(); i++){
            String parameter= parameterMappings.get(i).getProperty();
            Object o = map.get(parameter);
            Class<?> aClass = o.getClass();
            typeHanderMap.get(aClass).setParameter(statement,i+1,o);
        }
    }
}
