package Controller;

import DAO.DAOImpObj;
import SceneManager.SceneManager;
import Session.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import model.User;

public class SettingController {

    private String userId = UserSession.getCurrentUserId();
    private DAOImpObj dao = DAOImpObj.getInstance();
    private User currentUser;

    @FXML
    public void initialize() {
        if (UserSession.isLoggedIn()) {
            userId = UserSession.getCurrentUserId();
            currentUser = dao.searchByUserId(userId);
        } else {
            showAlert("Error", "No user is logged in. Please log in to access settings.");
        }
    }

    @FXML
    private void onClickProfileSettings(ActionEvent event) throws Exception {
        SceneManager.loadScene("/View/ProfileSetting.fxml");
    }

    @FXML
    private void onClickAccountSettings(ActionEvent event) throws Exception {
        SceneManager.loadScene("/View/AccountSetting.fxml");
    }

    @FXML
    private void onClickNotificationSettings(ActionEvent event) throws Exception {
        SceneManager.loadScene("/View/NotificationSetting.fxml");
    }

    @FXML
    private void onClickReturnToHome(ActionEvent event) throws Exception {
        SceneManager.loadScene("/View/HomePage.fxml");
    }

    @FXML
    private void onClickReturnToSetting(ActionEvent event) throws Exception {
        SceneManager.loadScene("/View/Setting.fxml");
    }

    @FXML
    private void onChangeUserName(ActionEvent event) {
        if (!validateUserSession()) return;

        TextField newUsernameField = new TextField();
        newUsernameField.setPromptText("Enter new username");

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Change Username");
        alert.setHeaderText("Current Username: " + currentUser.getUsername());
        alert.setContentText("Please enter a new username:");
        alert.getDialogPane().setContent(newUsernameField);
        alert.showAndWait();

        String newUsername = newUsernameField.getText();

        if (newUsername == null || newUsername.trim().isEmpty()) {
            showAlert("Error", "Username cannot be empty.");
            return;
        }

        if (dao.searchByUsername(newUsername) != null) {
            showAlert("Error", "This username is already taken. Please choose a different username.");
            return;
        }

        currentUser.setUsername(newUsername);
        dao.updateUser(currentUser);
        showAlert("Success", "Your username has been updated successfully.");
    }

    @FXML
    private void onUpdateHeight(ActionEvent event) {
        if (!validateUserSession()) return;

        TextField newHeightField = new TextField();
        newHeightField.setPromptText("Enter new height in cm");

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Update Height");
        alert.setHeaderText("Current Height: " + currentUser.getHeight() + " cm");
        alert.setContentText("Please enter your new height:");
        alert.getDialogPane().setContent(newHeightField);
        alert.showAndWait();

        String newHeightInput = newHeightField.getText();

        if (newHeightInput == null || newHeightInput.trim().isEmpty()) {
            showAlert("Error", "Height cannot be empty.");
            return;
        }

        double newHeight;
        try {
            newHeight = Double.parseDouble(newHeightInput.trim());
            if (newHeight <= 0) {
                showAlert("Error", "Height must be a positive number.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid height format. Please enter a numeric value.");
            return;
        }

        currentUser.setHeight(newHeight);
        dao.updateUser(currentUser);
        showAlert("Success", "Your height has been updated successfully.");
    }

    @FXML
    private void onUpdateWeight(ActionEvent event) {
        if (!validateUserSession()) return;

        TextField newWeightField = new TextField();
        newWeightField.setPromptText("Enter new weight in kg");

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Update Weight");
        alert.setHeaderText("Current Weight: " + currentUser.getWeight() + " kg");
        alert.setContentText("Please enter your new weight:");
        alert.getDialogPane().setContent(newWeightField);
        alert.showAndWait();

        String newWeightInput = newWeightField.getText();

        if (newWeightInput == null || newWeightInput.trim().isEmpty()) {
            showAlert("Error", "Weight cannot be empty.");
            return;
        }

        double newWeight;
        try {
            newWeight = Double.parseDouble(newWeightInput.trim());
            if (newWeight <= 0) {
                showAlert("Error", "Weight must be a positive number.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid weight format. Please enter a numeric value.");
            return;
        }

        currentUser.setWeight(newWeight);
        dao.updateUser(currentUser);
        showAlert("Success", "Your weight has been updated successfully.");
    }

    @FXML
    private void onChangePassword(ActionEvent event) {
        if (!validateUserSession()) return;

        TextField oldPasswordField = new TextField();
        oldPasswordField.setPromptText("Enter current password");
        oldPasswordField.setStyle("-fx-text-fill: #000000;");

        TextField newPasswordField = new TextField();
        newPasswordField.setPromptText("Enter new password");
        newPasswordField.setStyle("-fx-text-fill: #000000;");

        TextField confirmPasswordField = new TextField();
        confirmPasswordField.setPromptText("Confirm new password");
        confirmPasswordField.setStyle("-fx-text-fill: #000000;");

        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(oldPasswordField, newPasswordField, confirmPasswordField);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Change Password");
        alert.setHeaderText("Change Password for: " + currentUser.getUsername());
        alert.setContentText("Please enter your current password and your new password:");
        alert.getDialogPane().setContent(vbox);
        alert.showAndWait();

        String oldPassword = oldPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (oldPassword == null || oldPassword.trim().isEmpty()) {
            showAlert("Error", "Current password cannot be empty.");
            return;
        }

        if (newPassword == null || newPassword.trim().isEmpty()) {
            showAlert("Error", "New password cannot be empty.");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            showAlert("Error", "New passwords do not match.");
            return;
        }

        if (newPassword.length() < 6) {
            showAlert("Error", "New password must be at least 6 characters long.");
            return;
        }

        if (!currentUser.getPassword().equals(oldPassword)) {
            showAlert("Error", "Current password is incorrect.");
            return;
        }

        currentUser.setPassword(newPassword);
        dao.updateUser(currentUser);
        showAlert("Success", "Your password has been updated successfully.");
    }

    @FXML
    private void onLogOut(ActionEvent event) throws Exception {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Log Out Confirmation");
        alert.setHeaderText("Are you sure you want to log out?");
        alert.setContentText("Click 'OK' to confirm.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    UserSession.logout();
                    SceneManager.loadScene("/View/Login.fxml"); // Redirect to login page after logout
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    private void onClickYes(ActionEvent event) {
        showAlert("Success", "Notifications have been turned ON.");
    }

    @FXML
    private void onClickNo(ActionEvent event) {
        showAlert("Success", "Notifications have been turned OFF.");
    }

    private boolean validateUserSession() {
        if (!UserSession.isLoggedIn()) {
            showAlert("Error", "No user is logged in. Please log in to perform this action.");
            return false;
        }

        if (currentUser == null) {
            showAlert("Error", "User not found in the system.");
            return false;
        }

        return true;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
