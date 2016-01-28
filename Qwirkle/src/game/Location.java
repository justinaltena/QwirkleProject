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
	
	public Location getLocation(String location) {
		String[] locations = location.split(",");
		return new Location(Integer.parseInt(locations[0]), Integer.parseInt(locations[1]));
	}
	
	@Override
	public String toString() {
		return "" + x + "," + y;
	}
	
	public boolean isEqual(Location location){
		return (this.getX() == location.getX() && this.getY() == location.getY());
	}

}
