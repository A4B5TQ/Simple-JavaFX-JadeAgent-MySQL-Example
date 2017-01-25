package app.agents;

import jade.core.Agent;
import java.sql.SQLException;

public class ProductExpirationAgent extends Agent {

    private static final int EXPIRATION_PERIOD_CHECK = 5000;

    @Override
    protected void setup() {
        try {
            this.addBehaviour(new ProductExpirationBehaviour(this, EXPIRATION_PERIOD_CHECK));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
