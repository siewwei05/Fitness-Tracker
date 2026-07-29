package model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Exercise {
    private String category;
    private String name;
    private String distanceReps;
    private int duration; // Duration in minutes
    private int caloriesBurned;
    private boolean completed;
    private LocalDateTime completedDateTime;

    // Default constructor
    public Exercise() {
    }

    // Parameterized constructor
    public Exercise(String category, String name, String distanceReps, int duration, int caloriesBurned, boolean completed, LocalDateTime completedDateTime) {
        this.category = category;
        this.name = name;
        this.distanceReps = distanceReps;
        this.duration = duration;
        this.caloriesBurned = caloriesBurned;
        this.completed = completed;
        this.completedDateTime = completedDateTime;
    }

    // Getters and Setters
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDistanceReps() {
        return distanceReps;
    }

    public void setDistanceReps(String distanceReps) {
        this.distanceReps = distanceReps;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getCaloriesBurned() {
        return caloriesBurned;
    }

    public void setCaloriesBurned(int caloriesBurned) {
        this.caloriesBurned = caloriesBurned;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public LocalDateTime getCompletedDateTime() {
        return completedDateTime;
    }

    public void setCompletedDateTime(LocalDateTime completedDateTime) {
        this.completedDateTime = completedDateTime;
    }

    // Override equals method to compare exercises
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Exercise exercise = (Exercise) o;
        return duration == exercise.duration &&
                caloriesBurned == exercise.caloriesBurned &&
                completed == exercise.completed &&
                Objects.equals(category, exercise.category) &&
                Objects.equals(name, exercise.name) &&
                Objects.equals(distanceReps, exercise.distanceReps) &&
                Objects.equals(completedDateTime, exercise.completedDateTime);
    }

    // Override hashCode method
    @Override
    public int hashCode() {
        return Objects.hash(category, name, distanceReps, duration, caloriesBurned, completed, completedDateTime);
    }

    // Override toString method for easy printing
    @Override
    public String toString() {
        return "Exercise{" +
                "category='" + category + '\'' +
                ", name='" + name + '\'' +
                ", distanceReps='" + distanceReps + '\'' +
                ", duration=" + duration +
                ", caloriesBurned=" + caloriesBurned +
                ", completed=" + completed +
                ", completedDateTime=" + completedDateTime +
                '}';
    }
}
