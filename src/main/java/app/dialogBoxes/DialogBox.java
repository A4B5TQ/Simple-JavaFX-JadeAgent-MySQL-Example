package app.dialogBoxes;

import javafx.scene.control.Alert;

public class DialogBox implements Dialog{

    public DialogBox() {
    }

    public void successfullyOperation() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Successful!");
        alert.showAndWait();
    }

    public void errorOperation() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Operation abort!");
        alert.showAndWait();
    }
    public void columnTakenErrorBox(String columnLetter) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setHeaderText("Wrong input data!");
        alert.setContentText("Column " + columnLetter + " already taken! Please upload file again");
        alert.showAndWait();
    }
}
