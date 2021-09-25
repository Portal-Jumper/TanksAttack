package TanksAttack;

import javafx.animation.AnimationTimer;
import javafx.animation.TranslateTransition;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.util.ArrayList;

public class gameController {

    private BooleanProperty wPressed = new SimpleBooleanProperty();
    private BooleanProperty aPressed = new SimpleBooleanProperty();
    private BooleanProperty sPressed = new SimpleBooleanProperty();
    private BooleanProperty dPressed = new SimpleBooleanProperty();
    private BooleanProperty lPressed = new SimpleBooleanProperty();
    private BooleanProperty shotOnce = new SimpleBooleanProperty(true);
    private volatile BooleanProperty timer = new SimpleBooleanProperty(true);

    private BooleanBinding keyPressed = wPressed.or(aPressed).or(sPressed).or(dPressed).or(lPressed);

    private double movementVariable = 1;

    @FXML
    private ImageView tank;

    @FXML
    private ImageView image1, image2, metal3, metal4, metal5, metal6, water1, water2, water3, water4, water5, water6,
            water7, water8, water9, water10, water11, water12, brick1, brick2, brick3, brick4, brick5, brick6, brick7
            , brick8, brick9, brick10;


    @FXML
    private AnchorPane scene;

    ArrayList<ImageView> blocks = new ArrayList<>();
    ArrayList<ImageView> bricks = new ArrayList<>();
    ArrayList<ImageView> bullets = new ArrayList<>();

    private TranslateTransition transition;

    Image brickImage = new Image("TanksAttack/Brick.png");
    Image waterImage = new Image("TanksAttack/Water.png");
    Image metalImage = new Image("TanksAttack/Metal_Plate.png");



    public void initialize() {
        addBlocks();
        addBricks();
        movementSetup();
        shotCooldown.start();
        keyPressed.addListener(((observableValue, aBoolean, t1) -> {
            if (!aBoolean) {
                Atimer.start();
            } else {
                Atimer.stop();
            }
        }));
        collisionBlockTimer.start();
        collisionBrickTimer.start();
    }

    private AnimationTimer Atimer = new AnimationTimer() {
        @Override
        public void handle(long l) {
            if (wPressed.get() && !checkPlayerCollision() && tank.getLayoutY() > 0) {
                tank.setLayoutY(tank.getLayoutY() - movementVariable);
                tank.setRotate(0);
                if (checkPlayerCollision()) {
                    tank.setLayoutY(tank.getLayoutY() + movementVariable);
                }
            }

            if (sPressed.get() && !checkPlayerCollision() && tank.getLayoutY() + tank.getFitHeight() < scene.getPrefHeight()) {
                tank.setLayoutY(tank.getLayoutY() + movementVariable);
                tank.setRotate(180);
                if (checkPlayerCollision()) {
                    tank.setLayoutY(tank.getLayoutY() - movementVariable);
                }
            }

            if (aPressed.get() && !checkPlayerCollision() && tank.getLayoutX() > 0) {
                tank.setLayoutX(tank.getLayoutX() - movementVariable);
                tank.setRotate(270);
                if (checkPlayerCollision()) {
                    tank.setLayoutX(tank.getLayoutX() + movementVariable);
                }
            }

            if (dPressed.get() && !checkPlayerCollision() && tank.getLayoutX() + tank.getFitWidth() < scene.getPrefWidth()) {
                tank.setLayoutX(tank.getLayoutX() + movementVariable);
                tank.setRotate(90);
                if (checkPlayerCollision()) {
                    tank.setLayoutX(tank.getLayoutX() - movementVariable);
                }
            }

            if(lPressed.get() && shotOnce.get() && timer.get()){
                ImageView img = createBullet();
                scene.getChildren().add(img);
                bullets.add(img);
                transition.play();
                shotOnce.set(false);
                timer.set(false);
            }
        }
    };

    Thread shotCooldown = new Thread(() -> {
        while(true) {
//            System.out.println("aaa");
            if (!timer.get()) {
                try {
                    Thread.sleep(700);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                timer.set(true);
            }
        }
    });


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

            if(e.getCode() == KeyCode.L){
                lPressed.set(true);
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

            if(e.getCode() == KeyCode.L){
                lPressed.set(false);
                shotOnce.set(true);
            }
        });
    }

    private boolean checkPlayerCollision() {
        for (ImageView imageView : blocks) {
            if (tank.getBoundsInParent().intersects(imageView.getBoundsInParent()))
                return true;
        }
        return false;
    }

    private AnimationTimer collisionBlockTimer = new AnimationTimer() {
        @Override
        public void handle(long l) {
            for (ImageView imageView : bullets) {
                for (ImageView imageView1 : blocks) {
                    if (imageView.getBoundsInParent().intersects(imageView1.getBoundsInParent())) {
                        if(!bricks.contains(imageView1)) {
                            scene.getChildren().remove(imageView);
                            bullets.remove(imageView);
                        }
                        return;
                    }
                }
            }
        }
    };

    private AnimationTimer collisionBrickTimer = new AnimationTimer() {
        @Override
        public void handle(long l) {
            for (ImageView imageView : bullets) {
                for (ImageView imageView1 : bricks) {
                    if (imageView.getBoundsInParent().intersects(imageView1.getBoundsInParent())) {
                        scene.getChildren().remove(imageView1);
                        scene.getChildren().remove(imageView);
                        blocks.remove(imageView1);
                        bricks.remove(imageView1);
                        bullets.remove(imageView);
                        return;
                    }
                }
            }
        }
    };

    private ImageView createBullet() {
        ImageView img = new ImageView("TanksAttack/bullet.png");
        img.setFitHeight(30);
        img.setFitWidth(10);
        if(tank.getRotate() == 0) {
            img.setLayoutX(tank.getLayoutX() + 20);
            img.setLayoutY(tank.getLayoutY() - 30);
            img.setRotate(0);
            transition = new TranslateTransition(Duration.seconds(2),img);
            transition.setToY(-600);
            transition.setOnFinished(e ->{
                scene.getChildren().remove(img);
                bullets.remove(img);
            });
        }

        if(tank.getRotate() == 90) {
            img.setLayoutX(tank.getLayoutX() + 55);
            img.setLayoutY(tank.getLayoutY() + 10);
            img.setRotate(90);
            transition = new TranslateTransition(Duration.seconds(3),img);
            transition.setToX(1050);
            transition.setOnFinished(e ->{
                scene.getChildren().remove(img);
                bullets.remove(img);
            });
        }

        if(tank.getRotate() == 180) {
            img.setLayoutX(tank.getLayoutX() + 20);
            img.setLayoutY(tank.getLayoutY() + 35);
            img.setRotate(180);
            transition = new TranslateTransition(Duration.seconds(2),img);
            transition.setToY(600);
            transition.setOnFinished(e ->{
                scene.getChildren().remove(img);
                bullets.remove(img);
            });
        }

        if(tank.getRotate() == 270) {
            img.setLayoutX(tank.getLayoutX() - 20);
            img.setLayoutY(tank.getLayoutY() + 10);
            img.setRotate(270);
            transition = new TranslateTransition(Duration.seconds(3),img);
            transition.setToX(-1050);
            transition.setOnFinished(e ->{
                scene.getChildren().remove(img);
                bullets.remove(img);
            });
        }

        return img;
    }


    private void addBricks() {
        bricks.add(brick1);
        bricks.add(brick2);
        bricks.add(brick3);
        bricks.add(brick4);
        bricks.add(brick5);
        bricks.add(brick6);
        bricks.add(brick7);
        bricks.add(brick8);
        bricks.add(brick9);
        bricks.add(brick10);
    }

    private void addBlocks() {
        addToBlocks(image1, image2, metal3, metal4, metal5, metal6, water1, water2, water3);
        addToBlocks(water4, water5, water6, water7, water8, water9, water10, water11, water12);
        addToBlocks(brick1, brick2, brick3, brick4, brick5, brick6, brick7, brick8, brick9);
        blocks.add(brick10);
    }

    private void addToBlocks(ImageView img1, ImageView img2, ImageView img3, ImageView img4, ImageView img5
            , ImageView img6, ImageView img7, ImageView img8, ImageView img9) {
        blocks.add(img1);
        blocks.add(img2);
        blocks.add(img3);
        blocks.add(img4);
        blocks.add(img5);
        blocks.add(img6);
        blocks.add(img7);
        blocks.add(img8);
        blocks.add(img9);
    }
}