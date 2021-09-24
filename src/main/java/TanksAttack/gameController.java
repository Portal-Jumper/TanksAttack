package TanksAttack;

import javafx.animation.AnimationTimer;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;

public class gameController {

    private BooleanProperty wPressed = new SimpleBooleanProperty();
    private BooleanProperty aPressed = new SimpleBooleanProperty();
    private BooleanProperty sPressed = new SimpleBooleanProperty();
    private BooleanProperty dPressed = new SimpleBooleanProperty();

    private BooleanBinding keyPressed = wPressed.or(aPressed).or(sPressed).or(dPressed);

    private double movementVariable = 0.5;

    @FXML
    private ImageView shape1;

    @FXML
    private Rectangle shape2;

    @FXML
    private Rectangle shape3;

    @FXML
    private Rectangle shape4;

    @FXML
    private Rectangle shape5;

    @FXML
    private Rectangle shape6;

    @FXML
    private Rectangle shape7;

    @FXML
    private Rectangle shape8;

    @FXML
    private AnchorPane scene;

    public void initialize() {
        movementSetup();
        keyPressed.addListener(((observableValue, aBoolean, t1) -> {
            if(!aBoolean){
                timer.start();
            } else {
                timer.stop();
            }
        }));
    }

    private AnimationTimer timer = new AnimationTimer() {
        @Override
        public void handle(long l) {
            if (wPressed.get() && !checkCollision()) {
                shape1.setLayoutY(shape1.getLayoutY() - movementVariable);
                shape1.setRotate(0);
                if (checkCollision()) {
                    shape1.setLayoutY(shape1.getLayoutY() + movementVariable);
                }
            }

            if (sPressed.get() && !checkCollision()) {
                shape1.setLayoutY(shape1.getLayoutY() + movementVariable);
                shape1.setRotate(180);
                if (checkCollision()) {
                    shape1.setLayoutY(shape1.getLayoutY() - movementVariable);
                }
            }

            if (aPressed.get() && !checkCollision()) {
                shape1.setLayoutX(shape1.getLayoutX() - movementVariable);
                shape1.setRotate(270);
                if (checkCollision()) {
                    shape1.setLayoutX(shape1.getLayoutX() + movementVariable);
                }
            }

            if (dPressed.get() && !checkCollision()) {
                shape1.setLayoutX(shape1.getLayoutX() + movementVariable);
                shape1.setRotate(90);
                if (checkCollision()) {
                    shape1.setLayoutX(shape1.getLayoutX() - movementVariable);
                }
            }


        }
    };


    public void movementSetup() {
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.W) {
                wPressed.set(true);
            }

            if (e.getCode() == KeyCode.A) {
                aPressed.set(true);
            }

            if (e.getCode() == KeyCode.S) {
                sPressed.set(true);
            }

            if (e.getCode() == KeyCode.D) {
                dPressed.set(true);
            }
        });

        scene.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.W) {
                wPressed.set(false);
            }

            if (e.getCode() == KeyCode.A) {
                aPressed.set(false);
            }

            if (e.getCode() == KeyCode.S) {
                sPressed.set(false);
            }

            if (e.getCode() == KeyCode.D) {
                dPressed.set(false);
            }
        });
    }

    private boolean checkCollision() {
        boolean b1 = shape1.getBoundsInParent().intersects(shape2.getBoundsInParent());
        boolean b2 = shape1.getBoundsInParent().intersects(shape3.getBoundsInParent());
        boolean b3 = shape1.getBoundsInParent().intersects(shape4.getBoundsInParent());
        boolean b4 = shape1.getBoundsInParent().intersects(shape5.getBoundsInParent());
        boolean b5 = shape1.getBoundsInParent().intersects(shape6.getBoundsInParent());
        boolean b6 = shape1.getBoundsInParent().intersects(shape7.getBoundsInParent());
        boolean b7 = shape1.getBoundsInParent().intersects(shape8.getBoundsInParent());

        return b1 || b2 || b3 || b4 || b5 || b6 || b7;
    }
}