package MainPackage;

public class PlayerDetails {

	public static int lastId;
	public String playerName = "";
	public int highScore = 0;
	private int playerId = 0;

	private int invisibility = 0, speedBoost = 0, shrinking = 0, monster = 0;

	public PlayerDetails(String playerName, int highScore, int playerId, int invisibility, int speedBoost,
			int shrinking, int monster) {

		this.playerName = playerName;
		this.highScore = highScore;
		this.playerId = playerId;
		this.invisibility = invisibility;
		this.speedBoost = speedBoost;
		this.shrinking = shrinking;
		this.monster = monster;
	}

	public int getInvisibility() {
		return this.invisibility;
	}

	public int getSpeedBoost() {
		return this.speedBoost;
	}

	public int getShrinking() {
		return this.shrinking;
	}

	public int getMonster() {
		return this.monster;
	}

	public int getId() {
		return this.playerId;
	}

	public void decrementMonster() {
		this.monster--;
	}

	public void decrementInvisibility() {
		this.invisibility--;
	}

	public void decrementShrinking() {
		this.shrinking--;
	}

	public void decrementSpeedBoost() {
		this.speedBoost--;
	}

}
