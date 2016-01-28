package game;

public class Trade implements Moves {
	private Stone stone;
	
	public Trade(Stone stone) {
		this.stone = stone;
	}
	
	public Stone getStone() {
		return this.stone;
	}
}
