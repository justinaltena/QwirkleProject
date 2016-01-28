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
	
	public Stone(String stone){
		String[] parts = stone.split("");
		String shape = parts[1];
		String color = parts[2];	
		this.shape = toShape(shape);
		this.color = toColor(color);
	}
	
	public Stone toStone(String stone) {
		String[] parts = stone.split("");
		String shape = parts[1];
		String color = parts[2];	
		return new Stone(toShape(shape), toColor(color));
	}
	
	public Shape getShape() {
		return this.shape;
	}
	
	public Color getColor() {
		return this.color;
	}
	
	public Shape toShape(String s) {
		Shape ss = Shape.EMPTY;
		switch(s) {
		case "C": ss = Shape.CIRCLE; break;
		case "O": ss = Shape.CROSS; break;
		case "D": ss = Shape.DIAMOND; break;
		case " ": ss = Shape.EMPTY; break;
		case "P": ss = Shape.PLUS; break;
		case "S": ss = Shape.SQUARE; break;
		case "A": ss = Shape.STAR; break;
		}
		return ss;
	}
	
	public Color toColor(String c) {
		Color cc = Color.EMPTY;
		switch(c) {
		case "B": cc = Color.BLUE; break;
		case " ": cc = Color.EMPTY; break;
		case "G": cc = Color.GREEN; break;
		case "O": cc = Color.ORANGE; break;
		case "P": cc = Color.PURPLE; break;
		case "R": cc = Color.RED; break;
		case "Y": cc = Color.YELLOW; break;
		}
		return cc;
	}


	
	@Override
	public String toString() {
		String shapeResult = " ";
		String colorResult = " ";
		switch(shape) {
		case CIRCLE: shapeResult = "C"; break;
		case CROSS: shapeResult = "O"; break;
		case DIAMOND: shapeResult = "D"; break;
		case EMPTY:	shapeResult = " "; break;
		case PLUS: shapeResult = "P"; break;
		case SQUARE: shapeResult = "S"; break;
		case STAR: shapeResult = "A"; break;
		default: shapeResult = " "; break;
		}
		switch(color) {
		case BLUE: colorResult = "B"; break;
		case EMPTY: colorResult = " "; break;
		case GREEN: colorResult = "G"; break;
		case ORANGE: colorResult = "O"; break;
		case PURPLE: colorResult = "P"; break;
		case RED: colorResult = "R"; break;
		case YELLOW: colorResult = "Y"; break;
		default: shapeResult = " "; break;
		}
		return new String("" + shapeResult + colorResult);
	}  
	
	public static void main(String[] args) {
		Stone test = new Stone("OB");
		System.out.println("test");
		System.out.println(test.toString());
	}

}
