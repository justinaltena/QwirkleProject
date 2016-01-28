package game;

import java.util.ArrayList;
import java.util.List;
import game.Stone.Color;
import game.Stone.Shape;

public class NaiveComputerPlayer implements Players {
	private Board board;
	private String name;
	private List<Stone> hand;
	private int points;
	private static final int MAXHANDSIZE = 6;
	
	public NaiveComputerPlayer(String name) {
		this.board = new Board();
		this.name = name;
		hand = new ArrayList<Stone>();
		points = 0;
	}

	//Get all available places to place a stone (Places with a stone next to it).
	public List<String> getSurroundingLocations() {
		List<String> surrounding = new ArrayList<String>();
		if(board.getBoard().keySet().size() == 0) {
			surrounding.add(0 + "," + 0);
		} else {
			for(String s: board.getBoard().keySet()) {
				String[] cooridinates = s.split(",");
				int xCoordinate = Integer.parseInt(cooridinates[0]);
				int yCoordinate = Integer.parseInt(cooridinates[1]);
				if(!surrounding.contains(xCoordinate - 1 + "," + yCoordinate)) {
					surrounding.add(xCoordinate - 1 + "," + yCoordinate);
				}
				if(!surrounding.contains((xCoordinate + 1) + "," + yCoordinate)) {
					surrounding.add((xCoordinate + 1) + "," + yCoordinate);
				}
				if(!surrounding.contains(xCoordinate + "," + (yCoordinate - 1))) {
					surrounding.add(xCoordinate + "," + (yCoordinate - 1));
				}
				if(!surrounding.contains(xCoordinate + "," + (yCoordinate + 1))) {
					surrounding.add(xCoordinate + "," + (yCoordinate + 1));
				}
			}
		}
		return surrounding;
	}
	
	
	
	public List<Moves> makeMove() {
		List<Moves> moves = new ArrayList<Moves>();
		boolean foundPlace = false;
			for(Stone stone: hand){
				for(String s: getSurroundingLocations()) {
					String[] cooridinates = s.split(",");
					int xCoordinate = Integer.parseInt(cooridinates[0]);
					int yCoordinate = Integer.parseInt(cooridinates[1]);
					if(board.isValidMove(stone, xCoordinate, yCoordinate) && !foundPlace) {
						moves.add(new Place(stone, xCoordinate, yCoordinate));
						foundPlace = true;
					}
				}
			}
			//If there could not be found a place to validly place a stone, trade whole hand
			if(!foundPlace) {
			for(Stone s: hand) {
				moves.add(new Trade(s));
			}
		}
	return moves;
	}


	public void takeStone(Stone stone) {
		int pos = 0;
		boolean cont = true;
		while(cont) {
			for(Stone s: hand) {
				if(cont == true) {
					if(!(s.toString().equals(stone.toString()))) {
						pos++;
					} else {
						cont = false;
					}
				}
			}
		}
		if(pos != MAXHANDSIZE) {
			hand.remove(pos);
		}
	}

	public void receiveStone(Stone stone) {
		hand.add(stone);
	}

	public void addPoints(int newPoints) {
		points = points + newPoints;
		
	}
			
	public List<Stone> getHand() {
		return hand;
	}
	
	public int getPoints(){
		return points;
		
	}
	public String getName() {
		 return	this.name;
	}
	
	public void setBoard(Board board) {
		this.board = board;
	}
	
	public static void main(String[] args) {
		NaiveComputerPlayer bart = new NaiveComputerPlayer("Bart");
        		bart.receiveStone(new Stone(Shape.CIRCLE, Color.BLUE));
        		bart.receiveStone(new Stone(Shape.SQUARE, Color.ORANGE));
        		bart.receiveStone(new Stone(Shape.CROSS, Color.ORANGE));
        		bart.receiveStone(new Stone(Shape.CIRCLE, Color.RED));
        		bart.receiveStone(new Stone(Shape.SQUARE, Color.GREEN));
        		bart.receiveStone(new Stone(Shape.STAR, Color.PURPLE));
        		bart.board.setField(0, 0, new Stone(Shape.SQUARE, Color.BLUE));
        		bart.board.setField(0, 1, new Stone(Shape.SQUARE, Color.BLUE));
	}

	public boolean containsStone(Stone stone){
		boolean result = false;
		for(Stone s: hand) {
			if(s.toString().equals(stone.toString())) {
				result = true;
			}
		}
		return result;
	}

}
	
