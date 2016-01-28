package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import game.Stone.Color;
import game.Stone.Shape;
import java.util.HashMap;
import java.util.Map;

public class Player implements Players {
	private Board board;
	private String name;
	private List<Stone> hand;
	private int points;
	private static final int MAXHANDSIZE = 6;
	
	public Player(String name) {
		this.name = name;
		hand = new ArrayList<Stone>();
		points = 0;
	}
	
	public List<Moves> makeMove() {
		List<Moves> moves = new ArrayList<Moves>();
		System.out.println("Would you like to place a stone or trade stones?");
		boolean validCommand = false;
		Scanner in = new Scanner(System.in);
		String command = in.nextLine();
		//Check if command is a valid command.
		while(!validCommand){
			if(!(command.equals("place") || command.equals("trade"))){
				System.out.println("Invalid command, use [place] or [trade]");
				command = in.nextLine();
			} else {
				validCommand = true;
			}
		}	
		if(command.equals("place")) {
				System.out.println("Which stones and where?"); 
				boolean cont = true;	
				while(cont) {
					String p = in.nextLine();
					if(!(p.equals("Done"))) {
						try{
						String[] parts = p.split(",");
						moves.add(new Place(new Stone(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2])));
						String placedStones = "[ ";
						for(Moves pl: moves) {
							placedStones += pl.getStone().toString() + " ";
						}
						placedStones += "]";
						System.out.println("Stone added, current stones to be placed: " + placedStones + "Add more, or type 'Done'");	
						} catch(Exception e) {
							System.out.println("Wrong input, usage: [STONE,Y-COORDINATE,X-COORDINATE]");
						}
					} else {
						cont = false;
					}
				}
			} else if (command.equals("trade")) {
				System.out.println("What stones do you want to trade?");
				boolean cont = true;
				while(cont) {
					String p = in.nextLine();
					if(!(p.equals("Done"))) {
						moves.add(new Trade(new Stone(p)));
						String tradedStones = "[ ";
						for(Moves s: moves) {
							 tradedStones += s.getStone().toString() + " ";
						}
						tradedStones += "]";
						System.out.println("Stone added, current stones to be traded: " + tradedStones + "If done, type 'Done'");
					} else {
						cont = false;
					}
				}
			}		
		return moves;
	}
	
	//TODO: check op toString is niet zo netjes.
	public void takeStone(Stone stone){
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
	
	
	public boolean containsStone(Stone stone){
		boolean result = false;
		for(Stone s: hand) {
			if(s.toString().equals(stone.toString())) {
				result = true;
			}
		}
		return result;
	}
	
	public void addPoints(int newPoints){
		this.points = this.points + newPoints;		
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
		Player bart = new Player("Bart");
        		bart.receiveStone(new Stone(Shape.CIRCLE, Color.BLUE));
        		bart.receiveStone(new Stone(Shape.SQUARE, Color.ORANGE));
        		bart.receiveStone(new Stone(Shape.CROSS, Color.ORANGE));
        		bart.receiveStone(new Stone(Shape.CIRCLE, Color.RED));
        		bart.receiveStone(new Stone(Shape.SQUARE, Color.GREEN));
        		bart.receiveStone(new Stone(Shape.STAR, Color.PURPLE));
        	System.out.println(bart.getHand().toString());
        	bart.takeStone(new Stone(Shape.SQUARE, Color.ORANGE));
        	System.out.println(bart.getHand().toString());
		}
	}
