package MainPackage;

import java.awt.Point;
import java.util.Random;

public class RocksCoordinates implements Constants {

	RocksCoordinatesUtil[] rock = new RocksCoordinatesUtil[MAX_NR_ROCKS_AT_ONCE];
	int currentNrRocks = 0;
	private Random rand = new Random();

	public RocksCoordinates() {

	}

	public void createNewRock() {

		boolean foundDisabledRock = false;

		Point newRockInitialCoordinates = new Point(SW, rand.nextInt(SH - DEFAULT_BALL_SIZE * 2));

		if (SH - DEFAULT_BALL_SIZE * 2 < 0) {
			newRockInitialCoordinates = new Point(SW, 0);
		}

		for (int i = 0; i < currentNrRocks; i++) {
			if (rock[i].enabled == false) {

				rock[i] = new RocksCoordinatesUtil(newRockInitialCoordinates.x, newRockInitialCoordinates.y);
				foundDisabledRock = true;
			}
		}

		if (foundDisabledRock == false && currentNrRocks < MAX_NR_ROCKS_AT_ONCE) {
			rock[currentNrRocks++] = new RocksCoordinatesUtil(newRockInitialCoordinates.x, newRockInitialCoordinates.y);
		}

		/// efficiency: i don't create new rocks, unless all created rocks are enabled
	}

	public void moveRocks(int gameSpeed) {
		for (int i = 0; i < this.currentNrRocks; i++) {
			if (this.rock[i].enabled) {
				this.rock[i].moveRockToLeft(gameSpeed);
			}
		}
	}

	public boolean iterateCollisions(Point mainBallCoordinates, int mainBallSize) {

		for (int i = 0; i < currentNrRocks; i++) {
			if (rock[i].enabled == true) {
				if (isCollision(mainBallCoordinates, rock[i].getRockCoordinates(), mainBallSize)) {

					if (mainBallSize == MONSTER_BALL_SIZE) {
						this.destroyCollidedRock(i);
						return false;
					}

					return true;
				}
			}
		}
		return false;
	}

	private void destroyCollidedRock(int collidedRockIndex) {
		this.currentNrRocks--;
		this.rock[collidedRockIndex] = new RocksCoordinatesUtil();
		RocksCoordinatesUtil.score++; // increase the score
	}

	private boolean isCollision(Point mainBallCoordinates, Point rockCoordinates, int mainBallSize) {

		int mainBallRadius = getBallRadius(mainBallSize);

		Point mainBallCenter = new Point(mainBallCoordinates.x + mainBallRadius,
				mainBallCoordinates.y + mainBallRadius);
		Point rockCenter = new Point(rockCoordinates.x + DEFAULT_BALL_RADIUS, rockCoordinates.y + DEFAULT_BALL_RADIUS);

		float distanceBetweenCenters = (float) Math.hypot(mainBallCenter.x - rockCenter.x,
				mainBallCenter.y - rockCenter.y);

		boolean isCollision = distanceBetweenCenters < DEFAULT_BALL_RADIUS + mainBallRadius;

		return isCollision;
	}

	private int getBallRadius(int mainBallSize) {
		return mainBallSize / 2;
	}
}

class RocksCoordinatesUtil {

	private Point coordinates;
	public boolean enabled;
	public static int score = 0;

	public RocksCoordinatesUtil(int x, int y) {
		this.coordinates = new Point(x, y);
		this.enabled = true;
	}

	public RocksCoordinatesUtil() {

	}

	public void disablePoint() {
		this.enabled = false;
	}

	public Point getRockCoordinates() {
		return new Point(coordinates.x, coordinates.y);
	}

	public void moveRockToLeft(int nrPixels) {
		if (coordinates.x < 0) { // disable dissapearing rocks
			this.disablePoint();
			score++;
		}
		coordinates.x -= nrPixels;
	}
}

//public void resolveCollision(Ball ball)
//{
//    // get the mtd
//    Vector2d delta = (position.subtract(ball.position));
//    float d = delta.getLength();
//    // minimum translation distance to push balls apart after intersecting
//    Vector2d mtd = delta.multiply(((getRadius() + ball.getRadius())-d)/d); 
//
//
//    // resolve intersection --
//    // inverse mass quantities
//    float im1 = 1 / getMass(); 
//    float im2 = 1 / ball.getMass();
//
//    // push-pull them apart based off their mass
//    position = position.add(mtd.multiply(im1 / (im1 + im2)));
//    ball.position = ball.position.subtract(mtd.multiply(im2 / (im1 + im2)));
//
//    // impact speed
//    Vector2d v = (this.velocity.subtract(ball.velocity));
//    float vn = v.dot(mtd.normalize());
//
//    // sphere intersecting but moving away from each other already
//    if (vn > 0.0f) return;
//
//    // collision impulse
//    float i = (-(1.0f + Constants.restitution) * vn) / (im1 + im2);
//    Vector2d impulse = mtd.normalize().multiply(i);
//
//    // change in momentum
//    this.velocity = this.velocity.add(impulse.multiply(im1));
//    ball.velocity = ball.velocity.subtract(impulse.multiply(im2));
//
//}