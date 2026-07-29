package DAO;

import model.UserData;
import model.User;

import java.io.*;
import java.nio.file.Paths;

public class UserDataDAO {
    private final String filePath = Paths.get("src/main/Data/UserData").toString();

    public UserData loadUserData(User user) throws IOException {
        UserData userData = new UserData();

        userData.syncFromUser(user);
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                try {
                    switch (lineNumber) {
                        //case 1 -> userData.setName(line);
                       // case 2 -> userData.setUserId(line);
                       // case 3 -> userData.setDateOfBirth(line);
                        case 1,2,3->{/*skiping those attribut can get from user*/}
                        case 4 -> userData.setHeight(Integer.parseInt(line));
                        case 5 -> userData.setWeight(Integer.parseInt(line));
                        case 6 -> userData.setGoalsCompleted(Integer.parseInt(line));
                        case 7 -> userData.setCaloriesBurned(Integer.parseInt(line));
                        case 8 -> userData.setCaloriesTaken(Integer.parseInt(line));
                        case 9 -> userData.setExerciseCompleted(Integer.parseInt(line));
                        default -> throw new IOException("Unexpected data in file.");
                    }
                } catch (NumberFormatException e) {
                    throw new IOException("Invalid number format at line " + lineNumber, e);
                }
            }

            if (lineNumber != 9) {
                throw new IOException("Incomplete data: expected 9 lines, found " + lineNumber);
            }
        }
        return userData;
    }

    public void saveUserData(UserData userData,User user) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(user.getName() + "\n");
            writer.write(user.getUserId() + "\n");
            writer.write(user.getDob() + "\n");


            writer.write(userData.getHeight() + "\n");
            writer.write(userData.getWeight() + "\n");
            writer.write(userData.getGoalsCompleted() + "\n");
            writer.write(userData.getCaloriesBurned() + "\n");
            writer.write(userData.getCaloriesTaken() + "\n");
            writer.write(userData.getExerciseCompleted() + "\n");
        }
    }
}
