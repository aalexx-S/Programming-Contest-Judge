package Judge;

import SharedGuiElement.InformationAlertBuilder;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 * Created by aalexx on 1/2/16.
 */
public class NewQuestionAlert {
    Alert alert;
    public NewQuestionAlert (Stage owner) {
        InformationAlertBuilder builder = InformationAlertBuilder.create();
        alert = builder.setTitle("New Question")
                .setHeaderText("Hey, listen!!")
                .setContentText("A new question arrived!!")
                .build();
        ((Stage) alert.getDialogPane().getChildren().get(0).getScene().getWindow()).initOwner(owner);
    }

    public NewQuestionAlert () {
        InformationAlertBuilder builder = InformationAlertBuilder.create();
        alert = builder.setTitle("New Question")
                .setHeaderText("Hey, listen!!")
                .setContentText("A new question arrived!!")
                .build();
    }

    public void show () {
        alert.show();
    }
}
