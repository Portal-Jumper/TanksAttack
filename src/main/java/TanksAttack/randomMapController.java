package TanksAttack;

import javafx.animation.AnimationTimer;
import javafx.animation.TranslateTransition;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class randomMapController {

    @FXML
    private AnchorPane scene;

    @FXML
    private Label allPointsLabel,runPointsLabel;

    @FXML
    private ImageView tank, enemyTank1, enemyTank2, enemyTank3, enemyTank4, enemyTank5;

    @FXML
    private Rectangle pointsRectangle;

    @FXML
    private ImageView image1, image2, image3, image4, image5, image6, image7, image8, image9, image10, image11,
            image12, image13, image14, image15, image16, image17, image18, image19, image20, image21, image22, image23,
            image24, image25, image26, image27, image28, image29, image30, image31, image32, image33, image34, image35,
            image36, image37, image38, image39, image40, image41, image42, image43, image44, image45, image46, image47,
            image48, image49, image50, image51, image52, image53, image54, image55, image56, image57, image58, image59,
            image60, image61, image62, image63, image64, image65, image66, image67, image68, image69, image70, image71,
            image72, image73, image74, image75, image76, image77, image78, image79, image80, image81, image82, image83,
            image84, image85, image86, image87, image88, image89, image90, image91, image92, image93, image94, image95,
            image96, image97, image98, image99, image100, image101, image102, image103, image104, image105, image106,
            image107, image108, image109, image110, image111, image112, image113, image114, image115, image116,
            image117, image118, image119, image120, image121, image122, image123, image124, image125, image126,
            image127, image128, image129, image130, image131, image132, image133, image134, image135, image136,
            image137, image138, image139, image140, image141, image142, image143, image144, image145, image146,
            image147, image148, image149, image150, image151, image152, image153, image154, image155, image156,
            image157, image158, image159, image160, image161, image162, image163, image164, image165, image166,
            image167, image168, image169, image170, image171, image172, image173, image174, image175, image176,
            image177, image178, image179, image180, image181, image182, image183, image184, image185, image186,
            image187, image188, image189, image190, image191, metal1, metal2, metal3, metal4, metal5, metal6;

    private BooleanProperty wPressed = new SimpleBooleanProperty();
    private BooleanProperty aPressed = new SimpleBooleanProperty();
    private BooleanProperty sPressed = new SimpleBooleanProperty();
    private BooleanProperty dPressed = new SimpleBooleanProperty();
    private BooleanProperty lPressed = new SimpleBooleanProperty();
    private BooleanProperty shotOnce = new SimpleBooleanProperty(true);
    private volatile BooleanProperty playerShotTimer = new SimpleBooleanProperty(true);
    private volatile BooleanProperty enemyShotTimer = new SimpleBooleanProperty(true);
    private volatile BooleanProperty gameOn = new SimpleBooleanProperty(true);
    private volatile BooleanProperty xPressed = new SimpleBooleanProperty();
    private volatile BooleanProperty gameOverLabelDisplayed = new SimpleBooleanProperty(false);

    private BooleanBinding keyPressed = wPressed.or(aPressed).or(sPressed).or(dPressed).or(lPressed).or(xPressed);

    private Image brickImage = new Image("TanksAttack/Brick.png");
    private Image waterImage = new Image("TanksAttack/Water.png");
    private Image metalImage = new Image("TanksAttack/Metal_Plate.png");

    private ArrayList<ImageView> unassignedBlocks = new ArrayList<>();
    private ArrayList<ImageView> blocks = new ArrayList<>();
    private ArrayList<ImageView> bricks = new ArrayList<>();
    private ArrayList<ImageView> bullets = new ArrayList<>();
    private ArrayList<ImageView> enemyBullets = new ArrayList<>();
    private ArrayList<ImageView> enemyTanks = new ArrayList<>();

    private double movementVariable = 1.9;
    private double enemyMovementVariable = 1.9;
    private int enemyShootCooldown = 1000;

    private TranslateTransition transition;

    private Random random = new Random();

    private volatile int tankRandomInt1 = 0;
    private volatile int tankRandomInt2 = 0;
    private volatile int tankRandomInt3 = 0;
    private volatile int tankRandomInt4 = 0;
    private volatile int tankRandomInt5 = 0;

    private int runPoints = 0;


    public void initialize() {

        runPointsLabel.setText("This Run: " + runPoints);
        allPointsLabel.setText("All: " + PlayerData.points);

        generateMap();
        movementSetup();

        keyPressed.addListener(((observableValue, aBoolean, t1) -> {
            if (!aBoolean) {
                PlayerATimer.start();
            } else {
                PlayerATimer.stop();
            }
        }));

        tankTimer.start();
        collisionTimer.start();
        shotCooldown.start();
        enemyMoveTimer.start();
        enemyShotCooldownThread.start();
    }

//    Player Movement && Shooting

    private void movementSetup() {
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

            if (e.getCode() == KeyCode.L) {
                lPressed.set(true);
            }
            if (e.getCode() == KeyCode.X) {
                xPressed.set(true);
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

            if (e.getCode() == KeyCode.L) {
                lPressed.set(false);
                shotOnce.set(true);
            }
            if (e.getCode() == KeyCode.X) {
                xPressed.set(false);
            }
        });
    }

    private AnimationTimer PlayerATimer = new AnimationTimer() {
        @Override
        public void handle(long l) {
            if (gameOn.get()) {
                if (wPressed.get() && !checkPlayerCollision() && tank.getLayoutY() > 0) {
                    tank.setLayoutY(tank.getLayoutY() - movementVariable);
                    tank.setRotate(0);
                    if (checkPlayerCollision()) {
                        tank.setLayoutY(tank.getLayoutY() + movementVariable);
                    }
                }

                if (sPressed.get() && !checkPlayerCollision() &&
                        tank.getLayoutY() + tank.getFitHeight() < scene.getPrefHeight()) {
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

                if (dPressed.get() && !checkPlayerCollision() &&
                        tank.getLayoutX() + tank.getFitWidth() < scene.getPrefWidth()) {
                    tank.setLayoutX(tank.getLayoutX() + movementVariable);
                    tank.setRotate(90);
                    if (checkPlayerCollision()) {
                        tank.setLayoutX(tank.getLayoutX() - movementVariable);
                    }
                }

                if (lPressed.get() && shotOnce.get() && playerShotTimer.get()) {
                    ImageView img = playerShootBullet();
                    scene.getChildren().add(img);
                    bullets.add(img);
                    transition.play();
                    shotOnce.set(false);
                    playerShotTimer.set(false);
                }
            }
            if(!gameOn.get() && !gameOverLabelDisplayed.get()){
                gameOverLabelDisplayed.set(true);
                Label label = new Label("GAME OVER! PRESS X TO CONTINUE");
                label.setLayoutX(17);
                label.setLayoutY(225);
                label.setPrefSize(965,100);
                label.setAlignment(Pos.CENTER);
                label.setFont(new Font(49));
                label.setTextFill(Color.RED);
                scene.getChildren().add(label);
            }
        }
    };

    private boolean checkPlayerCollision() {
        for (ImageView imageView : blocks) {
            if (tank.getBoundsInParent().intersects(imageView.getBoundsInParent()))
                return true;
        }
        for (ImageView imageView : enemyTanks) {
            if (tank.getBoundsInParent().intersects(imageView.getBoundsInParent()))
                return true;
        }
        return tank.getBoundsInParent().intersects(pointsRectangle.getBoundsInParent());
    }

    private Thread shotCooldown = new Thread(() -> {
        while (true) {
            if (!playerShotTimer.get()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                playerShotTimer.set(true);
            }
            if(!gameOn.get() && xPressed.get()) {
                try {
                    App.setRoot("welcomeScreen");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Thread.currentThread().interrupt();
                return;
            }
        }
    });

    private AnimationTimer collisionTimer = new AnimationTimer() {
        @Override
        public void handle(long l) {
            checkBlockBulletCollision(bullets);
            checkBlockBulletCollision(enemyBullets);
            checkTankBulletCollision();
        }
    };

    private String waterUrl = "file:/C:/Users/PortalJumper/IdeaProjects/TanksAttack/target/classes/TanksAttack/Water" +
            ".png";

    private void checkBlockBulletCollision(ArrayList<ImageView> bullets) {
        for (ImageView imageView : bullets) {
            for (ImageView imageView1 : blocks) {
                if (imageView.getBoundsInParent().intersects(imageView1.getBoundsInParent())) {
                    if (!bricks.contains(imageView1) && !imageView1.getImage().getUrl().equals(waterUrl)) {
                        scene.getChildren().remove(imageView);
                        bullets.remove(imageView);
                    } else if (bricks.contains(imageView1)) {
                        scene.getChildren().remove(imageView1);
                        scene.getChildren().remove(imageView);
                        blocks.remove(imageView1);
                        bricks.remove(imageView1);
                        bullets.remove(imageView);
                    }
                    return;
                }
            }
        }
    }

    private void checkTankBulletCollision() {
        for (ImageView imageView : bullets) {
            for (ImageView imageView1 : enemyTanks) {
                if (imageView.getBoundsInParent().intersects((imageView1.getBoundsInParent()))) {
                    imageView1.setLayoutX(randomX());
                    imageView1.setLayoutY(randomY());
                    while (checkEnemyCollision(imageView1)) {
                        imageView1.setLayoutX(randomX());
                        imageView1.setLayoutY(randomY());
                    }
                    scene.getChildren().remove(imageView);
                    bullets.remove(imageView);
                    runPoints += 100;
                    runPointsLabel.setText("This Run: " + runPoints);
                    PlayerData.points += 100;
                    allPointsLabel.setText("All: " + PlayerData.points);
                    return;
                }
            }
        }
        for (ImageView imageView : enemyBullets){
            if(imageView.getBoundsInParent().intersects(tank.getBoundsInParent())){
                scene.getChildren().remove(tank);
                gameOn.set(false);
            }
        }
    }

    private int randomX() {
        int rand = random.nextInt(951);
        if (!(rand % 50 == 0))
            rand = random.nextInt(951);
        return rand;
    }

    private int randomY() {
        int rand = random.nextInt(551);
        if (!(rand % 50 == 0))
            rand = random.nextInt(551);
        return rand;
    }

    private ImageView playerShootBullet() {
        ImageView img = new ImageView("TanksAttack/bullet.png");
        img.setFitHeight(30);
        img.setFitWidth(10);
        if (tank.getRotate() == 0) {
            img.setLayoutX(tank.getLayoutX() + 20);
            img.setLayoutY(tank.getLayoutY() - 30);
            img.setRotate(0);
            transition = new TranslateTransition(Duration.seconds(2), img);
            transition.setToY(-600);
            transition.setOnFinished(e -> {
                scene.getChildren().remove(img);
                bullets.remove(img);
            });
        }

        if (tank.getRotate() == 90) {
            img.setLayoutX(tank.getLayoutX() + 55);
            img.setLayoutY(tank.getLayoutY() + 10);
            img.setRotate(90);
            transition = new TranslateTransition(Duration.seconds(3), img);
            transition.setToX(1050);
            transition.setOnFinished(e -> {
                scene.getChildren().remove(img);
                bullets.remove(img);
            });
        }

        if (tank.getRotate() == 180) {
            img.setLayoutX(tank.getLayoutX() + 20);
            img.setLayoutY(tank.getLayoutY() + 35);
            img.setRotate(180);
            transition = new TranslateTransition(Duration.seconds(2), img);
            transition.setToY(600);
            transition.setOnFinished(e -> {
                scene.getChildren().remove(img);
                bullets.remove(img);
            });
        }

        if (tank.getRotate() == 270) {
            img.setLayoutX(tank.getLayoutX() - 20);
            img.setLayoutY(tank.getLayoutY() + 10);
            img.setRotate(270);
            transition = new TranslateTransition(Duration.seconds(3), img);
            transition.setToX(-1050);
            transition.setOnFinished(e -> {
                scene.getChildren().remove(img);
                bullets.remove(img);
            });
        }

        return img;
    }

//    Enemy Tanks Movement && Shooting

    private AnimationTimer tankTimer = new AnimationTimer() {
        @Override
        public void handle(long l) {
            int counter = 1;
            for (ImageView tank : enemyTanks) {
                if (counter == 1)
                    enemyTankMove(tankRandomInt1, tank);
                if (counter == 2)
                    enemyTankMove(tankRandomInt2, tank);
                if (counter == 3)
                    enemyTankMove(tankRandomInt3, tank);
                if (counter == 4)
                    enemyTankMove(tankRandomInt4, tank);
                if (counter == 5)
                    enemyTankMove(tankRandomInt5, tank);

                if (checkEnemyCollision(tank)) {
                    tank.setLayoutY(tank.getLayoutY() + enemyMovementVariable);
                    if (checkEnemyCollision(tank))
                        tank.setLayoutY(tank.getLayoutY() - (enemyMovementVariable * 2));
                    if (checkEnemyCollision(tank)) {
                        tank.setLayoutY(tank.getLayoutY() + enemyMovementVariable);
                        tank.setLayoutX(tank.getLayoutX() + enemyMovementVariable);
                    }
                    if (checkEnemyCollision(tank))
                        tank.setLayoutX(tank.getLayoutX() - (enemyMovementVariable * 2));
                }

                if(enemyShotTimer.get()) {
                    ImageView img = enemyShootBullet(tank);
                    scene.getChildren().add(img);
                    enemyBullets.add(img);
                    transition.play();
                }
                if(counter == 5)
                    enemyShotTimer.set(false);
                counter++;
            }
        }
    };

    private ImageView enemyShootBullet(ImageView tank) {
        ImageView img = new ImageView("TanksAttack/bullet.png");
        img.setFitHeight(30);
        img.setFitWidth(10);
        if (tank.getRotate() == 0) {
            img.setLayoutX(tank.getLayoutX() + 20);
            img.setLayoutY(tank.getLayoutY() - 30);
            img.setRotate(0);
            transition = new TranslateTransition(Duration.seconds(2), img);
            transition.setToY(-600);
            transition.setOnFinished(e -> {
                scene.getChildren().remove(img);
                enemyBullets.remove(img);
            });
        }

        if (tank.getRotate() == 90) {
            img.setLayoutX(tank.getLayoutX() + 55);
            img.setLayoutY(tank.getLayoutY() + 10);
            img.setRotate(90);
            transition = new TranslateTransition(Duration.seconds(3), img);
            transition.setToX(1050);
            transition.setOnFinished(e -> {
                scene.getChildren().remove(img);
                enemyBullets.remove(img);
            });
        }

        if (tank.getRotate() == 180) {
            img.setLayoutX(tank.getLayoutX() + 20);
            img.setLayoutY(tank.getLayoutY() + 35);
            img.setRotate(180);
            transition = new TranslateTransition(Duration.seconds(2), img);
            transition.setToY(600);
            transition.setOnFinished(e -> {
                scene.getChildren().remove(img);
                enemyBullets.remove(img);
            });
        }

        if (tank.getRotate() == 270) {
            img.setLayoutX(tank.getLayoutX() - 20);
            img.setLayoutY(tank.getLayoutY() + 10);
            img.setRotate(270);
            transition = new TranslateTransition(Duration.seconds(3), img);
            transition.setToX(-1050);
            transition.setOnFinished(e -> {
                scene.getChildren().remove(img);
                enemyBullets.remove(img);
            });
        }
        return img;
    }


    private boolean checkEnemyCollision(ImageView enemyTank) {
        for (ImageView imageView : blocks) {
            if (enemyTank.getBoundsInParent().intersects(imageView.getBoundsInParent()))
                return true;
        }
        if (enemyTank.getLayoutY() <= 0)
            return true;
        if (enemyTank.getLayoutY() + enemyTank.getFitHeight() >= scene.getPrefHeight())
            return true;
        if (enemyTank.getLayoutX() + enemyTank.getLayoutX() <= 0)
            return true;
        if (enemyTank.getLayoutX() + enemyTank.getFitWidth() > scene.getPrefWidth())
            return true;
        ArrayList<ImageView> temp = new ArrayList<>(enemyTanks);
        temp.remove(enemyTank);
        for (ImageView imageView1 : temp) {
            if (enemyTank.getBoundsInParent().intersects(imageView1.getBoundsInParent()))
                return true;
        }
        if(enemyTank.getBoundsInParent().intersects(pointsRectangle.getBoundsInParent()))
            return true;
        return enemyTank.getBoundsInParent().intersects(tank.getBoundsInParent());
    }

    private void enemyTankMove(int tankRandomInt, ImageView tank) {
        if (!checkEnemyCollision(tank)) {
            if (tankRandomInt == 0) {
                tank.setLayoutY(tank.getLayoutY() - enemyMovementVariable);
                tank.setRotate(0);
            }
            if (tankRandomInt == 1) {
                tank.setLayoutY(tank.getLayoutY() + enemyMovementVariable);
                tank.setRotate(180);
            }
            if (tankRandomInt == 2) {
                tank.setLayoutX(tank.getLayoutX() - enemyMovementVariable);
                tank.setRotate(270);
            }
            if (tankRandomInt == 3) {
                tank.setLayoutX(tank.getLayoutX() + enemyMovementVariable);
                tank.setRotate(90);
            }
        }
    }

    private Thread enemyMoveTimer = new Thread(() -> {
        while (true) {
            tankRandomInt1 = random.nextInt(4);
            tankRandomInt2 = random.nextInt(4);
            tankRandomInt3 = random.nextInt(4);
            tankRandomInt4 = random.nextInt(4);
            tankRandomInt5 = random.nextInt(4);
            try {
                Thread.sleep(700);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    });

    private Thread enemyShotCooldownThread = new Thread(() -> {
        while (true) {
            if (!enemyShotTimer.get()) {
                try {
                    Thread.sleep(enemyShootCooldown);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                enemyShotTimer.set(true);
            }
        }
    });


//    Random Map Generation

    private void generateMap() {
        fillEnemyTanks();
        fillUnassignedBlocks();
        blocks.add(metal1);
        blocks.add(metal2);
        blocks.add(metal3);
        blocks.add(metal4);
        blocks.add(metal5);
        blocks.add(metal6);
        for (ImageView img : unassignedBlocks) {
            int rand = random.nextInt(3);
            if (rand == 0) {
                int rand2 = random.nextInt(3);
                if (rand2 == 2) {
                    img.setImage(metalImage);
                    blocks.add(img);
                } else {
                    scene.getChildren().remove(img);
                }
            }
            if (rand == 1) {
                int rand3 = random.nextInt(3);
                if (rand3 == 2) {
                    img.setImage(waterImage);
                    blocks.add(img);
                } else {
                    scene.getChildren().remove(img);
                }
            }
            if (rand == 2) {
                img.setImage(brickImage);
                blocks.add(img);
                bricks.add(img);
            }
        }
    }

    private void fillEnemyTanks() {
        enemyTanks.add(enemyTank1);
        enemyTanks.add(enemyTank2);
        enemyTanks.add(enemyTank3);
        enemyTanks.add(enemyTank4);
        enemyTanks.add(enemyTank5);
    }

    private void fillUnassignedBlocks() {
        addToUnassignedBlocks(image1, image2, image3, image4, image5, image6, image7, image8, image9, image10);
        addToUnassignedBlocks(image11, image12, image13, image14, image15, image16, image17, image18, image19, image20);
        addToUnassignedBlocks(image21, image22, image23, image24, image25, image26, image27, image28, image29, image30);
        addToUnassignedBlocks(image31, image32, image33, image34, image35, image36, image37, image38, image39, image40);
        addToUnassignedBlocks(image41, image42, image43, image44, image45, image46, image47, image48, image49, image50);
        addToUnassignedBlocks(image51, image52, image53, image54, image55, image56, image57, image58, image59, image60);
        addToUnassignedBlocks(image61, image62, image63, image64, image65, image66, image67, image68, image69, image70);
        addToUnassignedBlocks(image71, image72, image73, image74, image75, image76, image77, image78, image79, image80);
        addToUnassignedBlocks(image81, image82, image83, image84, image85, image86, image87, image88, image89, image90);
        addToUnassignedBlocks(image91, image92, image93, image94, image95, image96, image97, image98, image99,
                image100);
        addToUnassignedBlocks(image101, image102, image103, image104, image105, image106, image107, image108,
                image109, image110);
        addToUnassignedBlocks(image111, image112, image113, image114, image115, image116, image117, image118,
                image119, image120);
        addToUnassignedBlocks(image121, image122, image123, image124, image125, image126, image127, image128,
                image129, image130);
        addToUnassignedBlocks(image131, image132, image133, image134, image135, image136,
                image137,
                image138, image139, image140);
        addToUnassignedBlocks(image141, image142, image143, image144, image145, image146, image147,
                image148, image149,
                image150);
        addToUnassignedBlocks(image151, image152, image153, image154, image155, image156, image157, image158, image159,
                image160);
        addToUnassignedBlocks(image161,
                image162, image163, image164, image165, image166, image167, image168, image169, image170);
        addToUnassignedBlocks(image171,
                image172, image173,
                image174, image175, image176, image177, image178, image179, image180);
        addToUnassignedBlocks(image181, image182, image183,
                image184, image185,
                image186, image187, image188, image189, image190);
        unassignedBlocks.add(image191);
    }


    private void addToUnassignedBlocks(ImageView image1, ImageView image2, ImageView image3, ImageView image4,
                                       ImageView image5, ImageView image6, ImageView image7, ImageView image8,
                                       ImageView image9, ImageView image10) {
        unassignedBlocks.add(image1);
        unassignedBlocks.add(image2);
        unassignedBlocks.add(image3);
        unassignedBlocks.add(image4);
        unassignedBlocks.add(image5);
        unassignedBlocks.add(image6);
        unassignedBlocks.add(image7);
        unassignedBlocks.add(image8);
        unassignedBlocks.add(image9);
        unassignedBlocks.add(image10);
    }

}