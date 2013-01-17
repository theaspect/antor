package com.blazer.exporter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Locale;

public class DataBase {
    String region = "US";
    String language = "en";
    Connection connection;
    private String url;
    private String login;
    private String password;

    public void setRegion(String region) {
        this.region = region;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void connect() throws SQLException {
        if (language != null && region != null) {
            Locale.setDefault(new Locale(language, region));
        }
        if (url != null && login != null && password != null) {
            connection = DriverManager.getConnection(url, login, password);
        }
    }

    public void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
