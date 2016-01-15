package game;


public class Stone {
	private Shape shape;
	private Color color;

	
	public enum Shape {
		CIRCLE, CROSS, DIAMOND, SQUARE, STAR, PLUS, EMPTY
	}
	
	public enum Color {
		RED, ORANGE, YELLOW, GREEN, BLUE, PURPLE, EMPTY
	}
	

	public Stone(Shape shape, Color color) {
		this.shape = shape;
		this.color = color;
	}
	
	public Shape getShape() {
		return this.shape;
	}
	
	public Color getColor() {
		return this.color;
	}

	@Override
	public String toString() {
		return shape + " " + color;
	}  

}
