package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.text.View;

import game.*;

public class Client extends Thread {
	private static final String USAGE = "<name> <address> <port>";
	private String clientName;
	private Socket sock;
	private BufferedReader in;
	private BufferedWriter out;
	private boolean registered;
	private List<String> players;
	private Game game;
	private View view;
	
	public Client(String name, InetAddress ip, int port) throws IOException {
		clientName = name;
		try {
		sock = new Socket(ip, port);
		} catch (IOException e) {
			System.out.println("Could not connect to this server");
		}
		in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
		registered = false;
		while(!registered) {
			registerAtServer();
		}
		startGame();
		
	}
	
	public void registerAtServer() {
		sendMessage(Protocol.CONNECT + Protocol.SPLIT + giveName() );
		String line = null;
		line = readString();
		if(line != null) {
			System.out.println(line);
			String[] parts = line.split(Protocol.SPLIT);
			if(!parts[0].equals(Protocol.ERROR)) {
				registered = true;
			}
		}
	}
	
	public void startGame() {
		System.out.println("Please enter the amount of players you would like to play with: [2,3,4]");
		boolean inLobby = false;
		while(!inLobby) {
			Scanner lineIn = new Scanner(System.in);
			String amountOfPlayers = null;
			amountOfPlayers = lineIn.nextLine();
			int numberOfPlayers = Integer.parseInt(amountOfPlayers);
			if(amountOfPlayers != null && (numberOfPlayers >= 2 && numberOfPlayers <= 4)) {
				joinGame(amountOfPlayers);
				inLobby = true;
			} else {
				System.out.println("not a valid option, chose [2,3,4]");
				amountOfPlayers = lineIn.nextLine();
			}
		}
	}
	
	public void createGame(String[] commandParts) {
		String[] newPlayers = new String[commandParts.length - 1];
		for (int i = 1; i < commandParts.length; i++) {
			newPlayers[i - 1] = commandParts[i];
		}
		Players[] players = new Players[newPlayers.length];
		for(int j = 0; j < newPlayers.length; j++) {
			players[j] = new Player(newPlayers[j]);
		}
		this.game = new Game(players);
	}
	
	public void joinGame(String amount) {
		sendMessage(Protocol.JOINAMOUNT + Protocol.SPLIT + amount);
		System.out.println("You have been placed in a game... waiting for other players...");
	}
	
	public void run() {
		String input = null;
		while((input = readString()) != null) {
			String currentCommand = input;
			String[] commandParts = currentCommand.split(Protocol.SPLIT);
			if(commandParts[0].equals(Protocol.ERROR)) {
				System.out.println("Error");
			}
			else if(commandParts[0].equals(Protocol.PLAYERS)) {
				if(commandParts.length >= 2) {
					for(int i = 1; i < commandParts.length; i++) {
						players.add(commandParts[i]);
					}
				}
			}
			else if(commandParts[0].equals(Protocol.JOINLOBBY)) {
				if(commandParts.length >= 2) {
					System.out.println("Player: " + commandParts[1] + " joined the lobby");
				}
				
			}
			else if(commandParts[0].equals(Protocol.START)) {
				if(commandParts.length >= 2) {
					boolean mustJoin = false;
					for(int i = 1; i < commandParts.length; i++) {
						if(commandParts[i].equals(clientName)) {
							mustJoin = true;
						}
					}
					if(mustJoin) {
						createGame(commandParts);
					}
				}
			}
			else if(game != null) {
				if(commandParts[0].equals(Protocol.PLACED)) {
					if(commandParts.length >= 5) {
						String[] stonesAndLocations = new String[commandParts.length - 2];
						for(int j = 2; j < commandParts.length; j++ ) {
							stonesAndLocations[j - 2] = commandParts[j];
						}
						placed(stonesAndLocations);
					}
				}
				else if(commandParts[0].equals(Protocol.TRADED)) {
					System.out.println("Player " + commandParts[1] + " traded " + commandParts[2] + " stones.");
				}
				else if(commandParts[0].equals(Protocol.TURN)) {
					if(commandParts.length == 2) {
						turn(commandParts);
					}
				}
				else if(commandParts[0].equals(Protocol.ENDGAME)) {
					endGame();
				}
				
			}
		}
	}
	
	public void endGame() {
		Players winner = game.getWinner();
		if(winner.getName().equals(clientName)) {
			System.out.println("Congratulations, you have won the game with: " + winner.getPoints() );
		}
		else {
			System.out.println("Awhh you lost, " + winner.getName() + " got: " + winner.getPoints() + " points.");
		}
		game = null;
		rematch();
	}
	
	public void rematch() {
		Scanner rematchScanner = new Scanner(System.in);
		String line = rematchScanner.nextLine();
		System.out.println("Do you want to play a new game? [y/n]");
		if (line.equals("y")) {
			startGame();
		} else {
			shutdown();
		}
	}
	
	public void placed(String[] stonesAndLocations) {
		List<Stone> stones = new ArrayList<Stone>();
		List<Location> locations = new ArrayList<Location>();
		for(int i = 1; 3 < stonesAndLocations.length; i++) {
			if(stonesAndLocations.length % 2 == 1) {
				stones.add(Protocol.intToStone(stonesAndLocations[i]));
			} else {
				locations.add(Protocol.intToLocation(stonesAndLocations[i]));
			}		
		}
		game.placeStones(stones, locations);
		game.getBoard().addPoints(game.currentPlayer(), Integer.parseInt(stonesAndLocations[0]));
//		view.update;
	}
	
	public void turn(String[] commandParts) {
		if(commandParts[1].equals(clientName)) {
			System.out.println("Make a move");
			game.currentPlayer().makeMove();
		} else {
			System.out.println("Player " + commandParts[1] + " is making a  move, please wait.");
		}
	}
	
	
	
	
	
	
	
	
	

	public void sendMessage(String msg) {
		try {
			out.write(msg);
			out.newLine();
			out.flush();
		} catch (IOException e) {
			System.out.println("Connection has been lost");
		}
	}
	
	private String readString() {
		String line = null;
		try {
			line = in.readLine();
		} catch (IOException e) {
			System.out.println("No line to read");
		}
		return line;
	}
	
	public void shutdown() {
		try {
			out.close();
			in.close();
			sock.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	private static void print(String message){
		System.out.println(message);
	}
	
	public String giveName() {
		return clientName;
	}
	
	
	
	/** Start een Client-applicatie op. */
	public static void main(String[] args) {
		if (args.length != 3) {
			System.out.println(USAGE);
			System.exit(0);
		}
		
		InetAddress host=null;
		int port =0;

		try {
			host = InetAddress.getByName(args[1]);
		} catch (UnknownHostException e) {
			print("ERROR: no valid hostname!");
			System.exit(0);
		}

		try {
			port = Integer.parseInt(args[2]);
		} catch (NumberFormatException e) {
			print("ERROR: no valid portnummer!");
			System.exit(0);
		}

		try {
			Client client = new Client(args[0], host, port);
			client.sendMessage(args[0]);
			client.start();
				
		} catch (IOException e) {
			print("ERROR: couldn't construct a client object!");
			System.exit(0);
		}

	}
}
