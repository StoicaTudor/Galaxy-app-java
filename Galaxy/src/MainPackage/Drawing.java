package MainPackage;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Timer;

public class Drawing implements Constants {

	private Main mainObj;
	private ImageFilter filter = new RGBImageFilter() {
		int transparentColor = Color.white.getRGB() | 0xFF000000;

		public final int filterRGB(int x, int y, int rgb) {
			if ((rgb | 0xFF000000) == transparentColor) {
				return 0x00FFFFFF & rgb;
			} else {
				return rgb;
			}
		}
	};
	private Point mainBallCoord;
	private Timer timer;
	public boolean endOfGame = false;
	private int mainBallSpeed = DEFAULT_MAIN_BALL_SPEED;

	private RocksCoordinates rocksCoordinates = new RocksCoordinates();

	public Drawing(Main mainObj, Point mainBallCoord, Timer timer) {
		this.mainObj = mainObj;
		this.mainBallCoord = mainBallCoord;
		this.timer = timer;
	}

	public void drawBackground(Graphics2D g) {
		BufferedImage bfImg = null;

		try {
			bfImg = ImageIO.read(getClass().getResourceAsStream("/grid.jpg"));
		} catch (IOException ex) {
			System.out.println(ex);
		}

		g.drawImage(bfImg, 0, 0, screenWidth, screenHeight, null);
	}

	public void drawStickMan(Graphics2D g, int stickManImageNr) {

		BufferedImage bfImg = null;

		try {
			bfImg = ImageIO.read(getClass().getResourceAsStream(getStickmanPath(stickManImageNr)));
		} catch (IOException ex) {
			System.out.println(ex);
		}

		g.drawImage(getTransparentImage(bfImg), stickManXPosition, stickManYPosition, this.mainObj);

		// g.drawImage(bfImg, 300, 300, null);
	}

	private Image getTransparentImage(BufferedImage bfImg) {
		ImageProducer filteredImgProd = new FilteredImageSource(bfImg.getSource(), filter);
		Image transparentImg = Toolkit.getDefaultToolkit().createImage(filteredImgProd);

		return transparentImg;
	}

	private String getStickmanPath(int imageNr) {

		StringBuilder path = new StringBuilder("/stick");
		path.append(imageNr);
		path.append(".png");

		return path.toString();
	}

	private void drawMyBall(Graphics2D g, int xCoord, int yCoord, boolean isMainBall) {
		if (isMainBall) {
			if (this.invisibilityForm) {
				g.setColor(Color.GRAY);
			} else {
				g.setColor(Color.BLACK);
			}

			if (this.monsterForm) {
				g.fillOval(xCoord, yCoord, MONSTER_BALL_SIZE, MONSTER_BALL_SIZE);
			} else if (this.shrinkingForm) {
				g.fillOval(xCoord, yCoord, SHRINKING_BALL_SIZE, SHRINKING_BALL_SIZE);
			} else {
				g.fillOval(xCoord, yCoord, DEFAULT_BALL_SIZE, DEFAULT_BALL_SIZE);
			}
		} else { // if it is an obstacle
			g.setColor(Color.ORANGE);
			g.fillOval(xCoord, yCoord, DEFAULT_BALL_SIZE, DEFAULT_BALL_SIZE);
		}
	}

//	jocul sa
//	fie mai smooth,timer 5.
//	sa dureze
//	doar o
//	perioada scurta
//	de timp
//	abilitatile si
//	sa iti
//	arate cate
//	secunde mai ai

	public void drawEverything(Graphics2D g) {

		drawMyBall(g, mainBallCoord.x, mainBallCoord.y, true); /// draw main ball

		for (int i = 0; i < rocksCoordinates.currentNrRocks; i++) {
			if (rocksCoordinates.rock[i].enabled) {

				Point coordinates = rocksCoordinates.rock[i].getRockCoordinates();
				drawMyBall(g, coordinates.x, coordinates.y, false);
			}
		}
	}

	public void drawScore(Graphics2D g) {

		g.setColor(Color.BLACK);
		g.setFont(new Font("TimesRoman", Font.PLAIN, 30));
		StringBuilder scoreString = new StringBuilder("Score: ");
		scoreString.append(RocksCoordinatesUtil.score);

		g.drawString(scoreString.toString(), 10, 20); // draw score at 10 10 coordinates
	}

	public void drawBigScore(Graphics2D g) {

		g.setColor(Color.BLACK);
		g.setFont(new Font("TimesRoman", Font.PLAIN, END_OF_GAME_STRING_SIZE));
		StringBuilder scoreString = new StringBuilder("You Lost. Score: ");
		scoreString.append(RocksCoordinatesUtil.score);

		g.drawString(scoreString.toString(), 0, screenHeight / 2 - END_OF_GAME_STRING_SIZE);
	}

	public void moveRocks(int gameSpeed) {
		this.rocksCoordinates.moveRocks(gameSpeed);
	}

	public void createNewRock() {
		rocksCoordinates.createNewRock();
	}

	public void goUp() {
		if (mainBallCoord.y - mainBallSpeed >= 0) {
			mainBallCoord = new Point(mainBallCoord.x, mainBallCoord.y - mainBallSpeed);
		}
	}

	public void goDown() {
		if (mainBallCoord.y + mainBallSpeed <= SH - 120) {
			mainBallCoord = new Point(mainBallCoord.x, mainBallCoord.y + mainBallSpeed);
		}
	}

	public void checkCollision() {
		if (rocksCoordinates.iterateCollisions(mainBallCoord, this.getMainBallSize())) {
			endOfGameScreen();
		}
	}

	private void endOfGameScreen() {
		// everything stops and a big you lost score appears
		timer.stop();
		this.endOfGame = true;
	}

	@Deprecated
	public void disableAllRocks() {
		for (int i = 0; i < rocksCoordinates.currentNrRocks; i++) {
			if (rocksCoordinates.rock[i].enabled) {
				rocksCoordinates.rock[i].disablePoint();
			}
		}
	}

	public void destroyAllRocks() {
		rocksCoordinates.currentNrRocks = 0;
		rocksCoordinates.rock = new RocksCoordinatesUtil[MAX_NR_ROCKS_AT_ONCE];
	}

	public void drawUtilities(Graphics2D g, int monster, int invisibility, int shrinking, int speedBoost) {

		g.setColor(Color.BLACK);
		g.setFont(new Font("TimesRoman", Font.PLAIN, 30));

		StringBuilder utilityString = new StringBuilder();

		StringBuilder monsterString = new StringBuilder("-1- Monster: ");
		monsterString.append(monster);

		StringBuilder invisibilityString = new StringBuilder("   -2- Invisibility: ");
		invisibilityString.append(invisibility);

		StringBuilder shrinkingString = new StringBuilder("   -3- Shrinking: ");
		shrinkingString.append(shrinking);

		StringBuilder speedBoostString = new StringBuilder("   -4- Speed Boost: ");
		speedBoostString.append(speedBoost);

		utilityString.append(monsterString);
		utilityString.append("|");
		utilityString.append(invisibilityString);
		utilityString.append("|");
		utilityString.append(shrinkingString);
		utilityString.append("|");
		utilityString.append(speedBoostString);

		g.drawString(utilityString.toString(), 200, 20); // draw score at 200 20 coordinates
	}

	private boolean monsterForm = false, invisibilityForm = false, shrinkingForm = false, speedBoost = false;

	public void setMonster(boolean value) {
		this.monsterForm = value;

		if (value == true) {
			this.setShrinking(false); // if monster is enabled, disable shrinking
			abilitiesTimer.start();
		}
	}

	public void setInvisibility(boolean value) {
		this.invisibilityForm = value;

		if (value) {
			abilitiesTimer.start();
		}
	}

	public void setShrinking(boolean value) {
		this.shrinkingForm = value;

		if (value == true) {
			this.setMonster(false);// if shrinking is enabled, disable monster
			abilitiesTimer.start();
		}
	}

	public void setSpeedBoost(boolean value) {
		this.speedBoost = value;

		if (value) {
			this.mainBallSpeed = MAIN_BALL_SPEED_BOOST;
			abilitiesTimer.start();
		} else {
			this.mainBallSpeed = DEFAULT_MAIN_BALL_SPEED;
		}
	}

	private int getMainBallSize() {
		if (this.invisibilityForm) {
			return MINUS_INFINITY; // in this way, if the ball is invisible, collisions are impossible
		} else {
			if (this.monsterForm) {
				return MONSTER_BALL_SIZE;
			} else if (this.shrinkingForm) {
				return SHRINKING_BALL_SIZE;
			} else {
				return DEFAULT_BALL_SIZE;
			}
		}
	}

	private Timer abilitiesTimer = new Timer(FIVE_SECOND_IN_MS, new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			setInvisibility(false);
			setMonster(false);
			setShrinking(false);
			setSpeedBoost(false);

			abilitiesTimer.stop(); // after the abilities time has passed, stop the timer
		}
	});
}
