package game;

import java.util.Scanner;

import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import game.Stone.Shape;
import server.NetworkPlayer;
import game.Stone.Color;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

public class Game {
	private static final int MAXSTONESPERHAND = 6;
	private Board board;
	private Players[] players;
	private int moveCounter;
	private TUI view;
	private Bag bag;
	private Map<Integer, Players> playerList;
	private Player currentPlayer;

	
	public Game(Players[] players) {
		board = new Board();
		bag = new Bag();
		view = new TUI(this);
		moveCounter = 0;
		this.players = players.clone();
		int i = 0;
		this.playerList = new HashMap<Integer, Players>();
		for(Players p : players){
			playerList.put(i, p);
			i++;
		}
		
	}
	
    public void start(String[] playersToPlayWith) {
    	players = new Players[playersToPlayWith.length];
    	for(int i = 0; i< players.length; i++) {
    		players[i] = new Player(playersToPlayWith[i]);
    	}
    	boolean cont = true;
    		while(cont) {
    			reset();
    			play();
    			cont = false;
    		}
    	
    }
    
    private List<Moves> getAllowedMoves(Players p) {
		boolean continueToMakeMove = true;
		List<Moves> moves = new ArrayList<>();
		//Give computer player the current board to calculate the moves for.
		p.setBoard(board);
    	while(continueToMakeMove){
    		boolean isValidMove = true;
   			moves = p.makeMove();
   			if(moves.size() == 0) {
   				isValidMove = false;
   			}
    		for(Moves m : moves) {
    			if(m instanceof Place) {
    				Place place  = ((Place) m);
    				Stone stone = place.getStone();
    				//Check if stone is in hand
    				if(!(p.containsStone(stone))) {
    					isValidMove = false;
    				}
    				if(!(board.isValidMove(stone, place.getX(), place.getY()))) {
    					isValidMove = false;
    				}
    			}
    			else if(m instanceof Trade) {
    				if(moveCounter == 0) {
    					isValidMove = false;
    				}
//    				Check if stone is in hand
    				Stone stone = ((Trade) m).getStone();
    				if(!p.containsStone(stone)) {
    					isValidMove = false;
    				}
    				
    			}
    		}    
    		if(isValidMove) {
    			continueToMakeMove = false;
    		}
   		}
		return moves;
    }
    
    private void play() {
    	//Give all players a starting hand
        for(Players p : players) {
        	for(int i = 0; i < MAXSTONESPERHAND; i ++){
        		p.receiveStone(bag.takeStone());
        	}
        }
        //Show board
        view.update();
        
        //game, uitgaande van dat alle moves in de moves list acceptable zijn
        while(!gameOver() && moveCounter < 40){
        	Players p = currentPlayer();
        	System.out.println("Player: " + p.getName() + " Points: " + p.getPoints() +  " Move#: " + moveCounter + " Hand: " + p.getHand().toString() + " Stones left in bag:" + bag.getSize());
        	List<Moves> moves = getAllowedMoves(p);
        		//ALs place is:
        		if(moves.get(0) instanceof Place) {
        			for(Moves m: moves) {
						Place place = ((Place) m);
						board.setField(place.getX(), place.getY(), place.getStone());
						p.takeStone(place.getStone());
        			}
        	       	p.addPoints(board.getTheoreticalPoints(moves));
					moveCounter++;
        		}
        		else if(moves.get(0) instanceof Trade) {
        			for(Moves m: moves) {
        				Stone stone = ((Trade) m).getStone();
        				p.takeStone(stone);
        				p.getHand().add(bag.tradeStone(stone));
        			}
        			moveCounter++;
        		}
        		fillHand(p);
        		view.update();
        	}
        		
        }   
        	

	public void placeStones(List<Stone> stones, List<Location> locations) {
		List<Moves> moves = new ArrayList<Moves>();
		for(int i = 0; i < stones.size(); i++) {
			moves.add(new Place(stones.get(i), locations.get(i).getX(), locations.get(i).getY()));
			}
		boolean isValidMove = true;
		if(!board.isValidMove(moves)) {
			isValidMove = false;
		}
		if(isValidMove) {
			for(Moves m: moves) {
				Stone stone = ((Place) m).getStone();
				int xCoordinate = ((Place) m).getX();
				int yCoordinate = ((Place) m).getY();
				board.setField(xCoordinate, yCoordinate, stone);
				currentPlayer.takeStone(stone);
			}
		}
		int points = board.getTheoreticalPoints(moves);
		currentPlayer.addPoints(points);
		fillHand(currentPlayer);
	}
	
    private void update() {
    	view.makeBoard();
    }
    
    private void reset(){
    	moveCounter = 0;
    	board.resetBoard();
    	bag.resetBag();
    }
    
    public List Stones(){
		return null;
    	
    }
    
    
    //TODO: add  age extra?
    public Players getFirstPlayer() {
    	int maxScore = 0;
    	Players starter = players[0];
		Map<Shape, Integer> shapeResult = new HashMap<Shape, Integer>();
		Map<Color, Integer> colorResult = new HashMap<Color, Integer>();
    	for(Players p: players) {
    		shapeResult.clear();
    		colorResult.clear();
    		for(Stone s : p.getHand()) {
    			if(shapeResult.containsKey(s.getShape())) {
    				shapeResult.put(s.getShape(), shapeResult.get(s.getShape()) + 1);
    			} else {
    			shapeResult.put(s.getShape(), 1);
    			}
    			if(colorResult.containsKey(s.getColor())) {
    				colorResult.put(s.getColor(), colorResult.get(s.getColor()) + 1);
    			} else {
    				colorResult.put(s.getColor(), 1);
    			}
    		}
    		for(Integer i: shapeResult.values()){
    			if(i > maxScore) {
    				maxScore = i;
    				starter = p;
    			}
    		}
    		for(Integer j: colorResult.values()) {
    			if(j > maxScore) {
    				maxScore = j;
    				starter = p;
    			}
    		} 		
    	}
    	return starter;
    }
    
    
    
    //Nog niet juiste implementatie
    public boolean gameOver() {
    	boolean result = false;
    	for(Players p: players) {
    		if(p.getHand().size() == 0) {
    			result = true;
    		}
    	}
    	return result;
    }
    
    public void fillHand(Players p) {
    	int handSize = p.getHand().size();
    	int stonesNeeded = MAXSTONESPERHAND - handSize;
    	for(int i =0; i < stonesNeeded; i++){
    		p.receiveStone(bag.takeStone());
    	}
    }
    
    public Players currentPlayer(){
    	Players p;
    	if(moveCounter == 0) {
    		p = getFirstPlayer();
    	} else {
    		p = playerList.get(moveCounter % playerList.keySet().size());
    	}
    	return p;
    }
    
    public Board getBoard(){
    	return this.board;
    }
    
    public Players getWinner() {
    	Players winner = null;
    	int maxScore = 0;
    	for(Players p: players) {
    		if(p.getPoints() > maxScore) {
    			winner = p;
    			maxScore = p.getPoints();
    		}
    	}
    	return winner;
    }
    
    
//    public static void main(String[] args) {
//		Game game = new Game(new Players[]{new Player("Bart")} );
//		game.start(players);
//	}
    	
}