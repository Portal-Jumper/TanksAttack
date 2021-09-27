package TanksAttack;

import javafx.fxml.FXML;

import java.io.IOException;

public class LoginController {

    @FXML
    private void logIn() throws IOException {
        App.setRoot("levelSelection");
    }

    @FXML
    private void click() throws IOException {
        App.setRoot("register");
    }

}
