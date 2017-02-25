package yahtzee.model;

import yahtzee.model.Dice;
import yahtzee.model.Player;

public class Game {
	
	private Dice[] d;
	private Player p1;
	private Player p2;
	private Player current;
	
	public Game(Player p1, Player p2) {
		d = new Dice[5];
		for (int i = 0; i < d.length; i++) {
			d[i] = new Dice();
		}
		this.p1 = p1;
		this.p2 = p2;
		current = p1;
	}
	
	public int[] getDice() {
		int[] diceTops = new int[5];
		for (int i = 0; i < d.length; i++) {
			diceTops[i] = d[i].getTop();
		}
		return diceTops;
	}
	
	public Player getP1() {
		return p1;
	}
	
	public Player getP2() {
		return p2;
	}
	
	public Player getCurrent() {
		return current;
	}	
	
	public void switchTurn() {
		if (current == p1) {
			current = p2;
			d = new Dice[5];
			for (int i = 0; i < d.length; i++) {
				d[i] = new Dice();
			}
		} else {
			current = p1;
			d = new Dice[5];
			for (int i = 0; i < d.length; i++) {
				d[i] = new Dice();
			}
		}
	}
	
	public boolean gameOver() {
		return p2.getRound() >= 6;
	}
	
    public boolean roll() {
    	if (current.getRolls() != 3) {
			for (int i = 0; i <= d.length - 1; i++) {
				boolean isDieHeld = d[i].getHold();
				if (!isDieHeld) {
					d[i].roll();
				}
			}
			current.addRoll();
		}
		if (current.getRolls() == 3) {
			return true;
		} else {
			return false;
		}
    }
    
    public boolean getHold(int die) {
    	return d[die].getHold();
    }
    
    public void setHold(int die) {
		d[die].setHold();
    }
    
    public int getScore() {
    	return current.getScore();
    }
    
    public int getP1Score() {
    	return p1.getScore();
    }
    
    public int getP2Score() {
    	return p2.getScore();
    }
    
    public void setScore(int roundScore) {
		current.setScore(roundScore);
    }
    
    public int[] endOfRound() {
    	int[] roundScore = new int[14];
    	int[] finalDice = getDice();
    	for (int i = 0; i < finalDice.length; i++) {
    		if (finalDice[i] == 1) { // Aces
    			roundScore[0] += 1;
    		} else if (finalDice[i] == 2) { // Twos
    			roundScore[1] += 2;
    		} else if (finalDice[i] == 3) { // Threes
    			roundScore[2] += 3;
    		} else if (finalDice[i] == 4) { // Fours
    			roundScore[3] += 4;
    		} else if (finalDice[i] == 5) { // Fives
    			roundScore[4] += 5;
    		} else if (finalDice[i] == 6) { // Sixes
    			roundScore[5] += 6;
    		}
    	}
    	if (roundScore[0] + roundScore[1] + roundScore[2] + roundScore[3] + roundScore[4] + roundScore[5] >= 63) { // Bonus
    		roundScore[6] = 35;
    	}
    	if (finalDice[0] == finalDice[1] && finalDice[1] == finalDice[2]) { // Three-of-a-kind
    		roundScore[7] = finalDice[0] + finalDice[1] + finalDice[2];
    	} else if (finalDice[1] == finalDice[2] && finalDice[2] == finalDice[3]) {
    		roundScore[7] = finalDice[1] + finalDice[2] + finalDice[3];
    	} else if (finalDice[2] == finalDice[3] && finalDice[3] == finalDice[4]) {
    		roundScore[7] = finalDice[2] + finalDice[3] + finalDice[4];
    	}
    	if (finalDice[0] == finalDice[1] && finalDice[1] == finalDice[2] && finalDice[2] == finalDice[3]) { // Four-of-a-kind
    		roundScore[8] = finalDice[0] + finalDice[1] + finalDice[2] + finalDice[3];
    	} else if (finalDice[1] == finalDice[2] && finalDice[2] == finalDice[3] && finalDice[3] == finalDice[4]) {
    		roundScore[8] = finalDice[1] + finalDice[2] + finalDice[3] + finalDice[4];
    	}
    	if (finalDice[0] == finalDice[1] && finalDice[1] == finalDice[2] && finalDice[2] == finalDice[3] && finalDice[3] == finalDice[4]) { // Yahtzee
    		roundScore[12] = 50;
    	}
    	for (int i = 0; i < finalDice.length; i++) { // Chance
    		roundScore[13] += finalDice[i];
    	}
    	current.setRoundTotal(roundScore);
    	current.endRound();
    	return roundScore;
    }

}