package DAO;

import model.Exercise;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ExerciseDAO {
    private static final String ONGOING_FILE_PATH = "src/main/data/ongoing_exercise";
    private static final String COMPLETED_FILE_PATH = "src/main/data/completed_exercise";

    public void saveOngoing(Exercise exercise) {
        saveToFile(exercise, ONGOING_FILE_PATH);
    }

    public void saveCompleted(Exercise exercise) {
        saveToFile(exercise, COMPLETED_FILE_PATH);
    }

    private void saveToFile(Exercise exercise, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(toCsvString(exercise) + "\n");
            System.out.println("Saved to file: " + filePath + " - " + exercise.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String toCsvString(Exercise exercise) {
        return String.join(",",
                exercise.getCategory(),
                exercise.getName(),
                exercise.getDistanceReps(),
                String.valueOf(exercise.getDuration()),
                String.valueOf(exercise.getCaloriesBurned()),
                String.valueOf(exercise.isCompleted()),
                exercise.getCompletedDateTime() != null ? exercise.getCompletedDateTime().toString() : ""
        );
    }

    public List<Exercise> loadOngoing() {
        return loadFromFile(ONGOING_FILE_PATH);
    }

    public List<Exercise> loadCompleted() {
        return loadFromFile(COMPLETED_FILE_PATH);
    }

    private List<Exercise> loadFromFile(String filePath) {
        List<Exercise> exercises = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    System.out.println("Skipping empty line");
                    continue;
                }
                String[] parts = line.split(",");
                if (parts.length < 6) {
                    System.out.println("Invalid exercise entry: " + line);
                    continue; // Skip invalid entries
                }
                Exercise exercise = new Exercise();
                exercise.setCategory(parts[0]);
                exercise.setName(parts[1]);
                exercise.setDistanceReps(parts[2]);
                exercise.setDuration(Integer.parseInt(parts[3]));
                exercise.setCaloriesBurned(Integer.parseInt(parts[4]));
                exercise.setCompleted(Boolean.parseBoolean(parts[5]));
                if (parts.length > 6 && !parts[6].isEmpty()) {
                    exercise.setCompletedDateTime(LocalDateTime.parse(parts[6]));
                }
                exercises.add(exercise);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return exercises;
    }

    public void deleteOngoing(Exercise exercise) {
        deleteFromFile(exercise, ONGOING_FILE_PATH);
    }

    private void deleteFromFile(Exercise exercise, String filePath) {
        List<Exercise> exercises = loadFromFile(filePath);
        List<Exercise> updatedExercises = new ArrayList<>();

        for (Exercise ex : exercises) {
            if (!isSameExercise(ex, exercise)) {
                updatedExercises.add(ex);
            } else {
                System.out.println("Deleting exercise: " + ex.getName());
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Exercise ex : updatedExercises) {
                writer.write(toCsvString(ex) + "\n");
            }
            System.out.println("File updated: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isSameExercise(Exercise ex1, Exercise ex2) {
        return ex1.getCategory().equals(ex2.getCategory()) &&
                ex1.getName().equals(ex2.getName()) &&
                ex1.getDistanceReps().equals(ex2.getDistanceReps()) &&
                ex1.getDuration() == ex2.getDuration() &&
                ex1.getCaloriesBurned() == ex2.getCaloriesBurned();
    }

    public void moveToCompleted(Exercise exercise) {
        saveCompleted(exercise);
        deleteOngoing(exercise);
        System.out.println("Exercise moved to completed: " + exercise.getName());
    }
}