package app.utils.stageUtils;

import app.constants.AppConstants;
import app.utils.screenUtils.Dimensions;
import app.utils.screenUtils.UIDimensions;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class StageManagerImpl implements StageManager {

    private final Dimensions deviceScreenDim = new UIDimensions();

    public FXMLLoader loadSceneToStage(Stage currentStage, String fxmlPath, Modality modality) {

        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getClassLoader()
                .getResource(fxmlPath));
        try {
            Region region = fxmlLoader.load();
            double screenDelimiter = this.deviceScreenDim.getCurrentDeviceWidth() / this.deviceScreenDim.getCurrentDeviceHeight();
            double stageWidth = this.deviceScreenDim.getCurrentDeviceWidth() / screenDelimiter;
            double stageHeight = this.deviceScreenDim.getCurrentDeviceHeight() / screenDelimiter;

            if (region.getPrefWidth() == Region.USE_COMPUTED_SIZE) {
                region.setPrefWidth(stageWidth);
            } else {
                stageWidth = region.getPrefWidth();
            }

            if (region.getPrefHeight() == Region.USE_COMPUTED_SIZE) {
                region.setPrefHeight(stageHeight);
            } else {
                stageHeight = region.getPrefHeight();
            }
            Group group = new Group(region);
            Pane rootPane = new StackPane();
            Scene scene = new Scene(rootPane, stageWidth, stageHeight);
            group.scaleXProperty().bind(scene.widthProperty().divide(stageWidth));
            group.scaleYProperty().bind(scene.heightProperty().divide(stageHeight));
            currentStage.setTitle(AppConstants.APPLICATION_TITLE);
            rootPane.getChildren().add(group);
            currentStage.setScene(scene);
            scene.getRoot().requestFocus();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.initModality(currentStage, modality);
        return fxmlLoader;
    }

    private void initModality(Stage currentStage, Modality modality) {
        if (modality == null) {
            modality = Modality.NONE;
        }
        switch (modality) {
            case NONE:
                currentStage.show();
                break;
            case APPLICATION_MODAL:
                currentStage.initModality(Modality.APPLICATION_MODAL);
                currentStage.showAndWait();
                break;
            case WINDOW_MODAL:
                currentStage.initModality(Modality.WINDOW_MODAL);
                currentStage.showAndWait();
                break;
        }
        currentStage.centerOnScreen();
    }
}
