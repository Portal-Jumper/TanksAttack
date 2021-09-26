package TanksAttack;

import java.io.IOException;
import javafx.fxml.FXML;

public class welcomeScreenController {

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("randomMap");
        App.resize();
    }
}
