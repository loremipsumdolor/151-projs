package yahtzee.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import yahtzee.model.Game;
import yahtzee.model.Player;

public class YahtzeeController {
	// Labels
	@FXML
	private Label score;
	@FXML
	private Label aces;
	@FXML
	private Label twos;
	@FXML
	private Label threes;
	@FXML
	private Label fours;
	@FXML
	private Label fives;
	@FXML
	private Label sixes;
	@FXML
	private Label bonus;
	@FXML
	private Label threeOfAKind;
	@FXML
	private Label fourOfAKind;
	@FXML
	private Label fullHouse;
	@FXML
	private Label smallStraight;
	@FXML
	private Label largeStraight;
	@FXML
	private Label yahtzee;
	@FXML
	private Label chance;
	@FXML
	private Label upperTotal;
	@FXML
	private Label lowerTotal;
	@FXML
	private Label grandTotal;
	@FXML
	private Label p1Score;
	@FXML
	private Label p2Score;
	
	// ImageViews
	@FXML
	private ImageView dieOne;
	@FXML
	private ImageView dieTwo;
	@FXML
	private ImageView dieThree;
	@FXML
	private ImageView dieFour;
	@FXML
	private ImageView dieFive;
	
	// BorderPanes
	@FXML
	private BorderPane borderDieOne;
	@FXML
	private BorderPane borderDieTwo;
	@FXML
	private BorderPane borderDieThree;
	@FXML
	private BorderPane borderDieFour;
	@FXML
	private BorderPane borderDieFive;
	
	// Buttons
	@FXML
	private Button rollButton;
	@FXML
	private Button nextButton;
	
	// Other
	private Game yahtzeeGame;
	// Idea from https://community.oracle.com/message/10407712#10407712 and http://stackoverflow.com/a/30774654
	private final String cssBorderOn = "-fx-border-color:red;\n-fx-border-radius:5;\n-fx-border-width:5.0;";
	private final String cssBorderOff = "-fx-border-color:none;\n-fx-border-radius:0;\n-fx-border-width:0.0;";
	
	@FXML
	public void initialize() {
		yahtzeeGame = new Game(new Player("Player 1"), new Player("Player 2"));
		Image image = new Image(getClass().getResourceAsStream("Dice0.png"));
		dieOne.setImage(image);
		dieTwo.setImage(image);
		dieThree.setImage(image);
		dieFour.setImage(image);
		dieFive.setImage(image);
	}
	
	@FXML
	public void setDieOneHold() {
		if (yahtzeeGame.getHold(0)) {
			yahtzeeGame.setHold(0);
			borderDieOne.setStyle(cssBorderOff);
		} else {
			yahtzeeGame.setHold(0);
			borderDieOne.setStyle(cssBorderOn);
		}
	}
	
	@FXML
	public void setDieTwoHold() {
		if (yahtzeeGame.getHold(1)) {
			yahtzeeGame.setHold(1);
			borderDieTwo.setStyle(cssBorderOff);
		} else {
			yahtzeeGame.setHold(1);
			borderDieTwo.setStyle(cssBorderOn);
		}
	}
	
	@FXML
	public void setDieThreeHold() {
		if (yahtzeeGame.getHold(2)) {
			yahtzeeGame.setHold(2);
			borderDieThree.setStyle(cssBorderOff);
		} else {
			yahtzeeGame.setHold(2);
			borderDieThree.setStyle(cssBorderOn);
		}
	}
	
	@FXML
	public void setDieFourHold() {
		if (yahtzeeGame.getHold(3)) {
			yahtzeeGame.setHold(3);
			borderDieFour.setStyle(cssBorderOff);
		} else {
			yahtzeeGame.setHold(3);
			borderDieFour.setStyle(cssBorderOn);
		}
	}
	
	@FXML
	public void setDieFiveHold() {
		if (yahtzeeGame.getHold(4)) {
			yahtzeeGame.setHold(4);
			borderDieFive.setStyle(cssBorderOff);
		} else {
			yahtzeeGame.setHold(4);
			borderDieFive.setStyle(cssBorderOn);
		}
	}
	
	@FXML
	public void rollDice() {
		boolean roll = yahtzeeGame.roll();
		if (roll) {
			int[] finalScore = yahtzeeGame.endOfRound();
			updateScore(finalScore);
			rollButton.setDisable(true);
			if (!yahtzeeGame.gameOver()) {
				nextButton.setDisable(false);
			}
		}
		updateViews();
	}
	
	@FXML
	public void nextGame() {
		rollButton.setDisable(false);
		nextButton.setDisable(true);
		yahtzeeGame.switchTurn();
		score.setText(Integer.toString(yahtzeeGame.getScore()));
		aces.setText("0");
		twos.setText("0");
		threes.setText("0");
		fours.setText("0");
		fives.setText("0");
		sixes.setText("0");
		bonus.setText("0");
		threeOfAKind.setText("0");
		fourOfAKind.setText("0");
		fullHouse.setText("0");
		smallStraight.setText("0");
		largeStraight.setText("0");
		yahtzee.setText("0");
		chance.setText("0");
		upperTotal.setText("0");
		lowerTotal.setText("0");
		grandTotal.setText("0");
	}
	
	private void updateViews() {
		int[] diceTops = yahtzeeGame.getDice();
		Image image = new Image(getClass().getResourceAsStream("Dice" + diceTops[0] + ".png"));
		dieOne.setImage(image);
		image = new Image(getClass().getResourceAsStream("Dice" + diceTops[1] + ".png"));
		dieTwo.setImage(image);
		image = new Image(getClass().getResourceAsStream("Dice" + diceTops[2] + ".png"));
		dieThree.setImage(image);
		image = new Image(getClass().getResourceAsStream("Dice" + diceTops[3] + ".png"));
		dieFour.setImage(image);
		image = new Image(getClass().getResourceAsStream("Dice" + diceTops[4] + ".png"));
		dieFive.setImage(image);
		p1Score.setText(Integer.toString(yahtzeeGame.getP1Score()));
		p2Score.setText(Integer.toString(yahtzeeGame.getP2Score()));
	}
	
	private void updateScore(int[] roundScore) {
		int lowerScore = 0;
		int upperScore = 0;
		aces.setText(Integer.toString(roundScore[0]));
		twos.setText(Integer.toString(roundScore[1]));
		threes.setText(Integer.toString(roundScore[2]));
		fours.setText(Integer.toString(roundScore[3]));
		fives.setText(Integer.toString(roundScore[4]));
		sixes.setText(Integer.toString(roundScore[5]));
		bonus.setText(Integer.toString(roundScore[6]));
		threeOfAKind.setText(Integer.toString(roundScore[7]));
		fourOfAKind.setText(Integer.toString(roundScore[8]));
		fullHouse.setText(Integer.toString(roundScore[9]));
		smallStraight.setText(Integer.toString(roundScore[10]));
		largeStraight.setText(Integer.toString(roundScore[11]));
		yahtzee.setText(Integer.toString(roundScore[12]));
		chance.setText(Integer.toString(roundScore[13]));
		for (int i = 0; i < 6; i++) {
			upperScore += roundScore[i];
		}
		upperTotal.setText(Integer.toString(upperScore));
		for (int i = 6; i < roundScore.length; i++) {
			lowerScore += roundScore[i];
		}
		lowerTotal.setText(Integer.toString(lowerScore));
		grandTotal.setText(Integer.toString(lowerScore + upperScore));
		score.setText(Integer.toString(yahtzeeGame.getScore() + Integer.parseInt(grandTotal.getText())));
		yahtzeeGame.setScore(Integer.parseInt(score.getText()));
	}
}
