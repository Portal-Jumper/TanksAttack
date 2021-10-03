package TanksAttack;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;


/**
 * JavaFX App
 */
public class App extends Application {

    private static Stage stage;
    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;
        Image image = new Image("TanksAttack/Icon.png");
        stage.getIcons().add(image);
        stage.setTitle("Tanks Attack");
        stage.setResizable(false);
        scene = new Scene(loadFXML("login"));
        stage.setScene(scene);
        stage.show();

    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    static void resizeBig() {
        stage.setHeight(637);
        stage.setWidth(1014);
    }

    static void resizeSmall() {
        stage.setHeight(437);
        stage.setWidth(614);
    }

    public static void main(String[] args) {
        launch();
    }

}