package MainPackage;

import static java.awt.Frame.MAXIMIZED_BOTH;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Menu implements MenuConstants {

	private PlayerDetails[] playersDetails = null;
	private JFrame mainFrame;
	private JPanel mainPanel;
	@SuppressWarnings("rawtypes")
	private JComboBox comboBox;
	private JButton addPlayerButton;
	private JButton exitGameButton;
	private JButton playButton;

	private Timer gameTimer;
	private JFrame gameFrame;
	private JsonProcesser jsonProcesser;
	public int currentPlayerId;

	Menu(PlayerDetails[] playersDetails, Timer gameTimer, JFrame gameFrame, JsonProcesser jsonProcesser) {
		this.playersDetails = playersDetails;
		this.gameTimer = gameTimer;
		this.gameFrame = gameFrame;
		this.jsonProcesser = jsonProcesser;
		initialize();
	}

	private void initialize() {

		this.mainPanel = new JPanel();
		this.mainPanel.setLayout(null);
		this.mainPanel.setBounds(0, 0, (int) screenWidth, (int) screenHeight);
		this.mainPanel.setBackground(Color.BLACK);

		mainFrame = new JFrame("Menu");
		mainFrame.add(this.mainPanel);
		mainFrame.getContentPane().setLayout(null);
		mainFrame.getContentPane().add(this.mainPanel);
		mainFrame.setUndecorated(true);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setExtendedState(MAXIMIZED_BOTH);
		mainFrame.pack();
		mainFrame.setVisible(true);

		setGifBackground();
		setComboBox();
		setAddPlayerButton();
		setExitGameButton();
		setPlayButton();
	}

	private void setAddPlayerButton() {
		this.addPlayerButton = new JButton("Add player");
		this.addPlayerButton.setBounds(addPlayerButtonX, addPlayerButtonY, elementsWidth / 2, elementsHeight);
		this.addPlayerButton.setForeground(Color.GRAY);
		this.addPlayerButton.setBackground(Color.black);

		this.addPlayerButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				addAddPlayerButtonListener();
			}
		});

		this.mainPanel.add(this.addPlayerButton);
	}

	@SuppressWarnings("unchecked")
	protected void addAddPlayerButtonListener() {
		String playerName = JOptionPane.showInputDialog(this.mainFrame, "Enter Name");

		if (playerName != null) { // if the user has succesfully introduced his name, load it into the combo box
									// with the model name | 0 -> highscore

			StringBuilder comboBoxInfo = new StringBuilder(playerName); // use string builder for this purpose
			comboBoxInfo.append(" | ");
			comboBoxInfo.append(0);

			this.comboBox.addItem(comboBoxInfo.toString()); // load it into the combo box

			loadNewPlayerIntoPlayerDetails(playerName);
		}
	}

	@SuppressWarnings("unchecked")
	public void refreshPlayerScoreInComboBox() {

		this.comboBox.removeAllItems(); // remove all combo box items
		for (int i = 0; i < this.playersDetails.length; i++) { // iterate all items again and place them in the combo
																// box (but now, refreshed with the new high score)
			StringBuilder comboBoxInfo = new StringBuilder(this.playersDetails[i].playerName);
			comboBoxInfo.append(" | ");
			comboBoxInfo.append(this.playersDetails[i].highScore);

			this.comboBox.addItem(comboBoxInfo.toString()); // load it into the combo box
		}
	}

	private void loadNewPlayerIntoPlayerDetails(String playerName) {

		PlayerDetails[] playersDetailsTemp;

		try {
			playersDetailsTemp = new PlayerDetails[this.playersDetails.length + 1]; // declare a temp
																					// obj

			for (int i = 0; i < this.playersDetails.length; i++) { // load every player into the temp obj
				playersDetailsTemp[i] = this.playersDetails[i];
			}

			// finally, load the new player in the temp obj
			playersDetailsTemp[this.playersDetails.length] = new PlayerDetails(playerName, 0, PlayerDetails.lastId++,
					ON_THE_HOUSE_MONSTER, ON_THE_HOUSE_INVISIBILITY, ON_THE_HOUSE_SHRINKING, ON_THE_HOUSE_SPEED_BOOST);

		} catch (NullPointerException ex) {
			playersDetailsTemp = new PlayerDetails[1]; // if this.playersDetails is null, we consider that its length is
														// 0

			playersDetailsTemp[0] = new PlayerDetails(playerName, 0, PlayerDetails.lastId++, ON_THE_HOUSE_MONSTER,
					ON_THE_HOUSE_INVISIBILITY, ON_THE_HOUSE_SHRINKING, ON_THE_HOUSE_SPEED_BOOST);
		}

		this.playersDetails = playersDetailsTemp;
	}

	private void setGifBackground() {

		Icon imgIcon = new ImageIcon("Resources\\aaa.gif");
		JLabel label = new JLabel(imgIcon);
		label.setBounds(0, 0, screenWidth, screenHeight);

		this.mainPanel.add(label);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void setComboBox() {
		this.comboBox = new JComboBox();
		this.comboBox.setBounds(comboBoxX, comboBoxY, elementsWidth, elementsHeight);
		this.comboBox.setBackground(Color.black);
		this.comboBox.setFont(new Font("Times new roman", Font.BOLD, 20));
		this.comboBox.setForeground(Color.GRAY);

		try { // if no player is registered, this.playersDetails is going to be null, so it
				// will throw NullPointerException
			for (int i = 0; i < this.playersDetails.length; i++) {

				StringBuilder entry = new StringBuilder(this.playersDetails[i].playerName);
				entry.append(" | ");
				entry.append(this.playersDetails[i].highScore);

				this.comboBox.addItem(entry.toString());
			}
		} catch (NullPointerException ex) {
			// we simply add nothing to the combo box
		}

		this.mainPanel.add(this.comboBox);
	}

	private void setExitGameButton() {
		this.exitGameButton = new JButton("Exit");
		this.exitGameButton.setBounds(exitButtonX, exitButtonY, elementsWidth / 2, elementsHeight);
		this.exitGameButton.setForeground(Color.GRAY);
		this.exitGameButton.setBackground(Color.black);

		this.exitGameButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		this.mainPanel.add(this.exitGameButton);
	}

	private void setPlayButton() {
		this.playButton = new JButton("Play");
		this.playButton.setBounds(playButtonX, playButtonY, elementsWidth, elementsHeight);
		this.playButton.setForeground(Color.GRAY);
		this.playButton.setBackground(Color.black);

		this.playButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (playersDetails != null) {
					mainFrame.setVisible(false); // close the menu frame
					gameFrame.setVisible(true); // open the game frame
					gameTimer.start(); // enable the timer
					jsonProcesser.transformPlayerDetailsIntoJsonArray(playersDetails);
					currentPlayerId = comboBox.getSelectedIndex();
				} else {
					JOptionPane.showMessageDialog(null, "Select a player first!");
				}
			}
		});

		this.mainPanel.add(this.playButton);
	}

	public void setFrameVisible() {
		this.mainFrame.setVisible(true);
	}
}
