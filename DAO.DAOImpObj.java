package DAO;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import model.User;


public class DAOImpObj {
    private static DAOImpObj instance;
    private final ArrayList<User> users;

    private final String FILE_PATH = "src/main/data/User";

    private DAOImpObj() {
        users = new ArrayList<>();
        ensureFilePathExists();
        loadUsersFromFile(FILE_PATH);
    }

    public static synchronized DAOImpObj getInstance() {
        if (instance == null) {
            instance = new DAOImpObj();
        }
        return instance;
    }

    private void ensureFilePathExists() {
        File file = new File(FILE_PATH);
        File directory = file.getParentFile();
        if (!directory.exists()) {
            if (directory.mkdirs()) {
                System.out.println("Directory created: " + directory.getPath());
            } else {
                System.err.println("Failed to create directory: " + directory.getPath());
            }
        }
    }

    public void addUser(User user) {
        if (searchByUsername(user.getUsername()) == null) {
            users.add(user);
            saveUsersToFile(FILE_PATH);
        } else {
            System.out.println("User already exists!");
        }
    }

    public User searchByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public User searchByUserId(String userId) {
        for (User user : users) {
            if (user.getUserId().equals(userId)) {
                return user;
            }
        }
        return null;
    }

    public void updateUser(User updatedUser) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUserId().equals(updatedUser.getUserId())) {
                users.set(i, updatedUser);
                saveUsersToFile(FILE_PATH);
                break;
            }
        }
    }

    public void saveUsersToFile(String filePath) {
        ensureFilePathExists();

        // Remove duplicate users by user ID
        Set<String> userIds = new HashSet<>();
        List<User> uniqueUsers = new ArrayList<>();

        for (User user : users) {
            if (userIds.add(user.getUserId())) {
                uniqueUsers.add(user); // Add only if the ID is unique
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false))) {
            for (User user : uniqueUsers) {
                writer.write(user.getUserId() + "," + user.getName() + "," + user.getUsername() + "," +
                        user.getPassword() + "," + user.getDob() + "," + user.getHeight() + "," + user.getWeight());
                writer.newLine();
            }
            System.out.println("Users saved to file successfully.");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void loadUsersFromFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("User data file not found. Starting with an empty list.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 7) {
                    String userId = parts[0];
                    String name = parts[1];
                    String username = parts[2];
                    String password = parts[3];
                    String dob = parts[4];
                    double height = Double.parseDouble(parts[5]);
                    double weight = Double.parseDouble(parts[6]);
                    users.add(new User(userId, name, username, password, dob, height, weight));
                } else {
                    System.out.println("Skipping invalid user data line: " + line);
                }
            }
            System.out.println("Users loaded from file successfully.");
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error reading from file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
