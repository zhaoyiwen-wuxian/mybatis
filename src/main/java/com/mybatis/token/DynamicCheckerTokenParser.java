package com.mybatis.token;

/**
 * 动态标签  这个类的作用主要是看是否被调用过，如果被调用过isDynamic必为true
 */
public class DynamicCheckerTokenParser implements TokenHandler {

    private boolean isDynamic;

    public DynamicCheckerTokenParser() {
        // Prevent Synthetic Access
    }

    public boolean isDynamic() {
        return isDynamic;
    }

    /**
     * 如果这个方法
     *
     * @param content
     * @return
     */
    @Override
    public String handleToken(String content) {
        this.isDynamic = true;
        return null;
    }
}
