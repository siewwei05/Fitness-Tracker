package model;

public class UserData {

    private String name;
    private String userId;
    private String username;
    private String dateOfBirth;
    private double height; // in cm
    private double weight; // in kg
    private int goalsCompleted;
    private int caloriesBurned;
    private int caloriesTaken;
    private int ExerciseCompleted;

    // Method to sync data from User
    public void syncFromUser(User user) {
       if(user!=null){ this.userId = user.getUserId();
        this.name = user.getName();
        this.username = user.getUsername();
        this.dateOfBirth = user.getDob();
        this.height =  user.getHeight();
        this.weight =  user.getWeight();
    }else{
           System.out.println("UserData.syncFromUser() User is null");
       }
    }


    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public double getHeight() { return height; }
    public void setHeight(double height) { this.height = height; }

    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }

    public int getGoalsCompleted() { return goalsCompleted; }
    public void setGoalsCompleted(int goalsCompleted) { this.goalsCompleted = goalsCompleted; }

    public int getCaloriesBurned() { return caloriesBurned; }
    public void setCaloriesBurned(int caloriesBurned) { this.caloriesBurned = caloriesBurned; }

    public int getCaloriesTaken() { return caloriesTaken; }
    public void setCaloriesTaken(int caloriesTaken) { this.caloriesTaken = caloriesTaken; }

    public int getExerciseCompleted() { return ExerciseCompleted; }
    public void setExerciseCompleted(int ExerciseCompleted) { this.ExerciseCompleted = ExerciseCompleted; }
}
