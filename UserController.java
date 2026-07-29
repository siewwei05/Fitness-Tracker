package Controller;

import java.util.Random;
import DAO.DAOImpObj;
import model.User;
import Session.UserSession;

public class UserController {

    private final DAOImpObj dao = DAOImpObj.getInstance();


    public String registerUser(String name, String username, String password, String dob, double height, double weight) {
        // Generate a unique user ID
        String userId = generateUniqueRandomId();
        // Create a new user object
        User user = new User(userId, name, username, password, dob, height, weight);
        if (dao.searchByUsername(username) == null) {
            // If the user doesn't exist, add the user and save to file
            dao.addUser(user);
            System.out.println("Creating user: " + name + ", " + username + ", ID: " + userId);
            UserSession.setCurrentUser(user);
            return userId;
        } else {
            System.out.println("User already exists!");
            return null;  // Return null if user already exists
        }
    }

    // Method to generate a unique 6-digit random number as a String
    private String generateUniqueRandomId() {
        Random random = new Random();
        String userId;
        do {
            int randomId = 100000 + random.nextInt(900000); // Generates a number between 100000 and 999999
            userId = String.valueOf(randomId);
        } while (!isUserIdUnique(userId)); // Check if the ID is unique
        return userId;
    }

    // Method to check if a user ID is unique
    private boolean isUserIdUnique(String userId) {
        return dao.searchByUserId(userId) == null; // Use the DAO to check if the ID already exists
    }


    public boolean login(String username, String password) {
        User user = dao.searchByUsername(username);
        if (user != null) {
            System.out.println("Found user during login: " + user.getUsername());
            if (user.getPassword().equals(password)) {
                System.out.println("Password matched for user: " + user.getUsername());
                UserSession.setCurrentUserId(user.getUserId());
                UserSession.setCurrentUser(user);
                return true;
            }
            System.out.println("Password mismatch for user: " + user.getUsername());
        } else {
            System.out.println("No user found with username: " + username);
        }
        return false;
    }

    public boolean verifyUser(String username, String userId) {
        User user = dao.searchByUsername(username);
        if (user != null && user.getUserId().equals(userId)) {
            System.out.println("User verified for forgot password: " + user.getUsername());
            return true;
        }
        System.out.println("User verification failed: username=" + username + ", userId=" + userId);
        return false;
    }

    public boolean forgotPassword(String username, String userId) {
        User user = dao.searchByUsername(username);
        if (user != null && user.getUserId().equals(userId)) {
            return true;
        }
        return false;
    }

    public boolean resetPassword(String userId, String newPassword) {
        User user = dao.searchByUserId(userId);
        if (user != null) {
            user.setPassword(newPassword);
            dao.updateUser(user);
            return true;
        }
        return false;
    }
}
