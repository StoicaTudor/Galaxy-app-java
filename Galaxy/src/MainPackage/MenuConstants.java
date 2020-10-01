package MainPackage;

import java.awt.Dimension;
import java.awt.Toolkit;

public interface MenuConstants {

	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	int screenHeight = (int) (screenSize.getHeight() * 1.25);
	int screenWidth = (int) (screenSize.getWidth() * 1.25);

	int comboBoxX = 100;
	int comboBoxY = 100;

	int elementsWidth = 500;
	int elementsHeight = 50;

	int addPlayerButtonX = comboBoxX;
	int addPlayerButtonY = comboBoxY - elementsHeight;

	int exitButtonX = addPlayerButtonX + elementsWidth / 2;
	int exitButtonY = addPlayerButtonY;

	int playButtonX = comboBoxX;
	int playButtonY = addPlayerButtonY - elementsHeight;
	
	int ON_THE_HOUSE_MONSTER=2;
	int ON_THE_HOUSE_INVISIBILITY=2;
	int ON_THE_HOUSE_SHRINKING=2;
	int ON_THE_HOUSE_SPEED_BOOST=2;
}
