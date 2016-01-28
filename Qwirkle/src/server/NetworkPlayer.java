package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
import game.Board;
import game.Location;
import game.Protocol;
import game.Stone;
import game.*;

public class NetworkPlayer extends Thread {
	private static final int MAXHANDSIZE = 6;
	private BufferedReader in;
	private BufferedWriter out;
	private NetworkGame game;
	private String name;
	private List<Stone> hand;
	private int points;
	private Server server;
	private String extras;
	
	
	public NetworkPlayer(Server server, Socket sock) throws IOException {
		in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
		this.server = server;	
		hand = new ArrayList<Stone>();
		points = 0;
		this.server = server;
		extras = "";
	}
	
	public void connect() throws IOException {
		String input = in.readLine();
		if(input != null) {
			String[] words = input.split(Protocol.SPLIT);
			if(words.length >= 2 && words[0].equals(Protocol.CONNECT)) {
				name = words[1];
			}
			if(words.length > 2){
				for(int i = 2; i < words.length; i++) {
					extras += words[i] + Protocol.SPLIT;
				}
			}
		}
	}
	

	public void run() {
		try {
			String line = in.readLine();
			String[] words = line.split(Protocol.SPLIT);
			if(words.length != 0) {
				if(words[0].equals(Protocol.PLACE)) {
					if(game.currentPlayer().equals(this)) {
						place(words);						
					}
				}
				if(words[0].equals(Protocol.TRADE)) {
					if(game.currentPlayer().equals(this)) {
						trade(words);
					}
				}
				if(words[0].equals(Protocol.JOINAMOUNT)) {
					int amount = Integer.parseInt(words[1]); {
						if(amount >= 1 && amount <= 4) {
							server.joinGame(this, amount);
						}
					}
				}
				if(words[0].equals(Protocol.GIVEPLAYERS)){
					sendMessage(Protocol.PLAYERS + Protocol.SPLIT + server.allCurrentPlayers(name));
				}
			}
		} catch (IOException e) {
			error(Protocol.Error.WRONGCOMMAND);
		}	
	}
	
	public void place(String[] words) {
		List<Stone> stones = new ArrayList<Stone>();
		List<Location> locations = new ArrayList<Location>();
		for(int i = 1; i < words.length; i++) {
			if(words.length % 2 == 1) {
				stones.add(Protocol.intToStone(words[i]));
			} else {
				locations.add(Protocol.intToLocation(words[i]));
			}		
		}
		game.placeStones(stones, locations);
	}
	
	public void trade(String[] words) {
		List<Stone> stones  = new ArrayList<Stone>();
		if(words.length >= 2) {
			for(int i = 1; i < words.length; i++) {
				stones.add(Protocol.intToStone(words[i]));
			}
		}
		game.tradeStones(stones);
	}
	
	public String getExtras() {
		return extras;
	}
	
	public boolean canMakeMove() {
		boolean result = false;
		List<String> locations = game.getBoard().getAvailablePlaces();
		for(Stone stone : hand) {
			for(String s: locations){
				String[] parts = s.split(",");
				int x = Integer.parseInt(parts[0]);
				int y = Integer.parseInt(parts[1]);
				if(game.getBoard().isValidMove(stone, x, y)) {
					result = true;
				}
			}	
		}
		return result;
	}
	
	
	/** send a message to a ClientHandler. */
	public void sendMessage(String msg) {
		try {
			out.write(msg);
			out.newLine();
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void isConnected() {
		sendMessage(Protocol.ISCONNECTED + Protocol.SPLIT);
	}
	
	public void acknowledge() {
		sendMessage(Protocol.ISCONNECTED + Protocol.SPLIT);
	}
	
	public void getAllPlayers() {
		sendMessage(Protocol.PLAYERS + Protocol.SPLIT + server.getPlayers(this));
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
	public NetworkGame getGame(){
		return game;
	}
	
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
	
	public String giveName() {
		 return	name;
	}
	
	public void setGame(NetworkGame game) {
		this.game = game;
	}
	
	public void error(Protocol.Error error) {
		sendMessage(Protocol.ERROR + Protocol.SPLIT + error);
	}
	
	public void shutdown() throws IOException {
		out.close();
		in.close();
		server.disconnectPlayer(this);
	}
		
}
	
