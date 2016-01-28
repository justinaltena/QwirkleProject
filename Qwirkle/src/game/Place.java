package game;

public class Place implements Moves {
	private Stone stone;	
	private int x,y;
	
	public Place(Stone stone, int x, int y) {
		this.stone = stone;
		this.x = x;
		this.y = y;
		}
	
	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}
	
	public Stone getStone() {
		return this.stone;
	}
	
	@Override
	public String toString() {
		return (stone.toString() + x + y);
	}
}
