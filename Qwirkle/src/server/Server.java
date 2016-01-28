package server;

import java.util.List;

import game.Protocol;

import java.util.List;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class Server {
	private static final String USAGE  = "usage: " + Server.class.getName() + " <port>";
	private int port;
	private List<NetworkPlayer> players;
	private List<NetworkGame> games;
	
	
	public Server(int port) {
		this.port = port;
		games = new ArrayList<NetworkGame>();
		players = new ArrayList<NetworkPlayer>();
	}
	
	public void run() {
		System.out.println("Server started up");
		try {
			ServerSocket server = new ServerSocket(port);
			while(true) {
				try {
					Socket client = server.accept();
					NetworkPlayer newPlayer = new NetworkPlayer(this, client);
					newPlayer.connect();
					if(isNewName(newPlayer.getName())) {
						players.add(newPlayer);
						newPlayer.isConnected();
						newPlayer.getAllPlayers();
						newPlayer.start();
						joinLobby(newPlayer);
					} else {
						newPlayer.sendMessage("Not an unique user name, try again");
					}			
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
    	System.out.println("Could not make server on this port");
		}
	}
	
    public void broadcast(String msg) {
    	for (NetworkPlayer player : players) {
    		player.sendMessage(msg);
    	}
    }
    
    public boolean isNewName(String name) {
    	boolean result = true;
    	for(NetworkPlayer p: players) {
    		if(p.getName().equals(name)){
    			result = false;
    		}
    	}
    	return result;
    }
	
    public void print(String message){
        System.out.println(message);
    }
        
	public void joinLobby(NetworkPlayer player) {
		broadcast(Protocol.JOINLOBBY + Protocol.SPLIT + player.giveName() + Protocol.SPLIT + player.getExtras());
	}
	
	public void joinGame(NetworkPlayer player, int amount) {
		NetworkGame gameToJoin = null;
		for(NetworkGame game: games) {
			if(game.getSize() == amount) {
				gameToJoin = game;
			}
		}
		if(gameToJoin == null) {
			gameToJoin = new NetworkGame(amount, this);
			games.add(gameToJoin);
		}
	}
	
    public String getPlayers(NetworkPlayer player) {
    	String result = "";
    	String currentPlayer = player.giveName();
    	for(NetworkPlayer p : players) {
    		if(!p.giveName().equals(currentPlayer)) {
    			result += p.giveName() + Protocol.SPLIT;
    		}
    	}
    	return result;
    }
	
	public String allCurrentPlayers(String name) {
		String result = "";
		for(NetworkPlayer p : players) {
			if(!(p.giveName().equals(name))) {
				result += (p.giveName() + Protocol.SPLIT);
			}
		}
		return result;
	}
    
    public void disconnectPlayer(NetworkPlayer player) {
			players.remove(player);
			if (player.getGame() != null) {
				player.getGame().removePlayer(player);
			}
			broadcast(Protocol.DISCONNECT + Protocol.SPLIT + player.giveName());
		}
    
    /** Start a server using the args when starting the server. */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println(USAGE);
            System.exit(0);
        }
        Server server = new Server(Integer.parseInt(args[0]));
        server.run();
        
    }
    
    
//    public void addGame(NetworkGame game) {
//    	games.add(game);
//    }
    

}
