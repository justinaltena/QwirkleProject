package game;

import game.Stone.Color;
import game.Stone.Shape;

public class Protocol {
	public static final String CONNECT = "hello";
	public static final String SPLIT = " ";
	public static final String ISCONNECTED = "hello_from_the_other_side";
	public static final String ERROR = "error";
	public static final String JOINLOBBY = "joinlobby";
	public static final String PLAYERS = "players";
	public static final String GIVEPLAYERS = "players?";
	public static final String JOINAMOUNT = "join";
	public static final String START = "start";
	public static final String CHAT = "chat";
	public static final String MSG = "msg";
	public static final String CHATPM = "chatpm";
	public static final String MSGPM = "msgpm";
	public static final String PLACE = "place";
	public static final String PLACED = "placed";
	public static final String TRADE = "trade";
	public static final String NEWSTONES = "newstones";
	public static final String TRADED = "traded";
	public static final String TURN = "turn";
	public static final String ENDGAME = "endgame";
	public static final String CHALLENGE = "challenge";
	public static final String NEWCHALLENGE = "newchallenge";
	public static final String ACCEPT = "accept";
	public static final String DECLINE = "decline";
	public static final String DISCONNECT = "disconnect";

	public enum Error {
		WRONGCOMMAND, WRONGTURN, INVALIDNAME, PLAYERDISCONNECTED, MISSINGOPTION
	}
	
//	public static Error getError(int code) {
//		Error error = null;
//		switch(code) {
//		case 0: error = Error.WRONGCOMMAND; break;
//		case 1: error = Error.WRONGTURN; break;
//		case 2: error = Error.INVALIDNAME; break;
//		case 3: error = Error.PLAYERDISCONNECTED; break;
//		case 4: error = Error.MISSINGOPTION; break;
//		}
//		return error;
//	}
	
	
	public static Stone intToStone(String number) {
		Shape ss = Shape.EMPTY;
		Color cc = Color.EMPTY;
		String[] parts = number.split(",");
		int form = Integer.parseInt(parts[0]);
		int color = Integer.parseInt(parts[1]);
		switch(form) {
		case 0: ss = Shape.CIRCLE; break;
		case 1: ss = Shape.CROSS; break;
		case 2: ss = Shape.DIAMOND; break;
		case 3: ss = Shape.DIAMOND; break;
		case 4: ss = Shape.STAR; break;
		case 5: ss = Shape.PLUS; break;
		}
		switch(color) {
		case 0: cc = Color.RED; break;
		case 1: cc = Color.ORANGE; break;
		case 2: cc = Color.YELLOW; break;
		case 3: cc = Color.GREEN; break;
		case 4: cc = Color.BLUE; break;
		case 5: cc = Color.PURPLE; break;
		}
		return new Stone(ss, cc);
		
	}
	
	public static Location intToLocation(String location) {
		String[] parts = location.split(",");
		int xCoordinate = Integer.parseInt(parts[0]);
		int yCoordinate = Integer.parseInt(parts[1]);
		return new Location(xCoordinate, yCoordinate);
	}
	
	public static String stoneToInt(Stone stone) {
		Shape shape = stone.getShape();
		Color color = stone.getColor();
		int shapeNumber = -1;
		int colorNumber = -1;
		switch(shape) {
		case CIRCLE: shapeNumber = 0; break;
		case CROSS: shapeNumber = 1; break;
		case DIAMOND: shapeNumber = 2; break;
		case SQUARE: shapeNumber = 3; break;
		case STAR: shapeNumber = 4; break;
		case PLUS: shapeNumber = 5; break;
		case EMPTY: shapeNumber = -1; break;
		}
		switch(color) {
		case RED: colorNumber = 0; break;
		case ORANGE: colorNumber = 1 ; break;
		case YELLOW: colorNumber = 2; break;
		case GREEN: colorNumber = 3 ; break;
		case BLUE: colorNumber = 4; break;
		case PURPLE: colorNumber = 5; break;
		case EMPTY: colorNumber = -1; break;
		}
		return "" + shapeNumber+ "," + colorNumber;
		
	}
	
}
