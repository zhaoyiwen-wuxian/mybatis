package com.mybatis.type;

import com.mybatis.token.TypeHander;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StringTypeHander implements TypeHander<String> {
    @Override
    public void setParameter(PreparedStatement statement, int i, String value) throws SQLException {
        statement.setString(i,value);
    }

    @Override
    public String getResult(ResultSet resultSet, String cloumn) throws SQLException {
        return resultSet.getString(cloumn);
    }
}
