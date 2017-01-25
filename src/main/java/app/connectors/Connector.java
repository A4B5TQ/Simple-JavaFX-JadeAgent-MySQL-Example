package app.connectors;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import static app.constants.SQLConnection.CREATE_IF_NO_EXIST;
import static app.constants.SQLConnection.MYSQL_CONFIG;

public class Connector implements AutoCloseable {

    private static Connection connection = null;

    public static void initConnection(String driver,String username,
                                      String password, String host,
                                      String port, String dbName) throws SQLException {
        Properties connectionProp = new Properties();
        connectionProp.put("user", username);
        connectionProp.put("password", password);
        connection = DriverManager.getConnection("jdbc:" + driver + "://" + host + ":"
                + port + "/" + dbName + CREATE_IF_NO_EXIST + MYSQL_CONFIG,connectionProp);
    }

    public static Connection getConnection() {
        return connection;
    }

    @Override
    public void close() throws Exception {
        if (connection != null) {
            connection.close();
        }
    }
}