package com.mybatis.factory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DataSourceFactory {
    private static final Properties PROPERTIES = new Properties();
    static {
        try {
            InputStream is = DataSourceFactory.class.getResourceAsStream("/mysql.properties");
            PROPERTIES.load(is);
            String driver = PROPERTIES.getProperty("jdbc.driverClassName");
            Class.forName(driver);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection connection() {

        String url = PROPERTIES.getProperty("jdbc.url");
        String username = PROPERTIES.getProperty("jdbc.username");
        String password = PROPERTIES.getProperty("jdbc.password");
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}
