package Controller;

import DAO.FitnessGoalDAO;
import SceneManager.SceneManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import model.FitnessGoal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FitnessGoalController {
    @FXML
    private TextField goalDescriptionField;
    @FXML
    private DatePicker completionDatePicker;
    @FXML
    private VBox goalsContainer;
    @FXML
    private Button returnButton;
    @FXML
    private Button deleteGoal;

    private int goalCount = 1;

    private FitnessGoalDAO goalDAO = new FitnessGoalDAO();
    private ObservableList<FitnessGoal> goals = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        try {
            List<FitnessGoal> loadedGoals = goalDAO.loadGoals();
            goals.addAll(loadedGoals);
            displayGoals();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getNextGoalId() {
        int maxId = 0;
        for (FitnessGoal goal : goals) {
            if (goal.getGoalId() > maxId) {
                maxId = goal.getGoalId();
            }
        }
        return maxId + 1; // Increment to get the next unique ID
    }

    @FXML
    public void addGoal() {
        String description = goalDescriptionField.getText();
        String completionDate = completionDatePicker.getValue() != null
                ? completionDatePicker.getValue().toString()
                : "";

        if (description.isEmpty() || completionDate.isEmpty()) {
            // Handle validation, maybe show an alert
            return;
        }

        int newGoalId = getNextGoalId(); // Get the next unique goal ID
        FitnessGoal newGoal = new FitnessGoal(newGoalId, description, completionDate, "Incomplete");

        // Add the new goal to the list and the UI
        goals.add(newGoal);
        try {
            goalDAO.saveGoals(new ArrayList<>(goals)); // Save updated list
        } catch (IOException e) {
            e.printStackTrace();
        }

        displayGoals(); // Update the UI

        goalDescriptionField.clear();
        completionDatePicker.setValue(null);
    }

    private void displayGoals() {
        goalsContainer.getChildren().clear();

        for (FitnessGoal goal : goals) {
            VBox goalBox = new VBox();
            goalBox.setStyle("-fx-padding: 10; -fx-background-color: #f0f0f0; -fx-border-color: #ccc;");
            goalBox.setSpacing(5);

            Label goalLabel = new Label("Goal " + goal.getGoalId() + ": " + goal.getDescription());
            Label statusLabel = new Label("Status: " + goal.getStatus());
            Label dateLabel = new Label("Complete By: " + goal.getCompletionDate());

            goalBox.getChildren().addAll(goalLabel, statusLabel, dateLabel);

            // Add click event to mark the goal for deletion
            goalBox.setOnMouseClicked(event -> {
                if (goalBox.getStyle().contains("yellow")) {
                    // Unselect if already selected
                    goalBox.setStyle("-fx-padding: 10; -fx-background-color: #f0f0f0; -fx-border-color: #ccc;");
                    goalBox.setUserData(null);
                } else {
                    // Highlight selected goal
                    goalBox.setStyle("-fx-padding: 10; -fx-background-color: yellow; -fx-border-color: #ccc;");
                    goalBox.setUserData(goal);
                    showCompletionAlert(goal);
                }
            });

            goalsContainer.getChildren().add(goalBox);
        }
    }

    private void showCompletionAlert(FitnessGoal goal) {
        // Create an Alert dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Update Goal Status");
        alert.setHeaderText("Goal: " + goal.getDescription());
        alert.setContentText("Would you like to mark this goal as completed?");

        ButtonType markCompleted = new ButtonType("Mark as Completed");
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(markCompleted, cancel);

        // Show the alert and handle the user's choice
        alert.showAndWait().ifPresent(response -> {
            if (response == markCompleted) {
                goal.setStatus("Complete");

                try {
                    goalDAO.saveGoals(goals); // Save the updated list
                    goalDAO.extractCompletedGoals(goals);
                    displayGoals(); // Refresh the UI
                } catch (IOException e) {
                    e.printStackTrace(); // Handle potential IOExceptions
                }
            }
        });
    }

    @FXML
    public void deleteGoal() {
        // Find all selected goals
        List<VBox> selectedGoalBoxes = goalsContainer.getChildren().stream()
                .filter(node -> node instanceof VBox)
                .map(node -> (VBox) node)
                .filter(vbox -> vbox.getUserData() != null)
                .collect(Collectors.toList());

        if (selectedGoalBoxes.isEmpty()) {
            showAlert("Error", "No goals selected to delete.");
            return;
        }

        // Remove selected goals from the list
        for (VBox goalBox : selectedGoalBoxes) {
            FitnessGoal goalToDelete = (FitnessGoal) goalBox.getUserData();
            goals.remove(goalToDelete);
        }

        try {
            goalDAO.saveGoals(goals);
            goalDAO.extractCompletedGoals(goals);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Refresh the UI
        displayGoals();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void onClickReturn() {
        SceneManager.loadScene("/View/HomePage.fxml");
    }

    @FXML
    private void goToProfile() {
        SceneManager.loadScene("/View/ProfileView.fxml");
    }
}