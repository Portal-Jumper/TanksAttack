package TanksAttack;

import javafx.animation.AnimationTimer;
import javafx.animation.TranslateTransition;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class level1Controller {

	@FXML
	private AnchorPane scene;

	@FXML
	private Label allPointsLabel, runPointsLabel;

	@FXML
	private ImageView tank, enemyTank1, enemyTank2, enemyTank3, enemyTank4, enemyTank5;

	@FXML
	private Rectangle pointsRectangle;

	@FXML
	private ImageView image1, image2, image3, image4, image5, image6, image7, image8, image9, image10, image11, image12,
			image13, image14, image15, image16, image17, image18, image19, image20, image21, image22, image23, image24,
			image25, image26, image27, image28, image29, image30, image31, image32, image33, image34, image35, image36,
			image37, image38, image39, image40, image41, image42, image43, image44, image45, image46, image47, image48,
			image49, image50, image51, image52, image53, image54, image55, image56, metal1, metal2, metal3, metal4,
			metal5, metal6, metal7, metal8, metal9, metal10, metal11;

	private BooleanProperty wPressed = new SimpleBooleanProperty();
	private BooleanProperty aPressed = new SimpleBooleanProperty();
	private BooleanProperty sPressed = new SimpleBooleanProperty();
	private BooleanProperty dPressed = new SimpleBooleanProperty();
	private BooleanProperty lPressed = new SimpleBooleanProperty();
	private BooleanProperty shotOnce = new SimpleBooleanProperty(true);
	private volatile BooleanProperty playerShotTimer = new SimpleBooleanProperty(true);
	private volatile BooleanProperty enemyShotTimer = new SimpleBooleanProperty(true);
	private volatile BooleanProperty gameOn = new SimpleBooleanProperty(true);
	private volatile BooleanProperty gameOverAlertDisplayed = new SimpleBooleanProperty(false);

	private BooleanBinding keyPressed = wPressed.or(aPressed).or(sPressed).or(dPressed).or(lPressed);

	private Image brickImage = new Image("TanksAttack/Brick.png");
	private Image waterImage = new Image("TanksAttack/Water.png");
	

	private ArrayList<ImageView> blocks = new ArrayList<>();
	private ArrayList<ImageView> bricks = new ArrayList<>();
	private ArrayList<ImageView> bullets = new ArrayList<>();
	private ArrayList<ImageView> enemyBullets = new ArrayList<>();
	private ArrayList<ImageView> enemyTanks = new ArrayList<>();

	private double movementVariable = GameData.movementVariable;
	private double enemyMovementVariable = GameData.enemyMovementVariable;
	private int enemyShootCooldown = GameData.enemyShootCooldown;

	private TranslateTransition transition;

	private Random random = new Random();

	private volatile int tankRandomInt1 = 0;
	private volatile int tankRandomInt2 = 0;
	private volatile int tankRandomInt3 = 0;
	private volatile int tankRandomInt4 = 0;
	private volatile int tankRandomInt5 = 0;

	private int runPoints = 0;

	public void initialize() {

		runPointsLabel.setText("Game: " + runPoints);
		allPointsLabel.setText("All: " + PlayerData.points);

		generateMap();
		keyAssigment();

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

	private void keyAssigment() {
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
			
			if(e.getCode() == KeyCode.ESCAPE) {
				try {
					App.setRoot("levelSelection");
				} catch (IOException e1) {
					
					e1.printStackTrace();
				}
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
			if (gameOn.get()) {
				if (wPressed.get() && !checkPlayerCollision() && tank.getLayoutY() > 0) {
					tank.setLayoutY(tank.getLayoutY() - movementVariable);
					tank.setRotate(0);
					if (checkPlayerCollision()) {
						tank.setLayoutY(tank.getLayoutY() + movementVariable);
					}
				}

				if (sPressed.get() && !checkPlayerCollision()
						&& tank.getLayoutY() + tank.getFitHeight() < scene.getPrefHeight()) {
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

				if (dPressed.get() && !checkPlayerCollision()
						&& tank.getLayoutX() + tank.getFitWidth() < scene.getPrefWidth()) {
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
			if (!gameOn.get() && !gameOverAlertDisplayed.get()) {
				gameOverAlertDisplayed.set(true);
				Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
				alert.setTitle(null);
				alert.setHeaderText("GAME OVER!");
				alert.setResizable(false);
				alert.setContentText("You earned " + runPoints + " points this run!");
				alert.setOnHidden(evt -> {
					try {
						App.setRoot("levelSelection");
					} catch (IOException e) {
						e.printStackTrace();
					}
				});
				alert.show();
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

	private void checkBlockBulletCollision(ArrayList<ImageView> bullets) {
		for (ImageView imageView : bullets) {
			for (ImageView imageView1 : blocks) {
				if (imageView.getBoundsInParent().intersects(imageView1.getBoundsInParent())) {
					if (!bricks.contains(imageView1) && !imageView1.getImage().equals(waterImage)) {
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
					runPointsLabel.setText("Game: " + runPoints);
					PlayerData.points += 100;
					allPointsLabel.setText("All: " + PlayerData.points);
					return;
				}
			}
		}
		for (ImageView imageView : enemyBullets) {
			if (imageView.getBoundsInParent().intersects(tank.getBoundsInParent())) {
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

				if (enemyShotTimer.get()) {
					ImageView img = enemyShootBullet(tank);
					scene.getChildren().add(img);
					enemyBullets.add(img);
					transition.play();
				}
				if (counter == 5)
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
		if (enemyTank.getBoundsInParent().intersects(pointsRectangle.getBoundsInParent()))
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

//     Map Generation

	private void generateMap() {
		fillEnemyTanks();
		addToBricks(image1, image2, image3, image4, image5, image6, image7, image8, image9, image10, blocks, bricks, brickImage);
		addToBricks(image11, image12, image13, image14, image15, image16, image17, image18, image19, image20, blocks, bricks, brickImage);
		addToBricks(image21, image22, image23, image24, image25, image26, image27, image28, image29, image30, blocks, bricks, brickImage);
		addToBricks(image31, image32, image33, image34, image35, image36, image37, image38, image39, image40, blocks, bricks, brickImage);
		setWater(image41, image42, image43, image44, blocks, waterImage);
		setWater(image45, image46, image47, image48, blocks, waterImage);
		setWater(image49, image50, image51, image52, blocks, waterImage);
		setWater(image53, image54, image55, image56, blocks, waterImage);
		blocks.add(metal1);
		blocks.add(metal2);
		blocks.add(metal3);
		blocks.add(metal4);
		blocks.add(metal5);
		blocks.add(metal6);
		blocks.add(metal7);
		blocks.add(metal8);
		blocks.add(metal9);
		blocks.add(metal10);
		blocks.add(metal11);

	}

	private void fillEnemyTanks() {
		enemyTanks.add(enemyTank1);
		enemyTanks.add(enemyTank2);
		enemyTanks.add(enemyTank3);
		enemyTanks.add(enemyTank4);
		enemyTanks.add(enemyTank5);
	}

	static void addToBricks(ImageView image1, ImageView image2, ImageView image3, ImageView image4, ImageView image5,
			ImageView image6, ImageView image7, ImageView image8, ImageView image9, ImageView image10, ArrayList<ImageView> blocks,
			ArrayList<ImageView> bricks, Image brickImage) {
		bricks.add(image1);
		bricks.add(image2);
		bricks.add(image3);
		bricks.add(image4);
		bricks.add(image5);
		bricks.add(image6);
		bricks.add(image7);
		bricks.add(image8);
		bricks.add(image9);
		bricks.add(image10);
		blocks.add(image1);
		blocks.add(image2);
		blocks.add(image3);
		blocks.add(image4);
		blocks.add(image5);
		blocks.add(image6);
		blocks.add(image7);
		blocks.add(image8);
		blocks.add(image9);
		blocks.add(image10);
		image1.setImage(brickImage);
		image2.setImage(brickImage);
		image3.setImage(brickImage);
		image4.setImage(brickImage);
		image5.setImage(brickImage);
		image6.setImage(brickImage);
		image7.setImage(brickImage);
		image8.setImage(brickImage);
		image9.setImage(brickImage);
		image10.setImage(brickImage);
	}

	static void setWater(ImageView image1, ImageView image2, ImageView image3, ImageView image4,
			ArrayList<ImageView> blocks, Image waterImage) {
		blocks.add(image1);
		blocks.add(image2);
		blocks.add(image3);
		blocks.add(image4);

		image1.setImage(waterImage);
		image2.setImage(waterImage);
		image3.setImage(waterImage);
		image4.setImage(waterImage);

	}

}