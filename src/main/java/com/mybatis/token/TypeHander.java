package com.mybatis.token;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface TypeHander<T> {
    void setParameter(PreparedStatement statement,int i,T value) throws SQLException;

    T getResult(ResultSet resultSet,String cloumn) throws SQLException;
}
