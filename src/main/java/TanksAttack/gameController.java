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
import java.util.Random;

public class gameController {

    @FXML
    private AnchorPane scene;

    @FXML
    private ImageView tank, enemyTank1, enemyTank2, enemyTank3, enemyTank4, enemyTank5;

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
            image127, image128, image129, image130,metal1,metal2,metal3,metal4,metal5,metal6;

    private BooleanProperty wPressed = new SimpleBooleanProperty();
    private BooleanProperty aPressed = new SimpleBooleanProperty();
    private BooleanProperty sPressed = new SimpleBooleanProperty();
    private BooleanProperty dPressed = new SimpleBooleanProperty();
    private BooleanProperty lPressed = new SimpleBooleanProperty();
    private BooleanProperty shotOnce = new SimpleBooleanProperty(true);
    private volatile BooleanProperty timer = new SimpleBooleanProperty(true);

    private BooleanBinding keyPressed = wPressed.or(aPressed).or(sPressed).or(dPressed).or(lPressed);

    Image brickImage = new Image("TanksAttack/Brick.png");
    Image waterImage = new Image("TanksAttack/Water.png");
    Image metalImage = new Image("TanksAttack/Metal_Plate.png");

    private ArrayList<ImageView> unassignedBlocks = new ArrayList<>();
    private ArrayList<ImageView> blocks = new ArrayList<>();
    private ArrayList<ImageView> bricks = new ArrayList<>();
    private ArrayList<ImageView> bullets = new ArrayList<>();
    private ArrayList<ImageView> enemyTanks = new ArrayList<>();

    private double movementVariable = 1;

    private TranslateTransition transition;

    private Random random = new Random();

    public void initialize() {
        generateMap();
        movementSetup();

        keyPressed.addListener(((observableValue, aBoolean, t1) -> {
            if (!aBoolean) {
                PlayerATimer.start();
            } else {
                PlayerATimer.stop();
            }
        }));

        collisionTimer.start();
        shotCooldown.start();
    }

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
        });
    }

    private AnimationTimer PlayerATimer = new AnimationTimer() {
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

            if (lPressed.get() && shotOnce.get() && timer.get()) {
                ImageView img = createBullet();
                scene.getChildren().add(img);
                bullets.add(img);
                transition.play();
                shotOnce.set(false);
                timer.set(false);
            }
        }
    };

    private boolean checkPlayerCollision() {
        for (ImageView imageView : blocks) {
            if (tank.getBoundsInParent().intersects(imageView.getBoundsInParent()))
                return true;
        }
        for (ImageView imageView : enemyTanks) {
            if(tank.getBoundsInParent().intersects(imageView.getBoundsInParent()))
                return true;
        }
        return false;
    }

    private Thread shotCooldown = new Thread(() -> {
        while (true) {
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

    private AnimationTimer collisionTimer = new AnimationTimer() {
        @Override
        public void handle(long l) {
            checkBlockBulletCollision();
            checkTankBulletCollision();
        }
    };

    private void checkBlockBulletCollision() {
        for (ImageView imageView : bullets) {
            for (ImageView imageView1 : blocks) {
                if (imageView.getBoundsInParent().intersects(imageView1.getBoundsInParent())) {
                    if (!bricks.contains(imageView1)) {
                        scene.getChildren().remove(imageView);
                        bullets.remove(imageView);
                    } else if (bricks.contains(imageView1)){
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
                if(imageView.getBoundsInParent().intersects((imageView1.getBoundsInParent()))){
                    scene.getChildren().remove(imageView);
                    bullets.remove(imageView);
                    scene.getChildren().remove(imageView1);
                    enemyTanks.remove(imageView1);
                    return;
                }
            }
        }
    }

    private ImageView createBullet() {
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

    private void generateMap() {
        fillEnemyTanks();
        fillUnassignedBlocks();
        for (int i = 0; i < 128; i++) {
            int rand = random.nextInt(3);
            ImageView img = unassignedBlocks.get(i);
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

    private void fillEnemyTanks(){
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
        unassignedBlocks.add(metal1);
        unassignedBlocks.add(metal2);
        unassignedBlocks.add(metal3);
        unassignedBlocks.add(metal4);
        unassignedBlocks.add(metal5);
        unassignedBlocks.add(metal6);
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