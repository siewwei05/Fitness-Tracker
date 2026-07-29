package DAO;

import model.FitnessGoal;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class FitnessGoalDAO {
    private static final String File_Path = "src/main/Data/goals";

    // Save goals to file in text format
    public void saveGoals(List<FitnessGoal> goals) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(File_Path))) {
            for (FitnessGoal goal : goals) {
                String goalData = goal.getGoalId() + "," +
                        goal.getDescription() + "," +
                        goal.getCompletionDate() + "," +
                        goal.getStatus();
                writer.write(goalData);
                writer.newLine(); // Move to the next line
            }
        }
    }

    // Load goals from file
    public List<FitnessGoal> loadGoals() throws IOException {
        List<FitnessGoal> goals = new ArrayList<>();
        File file = new File(File_Path);

        if (!file.exists()) {
            return goals; // Return an empty list if file doesn't exist
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(File_Path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(","); // Split the line by commas
                if (data.length == 4) { // Ensure the data has all 4 fields
                    int goalId = Integer.parseInt(data[0].trim());
                    String description = data[1].trim();
                    String completionDate = data[2].trim();
                    String status = data[3].trim();

                    FitnessGoal goal = new FitnessGoal(goalId, description, completionDate, status);
                    goals.add(goal);
                }
            }
        }
        return goals;
    }

    public void extractCompletedGoals(List<FitnessGoal> goals) throws IOException {
        String completedFilePath = "src/main/Data/completedGoals"; // Path for the new file
        File completedFile = new File(completedFilePath);

        // Load all goals
        List<FitnessGoal> allGoals = loadGoals();
        List<FitnessGoal> completedGoals = new ArrayList<>();

        // Filter completed goals
        for (FitnessGoal goal : allGoals) {
            if ("Complete".equalsIgnoreCase(goal.getStatus())) {
                completedGoals.add(goal);
            }
        }

        // Write completed goals to the completedGoals file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(completedFile))) {
            for (FitnessGoal goal : completedGoals) {
                String goalData = goal.getGoalId() + "," +
                        goal.getDescription() + "," +
                        goal.getCompletionDate() + "," +
                        goal.getStatus();
                writer.write(goalData);
                writer.newLine(); // Move to the next line
            }
        }
    }
}