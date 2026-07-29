package Main;

import javafx.application.Application;
import javafx.stage.Stage;
import DAO.DAOImpObj;
import SceneManager.SceneManager;
import java.io.File;

public class Main extends Application {

    private final DAOImpObj dao = DAOImpObj.getInstance();

    @Override
    public void start(Stage primaryStage) {
        ensureDirectoryExists("src/main/data");
        dao.loadUsersFromFile("src/main/data/User");
        SceneManager.setStage(primaryStage);
        SceneManager.loadScene("/View/Login.fxml");
        primaryStage.setTitle("Fitness Tracker");

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }


    public static void showLogin() throws Exception {
        SceneManager.loadScene("/View/Login.fxml");
    }

    public static void showForgotPassword() throws Exception {
        SceneManager.loadScene("/View/ForgotPassword.fxml");
    }

    public static void showResetPassword() throws Exception {
        SceneManager.loadScene("/View/ResetPassword.fxml");
    }

    public static void showRegister() throws Exception {
        SceneManager.loadScene("/View/Register.fxml");
    }

    public static void showHome() throws Exception {
        SceneManager.loadScene("/View/HomePage.fxml");
    }

    public void ensureDirectoryExists(String path) {
        File directory = new File(path);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }
}
