package pigRace.gui;

import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import pigRace.model.Pig;

public class PigRaceController {
	@FXML
	private BorderPane borderPane;
	@FXML
	private Ellipse pig1;
	@FXML
	private Ellipse pig2;
	@FXML
	private Ellipse pig3;
	@FXML
	private Button raceButton;
	@FXML
	private Button resetButton;
	@FXML
	private Label score;
	@FXML
	private ChoiceBox<String> pigChoice;
	@FXML
	private Slider betSlider;
	
	private Pig pigOne;
	private Pig pigTwo;
	private Pig pigThree;
	
	private ArrayList<String> finish = new ArrayList<String>();
	
	private long FRAMES_PER_SEC = 30L;
	private long INTERVAL = 1000000000L / FRAMES_PER_SEC;
	
	private AnimationTimer clock = new AnimationTimer() {
		long last = 0;
		@Override
		public void handle(long now) {
			if (now - last > INTERVAL) {
				if (pig1.getTranslateX() + pig1.getRadiusX() * 2 > borderPane.getWidth()) {
					if (!finish.contains("One")) {
						finish.add("One");
					}
				} else {
					pig1.setTranslateX(pig1.getTranslateX() + pigOne.getSpeed());
				}
				if (pig2.getTranslateX() + pig2.getRadiusX() * 2 > borderPane.getWidth()) {
					if (!finish.contains("Two")) {
						finish.add("Two");
					}
				} else {
					pig2.setTranslateX(pig2.getTranslateX() + pigTwo.getSpeed());
				}
				if (pig3.getTranslateX() + pig3.getRadiusX() * 2 > borderPane.getWidth()) {
					if (!finish.contains("Three")) {
						finish.add("Three");
					}
				} else {
					pig3.setTranslateX(pig3.getTranslateX() + pigThree.getSpeed());
				}
				last = now;
				if (finish.size() == 3) {
					gameFinished();
				}
			}
		}	
	};
	
	@FXML
	private void initialize() {
		pigOne = new Pig();
		pigTwo = new Pig();
		pigThree = new Pig();
		pig1.setFill(pigOne.getColor());
		pig2.setFill(pigTwo.getColor());
		pig3.setFill(pigThree.getColor());
		pigChoice.getItems().addAll("One - " + pigOne.getName(), "Two - " + pigTwo.getName(), "Three - " + pigThree.getName());
		pigChoice.getSelectionModel().selectFirst();
	}
	
	@FXML
	private void race() {
		pigChoice.setDisable(true);
		raceButton.setDisable(true);
		resetButton.setDisable(true);
		clock.start();
	}
	
	private void gameFinished() {
		clock.stop();
		if (finish.get(0).equals("One")) {
			pig1.setFill(Color.GOLD);
		} else if (finish.get(0).equals("Two")) {
			pig2.setFill(Color.GOLD);
		} else if (finish.get(0).equals("Three")) {
			pig3.setFill(Color.GOLD);
		}
		if (finish.get(1).equals("One")) {
			pig1.setFill(Color.SILVER);
		} else if (finish.get(1).equals("Two")) {
			pig2.setFill(Color.SILVER);
		} else if (finish.get(1).equals("Three")) {
			pig3.setFill(Color.SILVER);
		}
		if (finish.get(2).equals("One")) {
			pig1.setFill(Color.SANDYBROWN);
		} else if (finish.get(2).equals("Two")) {
			pig2.setFill(Color.SANDYBROWN);
		} else if (finish.get(2).equals("Three")) {
			pig3.setFill(Color.SANDYBROWN);
		}
		String chosenPig = pigChoice.getSelectionModel().getSelectedItem();
		Double bet = betSlider.getValue();
		if (chosenPig.substring(0, 3).equals(finish.get(0).substring(0, 3))) {
			score.setText(Integer.toString(Integer.parseInt(score.getText()) + bet.intValue()));
		} else {
			score.setText(Integer.toString(Integer.parseInt(score.getText()) - bet.intValue()));
		}
		if (Integer.parseInt(score.getText()) < 0) {
			score.setTextFill(Color.RED);
		} else {
			score.setTextFill(Color.BLACK);
		}
		resetButton.setDisable(false);
	}
	
	@FXML
	private void reset() {
		pigOne = new Pig();
		pigTwo = new Pig();
		pigThree = new Pig();
		pig1.setFill(pigOne.getColor());
		pig2.setFill(pigTwo.getColor());
		pig3.setFill(pigThree.getColor());
		pig1.setTranslateX(0);
		pig2.setTranslateX(0);
		pig3.setTranslateX(0);
		raceButton.setDisable(false);
		pigChoice.setDisable(false);
		finish.clear();
		pigChoice.getItems().clear();
		pigChoice.getItems().addAll("One - " + pigOne.getName(), "Two - " + pigTwo.getName(), "Three - " + pigThree.getName());
		pigChoice.getSelectionModel().selectFirst();
	}
}
