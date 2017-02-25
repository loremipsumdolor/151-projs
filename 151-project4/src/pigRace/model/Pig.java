package pigRace.model;

import java.util.concurrent.ThreadLocalRandom;

import javafx.scene.paint.Color;

public class Pig {
	private int speed;
	private Color color;
	private String name;
	private final String[] names = {"Mark", "Lincoln", "Jacob", "Travis", "Taylor"};
	private final Color[] colors = {Color.RED, Color.YELLOW, Color.LIMEGREEN, Color.DODGERBLUE, Color.LIGHTPINK};
	
	public Pig() {
		speed = ThreadLocalRandom.current().nextInt(1, 10);
		name = names[ThreadLocalRandom.current().nextInt(0, names.length)];
		color = colors[ThreadLocalRandom.current().nextInt(0, colors.length)];
	}
	
	public int getSpeed() {
		return speed;
	}
	
	public Color getColor() {
		return color;
	}
	
	public String getName() {
		return name;
	}
}
