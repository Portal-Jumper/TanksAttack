package TanksAttack;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController {

    @FXML
    TextField usernameField;

    @FXML
    PasswordField passwordField;

    @FXML
    private void logIn() throws IOException, SQLException, ClassNotFoundException {
        if(DatabaseConnection.LogIn(usernameField.getText().toLowerCase(),passwordField.getText()))
        App.setRoot("levelSelection");
        PlayerData.points = DatabaseConnection.getPointsFromDB(usernameField.getText().toLowerCase());
    }

    @FXML
    private void click() throws IOException {
        App.setRoot("register");
    }

}
