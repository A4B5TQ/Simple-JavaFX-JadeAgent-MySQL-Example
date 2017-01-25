package app;

import app.agents.ProductExpirationAgent;
import app.connectors.Connector;
import app.constants.AppConstants;
import app.constants.SQLConnection;
import app.utils.stageUtils.StageManager;
import app.utils.stageUtils.StageManagerImpl;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.PlatformController;
import javafx.application.Application;
import javafx.stage.Stage;

public class ProductCatalog extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Runtime runtime = Runtime.instance();
        runtime.setCloseVM(true);

        Profile profile = new ProfileImpl(null, 1200, null);
        PlatformController mainContainer = runtime.createMainContainer(profile);
        AgentController productAgent = mainContainer.createNewAgent("product_expiration",
                ProductExpirationAgent.class.getName(), new Object[0]);
        productAgent.start();

        StageManager stageManager = new StageManagerImpl();
        stageManager.loadSceneToStage(primaryStage, AppConstants.MAIN_VIEW_PATH, null);
        Connector.initConnection(SQLConnection.DRIVER,SQLConnection.USERNAME,SQLConnection.PASSWORD,
                SQLConnection.HOST,SQLConnection.PORT,SQLConnection.DB_NAME);

    }

    public static void main(String[] args) {
        launch(args);
    }
}
