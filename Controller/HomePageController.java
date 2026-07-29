package Controller;

import Main.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import SceneManager.SceneManager;

public class HomePageController {

    @FXML
    private void goToNutrition() {
        SceneManager.loadScene("/View/Nutrition.fxml");
    }

    @FXML
    private void goToActivity() {
        SceneManager.loadScene("/View/ActivityRecord.fxml", "home");
    }

    @FXML
    private void goToExercise() {
        SceneManager.loadScene("/View/Exercise.fxml");
    }

    @FXML
    private void goToFitnessGoal() {
        SceneManager.loadScene("/View/FitnessGoal.fxml");
    }

    @FXML
    private void goToProfilePage(MouseEvent event) {
        SceneManager.loadScene("/View/ProfileView.fxml");
    }

    @FXML
    public void goToLoginWithAlert() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Log Out Confirmation");
        alert.setHeaderText("Are you sure you want to log out?");
        alert.setContentText("Click 'OK' to confirm.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    Main.showLogin();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    private void goToSetting(MouseEvent event) {
        SceneManager.loadScene("/View/Setting.fxml");
    }
}
