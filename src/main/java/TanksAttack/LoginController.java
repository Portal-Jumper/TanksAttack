package TanksAttack;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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
        if (DatabaseConnection.LogIn(usernameField.getText().toLowerCase(), passwordField.getText())) {
            PlayerData.points = DatabaseConnection.getPointsFromDB(usernameField.getText().toLowerCase());
            App.setRoot("levelSelection");
            PlayerData.login = usernameField.getText().toLowerCase();
        }

        if (!DatabaseConnection.LogIn(usernameField.getText().toLowerCase(), passwordField.getText())) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(null);
            alert.setHeaderText("Invalid login or password");
            alert.setResizable(false);
            alert.setContentText(null);
            alert.show();
        }
    }

    @FXML
    private void click() throws IOException {
        App.setRoot("register");
    }

}
