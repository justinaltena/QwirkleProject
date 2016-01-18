package players;

import game.Bag;
import game.Board;
import game.Stone;
import game.Location;

public class Player {
	private Board board;
	private String name;
	private Stone[] hand;
	private int points;
	
	public Player(Board board, String name) {
		this.board = board;
		this.name = name;
		points = 0;
	}
	
	//Wat is de bedoeling van MakeMove?
	public void makeMove(){
		
	}
	
	public void take(){
		Bag.takeStone();
	}
	
	public void place(Stone stone,) {
		board.setField(int x, int y, Stone stone);
	}
	
	public String trade(){
		Bag.tradeStone(Stone[] stones) {
		}
		
	}
	
	public void addPoints(int newPoints){
		this.points = this.points + newPoints;
		
		
	}
			
	
	public int getPoints(){
		return points;
		
	}

}
