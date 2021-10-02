package TanksAttack;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.w3c.dom.css.CSSStyleDeclaration;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegisterController {

    @FXML
    TextField usernameField,emailField;

    @FXML
    PasswordField passwordField,confirmPasswordField;

    @FXML
    private void register() throws IOException, SQLException, ClassNotFoundException {

        boolean usernameFree = true;
        String username = usernameField.getText().toLowerCase();

        ResultSet rs = DatabaseConnection.returnUsernames();
        while (rs.next())
            if(rs.getString("Login").equals(username))
                usernameFree = false;

        if(usernameFree && passwordField.getText().equals(confirmPasswordField.getText()))
            if(emailField.getText().contains("@"))
                DatabaseConnection.addUser(username,passwordField.getText(),emailField.getText());

        App.setRoot("login");
    }

}
