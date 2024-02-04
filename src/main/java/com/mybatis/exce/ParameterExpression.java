package com.mybatis.exce;

import com.mybatis.exce.BuilderException;

import java.util.HashMap;

public class ParameterExpression extends HashMap<String, String> {

    private static final long serialVersionUID = -2417552199605158680L;

    /**
     * 这里完成了实际的实际的内容拆分
     *
     * @param expression
     */
    public ParameterExpression(String expression) {
        parse(expression);
    }

    /**
     * 完成对{@code expression}的解析代码
     *
     * @param expression
     */
    private void parse(String expression) {
        //这一步目的是为了去掉{@code expression}开头部分的滤掉控制字符或通信专用字符、和(
        int p = skipWS(expression, 0);
//        "("的ASC码为0x28
        if (expression.charAt(p) == '(') {
            expression(expression, p + 1);
        } else {
            property(expression, p);
        }
    }

    /**
     * 这里是找到闭合的的表达式()
     *
     * @param expression
     * @param left
     */
    private void expression(String expression, int left) {
        int match = 1;
        int right = left + 1;
        //这一段代码就是找到闭合的() 这里如果到结束都找到不 会抛异常
        while (match > 0) {
            if (expression.charAt(right) == ')') {
                match--;
            } else if (expression.charAt(right) == '(') {
                match++;
            }
            right++;
        }
        put("expression", expression.substring(left, right - 1));
        jdbcTypeOpt(expression, right);
    }

    /**
     * 最左边的位置
     *
     * @param expression 表示要解析的内容
     * @param left 表示{@code expression}的位置
     */
    private void property(String expression, int left) {
        if (left < expression.length()) {
            //这段代码是从{@code expression}的{@code left}的位置，获取第一个符合",:"的内容
            int right = skipUntil(expression, left, ",:");
            //trimmedStr的作用是截取{@code expression}从{@code left}到{@code right}之间的字符窜
            put("property", trimmedStr(expression, left, right));
            jdbcTypeOpt(expression, right);
        }
    }

    /**
     * 剔除{@code  expression}的开头部分中所有的控制字符或通信专用字符
     *
     * @param expression 需要查看的字符窜
     * @param p          本轮expression开始的位置
     * @return 从{@code expression}的{@code p}位置开始 获取第一个非控制字符或通信专用字符所在位置
     */
    private int skipWS(String expression, int p) {
        for (int i = p; i < expression.length(); i++) {
//          0x20是Asc空格，这个条件的意思是过滤掉控制字符或通信专用字符
            if (expression.charAt(i) > 0x20) {
                return i;
            }
        }
        return expression.length();
    }

    /**
     * 从{@code expression}的p位置起，查找任意一个包含在{@code endChars}里字符，并且返回其在{@code expression}中的位置
     *
     * @param expression 表达式
     * @param p          表示{@code expression}开始的位置
     * @param endChars   表示结束的字符，如果{@code expression}包含{@code endChars}里的任何字符，都返回其对应的位置
     * @return 从{@code expression}的p位置起，查找任意一个包含在{@code endChars}里字符，并且返回其在{@code expression}中的位置
     */
    private int skipUntil(String expression, int p, final String endChars) {
        for (int i = p; i < expression.length(); i++) {
            char c = expression.charAt(i);
            if (endChars.indexOf(c) > -1) {
                return i;
            }
        }
        return expression.length();
    }

    /**
     * jdbc的选项剔除
     *
     * @param expression
     * @param p
     */
    private void jdbcTypeOpt(String expression, int p) {
        //过滤从{@code expression}的{@code p}位置，过滤到控制字符和通信字符 已经（
        p = skipWS(expression, p);
        if (p < expression.length()) {
            if (expression.charAt(p) == ':') {
                //这一步的条件说明对于#{}的内容配置，其实是可以写成#{property：jdbcType}类型的，如果是这样的类型就走这一步
                jdbcType(expression, p + 1);
            } else if (expression.charAt(p) == ',') {
                //这一步是走#{property,jdbcType=xx...}等类型的解析
                option(expression, p + 1);
            } else {
                 new BuilderException("Parsing error in {" + expression + "} in position " + p);
            }
        }
    }

    /**
     * 这里可以看出来，#{}可以使用propertis：jdbcType的方式进行设置
     *
     * @param expression
     * @param p
     */
    private void jdbcType(String expression, int p) {
        int left = skipWS(expression, p);
        int right = skipUntil(expression, left, ",");
        if (right > left) {
            put("jdbcType", trimmedStr(expression, left, right));
        } else {
            new BuilderException("Parsing error in {" + expression + "} in position " + p);
        }
        option(expression, right + 1);
    }

    /**
     * 递归处理的 xxx=xxx的
     *
     * @param expression
     * @param p
     */
    private void option(String expression, int p) {
        int left = skipWS(expression, p);
        if (left < expression.length()) {
            int right = skipUntil(expression, left, "=");
            String name = trimmedStr(expression, left, right);
            left = right + 1;
            right = skipUntil(expression, left, ",");
            String value = trimmedStr(expression, left, right);
            put(name, value);
            option(expression, right + 1);
        }
    }

    /**
     * 获取stri的start和end之间的内容，并且清除控制字符或通信专用字符
     *
     * @param str
     * @param start
     * @param end
     * @return
     */
    private String trimmedStr(String str, int start, int end) {
        //0x20以下的都是控制字符或通信专用字符
        while (str.charAt(start) <= 0x20) {
            start++;
        }
        while (str.charAt(end - 1) <= 0x20) {
            end--;
        }
        return start >= end ? "" : str.substring(start, end);
    }

}