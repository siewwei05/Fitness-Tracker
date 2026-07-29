package Controller;




import DAO.UserDataDAO;
import SceneManager.SceneManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import model.UserData;
import model.User;
import Session.UserSession;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class ProfileController {
    @FXML private Label heightLabel;
    @FXML private Label weightLabel;
    @FXML private TextField heightField;
    @FXML private TextField weightField;




    @FXML private PieChart piechart;
    @FXML private LineChart<String, Number> progressChart;
    @FXML
    private Button ReturnButton;
    @FXML
    private Button GoalButton;
    @FXML
    private Button ExerciseCompleteButton;
    @FXML
    private Button caloryBurnedButton;
    @FXML
    private Button calorytakenButton;
    @FXML
    private ImageView usericon;
    @FXML
    private Label userIDLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label BODLabel;




    private final UserDataDAO userDataDAO = new UserDataDAO();
    private UserData userData;
    private User user;
    public void setUser(User user) {
        this.user = user;  // Set the user object
    }


    private static final String COMPLETED_GOALS_FILE = "src/main/data/completedGoals";
    private static final String EXERCISES_FILE = "src/main/data/completed_exercise";
    private static final String MEALS_FILE = "src/main/data/meals";
//    private static final String CAL_BURNED_FILE = "src/main/data/calories_burned";


    public void initialize() {
        User currentUser = UserSession.getCurrentUser();


        try {
            userData = userDataDAO.loadUserData(user);

            if (user != null) {
                // Proceed with syncing user data
                userData.syncFromUser(user);
            } else {
                System.out.println("User is null. Cannot sync user data.");
                // Handle the null user scenario, e.g., redirect to login
            }

            updateUI();
        } catch (Exception e) {
            e.printStackTrace();
        }


        // Sync labels with user data

        if (currentUser != null) {
            userIDLabel.setText(currentUser.getUserId());
            nameLabel.setText(currentUser.getName());
            BODLabel.setText(currentUser.getDob());
            heightLabel.setText("Height : "+String.valueOf(currentUser.getHeight())+"cm");
            weightLabel.setText("Weight : "+String.valueOf(currentUser.getWeight())+ "kg");
        } else {
            System.err.println("Error: userData is null.");
        }


       try {
            updateGoalProgress();
            updateExerciseProgress();
           updateCaloryBurnedProgress();
            updateCaloryTakenProgress();
        } catch (Exception e) {
            System.err.println("Error updating progress: " + e.getMessage());
            e.printStackTrace();
        }

       try {
            populateChart("Fitness Goals", "src/main/data/goals",2);
        } catch (Exception e) {
            System.err.println("Error populating chart: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private int countLinesInFile(String filePath) {
        int count = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            while (reader.readLine() != null) {
                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return count;
    }

    private List<Integer> readValuesFromFile(String filePath) {
        List<Integer> values = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                values.add(Integer.parseInt(line.trim()));
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return values;
    }



    private void updateGoalProgress() {
        int goalCount = countLinesInFile(COMPLETED_GOALS_FILE);
        GoalButton.setText("Goals Completed:"+ goalCount);
    }

    @FXML
    private void updateExerciseProgress() {
        int exerciseCount = countLinesInFile(EXERCISES_FILE);
        ExerciseCompleteButton.setText("Exercise Done:"+ exerciseCount);
    }

   @FXML
   private void updateCaloryBurnedProgress() {
        int totalCaloriesBurned = SumCalculator(EXERCISES_FILE,4);
       caloryBurnedButton.setText("Calories Burned:"+String.valueOf(totalCaloriesBurned));
    }


    @FXML
    private void updateCaloryTakenProgress() {
        int totalCaloriesTaken = SumCalculator(MEALS_FILE,1);
        calorytakenButton.setText("Calories Taken:"+String.valueOf(totalCaloriesTaken));
    }

    public static int SumCalculator(String filePath, int calorieColumnIndex) {
        int totalCalories = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            // Read each line in the file
            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(","); // Assuming comma-separated values

                if (columns.length > calorieColumnIndex) {
                    String calorieColumn = columns[calorieColumnIndex].trim();

                    try {
                        // Attempt to parse the numeric value from the column
                        int calories = Integer.parseInt(calorieColumn);
                        totalCalories += calories;  // Add to the total
                    } catch (NumberFormatException e) {
                        // If it can't be parsed (e.g., non-numeric data), skip this line
                        // or handle it as you see fit
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return totalCalories;  // Return the total sum of calories
    }
  /*  private int calculateSumFromFile(String filePath) {
        int sum = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sum += Integer.parseInt(line.trim());
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return sum;
    }*/

    private void updateUI() {
        heightLabel.setText("Height: " + userData.getHeight() + " cm");
        weightLabel.setText("Weight: " + userData.getWeight() + " kg");
       // GoalLAbel.setText("Goals Completed: " + userData.getGoalsCompleted());


       // ExerciseCompletelabel.setText("Exercises Done: " + userData.getExerciseCompleted());

        // Use User for personal information
       if(user!=null) {
            userIDLabel.setText("User ID: " + user.getUserId());
            nameLabel.setText("Name: " + user.getName());
            BODLabel.setText("Date of Birth: " + user.getDob());
       }else{
            System.out.println("User is null. Cannot sync user data.");
       }
    }



    private void saveData() {
        try {
            userDataDAO.saveUserData(userData, user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void chartWithData(String chartTitle, String filePath, int dateColumnIndex, int valueColumnIndex) {
        progressChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(chartTitle);

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(",");

                if (columns.length > Math.max(dateColumnIndex, valueColumnIndex)) {
                    // Extract and validate the date
                    String dateStr = columns[dateColumnIndex].trim();
                    LocalDate date = parseDate(dateStr);

                    // Extract and validate the value
                    String valueStr = columns[valueColumnIndex].trim();
                    Number value = parseValue(valueStr);

                    if (date != null && value != null) {
                        // Add the data to the series with the date formatted as yyyy-MM-dd
                        series.getData().add(new XYChart.Data<>(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), value));
                    }
                } else {
                    System.err.println("Invalid line format: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + filePath);
            e.printStackTrace();
        }

        progressChart.getData().add(series);
    }

    private LocalDate parseDate(String dateStr) {
        try {
            if (dateStr.contains("T")) {
                // Handle timestamps (e.g., from completed_exercise)
                return LocalDate.parse(dateStr.split("T")[0]);
            } else {
                // Handle plain dates (e.g., from meals)
                return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }
        } catch (DateTimeParseException e) {
            System.err.println("Invalid date format: " + dateStr);
            return null;
        }
    }

    private Number parseValue(String valueStr) {
        try {
            return Integer.parseInt(valueStr);
        } catch (NumberFormatException e) {
            System.err.println("Invalid value format: " + valueStr);
            return null;
        }
    }

    public void visualizeExerciseData() {
        String filePath = "src/main/data/completed_exercise"; // Adjust path if necessary

        // Initialize counters for each category
        int flexibilityCount = 0;
        int strengthCount = 0;
        int balanceCount = 0;
        int cardioCount = 0;

        // Read the file and count exercises by category
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Remove parentheses and split by commas
                String cleanedLine = line.replaceAll("[()]", "");
                String[] fields = cleanedLine.split(",");
                if (fields.length >= 6 && "true".equals(fields[5].trim())) { // Ensure the exercise is completed
                    String category = fields[0].trim();

                    // Increment the counter based on the category
                    switch (category) {
                        case "Flexibility":
                            flexibilityCount++;
                            break;
                        case "Strength":
                            strengthCount++;
                            break;
                        case "Balance":
                            balanceCount++;
                            break;
                        case "Cardio":
                            cardioCount++;
                            break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Prepare data for PieChart
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        if (flexibilityCount > 0) pieChartData.add(new PieChart.Data("Flexibility", flexibilityCount));
        if (strengthCount > 0) pieChartData.add(new PieChart.Data("Strength", strengthCount));
        if (balanceCount > 0) pieChartData.add(new PieChart.Data("Balance", balanceCount));
        if (cardioCount > 0) pieChartData.add(new PieChart.Data("Cardio", cardioCount));

        // Set data to the PieChart
        piechart.setData(pieChartData);
    }





    public void populateChart(String chartTitle, String filePath, int dateColumnIndex) {
        progressChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(chartTitle);

        List<String> dates = new ArrayList<>();
        List<Integer> counts = new ArrayList<>();

        int cumulativeCount = 0;  // This variable will store the cumulative count of goals

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(","); // assuming comma-separated values
                if (columns.length > dateColumnIndex) {
                    // Extract the date from the specified column
                    String dateStr = columns[dateColumnIndex].trim();
                    LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                    // Increment the count for each goal entry on this date
                    cumulativeCount++; // Simulate a rising count

                    // Add the date and the cumulative count to the lists
                    dates.add(date.toString());  // Store the date in the list (as a string)
                    counts.add(cumulativeCount); // Store the cumulative count in the list
                }
            }

            // Now, populate the chart with the cumulative data
            for (int i = 0; i < dates.size(); i++) {
                String date = dates.get(i);
                int count = counts.get(i);

                // Add the data to the series for the chart
                series.getData().add(new XYChart.Data<>(date, count));
            }

            // Add the series to the chart (assuming you have a LineChart in your FXML)
            progressChart.getData().add(series);

            // Update the x-axis with the actual dates
            progressChart.getXAxis().setLabel("Date");

        } catch (IOException e) {
            e.printStackTrace();
        }


    }//after gettingdata fromothers, create a new text file that only served for progress(Goal_progress) method cna do like ++



    // Action for the Goal button
    @FXML
    private void onGoalButtonClick(ActionEvent event) {
        piechart.setVisible(false);
        progressChart.setVisible(true);
        int goalCount = countLinesInFile(COMPLETED_GOALS_FILE);
        GoalButton.setText("Goals Completed:"+ goalCount);
        populateChart("Fitness Goals", COMPLETED_GOALS_FILE,2);

    }


    // Action for the Exercise Complete button
    @FXML
    private void onExerciseCompleteButtonClick(ActionEvent event) {
        piechart.setVisible(true);
        progressChart.setVisible(false);
        int exerciseCount = countLinesInFile(EXERCISES_FILE);
        ExerciseCompleteButton.setText("Exercises Done:"+ exerciseCount);
        visualizeExerciseData();


       // populateChart("Exercises Completed", EXERCISES_FILE);
    }

    // Action for the Calory Burned button
    @FXML
    private void onCaloryBurnedButtonClick(ActionEvent event) {
        piechart.setVisible(false);
        progressChart.setVisible(true);
        chartWithData("Calories Taken", EXERCISES_FILE,6,4);
        int totalCaloriesBurned = SumCalculator(EXERCISES_FILE,4);
        caloryBurnedButton.setText("Calories Burned:"+ String.valueOf(totalCaloriesBurned));
    }

    // Action for the Calory Taken button
    @FXML
    private void onCaloryTakenButtonClick(ActionEvent event) {

        piechart.setVisible(false);
        progressChart.setVisible(true);
        chartWithData("Calories Taken", MEALS_FILE,2,1);
        int totalCaloriesTaken = SumCalculator(MEALS_FILE,1);
        calorytakenButton.setText("Calories Taken:"+ String.valueOf(totalCaloriesTaken));
        //populateChart("Calories Taken", CAL_TAKEN_FILE);
    }

 /*  @FXML
    private void onHeightLabelClick() {
        heightLabel.setVisible(false);
        heightField.setVisible(true);
        heightField.setText(String.valueOf(userData.getHeight()));
    }

    @FXML
    private void onWeightLabelClick() {
        weightLabel.setVisible(false);
        weightField.setVisible(true);
        weightField.setText(String.valueOf(userData.getWeight()));
    }

    @FXML
    private void onHeightEdit() {
        int newHeight = Integer.parseInt(heightField.getText());
        if (newHeight > 0) {
            userData.setHeight(newHeight);
            user.setHeight(newHeight); // Update User

            heightLabel.setVisible(true);
            heightField.setVisible(false);
            updateUI();
            saveData();}else {
            throw new IllegalArgumentException("Height must be positive.");
        }

    }

    @FXML
    private void onWeightEdit() {
        int newWeight = Integer.parseInt(weightField.getText());
        if(newWeight > 0) {
            userData.setWeight(newWeight);
            user.setWeight(newWeight);
            weightLabel.setVisible(true);
            weightField.setVisible(false);
            updateUI();
            saveData();}else {
            throw new IllegalArgumentException("Weight must be positive.");
        }
    }
    // Action for clicking on the Height label to enable editing
    @FXML
    void onHeightLabelClick(MouseEvent event) {
        heightLabel.setVisible(false);
        heightField.setVisible(true);
        heightField.requestFocus();
    }

    // Action for submitting the height edit
    @FXML
    void onHeightEdit(ActionEvent event) {
        double newHeight = Double.parseDouble(heightField.getText());

        if (newHeight >0) {
            userData.setHeight(newHeight);
            user.setHeight(newHeight); // Update User
            heightLabel.setVisible(true);
            heightField.setVisible(false);
            updateUI();
            saveData();
        } else {
            throw new IllegalArgumentException("Height must be positive.");
        }
    }

    // Action for clicking on the Weight label to enable editing
    @FXML
    void onWeightLabelClick(MouseEvent event) {
        weightLabel.setVisible(false);
        weightField.setVisible(true);
        weightField.requestFocus();
    }

    // Action for submitting the weight edit
    @FXML
    void onWeightEdit(ActionEvent event) {
        double newWeight = Double.parseDouble(weightField.getText());

        if (newWeight > 0) {
            userData.setWeight(newWeight);
            user.setWeight(newWeight); // Update User
            weightLabel.setVisible(true);
            weightField.setVisible(false);
            updateUI();
            saveData();
        } else {
            throw new IllegalArgumentException("Weight must be positive.");
        }
    }
*/
    @FXML
    private  void goHomePage(ActionEvent event) throws Exception {
        SceneManager.loadScene("/View/HomePage.fxml");
    }


}