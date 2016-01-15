package game;

import game.Stone.Color;
import game.Stone.Shape;

public class TUI {
	private Board board;
	
	private TUI(Board board) {
		this.board = board;
	}
	
	public void makeBoard() {
			for(int i = 0; i < board.getDimentions(); i++) {
				System.out.print("|");
				for(int k = 0; k < board.getDimentions(); k++) {
					Stone stone = board.getBoard().get(new Location(k,i).getLocationString());
					if(stone != null) {
						System.out.print(stone.toString());
					} else {
						System.out.print("           ");
					}
					System.out.print("|");
				}
				System.out.println();
			}
	}
	
	public static void main(String[] args) {
		Board testboard = new Board();
		testboard.setField(0, 0, new Stone(Shape.CIRCLE, Color.RED));
		testboard.setField(1, 0, new Stone(Shape.CIRCLE, Color.YELLOW));
		testboard.setField(2, 0, new Stone(Shape.CIRCLE, Color.GREEN));
		TUI test = new TUI(testboard);
		test.makeBoard();
		

	}
}
