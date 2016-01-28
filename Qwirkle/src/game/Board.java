package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.SynchronousQueue;
import java.util.HashMap;

import game.Stone.Color;
import game.Stone.Shape;

public class Board {
	private static final int MAXROWLENGHT = 6;
	public int DIMX = 0;
	public int DIMY = 0;
	private Map<String, Stone> board;
	public Map<Players, Integer> points;

	public Board() {
		board = new HashMap<>();
		this.resetBoard();
	}

	// Is handig???
	public void resetBoard() {
		for (int x = 0; x < DIMX; x++)
			for (int y = 0; y < DIMY; y++)
				this.setField(x, y, null);
	}

	public void setField(int x, int y, Stone stone) {
		board.put(new Location(x, y).getLocationString(), stone);
	}

	public Stone getField(int x, int y) {
		return board.get(new String(x + "," + y));

	}

	// TODO: FIX implementation with max border sizes.
	public Board deepCopy() {
		Board newBoard = new Board();
		for (int x = 0; x < 1000; x++)
			for (int y = 0; y < 10; y++)
				if (!(board.get("" + x + "," + y) == null)) {
					newBoard.setField(x, y, this.getField(x, y));
				}
		return newBoard;

	}

	public boolean isValidMove(List<Moves> moves) {
		boolean result = true;
		List<Place> places = new ArrayList<Place>();
		Board dBoard = this.deepCopy();
		System.out.println(dBoard.toString());
		for (Moves m : moves) {
			if (m instanceof Place) {
				places.add((Place) m);
			}
		}
		for (Place p : places) {
			if (dBoard.isValidMove(p.getStone(), p.getX(), p.getY())) {
				dBoard.setField(p.getX(), p.getY(), p.getStone());
			} else {
				result = false;
			}
		}
		return result;
	}

	public boolean isValidMove(Stone stone, int x, int y) {
		boolean result = true;
		// Check if place is already occupied
		if (this.getField(x, y) != null) {
			result = false;
		}
		// Check if one of the surrounding stones has the same color or shape,
		// or if the stone is placed at center
		for (Stone e : getSurroundingStones(x, y)) {
			if ((!(e.getColor() == stone.getColor() || e.getShape() == stone.getShape()) || (x == 0 && y == 0))) {
				result = false;
			}
		}
		//Check if there are stones surrounding it
		if(getSurroundingStones(x, y).size() == 0 && !(x == 0 && y == 0)) {
			result = false;
		}
		// Check if stone fits in the specific row/column (Not already in there
		// and all stones have the shape or color)).
		for (Stone s : getStoneList(x, y)) {
			if (s.toString().equals(stone.toString())
					|| (!(s.getColor() == stone.getColor() || s.getShape() == stone.getShape()))) {
				result = false;
			}
		}
		// if(!result) {
		// System.out.println("Not a valid move");
		// }
		return result;
	}

	// Returns the stones on the 4 sides of the coordinates if they exist.
	public List<Stone> getSurroundingStones(int x, int y) {
		List<Stone> stones = new ArrayList<Stone>();
		if (!(getField(x - 1, y) == null)) {
			stones.add(getField(x - 1, y));
		}
		if (!(getField(x + 1, y) == null)) {
			stones.add(getField(x + 1, y));
		}
		if (!(getField(x, y - 1) == null)) {
			stones.add(getField(x, y - 1));
		}
		if (!(getField(x, y + 1) == null)) {
			stones.add(getField(x, y + 1));
		}
		return stones;

	}

	// All locations of stones that need to be considered, + the direction of
	// the line
	public Map<Character, List<Location>> getLocationList(int x, int y) {
		Map<Character, List<Location>> locations = new HashMap<Character, List<Location>>();
		for (int xc = -1, yc = -1; Math.abs(xc) < 2 && Math.abs(yc) < 2; xc = xc + 2, yc = yc + 2) {
			for (int i = 1; i <= MAXROWLENGHT; i++) {
				if (!(getField(x, y + yc * i) == null)) {
					if (yc == -1) {
						List<Location> lineInDirection = (locations.get('l') == null) ? new ArrayList<Location>() : locations.get('l');
						lineInDirection.add(new Location(x, (y + yc * i)));
						locations.put('l', lineInDirection);
					} else {
						List<Location> lineInDirection = (locations.get('r') == null) ? new ArrayList<Location>() : locations.get('r');
						lineInDirection.add(new Location(x, (y + yc * i)));
						locations.put('r', lineInDirection);
					}
				}
				if (!(getField(x + xc * i, y) == null)) {
					if (xc == -1) {
						List<Location> lineInDirection = (locations.get('u') == null) ? new ArrayList<Location>() : locations.get('u');
						lineInDirection.add(new Location((x + xc * i), y));
						locations.put('u', lineInDirection);
					} else {
						List<Location> lineInDirection = (locations.get('d') == null) ? new ArrayList<Location>() : locations.get('d');
						lineInDirection.add(new Location((x + xc * i), y));
						locations.put('d',lineInDirection);
					}
				}
			}
		}
		return locations;
	}

	// All stones that needs to be concidered when checking for the same stone
	public List<Stone> getStoneList(int x, int y) {
		List<List<Location>> locations = new ArrayList<List<Location>>(getLocationList(x, y).values());
		List<Stone> stones = new ArrayList<Stone>();
		for(List<Location> locList: locations) {
			for (Location l : locList) {
				if (!(getField(l.getX(), l.getY()) == null)) {
					stones.add(getField(l.getX(), l.getY()));
				}
			}
		}
		return stones;
	}

	// Gives all places as string surrounding the placed stones
	public List<String> getAvailablePlaces() {
		List<String> locations = new ArrayList<String>();
		for (String l : board.keySet()) {
			String[] coordinates = l.split(",");
			for (int xc = -1; Math.abs(xc) < 2; xc = xc + 2) {
				for (int yc = -1; Math.abs(yc) < 2; yc = yc + 2) {
					if (!(locations.contains(
							"" + (Integer.parseInt(coordinates[0]) + xc) + "," + Integer.parseInt(coordinates[1])))) {
						locations.add((Integer.parseInt(coordinates[0]) + xc) + "," + Integer.parseInt(coordinates[1]));
					}
					if (!(locations.contains(
							"" + (Integer.parseInt(coordinates[0])) + "," + (Integer.parseInt(coordinates[1]) + yc)))) {
						locations.add(Integer.parseInt(coordinates[0]) + "," + (Integer.parseInt(coordinates[1]) + yc));
					}
				}
			}
		}
		return locations;
	}

	// Moet nog dinamisb maken >> Moet nog DIMy en DIM opsplitsen.
	public void setDimentions() {
		int maxX = 0;
		int minX = 0;
		int maxY = 0;
		int minY = 0;
		for (String e : board.keySet()) {
			String[] parts = e.split(",");
			if (Integer.parseInt(parts[0]) > maxX) {
				maxX = Integer.parseInt(parts[0]);
			}
			if (Integer.parseInt(parts[0]) < minX) {
				minX = Integer.parseInt(parts[0]);
			}
			if (Integer.parseInt(parts[1]) > maxY) {
				maxY = Integer.parseInt(parts[1]);
			}
			if (Integer.parseInt(parts[1]) < minY) {
				minY = Integer.parseInt(parts[1]);
			}
		}
		DIMX = Math.abs(minX) + maxX + 3;
		DIMY = Math.abs(minY) + maxY + 3;
	}

	public Map<String, Stone> getBoard() {
		return this.board;
	}

	public Map<Players, Integer> getPoints() {
		return this.points;
	}

	public void addPoints(Players p, int value) {
		points.put(p, points.get(p) + value);
	}
	
	
	// Based on the List<Moves> moves gotten from the makemove, makes a copy of
	// the playing board, and calculates the points
	// gained using the filled in board, and the
	public int getTheoreticalPoints(List<Moves> moves) {
		int result = 0;
		if((moves.get(0) instanceof Place) && moves.size() == 1) {
			Place onePoint = ((Place) moves.get(0));
			if(getLocationList(onePoint.getX(), onePoint.getY()).size() == 0) {
				result = 1;
			}
		}
		if( result != 1) {
			// Place all the stones
			if (moves.get(0) instanceof Place) {
				for (Moves m : moves) {
					Place current = ((Place) m);
					int xCoordinate = current.getX();
					int yCoordinate = current.getY();
					Stone stone = current.getStone();
					setField(xCoordinate, yCoordinate, stone);
				}
				// Calculate the score
				List<List<Location>> allLines = new ArrayList<List<Location>>();
				for (Moves move : moves) {
					Place current = ((Place) move);
					int xCoordinate = current.getX();
					int yCoordinate = current.getY();
					Map<Character, List<Location>> linesForCurrentStone = getLocationList(xCoordinate, yCoordinate);
					// Logic that devides the lines of Current stone into the 4
					// or less maps, with the stones and locations
					List<Character> lineDirections = new ArrayList<Character>(linesForCurrentStone.keySet());
					for(int l = 0; l < lineDirections.size(); l++){
						List<Location> lineToGetPointsFor = linesForCurrentStone.get(lineDirections.get(l));
						//The stone itselve needs to be added to the line
						lineToGetPointsFor.add(new Location(xCoordinate, yCoordinate));
						allLines.add(lineToGetPointsFor);
					}
				}
				List<List<Location>> NewallLines = checkForDuplicateLines(allLines);
				for(List<Location> thisLine: NewallLines) {
					result = result + thisLine.size();
					if(thisLine.size() == MAXROWLENGHT) {
						result = result + MAXROWLENGHT;
					}
				}
			}
		}
		return result;
	}
	
	//Method that checks if there are duplicate list in the list, and removes them
	public List<List<Location>> checkForDuplicateLines(List<List<Location>> locations){
		List<List<Location>> duplicateLines = new ArrayList<List<Location>>();
		if(locations.size() > 1) {
			//Check every list
			List<List<Location>> newLocations = new ArrayList<List<Location>>(locations);
			for(List<Location> listToCheck: locations) {
				List<Location> toRemove = listToCheck;
				newLocations.remove(toRemove);
				//Compared to every other list
				boolean isNewLine = false;
				for(List<Location> listToCompareWith: newLocations) {
					List<Boolean> hasCoordinate = new ArrayList<Boolean>();
					for(int b = 0; b < listToCheck.size(); b++) {
						hasCoordinate.add(false);
					}
					//Check if every value of the list to check
					for(int i = 0; i < listToCheck.size(); i ++) {
						for(Location compareLocation: listToCompareWith){
							boolean containsLocation = false;
							if(compareLocation.isEqual(listToCheck.get(i))) {
								containsLocation = true;
							}
							if(containsLocation){
								hasCoordinate.set(i, true);
							}
						}
					}
					if(hasCoordinate.contains(false)) {
						isNewLine = true;
					}
					if(!hasCoordinate.contains(false) && listToCheck.equals(listToCompareWith) && !duplicateLines.contains(listToCheck)) {
						duplicateLines.add(listToCheck);
					}
					if(!isNewLine) {

						if(!duplicateLines.contains(listToCheck) && !listToCheck.equals(listToCompareWith)) {
							duplicateLines.add(listToCheck);
						}
					}
				}
			}
			for(List<Location> location: duplicateLines) {
				locations.remove(location);
			}
		}
		return locations;
	}

	
	

	public static void main(String[] args) {
		Board newboard = new Board();
		Location Loc1 = new Location(0, 0);
		Location Loc2 = new Location(0, -1);
		Location Loc3 = new Location(4, -1);
		Location Loc4 = new Location(7,-1);
//		Location Loc5 = new Location(8, 2);
		List<Location> list1 = new ArrayList<Location>();
		List<Location> list2 = new ArrayList<Location>();
		list1.add(Loc1);
		list1.add(Loc2);
		list2.add(Loc1);
		list2.add(Loc2);
		List<List<Location>> locationList = new ArrayList<List<Location>>();
		locationList.add(list2);
		locationList.add(list1);
		System.out.println(locationList);
//		System.out.println(list1);
//		System.out.println(list2);
		System.out.println(newboard.checkForDuplicateLines(locationList));
//		[[0,-1, 0,0], [0,0, 0,-1]]
		// System.out.println(newboard.getDimentions());
	}

}
