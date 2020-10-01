package MainPackage;

import java.awt.Dimension;
import java.awt.Toolkit;

public interface Constants {
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	int screenHeight = (int) (screenSize.getHeight() * 1.25);
	int screenWidth = (int) (screenSize.getWidth() * 1.25);

	int SH = (int) screenSize.getHeight();
	int SW = (int) screenSize.getWidth();

	int gridImageWidth = 1080, gridImageHeight = 1080;
	int stickManWidth = 200;
	int stickManHeight = 300;

	int stickManXPosition = 100;
	int stickManYPosition = screenHeight - 500; // -700

	int MAX_NR_ROCKS_AT_ONCE = 25;
	int DEFAULT_BALL_SIZE = 75;
	int DEFAULT_BALL_RADIUS = DEFAULT_BALL_SIZE / 2;
	int STARTING_GAME_SPEED = 15;
	int DEFAULT_MAIN_BALL_SPEED = 10;
	int MAIN_BALL_SPEED_BOOST = 40;
	int DEFAULT_TIMER_REFRESH_RATE = 10;
	int MONSTER_BALL_SIZE = 200;
	int SHRINKING_BALL_SIZE = 20;
	int MINUS_INFINITY = -((1 << 31) - 1);

	int END_OF_GAME_STRING_SIZE = 150;

	int MAX_NR_NEW_PLAYERS_PER_SESSION = 10;

	int FIVE_SECOND_IN_MS = 5000;
}
