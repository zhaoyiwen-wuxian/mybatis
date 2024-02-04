package com.mybatis.type;

import com.mybatis.token.TypeHander;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IntegerTypeHander implements TypeHander<Integer> {
    @Override
    public void setParameter(PreparedStatement statement, int i, Integer value) throws SQLException {
        statement.setInt(i,value);
    }

    @Override
    public Integer getResult(ResultSet resultSet, String cloumn) throws SQLException {
        return resultSet.getInt(cloumn);
    }
}