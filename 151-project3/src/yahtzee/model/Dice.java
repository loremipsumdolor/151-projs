/*
 * This file copied from the Pig game project example
 * (c) Dr. Mark Goadrich, tweaks by Jacob Turner
 */
package yahtzee.model;

// Idea from http://stackoverflow.com/a/363692
import java.util.concurrent.ThreadLocalRandom;

public class Dice {

	private int sides;
	private int top;
	private boolean hold;
	
	public Dice(int sides, int top) {
		this.sides = sides;
		this.top = top;
		this.hold = false;
	}
	
	public Dice() {
		this(6, 0);
	}
	
	public int getTop() {
		return top;
	}
	
	public boolean getHold() {
		return hold;
	}
	
	public void setTop(int top) {
		if (top >= 0 && top <= sides) {
			this.top = top;
		}
	}
	
	public void setHold() {
		hold = !hold;
	}
	
	public void roll() {
		if (!hold) {
			top = ThreadLocalRandom.current().nextInt(1, sides + 1);
		}
	}
}
