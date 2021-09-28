package TanksAttack;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class LevelSelectionController {

    @FXML
    Button button1, button2, button3, button4, buttonRandom;

    @FXML
    Pane pane2, pane3, pane4;

    @FXML
    Label pointsLabel, difficultyLane1, difficultyLane2, difficultyLane3, difficultyLane4;

    @FXML
    ChoiceBox<String> levelChoiceBox = new ChoiceBox<>();

    public void initialize() {
        App.resizeSmall();
        difficultyEasy();
        levelChoiceBox.setValue("Easy");
        levelChoiceBox.getItems().add("Easy");
        levelChoiceBox.getItems().add("Medium");
        levelChoiceBox.getItems().add("Hard");
        levelChoiceBox.getItems().add("Unplayable");

        pointsLabel.setText("Points gathered so far: " + PlayerData.points);
        button1.setTooltip(new Tooltip("Unlocked"));
        Tooltip tooltip2 = new Tooltip("Points needed to unlock: 1000");
        Tooltip.install(pane2, tooltip2);
        Tooltip tooltip3 = new Tooltip("Points needed to unlock: 2000");
        Tooltip.install(pane3, tooltip3);
        Tooltip tooltip4 = new Tooltip("Points needed to unlock: 3000");
        Tooltip.install(pane4, tooltip4);
        button2.setDisable(true);
        button3.setDisable(true);
        button4.setDisable(true);
        if (PlayerData.points >= 1000) {
            button2.setDisable(false);
            button2.setTooltip(new Tooltip("Unlocked"));
            Tooltip.uninstall(pane2, tooltip2);
        }
        if (PlayerData.points >= 2000) {
            button3.setDisable(false);
            button3.setTooltip(new Tooltip("Unlocked"));
            Tooltip.uninstall(pane3, tooltip3);
        }
        if (PlayerData.points >= 3000) {
            button4.setDisable(false);
            button4.setTooltip(new Tooltip("Unlocked"));
            Tooltip.uninstall(pane4, tooltip4);
        }

        levelChoiceBox.setOnAction(actionEvent -> {
            if (levelChoiceBox.getValue().equals("Easy"))
                difficultyEasy();
            if (levelChoiceBox.getValue().equals("Medium"))
                difficultyMedium();
            if (levelChoiceBox.getValue().equals("Hard"))
                difficultyHard();
            if (levelChoiceBox.getValue().equals("Unplayable"))
                difficultyUnplayable();
        });
    }

    public void level1() throws IOException {
        App.setRoot("level1");
        App.resizeBig();
    }

    public void level2() throws IOException {
        App.setRoot("level2");
        App.resizeBig();
    }

    public void level3() throws IOException {
        App.setRoot("level3");
        App.resizeBig();
    }

    public void level4() throws IOException {
        App.setRoot("level4");
        App.resizeBig();
    }

    public void randomLevel() throws IOException {
        App.setRoot("randomMap");
        App.resizeBig();
    }

    private void difficultyEasy() {
        difficultyLane1.setText("Easy Difficulty:");
        difficultyLane2.setText("Your movement speed: 1.9");
        difficultyLane3.setText("Enemy movement speed: 1");
        difficultyLane4.setText("Enemies shoot every: 1,5sec");
        GameData.movementVariable = 1.9;
        GameData.enemyMovementVariable = 1;
        GameData.enemyShootCooldown = 1500;
    }

    private void difficultyMedium() {
        difficultyLane1.setText("Medium Difficulty:");
        difficultyLane2.setText("Your movement speed: 1.9");
        difficultyLane3.setText("Enemy movement speed: 1.9");
        difficultyLane4.setText("Enemies shoot every: 1sec");
        GameData.movementVariable = 1.9;
        GameData.enemyMovementVariable = 1.9;
        GameData.enemyShootCooldown = 1000;
    }

    private void difficultyHard() {
        difficultyLane1.setText("Hard Difficulty:");
        difficultyLane2.setText("Your movement speed: 1");
        difficultyLane3.setText("Enemy movement speed: 1.9");
        difficultyLane4.setText("Enemies shoot every: 0.7sec");
        GameData.movementVariable = 1;
        GameData.enemyMovementVariable = 1.9;
        GameData.enemyShootCooldown = 700;
    }

    private void difficultyUnplayable() {
        difficultyLane1.setText("Unplayable Difficulty:");
        difficultyLane2.setText("Your movement speed: 0.5");
        difficultyLane3.setText("Enemy movement speed: 1.9");
        difficultyLane4.setText("Enemies shoot every: 0,5sec");
        GameData.movementVariable = 0.5;
        GameData.enemyMovementVariable = 1.9;
        GameData.enemyShootCooldown = 500;
    }
}
