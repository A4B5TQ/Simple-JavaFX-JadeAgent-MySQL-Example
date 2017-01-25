package app.agents;

import app.constants.SQLConnection;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import static app.constants.SQLConnection.CREATE_IF_NO_EXIST;
import static app.constants.SQLConnection.MYSQL_CONFIG;


public class ProductExpirationBehaviour extends TickerBehaviour {

    private final Connection connection;
    private final String DELETE_EXPIRED_PRODUCTS_QUERY = "DELETE FROM products WHERE expiration_date <= NOW()";

    public ProductExpirationBehaviour(Agent agent, long period) throws SQLException {
        super(agent, period);
        Properties connectionProp = new Properties();
        connectionProp.put("user", SQLConnection.USERNAME);
        connectionProp.put("password", SQLConnection.PASSWORD);
        this.connection = DriverManager.getConnection("jdbc:" + SQLConnection.DRIVER + "://" + SQLConnection.HOST + ":"
                + SQLConnection.PORT + "/" + SQLConnection.DB_NAME + CREATE_IF_NO_EXIST + MYSQL_CONFIG,connectionProp);
    }

    @Override
    protected void onTick() {
        try {
            Statement stmt = this.connection.createStatement();
            stmt.executeUpdate(DELETE_EXPIRED_PRODUCTS_QUERY);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
