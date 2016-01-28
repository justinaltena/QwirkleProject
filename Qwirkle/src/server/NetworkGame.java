package server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PropertyResourceBundle;

import javax.print.attribute.standard.PrinterMessageFromOperator;

import game.Bag;
import game.Board;
import game.Location;
import game.Moves;
import game.Place;
import game.Players;
import game.Protocol;
import game.Stone;
import game.TUI;
import game.Stone.Color;
import game.Stone.Shape;

public class NetworkGame extends Thread {
	private static final int MAXSTONESPERHAND = 6;
	private Board board;
	private NetworkPlayer[] players;
	private int moveCounter;
	private int gameSize;
	private Bag bag;
	private NetworkPlayer currentPlayer;
	private Server server;
	private int size;
	
	public NetworkGame(int gameSize, Server server) {
		this.gameSize = gameSize;
		players = new NetworkPlayer[gameSize];
		board = new Board();
		bag = new Bag();
		this.server = server;
	}
	
	public void run() {
		System.out.println("Game has started");
		for(NetworkPlayer p : players) {
			fillHand(p);
		}
		while(true)
			giveTurn();
	}
	
    public void fillHand(Players p) {
    	int handSize = p.getHand().size();
    	int stonesNeeded = MAXSTONESPERHAND - handSize;
    	for(int i =0; i < stonesNeeded; i++){
    		p.receiveStone(bag.takeStone());
    	}
    }
    
    public void giveTurn() {
    	broadcast(Protocol.TURN + Protocol.SPLIT + currentPlayer.giveName());
    }
	
	
	public NetworkPlayer getFirstPlayer() {
    	int maxScore = 0;
    	NetworkPlayer starter = players[0];
		Map<Shape, Integer> shapeResult = new HashMap<Shape, Integer>();
		Map<Color, Integer> colorResult = new HashMap<Color, Integer>();
    	for(NetworkPlayer p: players) {
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
		placedStones(stones, locations, points);
		fillHand(currentPlayer);
	}
	
	public void tradeStones(List<Stone> stones) {
		boolean isValidMove = true;
		for(Stone s: stones) {
			if(!(currentPlayer.getHand().contains(s))) {
				isValidMove = false;
			}
		}
		if(isValidMove) {
			for(Stone stone: stones) {
				currentPlayer.takeStone(stone);
				currentPlayer.getHand().add(bag.tradeStone(stone));
			}
		}
	}
	
	private void broadcast(String message) {
		for (NetworkPlayer p : players) {
			p.sendMessage(message);
		}
	}
	
	public void placedStones(List<Stone> stones, List<Location> locations, int points) {
		String message = Protocol.PLACED + Protocol.SPLIT + currentPlayer.giveName() +
				Protocol.SPLIT + points + Protocol.SPLIT;
		for(int i = 0; i < stones.size(); i++) {
			message += Protocol.stoneToInt(stones.get(i)) + Protocol.SPLIT 
					+ locations.get(i).toString() + Protocol.SPLIT;
		}
		broadcast(message);
	}
	
	
	
    public void fillHand(NetworkPlayer p) {
    	int handSize = p.getHand().size();
    	int stonesNeeded = MAXSTONESPERHAND - handSize;
    	for(int i =0; i < stonesNeeded; i++){
    		p.receiveStone(bag.takeStone());
    	}
    }
    
    public Board getBoard() {
    	return board;
    }
    
		
	public int getSize(){
		return size;
	}
	
	public void removePlayer(NetworkPlayer player){
		this.interrupt();
	}
	
    public NetworkPlayer currentPlayer(){
    	NetworkPlayer p;
    	if(moveCounter == 0) {
    		p = getFirstPlayer();
    	} else {
    		p = players[moveCounter % gameSize];
    	}
    	return p;
    }
}
