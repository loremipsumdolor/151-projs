package yahtzee.model;

public class Player {
	
	private String name;
	private int score;
	private int[][] playerData;
	private int rolls;
	private int round;
	
	public Player(String name) {
		this.name = name;
		this.playerData = new int[6][14];
		this.rolls = 0;
		this.round = 1;
		this.score = 0;
	}
	
	public int getRound() {
		return round;
	}
	
	public void endRound() {
		rolls = 0;
		round++;
	}
	
	public int getRolls() {
		return rolls;
	}
	
	public void addRoll() {
		rolls++;
	}
	
	public void setRoundTotal(int[] roundScore) {
		for (int i = 0; i < playerData[getRound()].length; i++) {
			playerData[getRound()][i] = roundScore[i];
		}
	}
	
	public int[] getRoundTotal(int round) {
		return playerData[round];
	}
	
	public void setScore(int roundScore) {
		score = roundScore;
	}
	
	public int getScore() {
		return score;
	}
}
