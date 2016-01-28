package game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import game.Stone.Color;
import game.Stone.Shape;

public class TUI {
	private Board board;
	private Game game;
	private static final int EDGESIZE = 1;
	
	public TUI(Board board) {
		this.board = board;
	}
	
	public TUI(Game game) {
		this.game = game;
		this.board = game.getBoard();
	}
	
	
	public int[] getEdges(Board board) {
		int[] result = new int[4];
		Map<String, Stone> allStones = board.getBoard();
		int maxX = 0, minX =0, maxY = 0, minY = 0;
		for(String e: allStones.keySet()) {
			String[] parts = e.split(",");
			maxX = (Integer.parseInt(parts[0]) > maxX) ? Integer.parseInt(parts[0]) :  maxX;
			minX = (Integer.parseInt(parts[0]) < minX) ? Integer.parseInt(parts[0]) :  minX;
			maxY = (Integer.parseInt(parts[1]) > maxY) ? Integer.parseInt(parts[1]) :  maxY;
			minY = (Integer.parseInt(parts[1]) < minY) ? Integer.parseInt(parts[1]) :  minY;
		}
		result[0] = minX;
		result[1] = maxX;
		result[2] = minY;
		result[3] = maxY;
		return result;
		
	}
	
	
	
	public void makeBoard() {
		int[] edges = getEdges(this.board);
		for(int k = edges[2] - EDGESIZE; k <= edges[3] + EDGESIZE; k++) {
			if(k < 0 || k >= 10) {
				System.out.print(k + " ");
			} else {
				System.out.print(k + "  ");
				System.out.print("");	
			}
		}
		System.out.println("");
		for(int i = edges[0] - EDGESIZE; i <= edges[1] + EDGESIZE; i++) {
			for(int j = edges[2]-EDGESIZE; j <= edges[3] + EDGESIZE; j++) { 
				if(i == edges[1] + EDGESIZE && (j != edges[3] + EDGESIZE)) {
					System.out.print("  |");
				} else if(board.getField(i, j) == null) {
					if(j == edges[2]-EDGESIZE) {
						System.out.print("__");
					} else {
						if (!((j == (edges[3] + EDGESIZE)) && (i == (edges[1] + EDGESIZE)))){
						System.out.print("|__");	
						} else {
							System.out.print("  ");
						}
					}
				} else if(j != edges[3] + EDGESIZE) {
					System.out.print("|" + this.board.getField(i, j));
					} else {
				System.out.println("");
			}
			}System.out.print(i);	
			System.out.println("");
		}
	}
	
	public void update() {
		makeBoard();
	}
}
