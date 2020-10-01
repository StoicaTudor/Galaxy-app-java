package MainPackage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JsonProcesser {

	private boolean fileExists;
	private PlayerDetails[] playerDetails;
	private JSONArray playersJsonArray;

	public JsonProcesser() {
		File file = new File("Resources\\players.txt");
		this.fileExists = file.exists();
	}

	public boolean fileExists() {
		return fileExists;
	}

	@SuppressWarnings("unchecked")
	public void transformPlayerDetailsIntoJsonArray(PlayerDetails[] playerDetails) {

		this.playerDetails = playerDetails;
		JSONObject[] playerDetailsObj = new JSONObject[playerDetails.length];
		JSONArray playersList = new JSONArray();

		for (int i = 0; i < playerDetails.length; i++) {
			playerDetailsObj[i] = new JSONObject();
			playerDetailsObj[i].put("Id", playerDetails[i].getId());
			playerDetailsObj[i].put("Name", playerDetails[i].playerName);
			playerDetailsObj[i].put("High Score", playerDetails[i].highScore);
			playerDetailsObj[i].put("Monster", playerDetails[i].getMonster());
			playerDetailsObj[i].put("Invisibility", playerDetails[i].getInvisibility());
			playerDetailsObj[i].put("Shrinking", playerDetails[i].getShrinking());
			playerDetailsObj[i].put("Speed boost", playerDetails[i].getSpeedBoost());

			playersList.add(playerDetailsObj[i]);
		}

		JSONObject mainObj = new JSONObject();
		mainObj.put("players", playersList);

		try {

			FileWriter myWriter = new FileWriter("Resources\\players.txt");
			myWriter.write(mainObj.toJSONString());
			myWriter.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String readJsonAndLoadIntoString() {
		try {
			String jsonString = new String(Files.readAllBytes(Paths.get("Resources\\players.txt")));
			return jsonString;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("rawtypes")
	private void retrievePlayersJsonArray() {
		try {
			Object jsonObject = new JSONParser().parse(readJsonAndLoadIntoString());
			JSONObject jsonObjMapped = new JSONObject((Map) jsonObject);
			JSONArray playersArray = (JSONArray) jsonObjMapped.get("players");

			this.playersJsonArray = playersArray;

		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public void retrievePlayerDetails() {
		retrievePlayersJsonArray();

		this.playerDetails = new PlayerDetails[this.playersJsonArray.size()];
		PlayerDetails.lastId = this.playersJsonArray.size();

		for (int i = 0; i < this.playersJsonArray.size(); i++) {

			Object entity = this.playersJsonArray.get(i);
			@SuppressWarnings("rawtypes")
			JSONObject playerObj = new JSONObject((Map) entity);

			int Id = (int) ((long) playerObj.get("Id"));
			String name = (String) playerObj.get("Name");
			int highScore = (int) ((long) playerObj.get("High Score"));

			int monster = (int) ((long) playerObj.get("Monster"));
			int invisibility = (int) ((long) playerObj.get("Invisibility"));
			int shrinking = (int) ((long) playerObj.get("Shrinking"));
			int speedBoost = (int) ((long) playerObj.get("Speed boost"));

			this.playerDetails[i] = new PlayerDetails(name, highScore, Id, invisibility, speedBoost, shrinking,
					monster);
		}
	}

	public void createFile() {

		File file = new File("Resources\\players.txt");
		try {
			file.createNewFile();
			this.playerDetails = null;
			PlayerDetails.lastId = 0;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public PlayerDetails[] getPlayersDetails() {
		return this.playerDetails;
	}

}
