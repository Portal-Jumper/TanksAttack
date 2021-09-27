package TanksAttack;

import javafx.fxml.FXML;

import java.io.IOException;

public class RegisterController {

    @FXML
    private void register() throws IOException {
        App.setRoot("login");
    }

}
