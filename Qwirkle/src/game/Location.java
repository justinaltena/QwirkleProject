package game;

public class Location {
	final int x;
	final int y;
	
	public Location(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public String getLocationString() {
		String result = this.x + "," + this.y;
		return result;
	}

}
