package com.mybatis.token;

import com.mybatis.mapper.ParameterMapping;
import com.mybatis.exce.BuilderException;
import com.mybatis.exce.ParameterExpression;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 用于处理 #{department, mode=OUT, jdbcType=CURSOR, javaType=ResultSet, resultMap=departmentResultMap}格式的入参
 */
public class ParameterMappingTokenHandler implements TokenHandler {


    private List<ParameterMapping> parameterMappings = new ArrayList<>();

    public List<ParameterMapping> getParameterMappings() {
        return parameterMappings;
    }

    /**
     * 处理展位符
     *
     * @param content
     * @return
     */
    @Override
    public String handleToken(String content) {
        //这里对内容进行了分析，并且注册到全局里。
        parameterMappings.add(new ParameterMapping(content));
        //结果返回的是一个？，也就是说#{}类型的参数都会被替换成？
        return "?";
    }
    /**
     * 解析#{department, mode=OUT, jdbcType=CURSOR, javaType=ResultSet, resultMap=departmentResultMap} 解析结果是一个Map里
     *
     * @param content
     * @return
     */
    private Map<String, String> parseParameterMapping(String content) {
        try {
            //这里可以看出来ParameterExpression是继承了Map的
            return new ParameterExpression(content);
        } catch (Exception ex) {
             new BuilderException("Parsing error was found in mapping #{" + content + "}.  Check syntax #{property|(expression), var1=value1, var2=value2, ...} "+ex);
        }
        return null;
    }
}