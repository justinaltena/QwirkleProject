package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import game.Stone.Color;
import game.Stone.Shape;


public class Board {
	public int DIM = 0;
	private Map<String, Stone> board;

	public Board() {	
		board = new HashMap<>();
		this.reset();
	}
	
	public void reset(){
		for(int x = 0; x < DIM; x++)
			for(int y = 0; y < DIM; y++)
				this.setField(x, y, new Stone(Shape.EMPTY, Color.EMPTY));
	}
	
	
	public void setField(int x, int y, Stone stone){
		if(getField(x, y) == null) {
			board.put(new Location(x, y).getLocationString(), stone);
		}
	}
	
	public Stone getField(int x, int y){
		return board.get(new String(x + "," + y));
		
	}
	
	public Board deepCopy(){
        Board newBoard = new Board();
        for(int x = 0; x < DIM; x++)
        	for(int y = 0; y < DIM; y++)
        	newBoard.setField(x, y, this.getField(x,y));
        return newBoard;
		
	}
	
	public boolean isValidMove(int x, int y, Stone stone){
		boolean result = true;
		if((x > DIM) || (y > DIM))
			result = false;
		if( getStoneList(x, y).isEmpty()) {
			result = false;
		}
		if(getStoneList(x, y).contains(stone)) {
			result = false;
		}
		for(Stone e: getSurroundingStones(x, y)) {
			if(e.getColor() == stone.getColor() || e.getShape() == stone.getShape()) {
				result = true;
			}
		}					
		return result;	
	}
	
	public String toString(){
		return null;
		
	}
	
	public List<Stone> getSurroundingStones(int x, int y) {
		List<Stone> stones = new ArrayList<Stone>();
		stones.add(getField(x-1,y));
		stones.add(getField(x+1,y));
		stones.add(getField(x,y-1));
		stones.add(getField(x,y+1));
		return stones;
		
	}
	
	public List<Stone> getStoneList(int x, int y) {
		boolean cont = true;
		List<Stone> stones = new ArrayList<Stone>();
		// Get  the stones to the right of the placed stone
		while(cont) {
			for(int i = 0; i < 6; i++)
				if(!getField(x + i, y ).getShape().equals(Shape.EMPTY)) {
					stones.add(getField(x + i , y ));
				} else {
					cont = false;
				} 
		}	
		// Get  the stones to the below the placed stone
		cont = true;
		while(cont) {
			for(int i = 0; i < 6; i++)
				if(!getField(x , y + i).getShape().equals(Shape.EMPTY)) {
					stones.add(getField(x + i , y ));
				} else {
					cont = false;
				} 
		}	
		// Get  the stones to the left of the placed stone
		cont = true;
		while(cont) {
			 for(int i = 0; i < 6; i++)
				if(!getField(x - i , y).getShape().equals(Shape.EMPTY)) {
					stones.add(getField(x + i , y ));
				} else {
					cont = false;
				} 
		}
		// Get  the stones to the above of the placed stone
		cont = true;
		while(cont) {
			 for(int i = 0; i < 6; i++)
				if(!getField(x , y - i).getShape().equals(Shape.EMPTY)) {
					stones.add(getField(x + i , y ));
				} else {
					cont = false;
				} 
		 }
		 return stones;
	}

	
	// Moet nog dinamisb maken >> Moet nog DIMy en DIM opsplitsen.
	public int getDimentions() {
		int maxX = 0;
		int minX = 0;
		int maxY = 0;
		int minY = 0;
		for(String e : board.keySet()) {
			String[] parts = e.split(",");
			if(Integer.parseInt(parts[0]) > maxX) {
				maxX = Integer.parseInt(parts[0]);
			}
			if(Integer.parseInt(parts[0]) < minX) {
				minX = Integer.parseInt(parts[0]);
			}
			if(Math.abs(Integer.parseInt(parts[1])) > maxY) {
				maxY = Integer.parseInt(parts[1]);
			}
			if(Math.abs(Integer.parseInt(parts[1])) < minY) {
				minY = Integer.parseInt(parts[1]);
			}
		}
		DIM = Math.abs(minX) + maxX + 2;
		if((Math.abs(minY) + maxY) > DIM) {
			DIM = (Math.abs(minY) + maxY) + 2;
		}
		return DIM;
	}
	
	public Map<String, Stone> getBoard() {
		return this.board;
	}
	
	public static void main(String[] args) {
		Board newboard = new Board();
		newboard.setField(0, 2, new Stone(Shape.CIRCLE, Color.BLUE));
		newboard.setField(0, 3, new Stone(Shape.CIRCLE, Color.BLUE));
		newboard.getDimentions();
		System.out.println(newboard.getDimentions());
	}

}
