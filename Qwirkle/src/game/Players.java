package game;

import java.util.List;

public interface Players {

	public List<Moves> makeMove();
	
	public void takeStone(Stone stone);
	
	public void receiveStone(Stone stone);
	
	public void addPoints(int newPoints);
	
	public List<Stone> getHand();
	
	public int getPoints();
	
	public String getName();
	
	public boolean containsStone(Stone stone);
	
	public void setBoard(Board board);
}
