package app.utils.stageUtils;

import javafx.fxml.FXMLLoader;
import javafx.stage.Modality;
import javafx.stage.Stage;

public interface StageManager {

    FXMLLoader loadSceneToStage(Stage currentStage, String fxmlPath, Modality modality);
}
