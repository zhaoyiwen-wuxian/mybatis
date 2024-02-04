package com.mybatis.exce;

public class BuilderException extends Throwable {
    public BuilderException(Object s) {
        try {
            throw new Throwable((String) s);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

    }
}
