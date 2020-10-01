package MainPackage;

import static java.awt.Frame.MAXIMIZED_BOTH;

import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.Timer;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main extends JPanel implements Constants {

	JFrame mainFrame;
	Drawing drawingObj;
	JsonProcesser jsonProcesser;
	Menu menuObj;

	Timer gameTimer = new Timer(DEFAULT_TIMER_REFRESH_RATE, new ActionListener() {

		public int createNewRockSignal = 0;
		public int incrementGameSpeedSignal = 0;

		public void actionPerformed(ActionEvent ev) {

			repaint();
			drawingObj.moveRocks(gameSpeed);

			if (createNewRockSignal == 0) {
				drawingObj.createNewRock();
			}

			createNewRockSignal++;
			incrementGameSpeedSignal++;

			if (createNewRockSignal == 5) {
				createNewRockSignal = 0;
			}

			if (incrementGameSpeedSignal == 60) {
				gameSpeed++;
			}
			
			drawingObj.checkCollision();
		}
	});

	public Main() {
		initialize();
		solveLoggingIn();
		// gameTimer.start();
	}

	private void solveLoggingIn() {
		this.jsonProcesser = new JsonProcesser();
		if (!this.jsonProcesser.fileExists()) {
			this.jsonProcesser.createFile();
		} else {
			this.jsonProcesser.retrievePlayerDetails();
		}

		this.menuObj = new Menu(this.jsonProcesser.getPlayersDetails(), this.gameTimer, this.mainFrame,
				this.jsonProcesser);

	}

	private int ballXCoord = stickManXPosition;
	private int ballYCoord = stickManYPosition;
	private int gameSpeed = STARTING_GAME_SPEED;

	private void initialize() {
		// mainPanel = new JPanel();
		setLayout(null);
		setBounds(0, 0, (int) screenWidth, (int) screenHeight);
		mainFrame = new JFrame("Galaxy");
		mainFrame.add(this);
		mainFrame.setExtendedState(MAXIMIZED_BOTH);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		// mainFrame.getContentPane().setLayout(null);
		mainFrame.getContentPane().add(this);

		mainFrame.setResizable(true);
		mainFrame.pack();

		mainFrame.setVisible(false);
		drawingObj = new Drawing(this, new Point(this.ballXCoord, this.ballYCoord), gameTimer); // upon opening the app,
																								// create a Drawing
																								// object
		mainFrame.addKeyListener(new KeyListener() {

			public void keyPressed(KeyEvent keyPressed) {

				keyPressedEvent(keyPressed);

				if (pressedEnterAndEndOfGame(keyPressed)) {
					reinitializeDrawingObject();

					if (jsonProcesser
							.getPlayersDetails()[menuObj.currentPlayerId].highScore < RocksCoordinatesUtil.score) {
						jsonProcesser
								.getPlayersDetails()[menuObj.currentPlayerId].highScore = RocksCoordinatesUtil.score;
						jsonProcesser.transformPlayerDetailsIntoJsonArray(jsonProcesser.getPlayersDetails());
						menuObj.refreshPlayerScoreInComboBox();

					}

					gameSpeed = STARTING_GAME_SPEED;
					RocksCoordinatesUtil.score = 0;
					drawingObj.endOfGame = false;
					gameTimer.start();
					// drawingObj.disableAllRocks(); // depracated method
					drawingObj.destroyAllRocks();

				} else if (pressedEsc(keyPressed)) {
					try {
						reinitializeDrawingObject();
						if (jsonProcesser
								.getPlayersDetails()[menuObj.currentPlayerId].highScore < RocksCoordinatesUtil.score) {
							jsonProcesser
									.getPlayersDetails()[menuObj.currentPlayerId].highScore = RocksCoordinatesUtil.score;
							jsonProcesser.transformPlayerDetailsIntoJsonArray(jsonProcesser.getPlayersDetails());
							menuObj.refreshPlayerScoreInComboBox();

						}
					} catch (Exception ex) {

					}

					gameSpeed = STARTING_GAME_SPEED;
					RocksCoordinatesUtil.score = 0;
					drawingObj.endOfGame = false;
					drawingObj.destroyAllRocks();

					gameTimer.stop(); // we stop the timer
					returnToMenu(); // and return to main menu
				}

				else if (pressed1(keyPressed)
						&& jsonProcesser.getPlayersDetails()[menuObj.currentPlayerId].getMonster() > 0) {
					jsonProcesser.getPlayersDetails()[menuObj.currentPlayerId].decrementMonster();
					drawingObj.setMonster(true);
				}

				else if (pressed2(keyPressed)
						&& jsonProcesser.getPlayersDetails()[menuObj.currentPlayerId].getInvisibility() > 0) {
					jsonProcesser.getPlayersDetails()[menuObj.currentPlayerId].decrementInvisibility();
					drawingObj.setInvisibility(true);
				}

				else if (pressed3(keyPressed)
						&& jsonProcesser.getPlayersDetails()[menuObj.currentPlayerId].getShrinking() > 0) {
					jsonProcesser.getPlayersDetails()[menuObj.currentPlayerId].decrementShrinking();
					drawingObj.setShrinking(true);
				}

				else if (pressed4(keyPressed)
						&& jsonProcesser.getPlayersDetails()[menuObj.currentPlayerId].getSpeedBoost() > 0) {
					jsonProcesser.getPlayersDetails()[menuObj.currentPlayerId].decrementSpeedBoost();
					drawingObj.setSpeedBoost(true);
				}
			}

			public void keyReleased(KeyEvent arg0) {
				// unimplemented
			}

			public void keyTyped(KeyEvent keyPressed) {
				// unimplemented
			}
		});
	}

	protected void reinitializeDrawingObject() { // this is imperial, because we do not want to load the previous game
													// session, upon opening another game (monster, speed boost etc)
		drawingObj = new Drawing(this, new Point(this.ballXCoord, this.ballYCoord), gameTimer);
	}

	protected void returnToMenu() {
		menuObj.setFrameVisible();
	}

	protected boolean pressedEnterAndEndOfGame(KeyEvent keyPressed) {

		return (keyPressed.getKeyCode() == KeyEvent.VK_ENTER && this.drawingObj.endOfGame);
	}

	protected boolean pressedEsc(KeyEvent keyPressed) {

		return (keyPressed.getKeyCode() == KeyEvent.VK_ESCAPE);
	}

	protected boolean pressed1(KeyEvent keyPressed) {

		return (keyPressed.getKeyCode() == KeyEvent.VK_1);
	}

	protected boolean pressed2(KeyEvent keyPressed) {

		return (keyPressed.getKeyCode() == KeyEvent.VK_2);
	}

	protected boolean pressed3(KeyEvent keyPressed) {

		return (keyPressed.getKeyCode() == KeyEvent.VK_3);
	}

	protected boolean pressed4(KeyEvent keyPressed) {

		return (keyPressed.getKeyCode() == KeyEvent.VK_4);
	}

	public void paint(Graphics g) {

		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		this.drawingObj.drawBackground(g2);

		this.drawingObj.drawEverything(g2);

		this.drawingObj.drawScore(g2);

//		try {
//			this.drawingObj.drawUtilities(g2, this.playersDetails[menuObj.currentPlayerId].getMonster(),
//					this.playersDetails[menuObj.currentPlayerId].getInvisibility(),
//					this.playersDetails[menuObj.currentPlayerId].getShrinking(),
//					this.playersDetails[menuObj.currentPlayerId].getSpeedBoost());
//		} catch (Exception ex) {
//
//		}

		this.drawingObj.drawUtilities(g2, this.jsonProcesser.getPlayersDetails()[menuObj.currentPlayerId].getMonster(),
				this.jsonProcesser.getPlayersDetails()[menuObj.currentPlayerId].getInvisibility(),
				this.jsonProcesser.getPlayersDetails()[menuObj.currentPlayerId].getShrinking(),
				this.jsonProcesser.getPlayersDetails()[menuObj.currentPlayerId].getSpeedBoost());

		if (this.drawingObj.endOfGame) {
			this.drawingObj.drawBigScore(g2);
		}
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new Main();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void keyPressedEvent(KeyEvent keyPressed) {

		int keyCode = keyPressed.getKeyCode();

		switch (keyCode) {
		case KeyEvent.VK_UP:
			drawingObj.goUp();
			break;
		case KeyEvent.VK_DOWN:
			drawingObj.goDown();
			break;
		case KeyEvent.VK_LEFT:
			// handle left
			break;
		case KeyEvent.VK_RIGHT:
			// handle right

		}
	}
}